package com.example.arduinoapp;

/**
 * Created by Christian on 17/11/2016.
 */

public class Menu {

    private String id;
    private int Imagen;
    private String Nombre;

    public Menu(String id,int imagen, String nombre) {
        this.id = id;
        this.Imagen = imagen;
        this.Nombre = nombre;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getImagen() {
        return Imagen;
    }

    public void setImagen(int imagen) {
        Imagen = imagen;
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String nombre) {
        Nombre = nombre;
    }
}
