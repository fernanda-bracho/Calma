package com.example.calmafinal.infrastructure.audio;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Respaldo sin conexión: entrega los audios embebidos en la app (modo offline).
 *
 * Recibe la lista ya armada para no depender de Android. Cuando se agreguen archivos en
 * res/raw, MusicaActivity los registra aquí, por ejemplo
 * {@code new PistaAudio("Lluvia", "lluvia", true)}.
 */
public class ServicioAudioLocal implements FuenteAudio {

    private final List<PistaAudio> pistas;

    public ServicioAudioLocal(List<PistaAudio> pistas) {
        this.pistas = pistas == null
                ? Collections.<PistaAudio>emptyList()
                : Collections.unmodifiableList(new ArrayList<>(pistas));
    }

    @Override
    public List<PistaAudio> obtenerPistas() {
        return pistas;
    }
}
