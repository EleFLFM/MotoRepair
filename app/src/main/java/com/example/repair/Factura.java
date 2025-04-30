package com.example.repair;

import java.util.List;

public class Factura {
    private String numeroFactura;
    private String nombreCliente;
    private String fecha;
    private List<ItemProduct> productos;
    private List<ItemServicio> servicios;
    private double total;
    private String userId;
    private String estado; // Nuevo campo: estado de la factura (pagada, pendiente, cancelada)
    private String descripcion; // Nuevo campo: descripción de la factura

    // Constructor vacío requerido por Firebase
    public Factura() {}

    // Getters y setters
    public String getNumeroFactura() { return numeroFactura; }
    public void setNumeroFactura(String numeroFactura) { this.numeroFactura = numeroFactura; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public String getNombreCliente() { return nombreCliente; }
    public void setNombreCliente(String nombreCliente) { this.nombreCliente = nombreCliente; }

    public String getFecha() { return fecha; }
    public void setFecha(String fecha) { this.fecha = fecha; }

    public List<ItemProduct> getProductos() { return productos; }
    public void setProductos(List<ItemProduct> productos) { this.productos = productos; }

    public List<ItemServicio> getServicios() { return servicios; }
    public void setServicios(List<ItemServicio> servicios) { this.servicios = servicios; }

    public double getTotal() { return total; }
    public void setTotal(double total) { this.total = total; }

    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }

    public void calcularTotal() {
        total = 0.0;
        if (productos != null) {
            for (ItemProduct producto : productos) {
                total += producto.getSubtotal();
            }
        }
        if (servicios != null) {
            for (ItemServicio servicio : servicios) {
                total += servicio.getPrecio();
            }
        }
    }
}