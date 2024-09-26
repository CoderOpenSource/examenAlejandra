package com.example.myapplication.rutina.data.models;

public class Ejercicio {
    private int idEjercicio;
    private String nombre;
    private String posicion;
    private String ejecucion;
    private String equipo;
    private String multimedia;
    private int idCategoria;

    // Constructor vacío
    public Ejercicio() {
    }

    // Constructor con parámetros
    public Ejercicio(int idEjercicio, String nombre, String posicion, String ejecucion, String equipo, String multimedia, int idCategoria) {
        this.idEjercicio = idEjercicio;
        this.nombre = nombre;
        this.posicion = posicion;
        this.ejecucion = ejecucion;
        this.equipo = equipo;
        this.multimedia = multimedia;
        this.idCategoria = idCategoria;
    }

    // Getters y Setters
    public int getIdEjercicio() {
        return idEjercicio;
    }

    public void setIdEjercicio(int idEjercicio) {
        this.idEjercicio = idEjercicio;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getPosicion() {
        return posicion;
    }

    public void setPosicion(String posicion) {
        this.posicion = posicion;
    }

    public String getEjecucion() {
        return ejecucion;
    }

    public void setEjecucion(String ejecucion) {
        this.ejecucion = ejecucion;
    }

    public String getEquipo() {
        return equipo;
    }

    public void setEquipo(String equipo) {
        this.equipo = equipo;
    }

    public String getMultimedia() {
        return multimedia;
    }

    public void setMultimedia(String multimedia) {
        this.multimedia = multimedia;
    }

    public int getIdCategoria() {
        return idCategoria;
    }

    public void setIdCategoria(int idCategoria) {
        this.idCategoria = idCategoria;
    }
}