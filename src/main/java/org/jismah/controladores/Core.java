package org.jismah.controladores;

import org.jismah.entidades.Producto;
import org.jismah.entidades.Usuario;
import org.jismah.entidades.carroCompras;
import org.jismah.entidades.ventasProductos;

import java.math.BigDecimal;
import java.util.*;

public class Core {
    private List<Producto> products;
    private List<Usuario> users;
    private Map<String, carroCompras> carts;

    private List<ventasProductos> sales;
    public static Core core = null;
    public static Usuario loggedUser;

    public Core() {
        super();
        this.products = new ArrayList<>();
        this.users = new ArrayList<>();
        this.carts = new HashMap<>();
        this.sales = new ArrayList<>();
    }

    public static Core getInstance() {
        if (core == null) {
            core = new Core();
        }
        return core;
    }

    public Producto addProduct(String name, BigDecimal price) {
        Producto product = new Producto(name, price);
        products.add(product);
        return product;
    }

    public void editProduct(UUID id, String name, BigDecimal price) {
        Producto product = getProductById(id);
        if (product != null) {
            product.setNombre(name);
            product.setPrecio(price);
        } else {
            System.out.print("No se encontro el producto con id: " + id.toString());
        }
    }

    public Usuario addUser(String username, String name, String password) {
        Usuario user = new Usuario(username, name, password);
        users.add(user);
        return user;
    }

    public void deleteUser(Usuario user) {
        users.remove(user);
    }

    public Producto getProductById(UUID id) {
        for (Producto product : products) {
            if (product.getId().equals(id)) {
                return product;
            }
        }
        return null;
    }

    public Producto getProductByName(String name) {
        for (Producto product : products) {
            if (product.getNombre().equals(name)) {
                return product;
            }
        }
        return null;
    }

    public void deleteProduct(Producto product) {
        products.remove(product);
    }

    public Usuario getUserByUsername(String username) {
        for (Usuario user : users) {
            if (user.getUsuario().equals(username)) {
                return user;
            }
        }
        return null;
    }

    public boolean authenticateUser(String username, String password) {
        Usuario user = getUserByUsername(username);
        if (user == null) {
            return false;
        }
        return user.getPassword().equals(password);
    }

    public List<Producto> getAllProducts() {
        return products;
    }

    public carroCompras getCartForSession(String sessionId) {
        if (!carts.containsKey(sessionId)) {
            carts.put(sessionId, new carroCompras());
        }
        return carts.get(sessionId);
    }

    public void addItemToCart(String sessionId, UUID productId, int cant) {
        carroCompras cart = getCartForSession(sessionId);
        cart.addItem(productId, cant);
    }

    public void removeItemFromCart(String sessionId, UUID productId) {
        carroCompras cart = getCartForSession(sessionId);
        cart.removeItem(productId);
    }

    public void reduceItemfromCart(String sessionId, UUID productId, int cant) {
        carroCompras cart = getCartForSession(sessionId);
        cart.reduceItem(productId, cant);
    }

    public Map<UUID, Integer> getItemsInCart(String sessionId) {
        carroCompras cart = getCartForSession(sessionId);
        return cart.getItems();
    }

    public List<Producto> getProductsInCart(String sessionId) {
        carroCompras cart = getCartForSession(sessionId);
        List<Producto> products = new ArrayList<>();
        for (UUID id : cart.getItems().keySet()) {
            products.add(getProductById(id));
        }
        return products;
    }

    public Integer getCartCountBySession(String sessionId) {
        carroCompras cart = getCartForSession(sessionId);
        return cart.getItemCount();
    }

    public BigDecimal getCartTotal(String sessionId) {
        carroCompras cart = getCartForSession(sessionId);
        return cart.getTotal();
    }

    public void clearCartBySession(String sessionId) {
        carroCompras cart = getCartForSession(sessionId);
        cart.clearCart();
    }

    public void newSale(String sessionId, String clientName) {
        carroCompras cart = getCartForSession(sessionId);
        sales.add(new ventasProductos(1L, new Date(),clientName, (List<Producto>) cart.getItems()));
    }

    public List<ventasProductos> getSales() {
        return sales;
    }

    public void editUser(String old, String username, String name, String password) {
        Usuario user = getUserByUsername(old);
        user.setNombre(name);
        user.setPassword(password);
        user.setUsuario(username);
    }

    public List<Usuario> getAllUsers() {
        return users;
    }
}
