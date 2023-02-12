package org.jismah.controladores;

import org.jismah.entidades.Producto;
import org.jismah.entidades.Usuario;
import org.jismah.entidades.carroCompras;
import org.jismah.entidades.ventasProductos;

import java.util.ArrayList;

public class Core {
    private ArrayList<Usuario> users;
    private ArrayList<Producto> products;
    private ArrayList<carroCompras> shops;
    private ArrayList<ventasProductos> sells;
    public static Core core = null;
    public static Usuario loggedUser;

    public Core() {
        super();
        this.users = new ArrayList<Usuario>();
        this.products = new ArrayList<Producto>();
        this.shops = new ArrayList<carroCompras>();
        this.sells = new ArrayList<ventasProductos>();
    }

    public static Core getInstance() {
        if (core == null) {
            core = new Core();
        }
        return core;
    }

    public ArrayList<Usuario> getUsers() {
        return users;
    }

    public void setUsers(ArrayList<Usuario> users) {
        this.users = users;
    }

    public ArrayList<Producto> getProducts() {
        return products;
    }

    public void setProducts(ArrayList<Producto> products) {
        this.products = products;
    }

    public ArrayList<carroCompras> getShops() {
        return shops;
    }

    public void setShops(ArrayList<carroCompras> shops) {
        this.shops = shops;
    }

    public ArrayList<ventasProductos> getSells() {
        return sells;
    }

    public void setSells(ArrayList<ventasProductos> sells) {
        this.sells = sells;
    }

    public static Usuario getLoggedUser() {
        return loggedUser;
    }

    public static void setLoggedUser(Usuario loggedUser) {
        Core.loggedUser = loggedUser;
    }

//    ADD ELEMENTS

    public void addUser(Usuario usuario) {
        users.add(usuario);
    }

    public void addProduct(Producto producto) {
        products.add(producto);
    }

    public void addCarroCompra(carroCompras cp) {
        shops.add(cp);
    }

    public void addVentas(ventasProductos vp) {
        sells.add(vp);
    }
    public static Core getStore() {
        return core;
    }

    // BUSQUEDA DE UN USUARIO
    public Usuario search_user(int id) {
        Usuario searched = null;
        for (Usuario user : users) {
            if (user.getId() == id) {
                searched = user;
            }
        }
        return searched;
    }

    // BUSQUEDA DE UN PRODUCTO
    public Producto search_product(int id) {
        Producto searched = null;
        for (Producto product : products) {
            if (product.getId() == id) {
                searched = product;
            }
        }
        return searched;
    }

    // BUSQUEDA DE UNA VENTA
    public ventasProductos search_sell(long id) {
        ventasProductos searched = null;
        for (ventasProductos sell : sells) {
            if (sell.getId() == id) {
                searched = sell;
            }
        }
        return searched;
    }

    // BUSQUEDA DE UN CARRITO
    public carroCompras search_shop(long id) {
        carroCompras searched = null;
        for (carroCompras shop : shops) {
            if (shop.getId() == id) {
                searched = shop;
            }
        }
        return searched;
    }



}
