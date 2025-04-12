package org.example.dto;

public class Cliente_FacturacionDTO {
    private String nombre;
    private float totalFacturado;

    public Cliente_FacturacionDTO(String nombre, float totalFacturado) {
        this.nombre = nombre;
        this.totalFacturado = totalFacturado;
    }

    public String getNombre() {
        return nombre;
    }

    public float getTotalFacturado() {
        return totalFacturado;
    }

    @Override
    public String toString() {
        return nombre + " | Total facturado: $" + totalFacturado;
    }
}
