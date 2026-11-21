package com.example.calmafinal;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "tabla_contactos_emergencia")
public class ContactoEmergencia {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String nombre;
    private String telefono;
    private String relacion;

    public ContactoEmergencia(String nombre, String telefono, String relacion) {
        this.nombre = nombre;
        this.telefono = telefono;
        this.relacion = relacion;
    }

    // Getters y Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNombre() { return nombre; }
    public String getTelefono() { return telefono; }
    public String getRelacion() { return relacion; }
}
