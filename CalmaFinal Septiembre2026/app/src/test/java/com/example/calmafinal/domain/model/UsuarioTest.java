package com.example.calmafinal.domain.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import java.util.List;

public class UsuarioTest {

    private UsuarioParticular nuevo() {
        return new UsuarioParticular(1, "  Luis Gómez ", " luis@mail.com ", "hash", null);
    }

    @Test
    public void recortaEspaciosYAceptaTelefonoNulo() {
        UsuarioParticular u = nuevo();
        assertEquals("Luis Gómez", u.getNombreCompleto());
        assertEquals("luis@mail.com", u.getCorreo());
        assertEquals("", u.getTelefono());
    }

    @Test(expected = IllegalArgumentException.class)
    public void exigeCorreo() {
        new UsuarioParticular(1, "Luis", " ", "hash", "");
    }

    @Test(expected = IllegalArgumentException.class)
    public void empleadoExigeCodigoEmpresa() {
        new Empleado(1, "Ana", "ana@pyme.com", "hash", "", true);
    }

    @Test
    public void composicionLosContactosSeCreanDesdeElUsuario() {
        UsuarioParticular u = nuevo();
        ContactoEmergencia c = u.agregarContacto(0, "Carmen Ruiz", "+57 310 467 8220", "Mamá");
        assertEquals(1, u.getContactos().size());
        assertEquals("Carmen Ruiz", c.getNombre());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void laListaDeContactosNoSePuedeModificarDesdeFuera() {
        nuevo().getContactos().add(new ContactoEmergencia(0, "X", "1", "Y"));
    }

    @Test
    public void agregacionElUsuarioAcumulaRegistrosDeSintomas() {
        UsuarioParticular u = nuevo();
        u.registrarSintoma(new RegistroSintoma(1, 1000L, 7, "Ansioso", "Dolor de cabeza"));
        u.registrarSintoma(new RegistroSintoma(2, 2000L, 4, "Tranquilo", ""));
        List<RegistroSintoma> registros = u.getRegistrosSintoma();
        assertEquals(2, registros.size());
        assertTrue(registros.get(0).getFechaMs() < registros.get(1).getFechaMs());
    }

    @Test(expected = IllegalArgumentException.class)
    public void estresFueraDeRangoSeRechaza() {
        new RegistroSintoma(1, 0L, 11, "Mal", "");
    }

    @Test
    public void empresaControlaSuSuscripcion() {
        Empresa e = new Empresa(1, "Café Sol SAS", "900123456-7", true);
        e.cancelarSuscripcion();
        assertEquals(false, e.isSuscripcionActiva());
        e.activarSuscripcion();
        assertEquals(true, e.isSuscripcionActiva());
    }
}
