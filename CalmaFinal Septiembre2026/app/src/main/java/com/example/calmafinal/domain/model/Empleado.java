package com.example.calmafinal.domain.model;

import com.example.calmafinal.domain.strategy.CalculoCostoCorporativo;
import com.example.calmafinal.domain.strategy.CalculoCostoParticular;
import com.example.calmafinal.domain.strategy.EstrategiaCalculoCosto;

/**
 * Colaborador de una PYME afiliada (B2B). Solo recibe cobertura si su convenio está activo.
 */
public class Empleado extends Usuario {

    private String codigoEmpresa;
    private boolean convenioActivo;

    public Empleado(long id, String nombreCompleto, String correo, String passwordHash,
                    String codigoEmpresa, boolean convenioActivo) {
        super(id, nombreCompleto, correo, passwordHash);
        this.codigoEmpresa = requerirTexto(codigoEmpresa, "codigoEmpresa");
        this.convenioActivo = convenioActivo;
    }

    @Override
    public boolean tieneAccesoCorporativo() {
        return convenioActivo;
    }

    @Override
    public EstrategiaCalculoCosto estrategiaCosto() {
        // Un empleado cuya empresa canceló el convenio paga como particular.
        return convenioActivo ? new CalculoCostoCorporativo() : new CalculoCostoParticular();
    }

    public String getCodigoEmpresa() { return codigoEmpresa; }

    public void setCodigoEmpresa(String codigoEmpresa) {
        this.codigoEmpresa = requerirTexto(codigoEmpresa, "codigoEmpresa");
    }

    public boolean isConvenioActivo() { return convenioActivo; }

    public void setConvenioActivo(boolean convenioActivo) { this.convenioActivo = convenioActivo; }
}
