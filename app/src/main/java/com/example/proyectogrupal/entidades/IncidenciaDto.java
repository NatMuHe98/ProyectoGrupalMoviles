package com.example.proyectogrupal.entidades;

public class IncidenciaDto {

    private String id;
    private String nombre;
    private String imageUrl;
    private String descripcion;
    private double latitud;
    private double longitud;
    private boolean estado;
    private String comentario;

    public IncidenciaDto(String id, String nombre, String imageUrl, String descripcion, double latitud, double longitud, boolean estado, String comentario) {
        if (nombre.trim().equals("")){
            nombre = "Sin nombre";
        }
        this.id = id;
        this.nombre = nombre;
        this.imageUrl = imageUrl;
        this.descripcion = descripcion;
        this.latitud = latitud;
        this.longitud = longitud;
        this.estado = estado;
        this.comentario = comentario;
    }
    public IncidenciaDto() {
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

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public double getLatitud() {
        return latitud;
    }

    public void setLatitud(double latitud) {
        this.latitud = latitud;
    }

    public double getLongitud() {
        return longitud;
    }

    public void setLongitud(double longitud) {
        this.longitud = longitud;
    }

    public boolean isEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }

    public String getComentario() {
        return comentario;
    }

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }
}
