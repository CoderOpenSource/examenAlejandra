package com.example.myapplication.rutina.business.repository;



import android.content.Context;
import android.database.Cursor;

import com.example.myapplication.rutina.data.daos.EjercicioDao;
import com.example.myapplication.rutina.data.models.Ejercicio;

import java.util.ArrayList;
import java.util.List;

public class EjercicioRepository {
    private EjercicioDao ejercicioDao;
    private Context context;

    public EjercicioRepository(Context context) {
        this.context = context;
        ejercicioDao = new EjercicioDao(context);
    }

    // Insertar un ejercicio
    public long insertarEjercicio(Ejercicio ejercicio) {
        return ejercicioDao.insertarEjercicio(ejercicio);
    }

    // Obtener todos los ejercicios
// Obtener todos los ejercicios y convertir el Cursor a una lista
    public List<Ejercicio> obtenerTodosLosEjercicios() {
        List<Ejercicio> listaEjercicios = new ArrayList<>();
        Cursor cursor = ejercicioDao.obtenerTodosLosEjercicios();

        if (cursor != null && cursor.moveToFirst()) {
            do {
                int idEjercicio = cursor.getInt(cursor.getColumnIndexOrThrow("id_ejercicio"));
                String nombre = cursor.getString(cursor.getColumnIndexOrThrow("nombre"));
                String posicion = cursor.getString(cursor.getColumnIndexOrThrow("posicion"));
                String ejecucion = cursor.getString(cursor.getColumnIndexOrThrow("ejecucion"));
                String equipo = cursor.getString(cursor.getColumnIndexOrThrow("equipo"));
                String multimedia = cursor.getString(cursor.getColumnIndexOrThrow("multimedia"));
                int idCategoria = cursor.getInt(cursor.getColumnIndexOrThrow("id_categoria"));

                Ejercicio ejercicio = new Ejercicio(idEjercicio, nombre, posicion, ejecucion, equipo, multimedia, idCategoria);
                listaEjercicios.add(ejercicio);
            } while (cursor.moveToNext());
        }

        if (cursor != null) {
            cursor.close();
        }

        return listaEjercicios;
    }
    // Actualizar un ejercicio
    public int actualizarEjercicio(Ejercicio ejercicio) {
        return ejercicioDao.actualizarEjercicio(ejercicio);
    }

    // Eliminar un ejercicio por ID
    public void eliminarEjercicio(int idEjercicio) {
        ejercicioDao.eliminarEjercicio(idEjercicio);
    }

    // Obtener un ejercicio por ID
    public Ejercicio obtenerEjercicioPorId(int idEjercicio) {
        Cursor cursor = ejercicioDao.obtenerEjercicioPorId(idEjercicio);

        if (cursor != null && cursor.moveToFirst()) {
            int id = cursor.getInt(cursor.getColumnIndexOrThrow("id_ejercicio"));
            String nombre = cursor.getString(cursor.getColumnIndexOrThrow("nombre"));
            String posicion = cursor.getString(cursor.getColumnIndexOrThrow("posicion"));
            String ejecucion = cursor.getString(cursor.getColumnIndexOrThrow("ejecucion"));
            String equipo = cursor.getString(cursor.getColumnIndexOrThrow("equipo"));
            String multimedia = cursor.getString(cursor.getColumnIndexOrThrow("multimedia"));
            int idCategoria = cursor.getInt(cursor.getColumnIndexOrThrow("id_categoria"));

            cursor.close();
            return new Ejercicio(id, nombre, posicion, ejecucion, equipo, multimedia, idCategoria);
        } else {
            if (cursor != null) {
                cursor.close();
            }
            return null;  // Si no se encuentra el ejercicio
        }
    }

}
