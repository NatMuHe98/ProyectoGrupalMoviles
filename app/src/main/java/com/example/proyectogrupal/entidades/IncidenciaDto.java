package com.example.proyectogrupal.entidades;

public class IncidenciaDto {

    private String id;
    private String nombre;
    private String imageUrl;
    private String descripcion;
    private int ubicacion;
    private boolean estado;
    private String comentario;

    public IncidenciaDto(String id, String nombre, String imageUrl, String descripcion, int ubicacion, boolean estado, String comentario) {
        if (nombre.trim().equals("")){
            nombre = "Sin nombre";
        }
        this.id = id;
        this.nombre = nombre;
        this.imageUrl = imageUrl;
        this.descripcion = descripcion;
        this.ubicacion = ubicacion;
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

    public int getUbicacion() {
        return ubicacion;
    }

    public void setUbicacion(int ubicacion) {
        this.ubicacion = ubicacion;
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
