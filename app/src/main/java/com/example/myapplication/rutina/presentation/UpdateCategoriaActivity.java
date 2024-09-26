package com.example.myapplication.rutina.presentation;


import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.myapplication.R;
import com.example.myapplication.rutina.business.repository.CategoriaRepository;
import com.example.myapplication.rutina.data.models.Categoria;

public class UpdateCategoriaActivity extends AppCompatActivity {

    private EditText edtNombreCategoria, edtDescripcionCategoria;
    private ImageView btnEditCategoria, btnSaveCategoria;
    private CategoriaRepository categoriaRepository;
    private Categoria categoriaActual; // Para guardar la categoría actual

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activiy_update_categoria_ejercicio); // Asegúrate de usar el nuevo archivo XML

        // Inicializar los campos de texto y botones
        edtNombreCategoria = findViewById(R.id.edt_nombre_categoria);
        edtDescripcionCategoria = findViewById(R.id.edt_descripcion_categoria);
        btnEditCategoria = findViewById(R.id.btn_edit_categoria);
        btnSaveCategoria = findViewById(R.id.btn_save_categoria);

        // Configurar la toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        toolbar.setTitle("Actualizar Categoría");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);  // Botón de "volver"
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.baseline_arrow_back_24);

        // Inicializar el repositorio de categorías
        categoriaRepository = new CategoriaRepository(this);

        // Obtener el ID de la categoría a editar desde el intent
        long categoriaId = getIntent().getLongExtra("categoria_id", -1);

        if (categoriaId != -1) {
            // Cargar la categoría desde el repositorio
            categoriaActual = categoriaRepository.obtenerCategoriaPorId((int)categoriaId);

            if (categoriaActual != null) {
                // Mostrar los datos de la categoría en los campos de texto
                edtNombreCategoria.setText(categoriaActual.getNombre());
                edtDescripcionCategoria.setText(categoriaActual.getDescripcion());
            }
        }

        // Acción para el botón de editar
        btnEditCategoria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Hacer los EditText editables
                edtNombreCategoria.setEnabled(true);
                edtDescripcionCategoria.setEnabled(true);
                // Mostrar el botón de guardar
                btnSaveCategoria.setVisibility(View.VISIBLE);
            }
        });

        // Acción para el botón de guardar
        btnSaveCategoria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Actualizar la categoría con los nuevos valores
                String nuevoNombre = edtNombreCategoria.getText().toString().trim();
                String nuevaDescripcion = edtDescripcionCategoria.getText().toString().trim();

                if (nuevoNombre.isEmpty() || nuevaDescripcion.isEmpty()) {
                    Toast.makeText(UpdateCategoriaActivity.this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show();
                } else {
                    // Actualizar el objeto categoría
                    categoriaActual.setNombre(nuevoNombre);
                    categoriaActual.setDescripcion(nuevaDescripcion);

                    // Actualizar la categoría en la base de datos
                    int resultado = categoriaRepository.actualizarCategoria(categoriaActual);

                    if (resultado > 0) {
                        Toast.makeText(UpdateCategoriaActivity.this, "Categoría actualizada con éxito", Toast.LENGTH_SHORT).show();
                        // Volver a la lista de categorías
                        Intent intent = new Intent(UpdateCategoriaActivity.this, CategoriaEjercicioActivity.class);
                        startActivity(intent);
                        finish(); // Finalizar esta actividad para que no se regrese aquí
                    } else {
                        Toast.makeText(UpdateCategoriaActivity.this, "Error al actualizar la categoría", Toast.LENGTH_SHORT).show();
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
