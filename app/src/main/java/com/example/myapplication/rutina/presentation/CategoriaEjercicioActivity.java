package com.example.myapplication.rutina.presentation;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.rutina.business.repository.CategoriaRepository;
import com.example.myapplication.rutina.data.models.Categoria;
import com.example.myapplication.rutina.presentation.CategoriaAdapter;
import com.example.myapplication.usuario.presentation.trainer.HomeTrainerActivity;
import com.example.myapplication.usuario.presentation.trainer.RegisterClientActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import java.util.List;

public class CategoriaEjercicioActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private CategoriaAdapter categoriaAdapter;
    private List<Categoria> categoriaList;
    private CategoriaRepository categoriaRepository; // Referencia al repositorio
    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categoria_ejercicio);


        // Inicializar el repositorio
        categoriaRepository = new CategoriaRepository(this);

        // Configurar RecyclerView
        recyclerView = findViewById(R.id.recycler_view_categorias);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Categoria Ejercicios");
        setSupportActionBar(toolbar);

        // Configurar el DrawerLayout
        drawerLayout = findViewById(R.id.drawer_layout);

        // Configurar el ActionBarDrawerToggle
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.open_drawer, R.string.close_drawer);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        // Referencia del NavigationView y configurar el listener
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.nav_home) {
                Intent homeIntent = new Intent(CategoriaEjercicioActivity.this, HomeTrainerActivity.class);
                startActivity(homeIntent);
            } else if (id == R.id.nav_clientes) {
                Intent clientIntent = new Intent(CategoriaEjercicioActivity.this, RegisterClientActivity.class);
                startActivity(clientIntent);
            } else if (id == R.id.nav_ejercicios) {
                Intent ejercicioIntent = new Intent(CategoriaEjercicioActivity.this,   ListarEjerciciosActivity.class);
                startActivity(ejercicioIntent);
            } else if (id == R.id.nav_categoria_ejercicio) {
                Intent categoriaEjercicioIntent = new Intent(CategoriaEjercicioActivity.this, CategoriaEjercicioActivity.class);
                startActivity(categoriaEjercicioIntent);
            }

            // Cerrar el Drawer después de la selección
            drawerLayout.closeDrawers();
            return true;
        });

        // Obtener la lista de categorías desde el repositorio
        categoriaList = categoriaRepository.obtenerTodasLasCategorias();

        // Configurar adapter con la lista de categorías
        categoriaAdapter = new CategoriaAdapter(categoriaList, new CategoriaAdapter.OnItemClickListener() {
            @Override
            public void onEditClick(Categoria categoria) {
                // Crear el intent para ir a la pantalla de actualización
                Intent intent = new Intent(CategoriaEjercicioActivity.this, UpdateCategoriaActivity.class);
                // Pasar el ID de la categoría como extra
                intent.putExtra("categoria_id", (long) categoria.getIdCategoria());
                // Iniciar la actividad de actualización
                startActivity(intent);
            }


            @Override
            public void onDeleteClick(Categoria categoria) {
                categoriaRepository.eliminarCategoria(categoria.getIdCategoria());
                // Volver a obtener la lista de categorías actualizada
                categoriaList = categoriaRepository.obtenerTodasLasCategorias();

                // Notificar al adapter que los datos han cambiado
                categoriaAdapter.updateData(categoriaList);
            }
        });
        // metodo para recargar
        recyclerView.setAdapter(categoriaAdapter);

        FloatingActionButton fabAddCategoria = findViewById(R.id.fab_add_categoria);
        fabAddCategoria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CategoriaEjercicioActivity.this, CrearCategoriaActivity.class);
                startActivity(intent);
            }
        });
    }
}