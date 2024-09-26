package com.example.myapplication.rutina.data.models;

public class RutinaEjercicio {
    private int idRutina;
    private int idEjercicio;
    private int repeticiones;
    private int series;
    private int descanso;

    // Constructor vacío
    public RutinaEjercicio() {
    }

    // Constructor con parámetros
    public RutinaEjercicio(int idRutina, int idEjercicio, int repeticiones, int series, int descanso) {
        this.idRutina = idRutina;
        this.idEjercicio = idEjercicio;
        this.repeticiones = repeticiones;
        this.series = series;
        this.descanso = descanso;
    }

    // Getters y Setters
    public int getIdRutina() {
        return idRutina;
    }

    public void setIdRutina(int idRutina) {
        this.idRutina = idRutina;
    }

    public int getIdEjercicio() {
        return idEjercicio;
    }

    public void setIdEjercicio(int idEjercicio) {
        this.idEjercicio = idEjercicio;
    }

    public int getRepeticiones() {
        return repeticiones;
    }

    public void setRepeticiones(int repeticiones) {
        this.repeticiones = repeticiones;
    }

    public int getSeries() {
        return series;
    }

    public void setSeries(int series) {
        this.series = series;
    }

    public int getDescanso() {
        return descanso;
    }

    public void setDescanso(int descanso) {
        this.descanso = descanso;
    }
}