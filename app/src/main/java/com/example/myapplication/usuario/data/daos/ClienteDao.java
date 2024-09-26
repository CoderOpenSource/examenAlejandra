package com.example.myapplication.usuario.data.daos;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.myapplication.database.GymBroDatabaseHelper;
import com.example.myapplication.usuario.data.models.Cliente;

public class ClienteDao {
    private SQLiteDatabase db;

    public ClienteDao(Context context) {
        GymBroDatabaseHelper dbHelper = new GymBroDatabaseHelper(context);
        db = dbHelper.getWritableDatabase();
    }

    // Insertar Cliente
    public long insertarCliente(Cliente cliente) {
        ContentValues values = new ContentValues();
        values.put("nombre", cliente.getNombre());
        values.put("apellido_paterno", cliente.getApellidoPaterno());
        values.put("apellido_materno", cliente.getApellidoMaterno());
        values.put("celular", cliente.getCelular());
        values.put("email", cliente.getEmail());
        values.put("edad", cliente.getEdad());
        values.put("altura", cliente.getAltura());
        values.put("peso", cliente.getPeso());

        return db.insert("Cliente", null, values);
    }

    // Obtener todos los clientes
    public Cursor obtenerTodosLosClientes() {
        return db.query("Cliente", null, null, null, null, null, null);
    }

    // Actualizar Cliente
    public int actualizarCliente(Cliente cliente) {
        ContentValues values = new ContentValues();
        values.put("nombre", cliente.getNombre());
        values.put("apellido_paterno", cliente.getApellidoPaterno());
        values.put("apellido_materno", cliente.getApellidoMaterno());
        values.put("celular", cliente.getCelular());
        values.put("email", cliente.getEmail());
        values.put("edad", cliente.getEdad());
        values.put("altura", cliente.getAltura());
        values.put("peso", cliente.getPeso());

        return db.update("Cliente", values, "id_cliente = ?", new String[]{String.valueOf(cliente.getIdCliente())});
    }

    // Eliminar Cliente
    public void eliminarCliente(int idCliente) {
        db.delete("Cliente", "id_cliente = ?", new String[]{String.valueOf(idCliente)});
    }
    // ClienteDao.java

    // Method to get a client by ID
    public Cliente obtenerClientePorId(int idCliente) {
        Cliente cliente = null;
        String[] columns = {"id_cliente", "nombre", "apellido_paterno", "apellido_materno", "celular", "email", "edad", "altura", "peso"};
        Cursor cursor = db.query("Cliente", columns, "id_cliente = ?", new String[]{String.valueOf(idCliente)}, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            // Create the Cliente object from the cursor data
            cliente = new Cliente(
                    cursor.getInt(cursor.getColumnIndexOrThrow("id_cliente")),
                    cursor.getString(cursor.getColumnIndexOrThrow("nombre")),
                    cursor.getString(cursor.getColumnIndexOrThrow("apellido_paterno")),
                    cursor.getString(cursor.getColumnIndexOrThrow("apellido_materno")),
                    cursor.getString(cursor.getColumnIndexOrThrow("celular")),
                    cursor.getString(cursor.getColumnIndexOrThrow("email")),
                    cursor.getInt(cursor.getColumnIndexOrThrow("edad")),
                    cursor.getDouble(cursor.getColumnIndexOrThrow("altura")),
                    cursor.getDouble(cursor.getColumnIndexOrThrow("peso"))
            );
        }

        if (cursor != null) {
            cursor.close();
        }

        return cliente;
    }

}
