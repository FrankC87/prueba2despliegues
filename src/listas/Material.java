package listas;

import java.io.Serializable;

public class Material implements Serializable{

    private String nombre;

    private Double cantidad;

    public Material(String nombre, Double cantidad) {
        this.nombre = nombre;
        this.cantidad = cantidad;
    }

    public Material() {
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Double getCantidad() {
        return cantidad;
    }

    public void setCantidad(Double cantidad) {
        this.cantidad = cantidad;
    }

    @Override
    public String toString() {
        return "{" + "nombre=" + nombre + ", cantidad=" + cantidad + '}';
    }
    
}
