package com.example.myapplication.rutina.presentation;

import android.app.Activity;
import android.app.DatePickerDialog;
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

import com.example.myapplication.R;
import com.example.myapplication.rutina.business.repository.RutinaRepository;
import com.example.myapplication.usuario.business.repository.ClienteRepository;
import com.example.myapplication.usuario.data.models.Cliente;
import com.example.myapplication.rutina.data.models.Rutina;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class CrearRutinaActivity extends AppCompatActivity {

    private Button btnFechaInicio, btnFechaFinal;
    private EditText edtDescripcion;
    private Spinner spinnerClientes;
    private Button btnGuardarRutina;
    private RutinaRepository rutinaRepository;
    private ClienteRepository clienteRepository; // Repositorio para obtener los clientes
    private List<Cliente> listaClientes; // Lista de clientes
    private Calendar calendarioInicio, calendarioFinal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_actualizar_rutina);

        // Configurar la toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Crear Rutina");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);  // Botón de "volver"
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.baseline_arrow_back_24);

        // Inicializar vistas
        btnFechaInicio = findViewById(R.id.btn_fecha_inicio);
        btnFechaFinal = findViewById(R.id.btn_fecha_final);
        edtDescripcion = findViewById(R.id.edt_descripcion);
        spinnerClientes = findViewById(R.id.spinner_clientes);
        btnGuardarRutina = findViewById(R.id.btn_guardar_rutina);

        // Inicializar los calendarios
        calendarioInicio = Calendar.getInstance();
        calendarioFinal = Calendar.getInstance();

        // Inicializar los repositorios
        rutinaRepository = new RutinaRepository(this);
        clienteRepository = new ClienteRepository(this);

        // Cargar los clientes en el spinner
        cargarClientesEnSpinner();

        // Configurar el DatePicker para el botón de Fecha de Inicio
        btnFechaInicio.setOnClickListener(v -> {
            DatePickerDialog datePickerDialog = new DatePickerDialog(CrearRutinaActivity.this,
                    (view, year, month, dayOfMonth) -> {
                        calendarioInicio.set(year, month, dayOfMonth);
                        btnFechaInicio.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
                    }, calendarioInicio.get(Calendar.YEAR), calendarioInicio.get(Calendar.MONTH), calendarioInicio.get(Calendar.DAY_OF_MONTH));
            datePickerDialog.show();
        });

        // Configurar el DatePicker para el botón de Fecha Final
        btnFechaFinal.setOnClickListener(v -> {
            DatePickerDialog datePickerDialog = new DatePickerDialog(CrearRutinaActivity.this,
                    (view, year, month, dayOfMonth) -> {
                        calendarioFinal.set(year, month, dayOfMonth);
                        btnFechaFinal.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
                    }, calendarioFinal.get(Calendar.YEAR), calendarioFinal.get(Calendar.MONTH), calendarioFinal.get(Calendar.DAY_OF_MONTH));
            datePickerDialog.show();
        });

        // Lógica del botón Guardar
        btnGuardarRutina.setOnClickListener(v -> guardarRutina());
    }

    private void cargarClientesEnSpinner() {
        // Obtener la lista de clientes desde el repositorio
        listaClientes = clienteRepository.obtenerListaClientes();

        // Crear una lista de nombres de clientes para mostrar en el Spinner
        List<String> nombresClientes = new ArrayList<>();
        for (Cliente cliente : listaClientes) {
            nombresClientes.add(cliente.getNombre() + " " + cliente.getApellidoPaterno() + " " + cliente.getApellidoMaterno());
        }

        // Crear un ArrayAdapter para el Spinner
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, nombresClientes);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerClientes.setAdapter(adapter);
    }

    private void guardarRutina() {
        String fechaInicio = btnFechaInicio.getText().toString().trim();
        String fechaFinal = btnFechaFinal.getText().toString().trim();
        String descripcion = edtDescripcion.getText().toString().trim();

        if (TextUtils.isEmpty(fechaInicio) || TextUtils.isEmpty(fechaFinal) || TextUtils.isEmpty(descripcion)) {
            Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        // Obtener el ID del cliente seleccionado
        int clienteSeleccionadoPos = spinnerClientes.getSelectedItemPosition();
        Cliente clienteSeleccionado = listaClientes.get(clienteSeleccionadoPos);
        int idCliente = clienteSeleccionado.getIdCliente();

        // Crear la nueva Rutina con el ID del cliente
        Rutina nuevaRutina = new Rutina(0, idCliente, fechaInicio, fechaFinal, descripcion);

        // Insertar en la base de datos
        long resultado = rutinaRepository.insertarRutina(nuevaRutina);

        if (resultado > 0) {
            Toast.makeText(this, "Rutina guardada con éxito", Toast.LENGTH_SHORT).show();

            // Iniciar AddExerciseToRoutineActivity esperando un resultado
            Intent intent = new Intent(CrearRutinaActivity.this, AddExerciseToRoutineActivity.class);
            intent.putExtra("idRutina", (int) resultado);  // Pasando el idRutina
            startActivityForResult(intent, 1);  // Esperando un resultado (código 1)
        } else {
            Toast.makeText(this, "Error al guardar la rutina", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();  // Volver a la actividad anterior
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Si AddExerciseToRoutineActivity devuelve un resultado OK, terminamos esta actividad con RESULT_OK
        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
            setResult(Activity.RESULT_OK);
            finish();  // Terminar y volver a ListarRutinasActivity
        }
    }
}
