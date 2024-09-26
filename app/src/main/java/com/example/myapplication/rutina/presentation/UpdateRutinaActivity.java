package com.example.myapplication.rutina.presentation;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
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

public class UpdateRutinaActivity extends AppCompatActivity {

    private Button btnFechaInicio, btnFechaFinal, btnVerEjercicios;
    private EditText edtDescripcion;
    private Spinner spinnerClientes;
    private Button btnActualizarRutina;
    private RutinaRepository rutinaRepository;
    private ClienteRepository clienteRepository;
    private List<Cliente> listaClientes;
    private Calendar calendarioInicio, calendarioFinal;
    private int idRutina;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crear_actualizar_rutina); // Usando el mismo layout

        // Configurar la toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Actualizar Rutina");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Inicializar vistas
        btnFechaInicio = findViewById(R.id.btn_fecha_inicio);
        btnFechaFinal = findViewById(R.id.btn_fecha_final);
        edtDescripcion = findViewById(R.id.edt_descripcion);
        spinnerClientes = findViewById(R.id.spinner_clientes);
        btnActualizarRutina = findViewById(R.id.btn_guardar_rutina);
        btnActualizarRutina.setText("Actualizar Rutina");

        // Inicializar botón para ver ejercicios
        btnVerEjercicios = findViewById(R.id.btn_ver_ejercicios);
        btnVerEjercicios.setVisibility(View.VISIBLE);  // Hacer el botón visible

        // Inicializar calendarios
        calendarioInicio = Calendar.getInstance();
        calendarioFinal = Calendar.getInstance();

        // Inicializar repositorios
        rutinaRepository = new RutinaRepository(this);
        clienteRepository = new ClienteRepository(this);

        // Obtener el id de la rutina a actualizar desde el intent
        idRutina = getIntent().getIntExtra("idRutina", -1);

        // Cargar los clientes en el spinner
        cargarClientesEnSpinner();

        if (idRutina != -1) {
            cargarDatosRutina(idRutina);
        }

        // Configurar el DatePicker para el botón de Fecha de Inicio
        btnFechaInicio.setOnClickListener(v -> {
            DatePickerDialog datePickerDialog = new DatePickerDialog(UpdateRutinaActivity.this,
                    (view, year, month, dayOfMonth) -> {
                        calendarioInicio.set(year, month, dayOfMonth);
                        btnFechaInicio.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
                    }, calendarioInicio.get(Calendar.YEAR), calendarioInicio.get(Calendar.MONTH), calendarioInicio.get(Calendar.DAY_OF_MONTH));
            datePickerDialog.show();
        });

        // Configurar el DatePicker para el botón de Fecha Final
        btnFechaFinal.setOnClickListener(v -> {
            DatePickerDialog datePickerDialog = new DatePickerDialog(UpdateRutinaActivity.this,
                    (view, year, month, dayOfMonth) -> {
                        calendarioFinal.set(year, month, dayOfMonth);
                        btnFechaFinal.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
                    }, calendarioFinal.get(Calendar.YEAR), calendarioFinal.get(Calendar.MONTH), calendarioFinal.get(Calendar.DAY_OF_MONTH));
            datePickerDialog.show();
        });

        // Lógica para actualizar la rutina
        btnActualizarRutina.setOnClickListener(v -> actualizarRutina());

        // Redirigir a la pantalla de ver ejercicios al hacer clic en el botón
        btnVerEjercicios.setOnClickListener(v -> {
            Intent intent = new Intent(UpdateRutinaActivity.this, VerEjerciciosRutinaActivity.class);
            intent.putExtra("idRutina", idRutina);  // Pasar el id de la rutina a la nueva actividad
            startActivity(intent);  // Redirigir a la nueva pantalla
        });
    }

    // Método para cargar datos de la rutina
    private void cargarDatosRutina(int idRutina) {
        Rutina rutina = rutinaRepository.obtenerRutinaPorId(idRutina);

        if (rutina != null) {
            btnFechaInicio.setText(rutina.getFechaInicio());
            btnFechaFinal.setText(rutina.getFechaFinal());
            edtDescripcion.setText(rutina.getDescripcion());

            // Seleccionar el cliente correcto en el Spinner
            int clientePosicion = obtenerPosicionCliente(rutina.getIdCliente());
            spinnerClientes.setSelection(clientePosicion);
        } else {
            Toast.makeText(this, "Error al cargar los datos de la rutina", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private void cargarClientesEnSpinner() {
        listaClientes = clienteRepository.obtenerListaClientes();

        List<String> nombresClientes = new ArrayList<>();
        for (Cliente cliente : listaClientes) {
            nombresClientes.add(cliente.getNombre() + " " + cliente.getApellidoPaterno() + " " + cliente.getApellidoMaterno());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, nombresClientes);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerClientes.setAdapter(adapter);
    }

    private int obtenerPosicionCliente(int idCliente) {
        for (int i = 0; i < listaClientes.size(); i++) {
            if (listaClientes.get(i).getIdCliente() == idCliente) {
                return i;
            }
        }
        return -1; // Cliente no encontrado
    }

    private void actualizarRutina() {
        String fechaInicio = btnFechaInicio.getText().toString().trim();
        String fechaFinal = btnFechaFinal.getText().toString().trim();
        String descripcion = edtDescripcion.getText().toString().trim();

        if (TextUtils.isEmpty(fechaInicio) || TextUtils.isEmpty(fechaFinal) || TextUtils.isEmpty(descripcion)) {
            Toast.makeText(this, "Completa todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        int clienteSeleccionadoPos = spinnerClientes.getSelectedItemPosition();
        Cliente clienteSeleccionado = listaClientes.get(clienteSeleccionadoPos);
        int idCliente = clienteSeleccionado.getIdCliente();

        Rutina rutinaActualizada = new Rutina(idRutina, idCliente, fechaInicio, fechaFinal, descripcion);

        int resultado = rutinaRepository.actualizarRutina(rutinaActualizada);

        if (resultado > 0) {
            Toast.makeText(this, "Rutina actualizada con éxito", Toast.LENGTH_SHORT).show();
            setResult(Activity.RESULT_OK);
            finish();
        } else {
            Toast.makeText(this, "Error al actualizar la rutina", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
