package com.example.repair;

import java.util.HashMap;
import java.util.Map;

public class Servicio {
    private String id;  // Necesario para Firebase
    private String nombre;
    private String descripcion;
    private double precio;


    // Constructor vacío obligatorio para Firebase
    public Servicio() {// Valores por defecto

    }

    // Constructor principal
    public Servicio(String nombre, String descripcion, double precio) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;

    }

    // Getters y setters (necesarios para Firebase)
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

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }



    // Método para convertir a Map (útil para actualizaciones en Firebase)
    public Map<String, Object> toMap() {
        Map<String, Object> result = new HashMap<>();
        result.put("nombre", nombre);
        result.put("descripcion", descripcion);
        result.put("precio", precio);
        return result;
    }

    // Método toString para depuración
    @Override
    public String toString() {
        return "Servicio{" +
                "id='" + id + '\'' +
                ", nombre='" + nombre + '\'' +
                ", descripcion='" + descripcion + '\'' +
                ", precio=" + precio +
                '}';
    }
}