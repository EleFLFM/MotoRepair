package com.example.repair;

import java.util.HashMap;
import java.util.Map;

public class ItemProduct {
    private String productId;
    private String name;
    private double unitPrice;
    private int quantity;
    private double subtotal;

    public ItemProduct() {}

    public ItemProduct(String productId, String name, double unitPrice, int quantity) {
        this.productId = productId;
        this.name = name;
        this.unitPrice = unitPrice;
        this.quantity = quantity;
        this.subtotal = unitPrice * quantity;
    }

    // Getters y Setters
    public String getProductId() { return productId; }
    public void setProductId(String productId) { this.productId = productId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public double getUnitPrice() { return unitPrice; }
    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
        calculateSubtotal();
    }

    public int getQuantity() { return quantity; }
    public void setQuantity(int quantity) {
        this.quantity = quantity;
        calculateSubtotal();
    }

    public double getSubtotal() { return subtotal; }
    public void setSubtotal(double subtotal) { this.subtotal = subtotal; }

    public void calculateSubtotal() {
        this.subtotal = unitPrice * quantity;
    }

    // Método para Firebase
    public Map<String, Object> toMap() {
        Map<String, Object> result = new HashMap<>();
        result.put("productId", productId);
        result.put("name", name);
        result.put("unitPrice", unitPrice);
        result.put("quantity", quantity);
        result.put("subtotal", subtotal);
        return result;
    }
}