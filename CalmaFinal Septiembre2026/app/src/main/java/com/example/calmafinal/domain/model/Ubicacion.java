package com.example.calmafinal.domain.model;

import java.util.Locale;

/**
 * Coordenadas GPS (latitud, longitud) con su marca de tiempo. Su responsabilidad es dar
 * formato a los enlaces de geolocalización. Es inmutable.
 */
public class Ubicacion {

    private final double latitud;
    private final double longitud;
    private final long marcaTiempoMs;

    public Ubicacion(double latitud, double longitud, long marcaTiempoMs) {
        if (latitud < -90 || latitud > 90) {
            throw new IllegalArgumentException("La latitud debe estar entre -90 y 90.");
        }
        if (longitud < -180 || longitud > 180) {
            throw new IllegalArgumentException("La longitud debe estar entre -180 y 180.");
        }
        this.latitud = latitud;
        this.longitud = longitud;
        this.marcaTiempoMs = marcaTiempoMs;
    }

    public double getLatitud() { return latitud; }

    public double getLongitud() { return longitud; }

    public long getMarcaTiempoMs() { return marcaTiempoMs; }

    /** Enlace de Google Maps. Usa Locale.US para que el separador decimal siempre sea el punto. */
    public String generarEnlaceMaps() {
        return String.format(Locale.US, "https://www.google.com/maps?q=%.6f,%.6f", latitud, longitud);
    }
}
