package com.example.calmafinal.domain.service;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Coordina las temporizaciones del ejercicio de respiración 4-7-8 (inhalar 4 s, aguantar 7 s,
 * exhalar 8 s) durante un número fijo de ciclos. Libera a RespiracionActivity de esa lógica.
 *
 * Es Java puro: no toca vistas ni animaciones. Avisa de cada fase a un {@link Listener} y la
 * capa ui decide cómo mostrarla (animar el círculo, cambiar el texto, hablar con
 * TextToSpeech). Los avisos llegan en un hilo de fondo, por lo que la Activity debe pasar a
 * runOnUiThread antes de tocar la interfaz.
 */
public class ControladorRespiracion {

    public enum Fase { INHALAR, AGUANTAR, EXHALAR }

    public interface Listener {
        /** Empieza una fase. {@code ciclo} va de 1 a {@code totalCiclos}. */
        void onFaseIniciada(Fase fase, int ciclo, int totalCiclos, long duracionMs);

        /** Terminaron todos los ciclos. */
        void onSesionCompletada();
    }

    public static final long DURACION_INHALAR_MS = 4_000;
    public static final long DURACION_AGUANTAR_MS = 7_000;
    public static final long DURACION_EXHALAR_MS = 8_000;
    public static final int CICLOS_POR_DEFECTO = 4;

    private final Listener listener;
    private final int totalCiclos;
    private final long[] duraciones;

    private ScheduledExecutorService planificador;
    private boolean enCurso;

    public ControladorRespiracion(Listener listener) {
        this(listener, CICLOS_POR_DEFECTO,
                DURACION_INHALAR_MS, DURACION_AGUANTAR_MS, DURACION_EXHALAR_MS);
    }

    /** Constructor con tiempos configurables; permite probar el controlador con duraciones cortas. */
    public ControladorRespiracion(Listener listener, int totalCiclos,
                                  long inhalarMs, long aguantarMs, long exhalarMs) {
        if (listener == null) {
            throw new IllegalArgumentException("El listener es obligatorio.");
        }
        if (totalCiclos < 1) {
            throw new IllegalArgumentException("Debe haber al menos un ciclo.");
        }
        this.listener = listener;
        this.totalCiclos = totalCiclos;
        this.duraciones = new long[]{inhalarMs, aguantarMs, exhalarMs};
    }

    /** Inicia una sesión nueva. Si ya había una en curso, la cancela y empieza de cero. */
    public synchronized void iniciar() {
        cancelar();
        planificador = Executors.newSingleThreadScheduledExecutor();
        enCurso = true;
        programarFase(planificador, 1, Fase.INHALAR);
    }

    /** Detiene la sesión sin avisar de que se completó. */
    public synchronized void cancelar() {
        enCurso = false;
        if (planificador != null) {
            planificador.shutdownNow();
            planificador = null;
        }
    }

    public synchronized boolean estaEnCurso() {
        return enCurso;
    }

    public int getTotalCiclos() {
        return totalCiclos;
    }

    private void programarFase(ScheduledExecutorService ejecutor, int ciclo, Fase fase) {
        ejecutor.execute(() -> {
            synchronized (ControladorRespiracion.this) {
                if (!enCurso || ejecutor != planificador) {
                    return; // sesión cancelada o reemplazada
                }
            }
            long duracion = duraciones[fase.ordinal()];
            listener.onFaseIniciada(fase, ciclo, totalCiclos, duracion);

            ejecutor.schedule(() -> avanzar(ejecutor, ciclo, fase), duracion, TimeUnit.MILLISECONDS);
        });
    }

    private void avanzar(ScheduledExecutorService ejecutor, int ciclo, Fase faseTerminada) {
        synchronized (this) {
            if (!enCurso || ejecutor != planificador) {
                return;
            }
        }
        switch (faseTerminada) {
            case INHALAR:
                programarFase(ejecutor, ciclo, Fase.AGUANTAR);
                break;
            case AGUANTAR:
                programarFase(ejecutor, ciclo, Fase.EXHALAR);
                break;
            case EXHALAR:
                if (ciclo < totalCiclos) {
                    programarFase(ejecutor, ciclo + 1, Fase.INHALAR);
                } else {
                    synchronized (this) {
                        enCurso = false;
                        planificador = null;
                    }
                    ejecutor.shutdown();
                    listener.onSesionCompletada();
                }
                break;
        }
    }
}
