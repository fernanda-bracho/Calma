package com.example.calmafinal.infrastructure.audio;

import java.util.List;

/**
 * Resiliencia de red (RF06): intenta la fuente principal y, si falla o llega vacía, usa la de
 * respaldo. Como también es una {@link FuenteAudio}, la vista no cambia ni una línea.
 */
public class FuenteAudioConRespaldo implements FuenteAudio {

    private final FuenteAudio principal;
    private final FuenteAudio respaldo;

    public FuenteAudioConRespaldo(FuenteAudio principal, FuenteAudio respaldo) {
        if (principal == null || respaldo == null) {
            throw new IllegalArgumentException("Se requieren una fuente principal y una de respaldo.");
        }
        this.principal = principal;
        this.respaldo = respaldo;
    }

    @Override
    public List<PistaAudio> obtenerPistas() throws FuenteAudioNoDisponibleException {
        try {
            List<PistaAudio> pistas = principal.obtenerPistas();
            if (pistas != null && !pistas.isEmpty()) {
                return pistas;
            }
        } catch (FuenteAudioNoDisponibleException | RuntimeException e) {
            // Sin red o servicio caído: se continúa con el respaldo.
        }
        return respaldo.obtenerPistas();
    }
}
