package com.example.nasa;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities={Nasa.class}, version=2)
public abstract class NasaDB extends RoomDatabase {
    abstract NasaGalery nasa_galery();

    private static final String DB_NAME = "nasa_galery.db";
    private static volatile NasaDB INSTANCE = null;

    synchronized static NasaDB get(Context ctxt) {

        if (INSTANCE == null) {
            INSTANCE = create(ctxt, false);
        }
        return (INSTANCE);
    }

    static NasaDB create(Context ctxt, boolean memoryOnly) {
        RoomDatabase.Builder<NasaDB> b;
        if (memoryOnly) {
            b = Room.inMemoryDatabaseBuilder(ctxt.getApplicationContext(),
                    NasaDB.class);
        } else {
            b = Room.databaseBuilder(ctxt.getApplicationContext(), NasaDB.class,
                    DB_NAME);
        }
        return (b.build());

    }
}