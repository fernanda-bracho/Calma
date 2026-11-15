package com.example.calmafinal.domain.model;

import com.example.calmafinal.domain.strategy.EstrategiaCalculoCosto;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Clase base abstracta de todos los actores del sistema (Empleado y UsuarioParticular).
 *
 * Encapsulamiento: todos los atributos son privados; el acceso es solo por métodos explícitos.
 * Composición: los {@link ContactoEmergencia} pertenecen al usuario y se crean desde él;
 *              si el usuario desaparece, sus contactos pierden validez.
 * Agregación: los {@link RegistroSintoma} se asocian al usuario pero conservan su
 *             independencia como dato analítico.
 */
public abstract class Usuario {

    private final long id;
    private String nombreCompleto;
    private String correo;
    private String passwordHash;

    private final List<ContactoEmergencia> contactos = new ArrayList<>();
    private final List<RegistroSintoma> registrosSintoma = new ArrayList<>();

    protected Usuario(long id, String nombreCompleto, String correo, String passwordHash) {
        this.id = id;
        this.nombreCompleto = requerirTexto(nombreCompleto, "nombreCompleto");
        this.correo = requerirTexto(correo, "correo");
        this.passwordHash = requerirTexto(passwordHash, "passwordHash");
    }

    // ---- Contrato que concretan las subclases ----

    /** Indica si el usuario goza de beneficios corporativos (convenio PYME activo). */
    public abstract boolean tieneAccesoCorporativo();

    /**
     * Estrategia de cobro que aplica a este usuario. Cada subclase decide la suya,
     * de modo que Cita nunca necesita preguntar por el tipo concreto.
     */
    public abstract EstrategiaCalculoCosto estrategiaCosto();

    // ---- Composición: contactos de emergencia ----

    public ContactoEmergencia agregarContacto(long idContacto, String nombre, String telefono, String relacion) {
        ContactoEmergencia contacto = new ContactoEmergencia(idContacto, nombre, telefono, relacion);
        contactos.add(contacto);
        return contacto;
    }

    public List<ContactoEmergencia> getContactos() {
        return Collections.unmodifiableList(contactos);
    }

    // ---- Agregación: historial de síntomas ----

    public void registrarSintoma(RegistroSintoma registro) {
        if (registro == null) {
            throw new IllegalArgumentException("El registro de síntoma no puede ser nulo.");
        }
        registrosSintoma.add(registro);
    }

    public List<RegistroSintoma> getRegistrosSintoma() {
        return Collections.unmodifiableList(registrosSintoma);
    }

    // ---- Getters / setters ----

    public long getId() { return id; }

    public String getNombreCompleto() { return nombreCompleto; }

    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = requerirTexto(nombreCompleto, "nombreCompleto");
    }

    public String getCorreo() { return correo; }

    public void setCorreo(String correo) {
        this.correo = requerirTexto(correo, "correo");
    }

    /** Devuelve el hash almacenado, nunca la contraseña en claro. */
    public String getPasswordHash() { return passwordHash; }

    public void setPasswordHash(String passwordHash) {
        this.passwordHash = requerirTexto(passwordHash, "passwordHash");
    }

    protected static String requerirTexto(String valor, String campo) {
        if (valor == null || valor.trim().isEmpty()) {
            throw new IllegalArgumentException("El campo '" + campo + "' es obligatorio.");
        }
        return valor.trim();
    }
}
