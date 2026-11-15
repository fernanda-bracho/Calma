package com.example.calmafinal.domain.model;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.example.calmafinal.domain.strategy.EstrategiaCalculoCosto;

import org.junit.Test;

/** Verifica RF04: citas gratis con convenio activo y con costo para los demás, sin instanceof. */
public class CitaTest {

    private static final double TARIFA = 80_000.0;

    private Empleado empleado(boolean convenioActivo) {
        return new Empleado(1, "Ana Pérez", "ana@pyme.com", "hash", "PYME-01", convenioActivo);
    }

    private UsuarioParticular particular() {
        return new UsuarioParticular(2, "Luis Gómez", "luis@mail.com", "hash", "3001234567");
    }

    @Test
    public void empleadoConConvenioActivoNoPaga() {
        Cita cita = new Cita(1, 0L, "Dra. Ríos", empleado(true), TARIFA);
        assertEquals(0.0, cita.getCostoFinal(), 0.0001);
    }

    @Test
    public void empleadoSinConvenioPagaTarifaCompleta() {
        Cita cita = new Cita(1, 0L, "Dra. Ríos", empleado(false), TARIFA);
        assertEquals(TARIFA, cita.getCostoFinal(), 0.0001);
    }

    @Test
    public void usuarioParticularPagaTarifaCompleta() {
        Cita cita = new Cita(1, 0L, "Dra. Ríos", particular(), TARIFA);
        assertEquals(TARIFA, cita.getCostoFinal(), 0.0001);
    }

    @Test
    public void accesoCorporativoDependeDelTipoDeUsuario() {
        assertTrue(empleado(true).tieneAccesoCorporativo());
        assertFalse(empleado(false).tieneAccesoCorporativo());
        assertFalse(particular().tieneAccesoCorporativo());
    }

    @Test
    public void permiteInyectarOtraEstrategia() {
        EstrategiaCalculoCosto mitad = base -> base / 2;
        Cita cita = new Cita(1, 0L, "Dra. Ríos", particular(), TARIFA, mitad);
        assertEquals(TARIFA / 2, cita.getCostoFinal(), 0.0001);
    }

    @Test(expected = IllegalArgumentException.class)
    public void rechazaTarifaNegativa() {
        new Cita(1, 0L, "Dra. Ríos", particular(), -1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void rechazaCitaSinProfesional() {
        new Cita(1, 0L, "  ", particular(), TARIFA);
    }

    @Test(expected = IllegalArgumentException.class)
    public void rechazaCitaSinUsuario() {
        new Cita(1, 0L, "Dra. Ríos", null, TARIFA);
    }
}
