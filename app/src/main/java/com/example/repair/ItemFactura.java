package com.example.repair;

import java.util.HashMap;
import java.util.Map;

public class ItemFactura {
    private String tipo; // "producto" o "servicio"
    private String descripcion;
    private double precioUnitario;
    private int cantidad;

    public ItemFactura() {}

    public ItemFactura(String tipo, String descripcion, double precioUnitario, int cantidad) {
        this.tipo = tipo;
        this.descripcion = descripcion;
        this.precioUnitario = precioUnitario;
        this.cantidad = cantidad;
    }

    // Getters y setters
    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public double getPrecioUnitario() { return precioUnitario; }
    public void setPrecioUnitario(double precioUnitario) { this.precioUnitario = precioUnitario; }
    public int getCantidad() { return cantidad; }
    public void setCantidad(int cantidad) { this.cantidad = cantidad; }

    public double getSubtotal() {
        return precioUnitario * cantidad;
    }

    public Map<String, Object> toMap() {
        Map<String, Object> result = new HashMap<>();
        result.put("tipo", tipo);
        result.put("descripcion", descripcion);
        result.put("precioUnitario", precioUnitario);
        result.put("cantidad", cantidad);
        return result;
    }
}