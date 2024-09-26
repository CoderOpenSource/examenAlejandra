package com.example.myapplication.rutina.data.daos;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.myapplication.database.GymBroDatabaseHelper;
import com.example.myapplication.rutina.data.models.Ejercicio;

public class EjercicioDao {
    private SQLiteDatabase db;

    public EjercicioDao(Context context) {
        GymBroDatabaseHelper dbHelper = new GymBroDatabaseHelper(context);
        db = dbHelper.getWritableDatabase();
    }

    // Insertar Ejercicio
    public long insertarEjercicio(Ejercicio ejercicio) {
        ContentValues values = new ContentValues();
        values.put("nombre", ejercicio.getNombre());
        values.put("posicion", ejercicio.getPosicion());
        values.put("ejecucion", ejercicio.getEjecucion());
        values.put("equipo", ejercicio.getEquipo());
        values.put("multimedia", ejercicio.getMultimedia());
        values.put("id_categoria", ejercicio.getIdCategoria());

        return db.insert("Ejercicio", null, values);
    }

    // Obtener todos los ejercicios (Devuelve un cursor en lugar de una lista)
    public Cursor obtenerTodosLosEjercicios() {
        return db.query("Ejercicio", null, null, null, null, null, null);
    }

    // Actualizar Ejercicio
    public int actualizarEjercicio(Ejercicio ejercicio) {
        ContentValues values = new ContentValues();
        values.put("nombre", ejercicio.getNombre());
        values.put("posicion", ejercicio.getPosicion());
        values.put("ejecucion", ejercicio.getEjecucion());
        values.put("equipo", ejercicio.getEquipo());
        values.put("multimedia", ejercicio.getMultimedia());
        values.put("id_categoria", ejercicio.getIdCategoria());

        return db.update("Ejercicio", values, "id_ejercicio = ?", new String[]{String.valueOf(ejercicio.getIdEjercicio())});
    }

    // Eliminar Ejercicio
    public void eliminarEjercicio(int idEjercicio) {
        db.delete("Ejercicio", "id_ejercicio = ?", new String[]{String.valueOf(idEjercicio)});
    }
    // Obtener un ejercicio por ID
    public Cursor obtenerEjercicioPorId(int idEjercicio) {
        return db.query("Ejercicio", null, "id_ejercicio = ?", new String[]{String.valueOf(idEjercicio)}, null, null, null);
    }

}