package com.example.calmafinal.infrastructure.audio;

import java.util.List;

/**
 * Abstracción de donde salen los sonidos relajantes (RF06). MusicaActivity depende solo de
 * esta interfaz y no sabe si las pistas vienen de Internet o del almacenamiento local.
 */
public interface FuenteAudio {

    /**
     * Puede bloquear (red o disco): llamarla desde un hilo de fondo, nunca desde el hilo de UI.
     *
     * @throws FuenteAudioNoDisponibleException si esta fuente no puede entregar pistas ahora
     */
    List<PistaAudio> obtenerPistas() throws FuenteAudioNoDisponibleException;
}
