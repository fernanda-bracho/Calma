package com.example.calmafinal.domain.model;

/**
 * Persona de confianza registrada por el usuario. Construye la plantilla del mensaje de
 * pánico integrando la ubicación. Pertenece al {@link Usuario} (composición).
 */
public class ContactoEmergencia {

    private final long id;
    private final String nombre;
    private final String telefono;
    private final String relacion;

    public ContactoEmergencia(long id, String nombre, String telefono, String relacion) {
        this.id = id;
        this.nombre = requerir(nombre, "nombre");
        this.telefono = requerir(telefono, "telefono");
        this.relacion = requerir(relacion, "relacion");
    }

    public long getId() { return id; }

    public String getNombre() { return nombre; }

    public String getTelefono() { return telefono; }

    public String getRelacion() { return relacion; }

    /** Teléfono solo con dígitos, listo para armar un enlace de WhatsApp. */
    public String getTelefonoNumerico() {
        return telefono.replaceAll("[^0-9]", "");
    }

    /**
     * Mensaje de alerta de crisis.
     *
     * @param ubicacion ubicación actual; puede ser null si el GPS no está disponible,
     *                  en cuyo caso el mensaje se envía sin enlace de mapa.
     */
    public String construirMensajePanico(Ubicacion ubicacion) {
        StringBuilder mensaje = new StringBuilder()
                .append("🚨 ¡EMERGENCIA CALMA+! Necesito apoyo inmediato. ")
                .append("Esta es una alerta de crisis activada desde la aplicación.");
        if (ubicacion != null) {
            mensaje.append("\n📍 Mi ubicación: ").append(ubicacion.generarEnlaceMaps());
        }
        return mensaje.toString();
    }

    private static String requerir(String valor, String campo) {
        if (valor == null || valor.trim().isEmpty()) {
            throw new IllegalArgumentException("El campo '" + campo + "' es obligatorio.");
        }
        return valor.trim();
    }
}
