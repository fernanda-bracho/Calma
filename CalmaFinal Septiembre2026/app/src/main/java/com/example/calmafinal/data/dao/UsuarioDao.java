package com.example.calmafinal.data.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.calmafinal.data.entities.UsuarioEntity;

@Dao
public interface UsuarioDao {

    /** @return el id generado, o -1 si no se pudo insertar */
    @Insert
    long insertar(UsuarioEntity usuario);

    /**
     * Busca por correo. El login ya no consulta por contraseña: se carga el usuario y el
     * dominio verifica el hash (ServicioHashContrasena).
     */
    @Query("SELECT * FROM usuario WHERE correo = :correo LIMIT 1")
    UsuarioEntity obtenerPorCorreo(String correo);

    @Query("UPDATE usuario SET passwordHash = :nuevoHash WHERE correo = :correo")
    void actualizarPasswordHash(String correo, String nuevoHash);
}
