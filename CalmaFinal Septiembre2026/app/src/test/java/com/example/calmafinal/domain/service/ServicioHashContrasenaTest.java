package com.example.calmafinal.domain.service;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class ServicioHashContrasenaTest {

    private final ServicioHashContrasena servicio = new ServicioHashContrasena();

    @Test
    public void elHashNoContieneLaContrasena() {
        String hash = servicio.hashear("MiClave123");
        assertFalse(hash.contains("MiClave123"));
    }

    @Test
    public void verificaLaContrasenaCorrecta() {
        String hash = servicio.hashear("MiClave123");
        assertTrue(servicio.verificar("MiClave123", hash));
    }

    @Test
    public void rechazaUnaContrasenaIncorrecta() {
        String hash = servicio.hashear("MiClave123");
        assertFalse(servicio.verificar("miclave123", hash));
        assertFalse(servicio.verificar("", hash));
    }

    @Test
    public void laSalHaceQueDosHashesDeLaMismaClaveSeanDistintos() {
        String h1 = servicio.hashear("MiClave123");
        String h2 = servicio.hashear("MiClave123");
        assertNotEquals(h1, h2);
        assertTrue(servicio.verificar("MiClave123", h1));
        assertTrue(servicio.verificar("MiClave123", h2));
    }

    @Test
    public void unHashMalFormadoNuncaVerifica() {
        assertFalse(servicio.verificar("MiClave123", "texto-plano"));
        assertFalse(servicio.verificar("MiClave123", "MiClave123"));
        assertFalse(servicio.verificar("MiClave123", "PBKDF2WithHmacSHA256$abc$zz$zz"));
        assertFalse(servicio.verificar("MiClave123", null));
        assertFalse(servicio.verificar(null, "a$1$00$00"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void noPermiteHashearVacio() {
        servicio.hashear("");
    }
}
