package com.example.calmafinal.domain.model;

import com.example.calmafinal.domain.strategy.CalculoCostoParticular;
import com.example.calmafinal.domain.strategy.EstrategiaCalculoCosto;

/**
 * Usuario independiente (B2C). No tiene acceso a convenios corporativos.
 */
public class UsuarioParticular extends Usuario {

    private String telefono;

    public UsuarioParticular(long id, String nombreCompleto, String correo, String passwordHash, String telefono) {
        super(id, nombreCompleto, correo, passwordHash);
        this.telefono = telefono == null ? "" : telefono.trim();
    }

    @Override
    public boolean tieneAccesoCorporativo() {
        return false;
    }

    @Override
    public EstrategiaCalculoCosto estrategiaCosto() {
        return new CalculoCostoParticular();
    }

    public String getTelefono() { return telefono; }

    public void setTelefono(String telefono) {
        this.telefono = telefono == null ? "" : telefono.trim();
    }
}
