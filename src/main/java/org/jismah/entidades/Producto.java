package org.jismah.entidades;

import java.math.BigDecimal;
import java.util.UUID;

public class Producto {

    private UUID id;

    private String nombre;

    private BigDecimal precio;

    private int cantidad;


    public Producto(String name, BigDecimal price) {
        this.nombre = name;
        this.precio = price;
        this.id = UUID.randomUUID();
    }


    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public BigDecimal getPrecio() {
        return precio;
    }



    public void setPrecio(BigDecimal precio) {
        this.precio = precio;
    }

    @Override
    public String toString() {
        return "Producto{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", precio=" + precio +
                '}';
    }
}
