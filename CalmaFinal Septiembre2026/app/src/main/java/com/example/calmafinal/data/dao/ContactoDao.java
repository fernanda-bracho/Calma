package com.example.calmafinal.data.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.calmafinal.data.entities.ContactoEmergenciaEntity;

import java.util.List;

@Dao
public interface ContactoDao {

    @Insert
    long insertar(ContactoEmergenciaEntity contacto);

    @Query("SELECT * FROM tabla_contactos_emergencia WHERE usuarioId = :usuarioId ORDER BY id")
    List<ContactoEmergenciaEntity> obtenerPorUsuario(long usuarioId);

    @Delete
    void eliminar(ContactoEmergenciaEntity contacto);
}
