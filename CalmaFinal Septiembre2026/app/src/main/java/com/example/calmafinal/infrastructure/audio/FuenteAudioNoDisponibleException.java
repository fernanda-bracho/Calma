package com.example.calmafinal.infrastructure.audio;

/** La fuente de audio no pudo entregar pistas (sin red, servicio caído, no implementada...). */
public class FuenteAudioNoDisponibleException extends Exception {

    private static final long serialVersionUID = 1L;

    public FuenteAudioNoDisponibleException(String mensaje) {
        super(mensaje);
    }

    public FuenteAudioNoDisponibleException(String mensaje, Throwable causa) {
        super(mensaje, causa);
    }
}
