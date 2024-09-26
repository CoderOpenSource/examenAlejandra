package com.example.myapplication.rutina.presentation;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.rutina.business.repository.CorreoService;
import com.example.myapplication.rutina.business.repository.GeneradorPDFRutina;
import com.example.myapplication.rutina.business.repository.RutinaRepository;
import com.example.myapplication.rutina.data.models.Rutina;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.FileNotFoundException;
import java.util.List;

public class ListarRutinasByClientActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private RutinaClienteAdapter rutinaAdapter;
    private RutinaRepository rutinaRepository;
    private int clienteId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listar_rutinas_by_client);
        // Configurar la toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        toolbar.setTitle("Rutinas del cliente");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);  // Botón de "volver"
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.baseline_arrow_back_24);

        // Obtener clienteId desde el Intent
        clienteId = getIntent().getIntExtra("clienteId", -1);
        if (clienteId == -1) {
            Toast.makeText(this, "Error: Cliente no válido", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Inicializar vistas
        recyclerView = findViewById(R.id.recycler_view_rutinas);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        FloatingActionButton fabAddRutina = findViewById(R.id.fab_add_rutina);
        fabAddRutina.setOnClickListener(v -> {
            // Acción para agregar nueva rutina (puedes crear la lógica de agregar aquí)
        });

        rutinaRepository = new RutinaRepository(this);

        // Cargar rutinas del cliente
        cargarRutinasCliente();
    }

    private void cargarRutinasCliente() {
        List<Rutina> rutinas = rutinaRepository.obtenerRutinasPorClienteId(clienteId);
        rutinaAdapter = new RutinaClienteAdapter(rutinas, new RutinaClienteAdapter.OnItemClickListener() {
            @Override
            public void onGeneratePdfClick(Rutina rutina) {
                try {
                    // Llamar al servicio para crear el PDF
                    String rutaPdf = GeneradorPDFRutina.crearPDF(rutina.getIdRutina(), ListarRutinasByClientActivity.this);

                    // Llamar al servicio para enviar el correo con el PDF adjunto
                    CorreoService.enviarCorreoConPDF("adrianaysamuel4ever@gmail.com",
                            "Rutina " + rutina.getDescripcion(),
                            "Te adjunto la rutina en formato PDF.",
                            rutaPdf);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    Log.e("PDF", "Error al crear el PDF: " + e.getMessage());
                }
            }


            @Override
            public void onDeleteClick(Rutina rutina) {
                // Eliminar la rutina
                rutinaRepository.eliminarRutina(rutina.getIdRutina());
                cargarRutinasCliente(); // Refrescar la lista
            }
        });
        recyclerView.setAdapter(rutinaAdapter);
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
