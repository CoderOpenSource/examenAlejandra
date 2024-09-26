package com.example.myapplication.rutina.business.repository;


import android.content.Context;
import android.database.Cursor;

import com.example.myapplication.rutina.data.daos.RutinaDao;
import com.example.myapplication.rutina.data.models.Rutina;

import java.util.ArrayList;
import java.util.List;

// RutinaRepository.java
public class RutinaRepository {
    private RutinaDao rutinaDao;
    private Context context;

    public RutinaRepository(Context context) {
        this.context = context;
        rutinaDao = new RutinaDao(context);
    }

    // Insertar una rutina
    public long insertarRutina(Rutina rutina) {
        return rutinaDao.insertarRutina(rutina);
    }

    // Obtener todas las rutinas
    public List<Rutina> obtenerTodasLasRutinas() {
        List<Rutina> listaRutinas = new ArrayList<>();
        Cursor cursor = rutinaDao.obtenerTodasLasRutinas();

        if (cursor != null && cursor.moveToFirst()) {
            do {
                int idRutina = cursor.getInt(cursor.getColumnIndexOrThrow("id_rutina"));
                int idCliente = cursor.getInt(cursor.getColumnIndexOrThrow("id_cliente"));
                String fechaInicio = cursor.getString(cursor.getColumnIndexOrThrow("fecha_inicio"));
                String fechaFinal = cursor.getString(cursor.getColumnIndexOrThrow("fecha_final"));
                String descripcion = cursor.getString(cursor.getColumnIndexOrThrow("descripcion"));

                Rutina rutina = new Rutina(idRutina, idCliente, fechaInicio, fechaFinal, descripcion);
                listaRutinas.add(rutina);
            } while (cursor.moveToNext());
        }

        if (cursor != null) {
            cursor.close(); // Cerrar cursor
        }

        return listaRutinas;
    }

    // Obtener rutinas por clienteId
    public List<Rutina> obtenerRutinasPorClienteId(int clienteId) {
        List<Rutina> listaRutinas = new ArrayList<>();
        Cursor cursor = rutinaDao.obtenerRutinasPorClienteId(clienteId);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                int idRutina = cursor.getInt(cursor.getColumnIndexOrThrow("id_rutina"));
                int idCliente = cursor.getInt(cursor.getColumnIndexOrThrow("id_cliente"));
                String fechaInicio = cursor.getString(cursor.getColumnIndexOrThrow("fecha_inicio"));
                String fechaFinal = cursor.getString(cursor.getColumnIndexOrThrow("fecha_final"));
                String descripcion = cursor.getString(cursor.getColumnIndexOrThrow("descripcion"));

                Rutina rutina = new Rutina(idRutina, idCliente, fechaInicio, fechaFinal, descripcion);
                listaRutinas.add(rutina);
            } while (cursor.moveToNext());
        }

        if (cursor != null) {
            cursor.close(); // Cerrar cursor
        }

        return listaRutinas;
    }

    // Actualizar una rutina
    public int actualizarRutina(Rutina rutina) {
        return rutinaDao.actualizarRutina(rutina);
    }

    // Obtener Rutina por ID
    public Rutina obtenerRutinaPorId(int idRutina) {
        return rutinaDao.obtenerRutinaPorId(idRutina);
    }

    // Eliminar una rutina
    public void eliminarRutina(int idRutina) {
        rutinaDao.eliminarRutina(idRutina);
    }
}
