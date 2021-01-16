package com.example.redesaplication.Models;

public class Usuario {
    private String Nombre;
    private String Contrasena;

    public Usuario() {
    }

    public Usuario(String nombre, String contrasena) {
        this.Nombre = nombre;
        this.Contrasena = contrasena;
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String nombre) {
        Nombre = nombre;
    }

    public String getContrasena() {
        return Contrasena;
    }

    public void setContrasena(String contrasena) {
        Contrasena = contrasena;
    }
}
