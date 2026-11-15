package com.example.calmafinal.data.entities;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

/**
 * Mapeo de {@code Usuario} (y sus subclases) a la tabla SQL. Una sola tabla con una columna
 * {@code tipo} que distingue EMPLEADO de PARTICULAR; los campos que no aplican quedan nulos.
 * Solo guarda el hash de la contraseña.
 */
@Entity(tableName = "usuario", indices = {@Index(value = "correo", unique = true)})
public class UsuarioEntity {

    public static final String TIPO_EMPLEADO = "EMPLEADO";
    public static final String TIPO_PARTICULAR = "PARTICULAR";

    @PrimaryKey(autoGenerate = true)
    private long id;

    private String nombreCompleto;
    private String correo;
    private String passwordHash;
    private String tipo;

    // Solo Empleado
    private String codigoEmpresa;
    private boolean convenioActivo;

    // Solo UsuarioParticular
    private String telefono;

    /** Constructor que usa Room. */
    public UsuarioEntity() {}

    @Ignore
    public UsuarioEntity(String nombreCompleto, String correo, String passwordHash, String tipo) {
        this.nombreCompleto = nombreCompleto;
        this.correo = correo;
        this.passwordHash = passwordHash;
        this.tipo = tipo;
    }

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public String getNombreCompleto() { return nombreCompleto; }
    public void setNombreCompleto(String nombreCompleto) { this.nombreCompleto = nombreCompleto; }

    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }

    public String getPasswordHash() { return passwordHash; }
    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public String getCodigoEmpresa() { return codigoEmpresa; }
    public void setCodigoEmpresa(String codigoEmpresa) { this.codigoEmpresa = codigoEmpresa; }

    public boolean isConvenioActivo() { return convenioActivo; }
    public void setConvenioActivo(boolean convenioActivo) { this.convenioActivo = convenioActivo; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
}
