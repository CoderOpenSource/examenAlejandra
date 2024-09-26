package com.example.myapplication.rutina.presentation;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.myapplication.R;
import com.example.myapplication.rutina.business.repository.CategoriaRepository;
import com.example.myapplication.rutina.data.models.Categoria;

public class CrearCategoriaActivity extends AppCompatActivity {

    private EditText edtNombreCategoria, edtDescripcionCategoria;
    private Button btnCrearCategoria;
    private CategoriaRepository categoriaRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_categoria_ejercicio);

        // Inicializar los campos de texto y el botón
        edtNombreCategoria = findViewById(R.id.edt_nombre_categoria);
        edtDescripcionCategoria = findViewById(R.id.edt_descripcion_categoria);
        btnCrearCategoria = findViewById(R.id.btn_crear_categoria);

        // Configurar la toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        toolbar.setTitle("Crear Categoria");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);  // Botón de "volver"
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.baseline_arrow_back_24);


        // Inicializar el repositorio de categorías
        categoriaRepository = new CategoriaRepository(this);

        // Configurar el botón para crear la categoría
        btnCrearCategoria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nombre = edtNombreCategoria.getText().toString().trim();
                String descripcion = edtDescripcionCategoria.getText().toString().trim();

                if (nombre.isEmpty() || descripcion.isEmpty()) {
                    Toast.makeText(CrearCategoriaActivity.this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show();
                } else {
                    // Crear el objeto Categoria
                    Categoria nuevaCategoria = new Categoria();
                    nuevaCategoria.setNombre(nombre);
                    nuevaCategoria.setDescripcion(descripcion);

                    // Insertar la nueva categoría en la base de datos
                    long resultado = categoriaRepository.insertarCategoria(nuevaCategoria);

                    if (resultado != -1) {
                        Toast.makeText(CrearCategoriaActivity.this, "Categoría creada con éxito", Toast.LENGTH_SHORT).show();
                        // Volver a la lista de categorías (CategoriaEjercicioActivity)
                        Intent intent = new Intent(CrearCategoriaActivity.this, CategoriaEjercicioActivity.class);
                        startActivity(intent);
                        finish(); // Finalizar esta actividad para que no se regrese aquí
                    } else {
                        Toast.makeText(CrearCategoriaActivity.this, "Error al crear la categoría", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();  // Volver a la actividad anterior
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}