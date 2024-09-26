package com.example.myapplication.usuario.presentation.trainer;

import android.app.Activity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.myapplication.R;
import com.example.myapplication.rutina.business.repository.ImageSource;
import com.example.myapplication.usuario.data.models.Cliente;
import com.example.myapplication.usuario.business.repository.ClienteRepository;

import java.io.IOException;
import java.io.InputStream;

public class RegisterClientActivity extends AppCompatActivity {

    private EditText edtNombre, edtApellidoPaterno, edtApellidoMaterno, edtCelular, edtEmail, edtEdad, edtAltura, edtPeso;
    private Button btnRegistrar;
    private ClienteRepository clienteRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_client);
        // Configurar la toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        toolbar.setTitle("Registrar Cliente");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);  // Botón de "volver"
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.baseline_arrow_back_24);

        // Referenciar cada uno de los EditTexts y el botón
        edtNombre = findViewById(R.id.edt_nombre);
        edtApellidoPaterno = findViewById(R.id.edt_apellido_paterno);
        edtApellidoMaterno = findViewById(R.id.edt_apellido_materno);
        edtCelular = findViewById(R.id.edt_celular);
        edtEmail = findViewById(R.id.edt_email);
        edtEdad = findViewById(R.id.edt_edad);
        edtAltura = findViewById(R.id.edt_altura);
        edtPeso = findViewById(R.id.edt_peso);
        btnRegistrar = findViewById(R.id.btn_registrar);

        // Inicializar el repositorio
        clienteRepository = new ClienteRepository(this);

        // Listener del botón de registrar
        btnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registrarCliente();
            }
        });
    }

    private void registrarCliente() {
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

        Cliente cliente = new Cliente();
        cliente.setNombre(edtNombre.getText().toString());
        cliente.setApellidoPaterno(edtApellidoPaterno.getText().toString());
        cliente.setApellidoMaterno(edtApellidoMaterno.getText().toString());
        cliente.setCelular(edtCelular.getText().toString());
        cliente.setEmail(edtEmail.getText().toString());
        cliente.setEdad(Integer.parseInt(edtEdad.getText().toString()));
        cliente.setAltura(Double.parseDouble(edtAltura.getText().toString()));
        cliente.setPeso(Double.parseDouble(edtPeso.getText().toString()));

        long resultado = clienteRepository.insertarCliente(cliente);

        if (resultado > 0) {
            Toast.makeText(this, "Cliente registrado con éxito", Toast.LENGTH_SHORT).show();

            // Return success result to ClienteActivity
            setResult(Activity.RESULT_OK);
            finish();  // Close this activity and return to ClienteActivity
        } else {
            Toast.makeText(this, "Error al registrar el cliente", Toast.LENGTH_SHORT).show();
        }

}

    // Método para limpiar los campos del formulario
    private void limpiarCampos() {
        edtNombre.setText("");
        edtApellidoPaterno.setText("");
        edtApellidoMaterno.setText("");
        edtCelular.setText("");
        edtEmail.setText("");
        edtEdad.setText("");
        edtAltura.setText("");
        edtPeso.setText("");
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