package com.example.calmafinal.data;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.calmafinal.ContactoEmergencia;

import java.util.List;

@Dao
public interface ContactoDao {
    @Insert
    void insertar(ContactoEmergencia contacto);

    @Query("SELECT * FROM tabla_contactos_emergencia")
    List<ContactoEmergencia> obtenerTodos();

    @Delete
    void eliminar(ContactoEmergencia contacto);
}