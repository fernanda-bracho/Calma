package com.example.calmafinal.data;

import android.content.Context;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import com.example.calmafinal.ContactoEmergencia;

@Database(entities = {Usuario.class, ContactoEmergencia.class}, version = 1, exportSchema = false)
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