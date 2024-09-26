package com.example.myapplication.rutina.data.daos;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.myapplication.database.GymBroDatabaseHelper;
import com.example.myapplication.rutina.data.models.Rutina;

// RutinaDao.java
public class RutinaDao {
    private SQLiteDatabase db;

    public RutinaDao(Context context) {
        GymBroDatabaseHelper dbHelper = new GymBroDatabaseHelper(context);
        db = dbHelper.getWritableDatabase();
    }

    // Insertar Rutina
    public long insertarRutina(Rutina rutina) {
        ContentValues values = new ContentValues();
        values.put("id_cliente", rutina.getIdCliente());
        values.put("fecha_inicio", rutina.getFechaInicio());
        values.put("fecha_final", rutina.getFechaFinal());
        values.put("descripcion", rutina.getDescripcion());

        return db.insert("Rutina", null, values);
    }

    // Obtener todas las rutinas (Devuelve un cursor)
    public Cursor obtenerTodasLasRutinas() {
        return db.query("Rutina", null, null, null, null, null, null);
    }

    // Actualizar Rutina
    public int actualizarRutina(Rutina rutina) {
        ContentValues values = new ContentValues();
        values.put("id_cliente", rutina.getIdCliente());
        values.put("fecha_inicio", rutina.getFechaInicio());
        values.put("fecha_final", rutina.getFechaFinal());
        values.put("descripcion", rutina.getDescripcion());

        return db.update("Rutina", values, "id_rutina = ?", new String[]{String.valueOf(rutina.getIdRutina())});
    }

    // Eliminar Rutina
    public void eliminarRutina(int idRutina) {
        db.delete("Rutina", "id_rutina = ?", new String[]{String.valueOf(idRutina)});
    }

    // Obtener Rutina por ID
    public Rutina obtenerRutinaPorId(int idRutina) {
        Cursor cursor = db.query("Rutina", null, "id_rutina = ?", new String[]{String.valueOf(idRutina)}, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            int idCliente = cursor.getInt(cursor.getColumnIndexOrThrow("id_cliente"));
            String fechaInicio = cursor.getString(cursor.getColumnIndexOrThrow("fecha_inicio"));
            String fechaFinal = cursor.getString(cursor.getColumnIndexOrThrow("fecha_final"));
            String descripcion = cursor.getString(cursor.getColumnIndexOrThrow("descripcion"));

            Rutina rutina = new Rutina(idRutina, idCliente, fechaInicio, fechaFinal, descripcion);
            cursor.close(); // Cerrar cursor
            return rutina;
        }

        return null; // Retornar null si no se encuentra
    }

    // Obtener rutinas por id_cliente
    public Cursor obtenerRutinasPorClienteId(int idCliente) {
        return db.query("Rutina", null, "id_cliente = ?", new String[]{String.valueOf(idCliente)}, null, null, null);
    }
}
