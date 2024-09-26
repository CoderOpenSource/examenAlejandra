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

public class CrearEjercicioActivity extends AppCompatActivity {

    private EditText edtNombre, edtPosicion, edtEjecucion, edtEquipo, edtMultimedia;
    private Spinner spinnerCategoria;
    private Button btnCrearEjercicio;
    private EjercicioRepository ejercicioRepository;
    private CategoriaRepository categoriaRepository;
    private List<Categoria> listaCategorias;
    private ImageSource imageSource;
    private ImageView ivImagePreview;
    private String multimediaPath;
    private Button btnCamera;
    private Button btnGallery;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_ejercicio);

        // Configurar la toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        toolbar.setTitle("Crear Ejercicio");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);  // Botón de "volver"
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.baseline_arrow_back_24);

        imageSource = new ImageSource(this, cameraLauncher, galleryLauncher);

        // Inicializar los campos de texto y botones
        edtNombre = findViewById(R.id.edt_nombre_ejercicio);
        edtPosicion = findViewById(R.id.edt_posicion_ejercicio);
        edtEjecucion = findViewById(R.id.edt_ejecucion_ejercicio);
        edtEquipo = findViewById(R.id.edt_equipo_ejercicio);
        spinnerCategoria = findViewById(R.id.spinner_categoria);
        btnCrearEjercicio = findViewById(R.id.btn_crear_ejercicio);
        ivImagePreview = findViewById(R.id.iv_image_preview);
        btnCamera = findViewById(R.id.btn_camera);
        btnGallery = findViewById(R.id.btn_gallery);
        // Inicializar repositorios
        ejercicioRepository = new EjercicioRepository(this);
        categoriaRepository = new CategoriaRepository(this);
        // Configurar listeners para los botones de cámara y galería
        btnCamera.setOnClickListener(v -> imageSource.openCamera());
        btnGallery.setOnClickListener(v -> imageSource.openGallery());
        // Cargar categorías en el Spinner
        cargarCategorias();

        // Acción para el botón de crear ejercicio
        btnCrearEjercicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nombre = edtNombre.getText().toString().trim();
                String posicion = edtPosicion.getText().toString().trim();
                String ejecucion = edtEjecucion.getText().toString().trim();
                String equipo = edtEquipo.getText().toString().trim();

                if (nombre.isEmpty() || posicion.isEmpty() || ejecucion.isEmpty() || equipo.isEmpty() || multimediaPath == null) {
                    Toast.makeText(CrearEjercicioActivity.this, "Por favor, completa todos los campos y selecciona una imagen", Toast.LENGTH_SHORT).show();
                } else {
                    // Obtener la categoría seleccionada del Spinner
                    Categoria categoriaSeleccionada = listaCategorias.get(spinnerCategoria.getSelectedItemPosition());

                    // Crear el objeto Ejercicio, pasando el path de la imagen (multimediaPath)
                    Ejercicio nuevoEjercicio = new Ejercicio(0, nombre, posicion, ejecucion, equipo, multimediaPath, categoriaSeleccionada.getIdCategoria());

                    // Insertar el nuevo ejercicio en la base de datos
                    long resultado = ejercicioRepository.insertarEjercicio(nuevoEjercicio);

                    if (resultado != -1) {
                        Toast.makeText(CrearEjercicioActivity.this, "Ejercicio creado con éxito", Toast.LENGTH_SHORT).show();

                        // Devolver un resultado indicando que se creó un ejercicio
                        Intent resultIntent = new Intent();
                        setResult(Activity.RESULT_OK, resultIntent);

                        finish(); // Volver a la lista anterior
                    } else {
                        Toast.makeText(CrearEjercicioActivity.this, "Error al crear el ejercicio", Toast.LENGTH_SHORT).show();
                    }

                }
            }
        });

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
