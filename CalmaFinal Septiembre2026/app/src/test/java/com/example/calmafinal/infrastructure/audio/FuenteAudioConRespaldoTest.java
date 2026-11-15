package com.example.calmafinal.infrastructure.audio;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/** Verifica RF06: si falla la red, se usa el audio local sin que quien llama lo note. */
public class FuenteAudioConRespaldoTest {

    private final List<PistaAudio> enLinea =
            Collections.singletonList(new PistaAudio("Lluvia HD", "https://ejemplo.com/lluvia.mp3", false));
    private final List<PistaAudio> embebidas =
            Arrays.asList(new PistaAudio("Olas", "olas", true), new PistaAudio("Bosque", "bosque", true));

    private FuenteAudio quePasa(final List<PistaAudio> pistas) {
        return () -> pistas;
    }

    private FuenteAudio queFalla() {
        return () -> {
            throw new FuenteAudioNoDisponibleException("Sin conexión");
        };
    }

    @Test
    public void usaLaFuentePrincipalCuandoFunciona() throws Exception {
        FuenteAudio fuente = new FuenteAudioConRespaldo(quePasa(enLinea), new ServicioAudioLocal(embebidas));
        List<PistaAudio> pistas = fuente.obtenerPistas();
        assertEquals(1, pistas.size());
        assertEquals("Lluvia HD", pistas.get(0).getTitulo());
    }

    @Test
    public void caeAlRespaldoCuandoLaPrincipalFalla() throws Exception {
        FuenteAudio fuente = new FuenteAudioConRespaldo(queFalla(), new ServicioAudioLocal(embebidas));
        List<PistaAudio> pistas = fuente.obtenerPistas();
        assertEquals(2, pistas.size());
        assertTrue(pistas.get(0).isLocal());
    }

    @Test
    public void caeAlRespaldoCuandoLaPrincipalLlegaVacia() throws Exception {
        FuenteAudio fuente = new FuenteAudioConRespaldo(
                quePasa(Collections.<PistaAudio>emptyList()), new ServicioAudioLocal(embebidas));
        assertEquals(2, fuente.obtenerPistas().size());
    }

    @Test
    public void pixabayPendienteNoRompeLaReproduccion() throws Exception {
        FuenteAudio fuente = new FuenteAudioConRespaldo(new ServicioAudioPixabay(), new ServicioAudioLocal(embebidas));
        assertEquals(2, fuente.obtenerPistas().size());
    }
}
