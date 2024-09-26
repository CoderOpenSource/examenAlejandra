package com.example.myapplication.usuario.presentation.trainer;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.myapplication.R;
import com.example.myapplication.rutina.presentation.CategoriaEjercicioActivity;
import com.example.myapplication.rutina.presentation.ListarEjerciciosActivity;
import com.example.myapplication.rutina.presentation.ListarRutinasActivity;
import com.google.android.material.navigation.NavigationView;

public class HomeTrainerActivity extends AppCompatActivity{

    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_trainer);

        // Configurar el Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Configurar el DrawerLayout
        drawerLayout = findViewById(R.id.drawer_layout);

        // Configurar el ActionBarDrawerToggle
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar ,R.string.open_drawer, R.string.close_drawer);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        // Referencia del NavigationView y configurar el listener
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Listener para manejar los clics en el NavigationView
        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.nav_home) {
                // Acción para el ítem "Inicio"
                Intent homeIntent = new Intent(HomeTrainerActivity.this, HomeTrainerActivity.class);
                startActivity(homeIntent);
            }else if (id == R.id.nav_rutinas) {
                // Acción para el ítem "Clientes"
                Intent rutinaIntent = new Intent(HomeTrainerActivity.this, ListarRutinasActivity.class);
                startActivity(rutinaIntent);
            } else if (id == R.id.nav_clientes) {
                // Acción para el ítem "Clientes"
                Intent clientIntent = new Intent(HomeTrainerActivity.this, ClienteActivity.class);
                startActivity(clientIntent);
            } else if (id == R.id.nav_ejercicios) {
                // Acción para el ítem "Ejercicios"
                    Intent ejercicioIntent = new Intent(HomeTrainerActivity.this,   ListarEjerciciosActivity.class);
                startActivity(ejercicioIntent);
            } else if (id == R.id.nav_categoria_ejercicio) {
                // Acción para el ítem "Categoría Ejercicio"
                Intent categoriaEjercicioIntent = new Intent(HomeTrainerActivity.this, CategoriaEjercicioActivity.class);
                startActivity(categoriaEjercicioIntent);
            }

            // Cerrar el Drawer después de la selección
            drawerLayout.closeDrawers();
            return true;
        });


    }

    // Método para manejar la navegación en el NavigationView


    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
}
