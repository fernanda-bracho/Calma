package com.example.calmafinal.domain.model;

/**
 * PYME contratante. Administra su razón social, identificador fiscal y el estado de la
 * suscripción B2B.
 */
public class Empresa {

    private final long id;
    private String razonSocial;
    private String identificadorFiscal;
    private boolean suscripcionActiva;

    public Empresa(long id, String razonSocial, String identificadorFiscal, boolean suscripcionActiva) {
        this.id = id;
        this.razonSocial = requerir(razonSocial, "razonSocial");
        this.identificadorFiscal = requerir(identificadorFiscal, "identificadorFiscal");
        this.suscripcionActiva = suscripcionActiva;
    }

    public long getId() { return id; }

    public String getRazonSocial() { return razonSocial; }

    public void setRazonSocial(String razonSocial) {
        this.razonSocial = requerir(razonSocial, "razonSocial");
    }

    public String getIdentificadorFiscal() { return identificadorFiscal; }

    public void setIdentificadorFiscal(String identificadorFiscal) {
        this.identificadorFiscal = requerir(identificadorFiscal, "identificadorFiscal");
    }

    public boolean isSuscripcionActiva() { return suscripcionActiva; }

    public void activarSuscripcion() { this.suscripcionActiva = true; }

    public void cancelarSuscripcion() { this.suscripcionActiva = false; }

    private static String requerir(String valor, String campo) {
        if (valor == null || valor.trim().isEmpty()) {
            throw new IllegalArgumentException("El campo '" + campo + "' es obligatorio.");
        }
        return valor.trim();
    }
}
