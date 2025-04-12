package org.example.dto;

public class Producto_RecaudacionDTO {
    private String nombre;
    private float recaudacion;

    public Producto_RecaudacionDTO(String nombre, float recaudacion) {
        this.nombre = nombre;
        this.recaudacion = recaudacion;
    }

    public String getNombre() {
        return nombre;
    }

    public float getRecaudacion() {
        return recaudacion;
    }

    @Override
    public String toString() {
        return "Producto más rentable: " + nombre + " | Recaudación: $" + recaudacion;
    }
}
