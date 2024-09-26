package com.example.myapplication.rutina.data.daos;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.myapplication.database.GymBroDatabaseHelper;
import com.example.myapplication.rutina.data.models.RutinaEjercicio;

import java.util.ArrayList;
import java.util.List;

public class RutinaEjercicioDao {
    private SQLiteDatabase db;

    public RutinaEjercicioDao(Context context) {
        GymBroDatabaseHelper dbHelper = new GymBroDatabaseHelper(context);
        db = dbHelper.getWritableDatabase();
    }

    // Insertar RutinaEjercicio
    public long insertarRutinaEjercicio(RutinaEjercicio rutinaEjercicio) {
        ContentValues values = new ContentValues();
        values.put("id_rutina", rutinaEjercicio.getIdRutina());
        values.put("id_ejercicio", rutinaEjercicio.getIdEjercicio());
        values.put("repeticiones", rutinaEjercicio.getRepeticiones());
        values.put("series", rutinaEjercicio.getSeries());
        values.put("descanso", rutinaEjercicio.getDescanso());

        return db.insert("RutinaEjercicio", null, values);
    }

    // Obtener todos los RutinaEjercicio (Devuelve un cursor en lugar de una lista)
    public Cursor obtenerTodosLosRutinaEjercicio() {
        return db.query("RutinaEjercicio", null, null, null, null, null, null);
    }

    // Actualizar RutinaEjercicio
    public int actualizarRutinaEjercicio(RutinaEjercicio rutinaEjercicio) {
        ContentValues values = new ContentValues();
        values.put("repeticiones", rutinaEjercicio.getRepeticiones());
        values.put("series", rutinaEjercicio.getSeries());
        values.put("descanso", rutinaEjercicio.getDescanso());

        return db.update("RutinaEjercicio", values, "id_rutina = ? AND id_ejercicio = ?",
                new String[]{String.valueOf(rutinaEjercicio.getIdRutina()), String.valueOf(rutinaEjercicio.getIdEjercicio())});
    }

    // Eliminar RutinaEjercicio
    public void eliminarRutinaEjercicio(int idRutina, int idEjercicio) {
        db.delete("RutinaEjercicio", "id_rutina = ? AND id_ejercicio = ?",
                new String[]{String.valueOf(idRutina), String.valueOf(idEjercicio)});
    }

    // NUEVO: Obtener RutinaEjercicios por idRutina
    public List<RutinaEjercicio> obtenerRutinaEjerciciosPorRutinaId(int idRutina) {
        List<RutinaEjercicio> rutinaEjercicios = new ArrayList<>();
        Cursor cursor = db.query("RutinaEjercicio", null, "id_rutina = ?", new String[]{String.valueOf(idRutina)},
                null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                int idEjercicio = cursor.getInt(cursor.getColumnIndexOrThrow("id_ejercicio"));
                int repeticiones = cursor.getInt(cursor.getColumnIndexOrThrow("repeticiones"));
                int series = cursor.getInt(cursor.getColumnIndexOrThrow("series"));
                int descanso = cursor.getInt(cursor.getColumnIndexOrThrow("descanso"));

                // Crear el objeto RutinaEjercicio
                RutinaEjercicio rutinaEjercicio = new RutinaEjercicio(idRutina, idEjercicio, repeticiones, series, descanso);
                rutinaEjercicios.add(rutinaEjercicio);
            } while (cursor.moveToNext());

            cursor.close(); // Cerrar el cursor
        }

        return rutinaEjercicios;
    }
}
