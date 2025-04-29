package com.example.repair;

public class FacturaItem {
    private String id;
    private String descripcion;
    private double precio;
    private int cantidad;
    private String tipo; // "servicio" o "producto"

    public FacturaItem() {}

    public FacturaItem(String descripcion, double precio, int cantidad, String tipo) {
        this.descripcion = descripcion;
        this.precio = precio;
        this.cantidad = cantidad;
        this.tipo = tipo;
    }

    // Getters y setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public double getPrecio() { return precio; }
    public void setPrecio(double precio) { this.precio = precio; }
    public int getCantidad() { return cantidad; }
    public void setCantidad(int cantidad) { this.cantidad = cantidad; }
    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public double getSubtotal() {
        return precio * cantidad;
    }
}