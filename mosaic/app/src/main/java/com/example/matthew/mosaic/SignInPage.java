package com.example.matthew.mosaic;

import com.example.matthew.mosaic.database.UserDB;
import android.arch.persistence.room.Room;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class SignInPage extends AppCompatActivity {
    private UserDB database;
    Integer hi;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in_page);

        // create database
        database = Room.databaseBuilder(getApplicationContext(), UserDB.class, "USERDB").build();
    }
}
