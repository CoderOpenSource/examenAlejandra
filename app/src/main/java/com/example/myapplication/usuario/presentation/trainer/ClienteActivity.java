package com.example.myapplication.usuario.presentation.trainer;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.rutina.presentation.CategoriaEjercicioActivity;
import com.example.myapplication.rutina.presentation.ListarEjerciciosActivity;
import com.example.myapplication.rutina.presentation.ListarRutinasActivity;
import com.example.myapplication.usuario.business.repository.ClienteRepository;
import com.example.myapplication.usuario.data.models.Cliente;
import com.example.myapplication.usuario.presentation.adapter.ClienteAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;

import java.util.List;

public class ClienteActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ClienteAdapter clienteAdapter;
    private FloatingActionButton fabAddCliente;
    private ClienteRepository clienteRepository;
    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cliente);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle("Clientes");
        setSupportActionBar(toolbar);

        // Configurar el DrawerLayout
        drawerLayout = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.open_drawer, R.string.close_drawer);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        // Referencia del NavigationView y configurar el listener
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.nav_home) {
                Intent homeIntent = new Intent(ClienteActivity.this, HomeTrainerActivity.class);
                startActivity(homeIntent);
            } else if (id == R.id.nav_rutinas) {
                Intent rutinaIntent = new Intent(ClienteActivity.this,   ListarRutinasActivity.class);
                startActivity(rutinaIntent);
            } else if (id == R.id.nav_ejercicios) {
                Intent ejercicioIntent = new Intent(ClienteActivity.this,   ListarEjerciciosActivity.class);
                startActivity(ejercicioIntent);
            } else if (id == R.id.nav_categoria_ejercicio) {
                Intent categoriaEjercicioIntent = new Intent(ClienteActivity.this, CategoriaEjercicioActivity.class);
                startActivity(categoriaEjercicioIntent);
            }

            // Cerrar el Drawer después de la selección
            drawerLayout.closeDrawers();
            return true;
        });

        // Configurar RecyclerView
        recyclerView = findViewById(R.id.recycler_view_clientes);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Obtener lista de clientes desde ClienteRepository
        clienteRepository = new ClienteRepository(this);
        List<Cliente> listaClientes = clienteRepository.obtenerListaClientes();

        clienteAdapter = new ClienteAdapter(this, listaClientes, new ClienteAdapter.OnItemClickListener() {
            @Override
            public void onEditClick(Cliente cliente) {
                // Start UpdateClientActivity with the client ID
                Intent intent = new Intent(ClienteActivity.this, UpdateClientActivity.class);
                intent.putExtra("clienteId", cliente.getIdCliente());
                startActivityForResult(intent, 2);
            }

            @Override
            public void onDeleteClick(Cliente cliente) {
                clienteRepository.eliminarCliente(cliente.getIdCliente());
                List<Cliente> updatedClientes = clienteRepository.obtenerListaClientes();
                clienteAdapter.updateData(updatedClientes);
                Toast.makeText(ClienteActivity.this, "Cliente eliminado", Toast.LENGTH_SHORT).show();
            }
        });

        recyclerView.setAdapter(clienteAdapter);

        // Botón para agregar nuevo cliente
        fabAddCliente = findViewById(R.id.fab_add_cliente);
        fabAddCliente.setOnClickListener(v -> {
            // Start RegisterClientActivity with requestCode 1
            Intent intent = new Intent(ClienteActivity.this, RegisterClientActivity.class);
            startActivityForResult(intent, 1);
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Check if the result is from RegisterClientActivity or UpdateClientActivity
        if ((requestCode == 1 || requestCode == 2) && resultCode == Activity.RESULT_OK) {
            // Update the list of clients after successful registration or update
            List<Cliente> listaClientes = clienteRepository.obtenerListaClientes();
            clienteAdapter.updateData(listaClientes);  // Update RecyclerView with the new list
        }
    }

}
