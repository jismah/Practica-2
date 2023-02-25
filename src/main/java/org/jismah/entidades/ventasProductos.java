package org.jismah.entidades;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public class ventasProductos {

    private long id;

    private Date fechaCompra;

    private String nombreCliente;

    private Map<UUID, Integer> listaProductos;

    public ventasProductos(long id, Date fechaCompra, String nombreCliente, List<Producto> listaProductos) {
        this.id = id;
        this.fechaCompra = fechaCompra;
        this.nombreCliente = nombreCliente;
        this.listaProductos = (Map<UUID, Integer>) listaProductos;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Date getFechaCompra() {
        return fechaCompra;
    }

    public void setFechaCompra(Date fechaCompra) {
        this.fechaCompra = fechaCompra;
    }

    public String getNombreCliente() {
        return nombreCliente;
    }

    public void setNombreCliente(String nombreCliente) {
        this.nombreCliente = nombreCliente;
    }

    public List<Producto> getListaProductos() {
        return (List<Producto>) listaProductos;
    }

    public void setListaProductos(List<Producto> listaProductos) {
        this.listaProductos = (Map<UUID, Integer>) listaProductos;
    }
}
