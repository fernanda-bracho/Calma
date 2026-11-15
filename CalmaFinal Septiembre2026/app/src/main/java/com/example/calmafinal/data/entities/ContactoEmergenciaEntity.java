package com.example.calmafinal.data.entities;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

/**
 * Mapeo de {@code ContactoEmergencia} a SQL. La clave foránea con ON DELETE CASCADE hace
 * efectiva la composición del diseño: al borrar al usuario, sus contactos se destruyen.
 */
@Entity(
        tableName = "tabla_contactos_emergencia",
        foreignKeys = @ForeignKey(
                entity = UsuarioEntity.class,
                parentColumns = "id",
                childColumns = "usuarioId",
                onDelete = ForeignKey.CASCADE),
        indices = {@Index("usuarioId")})
public class ContactoEmergenciaEntity {

    @PrimaryKey(autoGenerate = true)
    private long id;

    private long usuarioId;
    private String nombre;
    private String telefono;
    private String relacion;

    /** Constructor que usa Room. */
    public ContactoEmergenciaEntity() {}

    @Ignore
    public ContactoEmergenciaEntity(long usuarioId, String nombre, String telefono, String relacion) {
        this.usuarioId = usuarioId;
        this.nombre = nombre;
        this.telefono = telefono;
        this.relacion = relacion;
    }

    public long getId() { return id; }
    public void setId(long id) { this.id = id; }

    public long getUsuarioId() { return usuarioId; }
    public void setUsuarioId(long usuarioId) { this.usuarioId = usuarioId; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    public String getRelacion() { return relacion; }
    public void setRelacion(String relacion) { this.relacion = relacion; }
}
