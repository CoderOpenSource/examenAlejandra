package com.example.myapplication.usuario.business.repository;

import android.content.Context;
import android.database.Cursor;

import com.example.myapplication.usuario.data.daos.ClienteDao;
import com.example.myapplication.usuario.data.models.Cliente;

import java.util.ArrayList;
import java.util.List;

public class ClienteRepository {
    private ClienteDao clienteDao;
    private Context context;

    public ClienteRepository(Context context) {
        this.context = context;
        clienteDao = new ClienteDao(context);
    }

    // Insertar un cliente
    public long insertarCliente(Cliente cliente) {
        return clienteDao.insertarCliente(cliente);
    }

    // Obtener todos los clientes
    public List<Cliente> obtenerListaClientes() {
        List<Cliente> listaClientes = new ArrayList<>();
        Cursor cursor = clienteDao.obtenerTodosLosClientes();

        if (cursor != null && cursor.moveToFirst()) {
            do {
                int idCliente = cursor.getInt(cursor.getColumnIndexOrThrow("id_cliente"));
                String nombre = cursor.getString(cursor.getColumnIndexOrThrow("nombre"));
                String apellidoPaterno = cursor.getString(cursor.getColumnIndexOrThrow("apellido_paterno"));
                String apellidoMaterno = cursor.getString(cursor.getColumnIndexOrThrow("apellido_materno"));
                String celular = cursor.getString(cursor.getColumnIndexOrThrow("celular"));
                String email = cursor.getString(cursor.getColumnIndexOrThrow("email"));
                int edad = cursor.getInt(cursor.getColumnIndexOrThrow("edad"));
                double altura = cursor.getDouble(cursor.getColumnIndexOrThrow("altura"));
                double peso = cursor.getDouble(cursor.getColumnIndexOrThrow("peso"));

                // Crear el objeto Cliente y agregarlo a la lista
                Cliente cliente = new Cliente(idCliente, nombre, apellidoPaterno, apellidoMaterno, celular, email, edad, altura, peso);
                listaClientes.add(cliente);
            } while (cursor.moveToNext());
        }

        if (cursor != null) {
            cursor.close();
        }

        return listaClientes;
    }
    // Actualizar un cliente
    public int actualizarCliente(Cliente cliente) {
        return clienteDao.actualizarCliente(cliente);
    }

    public Cliente obtenerClientePorId(int idCliente) {
        return clienteDao.obtenerClientePorId(idCliente);
    }
    // Eliminar un cliente por ID
    public void eliminarCliente(int idCliente) {
        clienteDao.eliminarCliente(idCliente);
    }
}