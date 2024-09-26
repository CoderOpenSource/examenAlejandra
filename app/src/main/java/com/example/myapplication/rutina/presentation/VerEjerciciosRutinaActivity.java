package com.example.myapplication.rutina.presentation;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.rutina.business.repository.EjercicioRepository;
import com.example.myapplication.rutina.business.repository.RutinaEjercicioRepository;
import com.example.myapplication.rutina.data.models.Ejercicio;
import com.example.myapplication.rutina.data.models.RutinaEjercicio;

import java.util.ArrayList;
import java.util.List;

public class VerEjerciciosRutinaActivity extends AppCompatActivity {

    private Spinner spinnerEjercicios;
    private EditText edtRepeticiones, edtSeries, edtDescanso;
    private Button btnAddEjercicio, btnGuardarCambios;
    private RecyclerView recyclerViewEjerciciosRutina;

    private RutinaEjercicioRepository rutinaEjercicioRepository;
    private EjercicioRepository ejercicioRepository;

    private List<Ejercicio> listaEjercicios;
    private List<RutinaEjercicio> rutinaEjerciciosList = new ArrayList<>();
    private List<RutinaEjercicio> ejerciciosParaEliminar = new ArrayList<>();
    private RutinaEjercicioAdapter2 rutinaEjercicioAdapter;
    private int idRutina;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_ejercicios_rutina);

        // Configurar la toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        toolbar.setTitle("Ver y Agregar Ejercicios");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Inicializar vistas
        spinnerEjercicios = findViewById(R.id.spinner_ejercicios);
        edtRepeticiones = findViewById(R.id.edt_repeticiones);
        edtSeries = findViewById(R.id.edt_series);
        edtDescanso = findViewById(R.id.edt_descanso);
        btnAddEjercicio = findViewById(R.id.btn_add_ejercicio);
        btnGuardarCambios = findViewById(R.id.btn_guardar_cambios);
        recyclerViewEjerciciosRutina = findViewById(R.id.recycler_view_ejercicios_rutina);

        // Repositorios
        rutinaEjercicioRepository = new RutinaEjercicioRepository(this);
        ejercicioRepository = new EjercicioRepository(this);

        // Obtener el idRutina del intent
        idRutina = getIntent().getIntExtra("idRutina", -1);

        // Cargar los ejercicios en el spinner
        listaEjercicios = ejercicioRepository.obtenerTodosLosEjercicios();
        cargarEjerciciosEnSpinner();



        // Configurar RecyclerView para mostrar los ejercicios añadidos
        rutinaEjercicioAdapter = new RutinaEjercicioAdapter2(rutinaEjerciciosList, rutinaEjercicio -> {
            // Marcar para eliminar si ya está en la base de datos
            if (rutinaEjercicio.getIdRutina() != 0) {
                ejerciciosParaEliminar.add(rutinaEjercicio);
            }
            rutinaEjercicioAdapter.eliminarEjercicio(rutinaEjercicio);
        });
        recyclerViewEjerciciosRutina.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewEjerciciosRutina.setAdapter(rutinaEjercicioAdapter);

        // Cargar ejercicios ya existentes en la rutina
        cargarEjerciciosDeLaRutina(idRutina);

        // Manejar la adición de nuevos ejercicios
        btnAddEjercicio.setOnClickListener(v -> addEjercicioToRutina());

        // Manejar el guardado de todos los cambios
        btnGuardarCambios.setOnClickListener(v -> guardarCambios());
    }

    private void cargarEjerciciosEnSpinner() {
        List<String> nombresEjercicios = new ArrayList<>();
        for (Ejercicio ejercicio : listaEjercicios) {
            nombresEjercicios.add(ejercicio.getNombre());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, nombresEjercicios);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerEjercicios.setAdapter(adapter);
    }

    private void cargarEjerciciosDeLaRutina(int idRutina) {
        rutinaEjerciciosList.clear();
        rutinaEjerciciosList.addAll(rutinaEjercicioRepository.obtenerRutinaEjerciciosPorRutinaId(idRutina));
        rutinaEjercicioAdapter.notifyDataSetChanged();
    }

    private void addEjercicioToRutina() {
        // Obtener el ejercicio seleccionado
        int selectedExercisePos = spinnerEjercicios.getSelectedItemPosition();
        Ejercicio selectedExercise = listaEjercicios.get(selectedExercisePos);

        // Validar los campos
        String repeticionesStr = edtRepeticiones.getText().toString().trim();
        String seriesStr = edtSeries.getText().toString().trim();
        String descansoStr = edtDescanso.getText().toString().trim();

        if (TextUtils.isEmpty(repeticionesStr) || TextUtils.isEmpty(seriesStr) || TextUtils.isEmpty(descansoStr)) {
            Toast.makeText(this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        // Convertir los valores ingresados
        int repeticiones = Integer.parseInt(repeticionesStr);
        int series = Integer.parseInt(seriesStr);
        int descanso = Integer.parseInt(descansoStr);

        // Crear el nuevo objeto RutinaEjercicio y añadirlo a la lista
        RutinaEjercicio nuevoEjercicio = new RutinaEjercicio(idRutina, selectedExercise.getIdEjercicio(), repeticiones, series, descanso);
        rutinaEjerciciosList.add(nuevoEjercicio);
        rutinaEjercicioAdapter.notifyDataSetChanged();

        // Limpiar los campos de entrada
        edtRepeticiones.setText("");
        edtSeries.setText("");
        edtDescanso.setText("");
    }

    private void guardarCambios() {
        // Eliminar los ejercicios que fueron marcados para eliminar
        for (RutinaEjercicio ejercicioParaEliminar : ejerciciosParaEliminar) {
            rutinaEjercicioRepository.eliminarRutinaEjercicio(ejercicioParaEliminar.getIdRutina(), ejercicioParaEliminar.getIdEjercicio());
        }

        // Guardar los ejercicios nuevos y existentes
        for (RutinaEjercicio nuevoEjercicio : rutinaEjerciciosList) {
            if (nuevoEjercicio.getIdRutina() == idRutina) {
                rutinaEjercicioRepository.insertarRutinaEjercicio(nuevoEjercicio);
            }
        }

        Toast.makeText(this, "Cambios guardados con éxito", Toast.LENGTH_SHORT).show();
        finish();
    }
}
