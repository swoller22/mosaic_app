package com.example.matthew.mosaic.database;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.arch.persistence.room.migration.Migration;
import android.content.Context;

import com.example.matthew.mosaic.LoginActivity;
import com.example.matthew.mosaic.database.entity.User;

@Database(entities = {User.class}, version = 1, exportSchema = false )
public abstract class UserDatabase extends RoomDatabase {
    private static UserDatabase INSTANCE;
    public abstract UserDao userDao();

    public static UserDatabase getAppDatabase(Context context) {
        if (INSTANCE == null) {
            INSTANCE =
                    Room.databaseBuilder(context.getApplicationContext(), UserDatabase.class, "user-database")
                            .build();
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }
}