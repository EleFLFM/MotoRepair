package com.example.repair;

public class Factura {
    private String id;
    private String cliente;
    private String fecha;
    private double total;
    private String descripcion;
    private String estado; // "pendiente", "pagada", "cancelada"

    // Constructor vacío requerido por Firebase
    public Factura() {}

    public Factura(String cliente, String fecha, double total, String descripcion) {
        this.cliente = cliente;
        this.fecha = fecha;
        this.total = total;
        this.descripcion = descripcion;
        this.estado = "pendiente";
    }

    // Getters y setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getCliente() { return cliente; }
    public void setCliente(String cliente) { this.cliente = cliente; }
    public String getFecha() { return fecha; }
    public void setFecha(String fecha) { this.fecha = fecha; }
    public double getTotal() { return total; }
    public void setTotal(double total) { this.total = total; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
}