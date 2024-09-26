package com.example.myapplication.rutina.business.repository;

import android.content.Context;

import com.example.myapplication.rutina.data.daos.RutinaEjercicioDao;
import com.example.myapplication.rutina.data.models.RutinaEjercicio;

import java.util.List;

public class RutinaEjercicioRepository {
    private RutinaEjercicioDao rutinaEjercicioDao;
    private Context context;

    public RutinaEjercicioRepository(Context context) {
        this.context = context;
        rutinaEjercicioDao = new RutinaEjercicioDao(context);
    }

    // Insertar RutinaEjercicio
    public long insertarRutinaEjercicio(RutinaEjercicio rutinaEjercicio) {
        return rutinaEjercicioDao.insertarRutinaEjercicio(rutinaEjercicio);
    }

    // Obtener todas las RutinaEjercicio
    public List<RutinaEjercicio> obtenerRutinaEjerciciosPorRutinaId(int idRutina) {
        return rutinaEjercicioDao.obtenerRutinaEjerciciosPorRutinaId(idRutina);
    }

    // Actualizar RutinaEjercicio
    public int actualizarRutinaEjercicio(RutinaEjercicio rutinaEjercicio) {
        return rutinaEjercicioDao.actualizarRutinaEjercicio(rutinaEjercicio);
    }

    // Eliminar RutinaEjercicio
    public void eliminarRutinaEjercicio(int idRutina, int idEjercicio) {
        rutinaEjercicioDao.eliminarRutinaEjercicio(idRutina, idEjercicio);
    }
}
