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
import com.example.myapplication.rutina.business.repository.EjercicioRepository;
import com.example.myapplication.rutina.data.models.Ejercicio;
import com.example.myapplication.usuario.presentation.trainer.ClienteActivity;
import com.example.myapplication.usuario.presentation.trainer.HomeTrainerActivity;
import com.example.myapplication.usuario.presentation.trainer.RegisterClientActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import java.util.List;

public class ListarEjerciciosActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private EjercicioAdapter ejercicioAdapter;
    private List<Ejercicio> ejercicioList;
    private EjercicioRepository ejercicioRepository;
    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listar_ejercicios);

        // Inicializar el repositorio
        ejercicioRepository = new EjercicioRepository(this);

        // Configurar RecyclerView
        recyclerView = findViewById(R.id.recycler_view_ejercicios);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Ejercicios");
        setSupportActionBar(toolbar);

        // Configurar el DrawerLayout
        drawerLayout = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.open_drawer, R.string.close_drawer);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        // Referencia del NavigationView y configurar el listener
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.nav_home) {
                Intent homeIntent = new Intent(ListarEjerciciosActivity.this, HomeTrainerActivity.class);
                startActivity(homeIntent);
            } else if (id == R.id.nav_rutinas) {
                Intent rutinaIntent = new Intent(ListarEjerciciosActivity.this, ListarRutinasActivity.class);
                startActivity(rutinaIntent);
            } else if (id == R.id.nav_clientes) {
                Intent clientIntent = new Intent(ListarEjerciciosActivity.this, ClienteActivity.class);
                startActivity(clientIntent);
            } else if (id == R.id.nav_ejercicios) {
                Intent ejercicioIntent = new Intent(ListarEjerciciosActivity.this,   ListarEjerciciosActivity.class);
                startActivity(ejercicioIntent);
            } else if (id == R.id.nav_categoria_ejercicio) {
                Intent categoriaEjercicioIntent = new Intent(ListarEjerciciosActivity.this, CategoriaEjercicioActivity.class);
                startActivity(categoriaEjercicioIntent);
            }

            // Cerrar el Drawer después de la selección
            drawerLayout.closeDrawers();
            return true;
        });

        // Obtener la lista de ejercicios desde el repositorio
        ejercicioList = ejercicioRepository.obtenerTodosLosEjercicios();

        // Configurar adapter con la lista de ejercicios
        ejercicioAdapter = new EjercicioAdapter(ejercicioList, new EjercicioAdapter.OnItemClickListener() {
            @Override
            public void onEditClick(Ejercicio ejercicio) {
                Intent intent = new Intent(ListarEjerciciosActivity.this, UpdateEjercicioActivity.class);
                intent.putExtra("idEjercicio", ejercicio.getIdEjercicio());  // Pasar el id del ejercicio
                startActivityForResult(intent, 1);  // Cambiar startActivity por startActivityForResult con un requestCode de 1
            }


            @Override
            public void onDeleteClick(Ejercicio ejercicio) {
                ejercicioRepository.eliminarEjercicio(ejercicio.getIdEjercicio());
                // Actualizar la lista
                ejercicioList = ejercicioRepository.obtenerTodosLosEjercicios();
                ejercicioAdapter.updateData(ejercicioList);
            }
        });
        recyclerView.setAdapter(ejercicioAdapter);

        // Acción para agregar un nuevo ejercicio
        FloatingActionButton fabAddEjercicio = findViewById(R.id.fab_add_ejercicio);
        fabAddEjercicio.setOnClickListener(v -> {
            Intent intent = new Intent(ListarEjerciciosActivity.this, CrearEjercicioActivity.class);
            startActivityForResult(intent, 2);  // Usamos un requestCode de 2 para distinguir entre creación y actualización
        });

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Verifica si el resultado es de UpdateEjercicioActivity o CrearEjercicioActivity y si fue exitoso
        if ((requestCode == 1 || requestCode == 2) && resultCode == Activity.RESULT_OK) {
            // Actualiza la lista de ejercicios
            ejercicioList = ejercicioRepository.obtenerTodosLosEjercicios();
            ejercicioAdapter.updateData(ejercicioList);  // Actualiza el RecyclerView con la nueva lista
        }
    }


}
