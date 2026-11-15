package com.example.calmafinal.domain.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class UbicacionYContactoTest {

    @Test
    public void enlaceDeMapsUsaPuntoDecimal() {
        Ubicacion u = new Ubicacion(20.659698, -103.349609, 0L);
        assertEquals("https://www.google.com/maps?q=20.659698,-103.349609", u.generarEnlaceMaps());
    }

    @Test(expected = IllegalArgumentException.class)
    public void latitudInvalida() {
        new Ubicacion(91, 0, 0L);
    }

    @Test(expected = IllegalArgumentException.class)
    public void longitudInvalida() {
        new Ubicacion(0, 181, 0L);
    }

    @Test
    public void mensajeDePanicoIncluyeLaUbicacion() {
        ContactoEmergencia c = new ContactoEmergencia(1, "Carmen", "+57 (310) 467-8220", "Mamá");
        String mensaje = c.construirMensajePanico(new Ubicacion(4.7110, -74.0721, 0L));
        assertTrue(mensaje.contains("EMERGENCIA CALMA+"));
        assertTrue(mensaje.contains("https://www.google.com/maps?q=4.711000,-74.072100"));
    }

    @Test
    public void mensajeDePanicoSinUbicacionNoTrae_enlace() {
        ContactoEmergencia c = new ContactoEmergencia(1, "Carmen", "3104678220", "Mamá");
        String mensaje = c.construirMensajePanico(null);
        assertTrue(mensaje.contains("EMERGENCIA CALMA+"));
        assertFalse(mensaje.contains("maps"));
    }

    @Test
    public void telefonoNumericoQuitaSimbolos() {
        ContactoEmergencia c = new ContactoEmergencia(1, "Carmen", "+57 (310) 467-8220", "Mamá");
        assertEquals("573104678220", c.getTelefonoNumerico());
    }
}
