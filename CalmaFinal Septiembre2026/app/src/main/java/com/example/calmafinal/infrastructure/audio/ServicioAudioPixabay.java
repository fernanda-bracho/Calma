package com.example.calmafinal.infrastructure.audio;

import java.util.List;

/**
 * Consume el catálogo en línea de Pixabay por REST cuando hay conexión.
 *
 * PENDIENTE DE IMPLEMENTAR. Hoy MusicaActivity solo abre la web de Pixabay en el navegador.
 * Antes de escribir el cliente hay que confirmar si Pixabay expone una API para audio
 * (y con qué clave y límites), declarar el permiso INTERNET en el manifiesto y hacer la
 * llamada en un hilo de fondo. Mientras tanto lanza la excepción, de modo que
 * {@link FuenteAudioConRespaldo} cae al audio local sin romper nada.
 */
public class ServicioAudioPixabay implements FuenteAudio {

    @Override
    public List<PistaAudio> obtenerPistas() throws FuenteAudioNoDisponibleException {
        throw new FuenteAudioNoDisponibleException("Cliente de Pixabay aún no implementado.");
    }
}
