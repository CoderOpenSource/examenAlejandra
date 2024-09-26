package com.example.myapplication.rutina.presentation;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
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

public class AddExerciseToRoutineActivity extends AppCompatActivity {

    private Spinner spinnerEjercicios;
    private EditText edtRepeticiones, edtSeries, edtDescanso;
    private Button btnAddEjercicio, btnGuardarRutina;
    private RecyclerView recyclerViewEjerciciosRutina;

    private RutinaEjercicioRepository rutinaEjercicioRepository;
    private EjercicioRepository ejercicioRepository;

    private List<Ejercicio> listaEjercicios;
    private List<RutinaEjercicio> rutinaEjerciciosList = new ArrayList<>();
    private RutinaEjercicioAdapter rutinaEjercicioAdapter;
    private int idRutina;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_exercise_routine);

        // Configurar la toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        toolbar.setTitle("Crear RUTINA_EJERCICIO");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);  // Botón de "volver"
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.baseline_arrow_back_24);

        // Inicializar vistas
        spinnerEjercicios = findViewById(R.id.spinner_ejercicios);
        edtRepeticiones = findViewById(R.id.edt_repeticiones);
        edtSeries = findViewById(R.id.edt_series);
        edtDescanso = findViewById(R.id.edt_descanso);
        btnAddEjercicio = findViewById(R.id.btn_add_ejercicio);
        btnGuardarRutina = findViewById(R.id.btn_guardar_rutina);  // Nuevo botón para guardar la rutina
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
        rutinaEjercicioAdapter = new RutinaEjercicioAdapter(rutinaEjerciciosList);
        recyclerViewEjerciciosRutina.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewEjerciciosRutina.setAdapter(rutinaEjercicioAdapter);

        // Manejar la adición de ejercicios
        btnAddEjercicio.setOnClickListener(v -> addEjercicioToRutina());

        // Manejar el guardado de todos los ejercicios
        btnGuardarRutina.setOnClickListener(v -> guardarRutinaEjercicios());
    }

    private void cargarEjerciciosEnSpinner() {
        // Cargar los ejercicios en el spinner
        List<String> nombresEjercicios = new ArrayList<>();
        for (Ejercicio ejercicio : listaEjercicios) {
            nombresEjercicios.add(ejercicio.getNombre());
        }

        // Usar ArrayAdapter para poblar el spinner con los nombres de los ejercicios
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, nombresEjercicios);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerEjercicios.setAdapter(adapter);
    }

    private void addEjercicioToRutina() {
        // Obtener el ejercicio seleccionado
        int selectedExercisePos = spinnerEjercicios.getSelectedItemPosition();
        Ejercicio selectedExercise = listaEjercicios.get(selectedExercisePos);

        // Verificar si el ejercicio ya está en la rutina
        for (RutinaEjercicio re : rutinaEjerciciosList) {
            if (re.getIdEjercicio() == selectedExercise.getIdEjercicio()) {
                Toast.makeText(this, "Este ejercicio ya está en la rutina", Toast.LENGTH_SHORT).show();
                return;
            }
        }

        // Obtener los valores ingresados por el usuario
        String repeticionesStr = edtRepeticiones.getText().toString().trim();
        String seriesStr = edtSeries.getText().toString().trim();
        String descansoStr = edtDescanso.getText().toString().trim();

        if (TextUtils.isEmpty(repeticionesStr) || TextUtils.isEmpty(seriesStr) || TextUtils.isEmpty(descansoStr)) {
            Toast.makeText(this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        // Convertir las entradas a enteros
        int repeticiones = Integer.parseInt(repeticionesStr);
        int series = Integer.parseInt(seriesStr);
        int descanso = Integer.parseInt(descansoStr);

        // Crear el nuevo objeto RutinaEjercicio y añadirlo a la lista
        RutinaEjercicio nuevaRutinaEjercicio = new RutinaEjercicio(idRutina, selectedExercise.getIdEjercicio(), repeticiones, series, descanso);
        rutinaEjerciciosList.add(nuevaRutinaEjercicio);
        rutinaEjercicioAdapter.notifyDataSetChanged();  // Actualizar el RecyclerView

        // Limpiar los campos de entrada
        edtRepeticiones.setText("");
        edtSeries.setText("");
        edtDescanso.setText("");
    }

    private void guardarRutinaEjercicios() {
        // Guardar todos los ejercicios de la rutina en la base de datos
        for (RutinaEjercicio rutinaEjercicio : rutinaEjerciciosList) {
            rutinaEjercicioRepository.insertarRutinaEjercicio(rutinaEjercicio);
        }

        // Devolver el resultado a la actividad anterior
        Intent resultIntent = new Intent();
        setResult(Activity.RESULT_OK, resultIntent);

        // Notificar al usuario y terminar la actividad
        Toast.makeText(this, "Rutina guardada con éxito", Toast.LENGTH_SHORT).show();
        finish();
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
