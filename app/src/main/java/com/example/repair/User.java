package com.example.repair;

public class User {
    private String email;
    private String password; // Cambiado de "contraseña" a "password" para seguir convenciones

    // Constructor vacío requerido por Firebase
    public User() {
    }

    // Constructor con parámetros
    public User(String email, String password) {
        this.email = email;
        this.password = password;
    }

    // Getters y setters
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}