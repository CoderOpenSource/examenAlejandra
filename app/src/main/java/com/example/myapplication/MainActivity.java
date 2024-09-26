package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import com.example.myapplication.usuario.presentation.trainer.*;
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Redireccionar a la actividad HomeTrainerActivity
        Intent intent = new Intent(MainActivity.this,HomeTrainerActivity.class);
        startActivity(intent);
        finish(); // Terminar MainActivity para que no esté en el stack de actividades
    }
}