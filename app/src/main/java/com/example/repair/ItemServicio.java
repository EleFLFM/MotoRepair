package com.example.repair;

import java.util.HashMap;
import java.util.Map;

public class ItemServicio {
    private String servicioId;
    private String nombre;
    private String descripcion;
    private double precio;

    // Constructor vacío para Firebase
    public ItemServicio() {}

    // Constructor desde Servicio
    public ItemServicio(Servicio servicio) {
        this.servicioId = servicio.getId();
        this.nombre = servicio.getNombre();
        this.descripcion = servicio.getDescripcion();
        this.precio = servicio.getPrecio();
    }
    // Constructor que coincida con tu llamado
    public ItemServicio(String servicioId, String nombre, String descripcion, double precio) {
        this.servicioId = servicioId;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precio = precio;
    }

    // Getters y Setters
    public String getServicioId() { return servicioId; }
    public void setServicioId(String servicioId) { this.servicioId = servicioId; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public double getPrecio() { return precio; }
    public void setPrecio(double precio) { this.precio = precio; }

    // Método para Firebase
    public Map<String, Object> toMap() {
        Map<String, Object> result = new HashMap<>();
        result.put("servicioId", servicioId);
        result.put("nombre", nombre);
        result.put("descripcion", descripcion);
        result.put("precio", precio);
        return result;
    }


}