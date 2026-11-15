package com.example.calmafinal.infrastructure.audio;

/**
 * Pista de sonido relajante. {@code origen} es una URL si la pista viene de la red, o el
 * nombre de un recurso embebido si es local.
 */
public class PistaAudio {

    private final String titulo;
    private final String origen;
    private final boolean local;

    public PistaAudio(String titulo, String origen, boolean local) {
        this.titulo = titulo;
        this.origen = origen;
        this.local = local;
    }

    public String getTitulo() { return titulo; }

    public String getOrigen() { return origen; }

    public boolean isLocal() { return local; }
}
