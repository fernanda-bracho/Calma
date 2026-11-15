package com.example.calmafinal.data.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.calmafinal.data.dao.ContactoDao;
import com.example.calmafinal.data.dao.UsuarioDao;
import com.example.calmafinal.data.entities.ContactoEmergenciaEntity;
import com.example.calmafinal.data.entities.UsuarioEntity;

/**
 * Singleton de la base Room.
 *
 * Versión 2: cambió el esquema (contraseña en hash, tipo de usuario, contactos ligados a un
 * usuario). Mientras la app esté en desarrollo se permite la migración destructiva; antes de
 * publicar hay que sustituirla por migraciones reales o se perderán los datos de los usuarios.
 */
@Database(
        entities = {UsuarioEntity.class, ContactoEmergenciaEntity.class},
        version = 2,
        exportSchema = false)
public abstract class BaseDatos extends RoomDatabase {

    private static volatile BaseDatos INSTANCE;

    public abstract UsuarioDao usuarioDao();

    public abstract ContactoDao contactoDao();

    public static BaseDatos getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (BaseDatos.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    BaseDatos.class, "calma_db")
                            .fallbackToDestructiveMigration(true)
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
