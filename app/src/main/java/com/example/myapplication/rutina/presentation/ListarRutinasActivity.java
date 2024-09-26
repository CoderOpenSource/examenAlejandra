package com.example.myapplication.rutina.presentation;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.rutina.business.repository.RutinaRepository;
import com.example.myapplication.rutina.data.models.Rutina;
import com.example.myapplication.usuario.presentation.trainer.ClienteActivity;
import com.example.myapplication.usuario.presentation.trainer.HomeTrainerActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import java.util.List;

public class ListarRutinasActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RutinaAdapter rutinaAdapter;
    private List<Rutina> rutinaList;
    private RutinaRepository rutinaRepository;
    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listar_rutinas);

        // Initialize repository
        rutinaRepository = new RutinaRepository(this);

        // Set up RecyclerView
        recyclerView = findViewById(R.id.recycler_view_rutinas);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Rutinas");
        setSupportActionBar(toolbar);

        // Set up DrawerLayout
        drawerLayout = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.open_drawer, R.string.close_drawer);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        // Navigation menu
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_home) {
                Intent homeIntent = new Intent(ListarRutinasActivity.this, HomeTrainerActivity.class);
                startActivity(homeIntent);
            } else if (id == R.id.nav_clientes) {
                Intent clientIntent = new Intent(ListarRutinasActivity.this,   ClienteActivity.class);
                startActivity(clientIntent);
            } else if (id == R.id.nav_ejercicios) {
                Intent ejercicioIntent = new Intent(ListarRutinasActivity.this,   ListarEjerciciosActivity.class);
                startActivity(ejercicioIntent);
            } else if (id == R.id.nav_categoria_ejercicio) {
                Intent categoriaEjercicioIntent = new Intent(ListarRutinasActivity.this, CategoriaEjercicioActivity.class);
                startActivity(categoriaEjercicioIntent);
            }
            // Add other navigation items here
            drawerLayout.closeDrawers();
            return true;
        });

        // Fetch and display Rutinas
        rutinaList = rutinaRepository.obtenerTodasLasRutinas();
        rutinaAdapter = new RutinaAdapter(rutinaList, new RutinaAdapter.OnItemClickListener() {
            @Override
            public void onEditClick(Rutina rutina) {
                Intent intent = new Intent(ListarRutinasActivity.this, UpdateRutinaActivity.class);
                intent.putExtra("idRutina", rutina.getIdRutina());
                startActivityForResult(intent, 1);
            }

            @Override
            public void onDeleteClick(Rutina rutina) {
                rutinaRepository.eliminarRutina(rutina.getIdRutina());
                refreshRutinas();
            }
        });
        recyclerView.setAdapter(rutinaAdapter);

        // FAB for adding a new Rutina
        FloatingActionButton fabAddRutina = findViewById(R.id.fab_add_rutina);
        fabAddRutina.setOnClickListener(v -> {
            Intent intent = new Intent(ListarRutinasActivity.this, CrearRutinaActivity.class);
            startActivityForResult(intent, 2);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Si se creó una nueva rutina o se editaron ejercicios, refrescar la lista
        if (resultCode == Activity.RESULT_OK) {
            refreshRutinas();  // Actualizar la lista de rutinas
        }
    }

    private void refreshRutinas() {
        rutinaList = rutinaRepository.obtenerTodasLasRutinas();
        rutinaAdapter.updateData(rutinaList);  // Método para actualizar los datos en el adapter
    }

}
