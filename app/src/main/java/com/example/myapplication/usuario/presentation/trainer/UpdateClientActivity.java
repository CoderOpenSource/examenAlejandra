package com.example.myapplication.usuario.presentation.trainer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.myapplication.R;
import com.example.myapplication.usuario.business.repository.ClienteRepository;
import com.example.myapplication.usuario.data.models.Cliente;
import com.example.myapplication.rutina.presentation.ListarRutinasByClientActivity;

public class UpdateClientActivity extends AppCompatActivity {

    private EditText edtNombre, edtApellidoPaterno, edtApellidoMaterno, edtCelular, edtEmail, edtEdad, edtAltura, edtPeso;
    private Button btnActualizar, btnVerRutinas;
    private ClienteRepository clienteRepository;
    private int clienteId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_client);

        // Toolbar setup
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        toolbar.setTitle("Actualizar Cliente");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.baseline_arrow_back_24);

        // Initialize views
        edtNombre = findViewById(R.id.edt_nombre);
        edtApellidoPaterno = findViewById(R.id.edt_apellido_paterno);
        edtApellidoMaterno = findViewById(R.id.edt_apellido_materno);
        edtCelular = findViewById(R.id.edt_celular);
        edtEmail = findViewById(R.id.edt_email);
        edtEdad = findViewById(R.id.edt_edad);
        edtAltura = findViewById(R.id.edt_altura);
        edtPeso = findViewById(R.id.edt_peso);
        btnActualizar = findViewById(R.id.btn_actualizar);
        btnVerRutinas = findViewById(R.id.btn_ver_rutinas);  // Botón "Ver Rutinas" ya incluido en el layout

        clienteRepository = new ClienteRepository(this);

        // Obtener clienteId pasado desde la actividad anterior
        clienteId = getIntent().getIntExtra("clienteId", -1);

        // Cargar datos del cliente
        if (clienteId != -1) {
            cargarCliente(clienteId);
        }

        // Acción del botón Actualizar
        btnActualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actualizarCliente();
            }
        });

        // Acción del botón Ver Rutinas
        btnVerRutinas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Abrir la actividad para listar rutinas del cliente
                Intent intent = new Intent(UpdateClientActivity.this, ListarRutinasByClientActivity.class);
                intent.putExtra("clienteId", clienteId);  // Pasar el clienteId a la nueva actividad
                startActivity(intent);
            }
        });
    }

    private void cargarCliente(int clienteId) {
        Cliente cliente = clienteRepository.obtenerClientePorId(clienteId);
        if (cliente != null) {
            edtNombre.setText(cliente.getNombre());
            edtApellidoPaterno.setText(cliente.getApellidoPaterno());
            edtApellidoMaterno.setText(cliente.getApellidoMaterno());
            edtCelular.setText(cliente.getCelular());
            edtEmail.setText(cliente.getEmail());
            edtEdad.setText(String.valueOf(cliente.getEdad()));
            edtAltura.setText(String.valueOf(cliente.getAltura()));
            edtPeso.setText(String.valueOf(cliente.getPeso()));

            // Hacer visible el botón Ver Rutinas una vez que se haya cargado el cliente
            btnVerRutinas.setVisibility(View.VISIBLE);
        }
    }

    private void actualizarCliente() {
        if (TextUtils.isEmpty(edtNombre.getText().toString()) ||
                TextUtils.isEmpty(edtApellidoPaterno.getText().toString()) ||
                TextUtils.isEmpty(edtApellidoMaterno.getText().toString()) ||
                TextUtils.isEmpty(edtCelular.getText().toString()) ||
                TextUtils.isEmpty(edtEmail.getText().toString()) ||
                TextUtils.isEmpty(edtEdad.getText().toString()) ||
                TextUtils.isEmpty(edtAltura.getText().toString()) ||
                TextUtils.isEmpty(edtPeso.getText().toString())) {

            Toast.makeText(this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show();
            return;
        }

        // Crear objeto Cliente actualizado
        Cliente clienteActualizado = new Cliente();
        clienteActualizado.setIdCliente(clienteId);  // Establecer el mismo ID
        clienteActualizado.setNombre(edtNombre.getText().toString());
        clienteActualizado.setApellidoPaterno(edtApellidoPaterno.getText().toString());
        clienteActualizado.setApellidoMaterno(edtApellidoMaterno.getText().toString());
        clienteActualizado.setCelular(edtCelular.getText().toString());
        clienteActualizado.setEmail(edtEmail.getText().toString());
        clienteActualizado.setEdad(Integer.parseInt(edtEdad.getText().toString()));
        clienteActualizado.setAltura(Double.parseDouble(edtAltura.getText().toString()));
        clienteActualizado.setPeso(Double.parseDouble(edtPeso.getText().toString()));

        // Actualizar el cliente en la base de datos
        long resultado = clienteRepository.actualizarCliente(clienteActualizado);

        if (resultado > 0) {
            Toast.makeText(this, "Cliente actualizado con éxito", Toast.LENGTH_SHORT).show();
            setResult(Activity.RESULT_OK);
            finish();  // Cerrar la actividad
        } else {
            Toast.makeText(this, "Error al actualizar el cliente", Toast.LENGTH_SHORT).show();
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
