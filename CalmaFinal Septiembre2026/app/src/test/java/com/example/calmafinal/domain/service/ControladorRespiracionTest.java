package com.example.calmafinal.domain.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.example.calmafinal.domain.service.ControladorRespiracion.Fase;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/** Usa duraciones de milisegundos para no esperar los 19 s reales de cada ciclo. */
public class ControladorRespiracionTest {

    private static class Grabador implements ControladorRespiracion.Listener {
        final List<String> eventos = Collections.synchronizedList(new ArrayList<String>());
        final CountDownLatch terminado = new CountDownLatch(1);

        @Override
        public void onFaseIniciada(Fase fase, int ciclo, int totalCiclos, long duracionMs) {
            eventos.add(ciclo + "/" + totalCiclos + ":" + fase);
        }

        @Override
        public void onSesionCompletada() {
            eventos.add("FIN");
            terminado.countDown();
        }
    }

    @Test
    public void recorreInhalarAguantarExhalarEnCadaCiclo() throws Exception {
        Grabador g = new Grabador();
        ControladorRespiracion c = new ControladorRespiracion(g, 2, 20, 20, 20);

        c.iniciar();
        assertTrue(g.terminado.await(5, TimeUnit.SECONDS));

        List<String> esperado = new ArrayList<>();
        esperado.add("1/2:INHALAR");
        esperado.add("1/2:AGUANTAR");
        esperado.add("1/2:EXHALAR");
        esperado.add("2/2:INHALAR");
        esperado.add("2/2:AGUANTAR");
        esperado.add("2/2:EXHALAR");
        esperado.add("FIN");
        assertEquals(esperado, new ArrayList<>(g.eventos));
        assertFalse(c.estaEnCurso());
    }

    @Test
    public void cancelarDetieneLaSesionSinAvisarDeFinalizacion() throws Exception {
        Grabador g = new Grabador();
        ControladorRespiracion c = new ControladorRespiracion(g, 4, 200, 200, 200);

        c.iniciar();
        Thread.sleep(50);
        c.cancelar();
        assertFalse(c.estaEnCurso());

        assertFalse(g.terminado.await(800, TimeUnit.MILLISECONDS));
        assertFalse(g.eventos.contains("FIN"));
        assertEquals(1, g.eventos.size()); // solo alcanzó a empezar INHALAR
    }

    @Test
    public void usaLosTiemposDelEjercicio478() {
        assertEquals(4_000, ControladorRespiracion.DURACION_INHALAR_MS);
        assertEquals(7_000, ControladorRespiracion.DURACION_AGUANTAR_MS);
        assertEquals(8_000, ControladorRespiracion.DURACION_EXHALAR_MS);
        assertEquals(4, ControladorRespiracion.CICLOS_POR_DEFECTO);
    }

    @Test(expected = IllegalArgumentException.class)
    public void exigeAlMenosUnCiclo() {
        new ControladorRespiracion(new Grabador(), 0, 1, 1, 1);
    }
}
