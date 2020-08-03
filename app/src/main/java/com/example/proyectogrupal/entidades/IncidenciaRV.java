package com.example.proyectogrupal.entidades;

public class IncidenciaRV {

    private String id;
    private String nombre;
    private boolean estado;


    public IncidenciaRV() {
    }

    public IncidenciaRV(String id, String nombre, boolean estado) {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public boolean isEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }
}
