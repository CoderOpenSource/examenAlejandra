package com.example.myapplication.rutina.presentation;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.myapplication.R;
import com.example.myapplication.rutina.business.repository.CategoriaRepository;
import com.example.myapplication.rutina.business.repository.EjercicioRepository;
import com.example.myapplication.rutina.business.repository.ImageSource;
import com.example.myapplication.rutina.data.models.Categoria;
import com.example.myapplication.rutina.data.models.Ejercicio;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class UpdateEjercicioActivity extends AppCompatActivity {
    private ImageView ivImagePreview;
    private Button btnCamera, btnGallery;
    private String multimediaPath;
    private ImageSource imageSource;

    private EditText edtNombre, edtPosicion, edtEjecucion, edtEquipo, edtMultimedia;
    private Spinner spinnerCategoria;
    private Button btnActualizarEjercicio;
    private EjercicioRepository ejercicioRepository;
    private CategoriaRepository categoriaRepository;
    private List<Categoria> listaCategorias;
    private int idEjercicio;  // ID del ejercicio a actualizar

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_ejercicio);

        // Configurar la toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        toolbar.setTitle("Actualizar Ejercicio");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);  // Botón de "volver"
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.baseline_arrow_back_24);

        // Inicializar los campos de texto y botones
        edtNombre = findViewById(R.id.edt_nombre_ejercicio);
        edtPosicion = findViewById(R.id.edt_posicion_ejercicio);
        edtEjecucion = findViewById(R.id.edt_ejecucion_ejercicio);
        edtEquipo = findViewById(R.id.edt_equipo_ejercicio);
        ivImagePreview = findViewById(R.id.iv_image_preview);
        btnCamera = findViewById(R.id.btn_camera);
        btnGallery = findViewById(R.id.btn_gallery);

// Inicializar ImageSource para manejar la cámara y galería
        imageSource = new ImageSource(this, cameraLauncher, galleryLauncher);

// Configurar listeners para los botones de cámara y galería
        btnCamera.setOnClickListener(v -> imageSource.openCamera());
        btnGallery.setOnClickListener(v -> imageSource.openGallery());
        spinnerCategoria = findViewById(R.id.spinner_categoria);
        btnActualizarEjercicio = findViewById(R.id.btn_actualizar_ejercicio);

        // Inicializar repositorios
        ejercicioRepository = new EjercicioRepository(this);
        categoriaRepository = new CategoriaRepository(this);

        // Obtener el ID del ejercicio a actualizar
        idEjercicio = getIntent().getIntExtra("idEjercicio", -1);

        if (idEjercicio != -1) {
            cargarEjercicio(idEjercicio);  // Cargar los datos del ejercicio en los campos
        }

        // Cargar categorías en el Spinner
        cargarCategorias();

        btnActualizarEjercicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nombre = edtNombre.getText().toString().trim();
                String posicion = edtPosicion.getText().toString().trim();
                String ejecucion = edtEjecucion.getText().toString().trim();
                String equipo = edtEquipo.getText().toString().trim();

                if (nombre.isEmpty() || posicion.isEmpty() || ejecucion.isEmpty() || equipo.isEmpty()) {
                    Toast.makeText(UpdateEjercicioActivity.this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show();
                } else {
                    // Obtener la categoría seleccionada del Spinner
                    Categoria categoriaSeleccionada = listaCategorias.get(spinnerCategoria.getSelectedItemPosition());

                    // Aquí solo usamos multimediaPath, ya no hay edtMultimedia
                    Ejercicio ejercicioActualizado = new Ejercicio(idEjercicio, nombre, posicion, ejecucion, equipo, multimediaPath, categoriaSeleccionada.getIdCategoria());

                    // Actualizar el ejercicio en la base de datos
                    int resultado = ejercicioRepository.actualizarEjercicio(ejercicioActualizado);

                    if (resultado > 0) {
                        Toast.makeText(UpdateEjercicioActivity.this, "Ejercicio actualizado con éxito", Toast.LENGTH_SHORT).show();

                        // Devolver un resultado indicando que se actualizó un ejercicio
                        Intent resultIntent = new Intent();
                        setResult(Activity.RESULT_OK, resultIntent);

                        finish(); // Volver a la lista anterior
                    } else {
                        Toast.makeText(UpdateEjercicioActivity.this, "Error al actualizar el ejercicio", Toast.LENGTH_SHORT).show();
                    }

                }
            }
        });

    }

    private void cargarEjercicio(int idEjercicio) {
        Ejercicio ejercicio = ejercicioRepository.obtenerEjercicioPorId(idEjercicio);

        if (ejercicio != null) {
            edtNombre.setText(ejercicio.getNombre());
            edtPosicion.setText(ejercicio.getPosicion());
            edtEjecucion.setText(ejercicio.getEjecucion());
            edtEquipo.setText(ejercicio.getEquipo());

            // Cargar la imagen si hay un multimedia asociado
            multimediaPath = ejercicio.getMultimedia();  // Guardar la ruta multimedia

            if (multimediaPath != null && !multimediaPath.isEmpty()) {
                Bitmap bitmap = BitmapFactory.decodeFile(multimediaPath);
                ivImagePreview.setImageBitmap(bitmap);
                ivImagePreview.setVisibility(View.VISIBLE);  // Mostrar la imagen cargada
            }
        }
    }


    private void cargarCategorias() {
        listaCategorias = categoriaRepository.obtenerTodasLasCategorias();

        List<String> nombresCategorias = new ArrayList<>();
        for (Categoria categoria : listaCategorias) {
            nombresCategorias.add(categoria.getNombre());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, nombresCategorias);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategoria.setAdapter(adapter);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();  // Volver a la actividad anterior
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    private final ActivityResultLauncher<Intent> cameraLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    Intent data = result.getData();
                    if (data != null && data.getExtras() != null) {
                        Bitmap imageBitmap = (Bitmap) data.getExtras().get("data");
                        imageBitmap = imageSource.compressImage(imageBitmap);
                        ivImagePreview.setImageBitmap(imageBitmap);
                        ivImagePreview.setVisibility(View.VISIBLE);
                        String imagePath = imageSource.saveImageToStorage(imageBitmap);  // Guardar imagen y obtener el path
                        multimediaPath = imagePath;  // Guardar el path en la variable
                    }
                }
            }
    );

    private final ActivityResultLauncher<Intent> galleryLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    Uri selectedImageUri = result.getData().getData();
                    if (selectedImageUri != null) {
                        try {
                            InputStream inputStream = getContentResolver().openInputStream(selectedImageUri);
                            Bitmap imageBitmap = BitmapFactory.decodeStream(inputStream);
                            imageBitmap = imageSource.compressImage(imageBitmap);
                            ivImagePreview.setImageBitmap(imageBitmap);
                            ivImagePreview.setVisibility(View.VISIBLE);
                            String imagePath = imageSource.saveImageToStorage(imageBitmap);  // Guardar imagen y obtener el path
                            multimediaPath = imagePath;  // Guardar el path en la variable
                            inputStream.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
    );

}
