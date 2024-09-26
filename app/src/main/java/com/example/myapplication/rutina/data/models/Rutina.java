package com.example.myapplication.rutina.data.models;


public class Rutina {
    private int idRutina;
    private int idCliente;
    private String fechaInicio;
    private String fechaFinal;
    private String descripcion;

    // Constructor vacío
    public Rutina() {
    }

    // Constructor con parámetros
    public Rutina(int idRutina, int idCliente, String fechaInicio, String fechaFinal, String descripcion) {
        this.idRutina = idRutina;
        this.idCliente = idCliente;
        this.fechaInicio = fechaInicio;
        this.fechaFinal = fechaFinal;
        this.descripcion = descripcion;
    }

    // Getters y Setters
    public int getIdRutina() {
        return idRutina;
    }

    public void setIdRutina(int idRutina) {
        this.idRutina = idRutina;
    }

    public int getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(int idCliente) {
        this.idCliente = idCliente;
    }

    public String getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(String fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public String getFechaFinal() {
        return fechaFinal;
    }

    public void setFechaFinal(String fechaFinal) {
        this.fechaFinal = fechaFinal;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
}
