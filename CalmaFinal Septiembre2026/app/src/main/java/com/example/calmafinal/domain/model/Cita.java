package com.example.calmafinal.domain.model;

import com.example.calmafinal.domain.strategy.EstrategiaCalculoCosto;

/**
 * Reserva de orientación psicológica. Delega el cálculo del cobro final en una
 * {@link EstrategiaCalculoCosto} (responsabilidad única + polimorfismo).
 */
public class Cita {

    private final long id;
    private final long fechaHoraMs;
    private final String profesional;
    private final Usuario usuario;
    private final double tarifaBase;
    private final EstrategiaCalculoCosto estrategiaCosto;

    /** Crea la cita con la estrategia de cobro que corresponde al tipo de usuario. */
    public Cita(long id, long fechaHoraMs, String profesional, Usuario usuario, double tarifaBase) {
        this(id, fechaHoraMs, profesional, usuario, tarifaBase,
                usuario == null ? null : usuario.estrategiaCosto());
    }

    /** Variante que permite inyectar otra estrategia (útil en pruebas o promociones). */
    public Cita(long id, long fechaHoraMs, String profesional, Usuario usuario,
                double tarifaBase, EstrategiaCalculoCosto estrategiaCosto) {
        if (usuario == null) {
            throw new IllegalArgumentException("La cita requiere un usuario.");
        }
        if (estrategiaCosto == null) {
            throw new IllegalArgumentException("La cita requiere una estrategia de cálculo de costo.");
        }
        if (profesional == null || profesional.trim().isEmpty()) {
            throw new IllegalArgumentException("La cita requiere un profesional asignado.");
        }
        if (tarifaBase < 0) {
            throw new IllegalArgumentException("La tarifa base no puede ser negativa.");
        }
        this.id = id;
        this.fechaHoraMs = fechaHoraMs;
        this.profesional = profesional.trim();
        this.usuario = usuario;
        this.tarifaBase = tarifaBase;
        this.estrategiaCosto = estrategiaCosto;
    }

    /** Monto final a cobrar según la estrategia: $0 para convenio corporativo, tarifa completa si no. */
    public double getCostoFinal() {
        return estrategiaCosto.calcularCostoFinal(tarifaBase);
    }

    public long getId() { return id; }

    public long getFechaHoraMs() { return fechaHoraMs; }

    public String getProfesional() { return profesional; }

    public Usuario getUsuario() { return usuario; }

    public double getTarifaBase() { return tarifaBase; }
}
