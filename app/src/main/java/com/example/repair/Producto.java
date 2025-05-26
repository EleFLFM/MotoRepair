package com.example.repair;

public class Producto {
    private String id;
    private String nombre;
    private double precio;
    private int cantidad;
    private String imageUrl; // Nuevo campo para la URL de la imagen

    public Producto() {}

    public Producto(String nombre, double precio, int cantidad, String imageUrl) {
        this.nombre = nombre;
        this.precio = precio;
        this.cantidad = cantidad;
        this.imageUrl = imageUrl;
    }

    // Getters y setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public double getPrecio() { return precio; }
    public void setPrecio(double precio) { this.precio = precio; }
    public int getCantidad() { return cantidad; }
    public void setCantidad(int cantidad) { this.cantidad = cantidad; }
    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
}