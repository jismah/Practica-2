package org.jismah.controladores;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import io.javalin.rendering.JavalinRenderer;
import io.javalin.rendering.template.JavalinThymeleaf;
import org.jismah.util.BaseControlador;
import io.javalin.Javalin;
import org.jismah.entidades.Usuario;
import java.math.BigDecimal;

import java.util.*;

public class PlantillasControlador extends BaseControlador {

    private static final String ADMIN_USERNAME = "admin";
    private static final String ADMIN_PASSWORD = "admin";
    private static final String SESSION_ID_KEY = "session-id";
    private static final Map<String, Boolean> ADMIN_SESSIONS = new HashMap<>();
    private static final Map<String, Boolean> SESSIONS = new HashMap<>();
    private static Usuario loggedUser = null;

    public PlantillasControlador(Javalin app) {
        super(app);
        registrandoPlantillas();
    }

    private void registrandoPlantillas(){
        JavalinRenderer.register(new JavalinThymeleaf(), ".html");
    }

    @Override
    public void aplicarRutas() {
        app.routes(() -> {

            app.get("/", ctx -> {
                String sessionId = getSessionId(ctx);
                if (isadmin(sessionId)) {
                    Map<String, Object> model = viewModel(sessionId);
                    model.put("message", "Saludos, " + sessionId);
                    ctx.render("/login.html", model);

                } else if (userLoggedSession(sessionId)) {
                    Map<String, Object> model = viewModel(sessionId);
                    model.put("message", "Bienvenido " + loggedUser.getNombre());
                    ctx.render("/login.html", model);
                } else {
                    ctx.redirect("/products");
                }
            });

            // Logins y registro de usuarios nuevos
            app.get("/login", ctx -> {
                String sessionId = getSessionId(ctx);

                Map<String, Object> model = viewModel(sessionId);
                ctx.render("/login.html", model);
            });

            app.post("/login", new Handler() {
                @Override
                public void handle(Context ctx) throws Exception {
                    String username = ctx.formParam("username");
                    String password = ctx.formParam("password");

                    if (ADMIN_USERNAME.equals(username) && ADMIN_PASSWORD.equals(password)) {
                        String sessionId = getSessionId(ctx);
                        ADMIN_SESSIONS.put(sessionId, true);
                        ctx.cookie(SESSION_ID_KEY, sessionId);
                        ctx.redirect("/");
                    } else if (Core.getInstance().authenticateUser(username, password)) {
                        String sessionId = getSessionId(ctx);
                        SESSIONS.put(sessionId, true);
                        loggedUser = Core.getInstance().getUserByUsername(username);

                        ctx.cookie(SESSION_ID_KEY, sessionId);
                        ctx.redirect("/");
                    } else {
                        ctx.status(401);
                        String sessionId = getSessionId(ctx);

                        Map<String, Object> model = viewModel(sessionId);
                        model.put("message", "Error en credenciales!");
                        model.put("return", "/login");
                        ctx.render("/index.html", model);
                    }
                }
            });

            app.get("/logout", ctx -> {
                String sessionId = getSessionId(ctx);
                if (ADMIN_SESSIONS.containsKey(sessionId)) {
                    ADMIN_SESSIONS.remove(sessionId);
                    ctx.removeCookie(SESSION_ID_KEY);
                } else {
                    SESSIONS.remove(sessionId);
                    ctx.removeCookie(SESSION_ID_KEY);
                }
                loggedUser = null;
                ctx.redirect("/login");
            });

            //CRUD Usuarios
            Handler userListHandler = ctx -> {
                if (isadmin(getSessionId(ctx))) {
                    String sessionId = getSessionId(ctx);

                    Map<String, Object> model = viewModel(sessionId);
                    model.put("users", Core.getInstance().getAllUsers());
                    ctx.render("/usuarios.html", model);
                } else {
                    ctx.redirect("/");
                }
            };
            app.get("/users", userListHandler);

            app.get("/user/new", ctx -> {
                String sessionId = getSessionId(ctx);
                Map<String, Object> model = viewModel(sessionId);
                ctx.render("/userDetails.html", model);
            });

            app.post("/user/new", new Handler() {
                @Override
                public void handle(Context ctx) throws Exception {
                    String username = ctx.formParam("username");
                    String name = ctx.formParam("name");
                    String password = ctx.formParam("password");

                    if (Core.getInstance().getUserByUsername(username) != null) {
                        ctx.status(401);
                        String sessionId = getSessionId(ctx);

                        Map<String, Object> model = viewModel(sessionId);
                        model.put("message", "Error: Usuario ya existe");
                        model.put("return", "/user/new");
                        ctx.render("/index.html", model);
                    } else {
                        Core.getInstance().addUser(username, name, password);
                        ctx.redirect("/users");
                    }
                }
            });

            app.get("/user/edit", ctx -> {
                String sessionId = getSessionId(ctx);
                if (isadmin(sessionId)) {
                    String username = ctx.queryParam("username");

                    Map<String, Object> model = viewModel(sessionId);
                    model.put("user", Core.getInstance().getUserByUsername(username));
                    ctx.render("/userDetails.html", model);
                } else {
                    ctx.redirect("/");
                }
            });

            app.post("/user/edit", new Handler() {
                @Override
                public void handle(Context ctx) throws Exception {
                    String old = ctx.formParam("old");
                    String username = ctx.formParam("username");
                    String name = ctx.formParam("name");
                    String password = ctx.formParam("password");

                    if (Core.getInstance().getUserByUsername(username) != null && !(old.equals(username))) {
                        String sessionId = getSessionId(ctx);

                        Map<String, Object> model = viewModel(sessionId);
                        model.put("message", "El username debe ser unico");
                        model.put("return", "/users");
                        ctx.render("/views/messages.html", model);
                    } else {
                        Core.getInstance().editUser(old, username, name, password);
                        ctx.redirect("/users");
                    }
                }
            });

            app.post("/user/delete", new Handler() {
                @Override
                public void handle(Context ctx) throws Exception {
                    String username = ctx.formParam("username");
                    Core.getInstance().deleteUser(Core.getInstance().getUserByUsername(username));
                    ctx.redirect("/users");
                }
            });

            //Carrito de Compras
            app.get("/cart", ctx -> {
                String sessionId = getSessionId(ctx);

                Map<String, Object> model = viewModel(sessionId);
                model.put("products", Core.getInstance().getProductsInCart(sessionId));
                model.put("items", Core.getInstance().getItemsInCart(sessionId));
                model.put("total", Core.getInstance().getCartTotal(sessionId));
                ctx.render("/views/cart.html", model);
            });

            app.post("/add-to-cart", new Handler() {
                @Override
                public void handle(Context ctx) throws Exception {
                    UUID id = UUID.fromString(ctx.formParam("id"));
                    Integer cant = Integer.valueOf(ctx.formParam("cant"));

                    String sessionId = getSessionId(ctx);
                    Core.getInstance().addItemToCart(sessionId, id, cant);

                    ctx.redirect("/cart");
                }
            });

            app.post("/reduce-from-cart", new Handler() {
                @Override
                public void handle(Context ctx) throws Exception {
                    UUID id = UUID.fromString(ctx.formParam("id"));
                    Integer cant = Integer.valueOf(ctx.formParam("cant"));

                    String sessionId = getSessionId(ctx);
                    Core.getInstance().reduceItemfromCart(sessionId, id, cant);

                    ctx.redirect("/cart");
                }
            });

            app.post("/remove-from-cart", new Handler() {
                @Override
                public void handle(Context ctx) throws Exception {
                    UUID id = UUID.fromString(ctx.formParam("id"));

                    String sessionId = getSessionId(ctx);
                    Core.getInstance().removeItemFromCart(sessionId, id);

                    ctx.redirect("/cart");
                }
            });

            // CRUD de Productos
            Handler productListHandler = ctx -> {
                String sessionId = getSessionId(ctx);

                Map<String, Object> model = viewModel(sessionId);
                model.put("products", Core.getInstance().getAllProducts());
                ctx.render("/views/products.html", model);
            };
            app.get("/products", productListHandler);

            app.get("/product/new", ctx -> {
                String sessionId = getSessionId(ctx);
                if (isadmin(sessionId)) {
                    Map<String, Object> model = viewModel(sessionId);
                    ctx.render("/views/productDetails.html", model);
                } else {
                    ctx.redirect("/");
                }
            });

            app.post("/product/new", new Handler() {
                @Override
                public void handle(Context ctx) throws Exception {
                    String name = ctx.formParam("name");
                    BigDecimal price = new BigDecimal(ctx.formParam("price"));

                    if (Core.getInstance().getProductByName(name) != null) {
                        ctx.status(401);
                        String sessionId = getSessionId(ctx);

                        Map<String, Object> model = viewModel(sessionId);
                        model.put("message", "ERROR: Un producto con ese nombre ya existe");
                        model.put("return", "/product/new");
                        ctx.render("/views/messages.html", model);
                    } else {
                        Core.getInstance().addProduct(name, price);
                        ctx.redirect("/products");
                    }
                }
            });

            app.get("/product/edit", ctx -> {
                String sessionId = getSessionId(ctx);
                if (isadmin(sessionId)) {
                    UUID id = UUID.fromString(ctx.queryParam("id"));

                    Map<String, Object> model = viewModel(sessionId);
                    model.put("product", Core.getInstance().getProductById(id));
                    ctx.render("/views/productDetails.html", model);
                } else {
                    ctx.redirect("/");
                }
            });

            app.post("/product/edit", new Handler() {
                @Override
                public void handle(Context ctx) throws Exception {
                    UUID id = UUID.fromString(ctx.formParam("id"));
                    String name = ctx.formParam("name");
                    BigDecimal price = new BigDecimal(ctx.formParam("price"));

                    Core.getInstance().editProduct(id, name, price);
                    ctx.redirect("/products");
                }
            });

            app.post("/product/delete", new Handler() {
                @Override
                public void handle(Context ctx) throws Exception {
                    UUID id = UUID.fromString(ctx.formParam("id"));

                    Core.getInstance().deleteProduct(Core.getInstance().getProductById(id));
                    ctx.redirect("/products");
                }
            });

            // Ventas

            Handler salesListHandler = ctx -> {
                String sessionId = getSessionId(ctx);

                if (isadmin(sessionId)) {
                    Map<String, Object> model = viewModel(sessionId);
                    model.put("sales", Core.getInstance().getSales());

                    ctx.render("/views/sales.html", model);
                } else {
                    Map<String, Object> model = viewModel(sessionId);
                    model.put("message", "Debe ser administrador para acceder a esta zona");
                    model.put("return", "/");
                    ctx.render("/views/messages.html", model);
                }
            };
            app.get("/sales", salesListHandler);
            app.post("/process-sale", new Handler() {
                @Override
                public void handle(Context ctx) throws Exception {
                    String sessionId = getSessionId(ctx);
                    if (Core.getInstance().getCartCountBySession(sessionId) > 0) {
                        if (isadmin(sessionId)) {
                            Core.getInstance().newSale(sessionId, "Admin");
                            Core.getInstance().clearCartBySession(sessionId);
                            ctx.redirect("/products");

                        } else if (loggedUser == null) {
                            ctx.status(401);

                            Map<String, Object> model = viewModel(sessionId);
                            model.put("message", "Necesita entrar a una cuenta antes de comprar");
                            model.put("return", "/login");
                            ctx.render("/views/messages.html", model);

                        } else {
                            Core.getInstance().newSale(sessionId, loggedUser.getNombre());
                            Core.getInstance().clearCartBySession(sessionId);
                            ctx.redirect("/products");
                        }
                    } else {
                        Map<String, Object> model = viewModel(sessionId);
                        model.put("message", "Debe tener productos en el carrito para procesar la venta");
                        model.put("return", "/products");
                        ctx.render("/views/messages.html", model);
                    }
                }
            });

        });




    }

    private static String getSessionId(Context ctx) {
        String sessionId = ctx.cookie(SESSION_ID_KEY);
        if (sessionId == null) {
            sessionId = genSessionID();
            ctx.cookie(SESSION_ID_KEY, sessionId);
        }
        return sessionId;
    }

    private static boolean userLoggedSession(String sessionId) {
        return SESSIONS.getOrDefault(sessionId, false);
    }

    private static String genSessionID() {
        return java.util.UUID.randomUUID().toString();
    }

    private static boolean isadmin(String sessionId) {
        return ADMIN_SESSIONS.getOrDefault(sessionId, false);
    }

    private static Map<String, Object> viewModel(String sessionId) {
        Map<String, Object> model = new HashMap<>();
        model.put("isAdmin", isadmin(sessionId));
        model.put("isLogged", loggedUser != null);
        model.put("itemCount", Core.getInstance().getCartCountBySession(sessionId));
        return model;
    }
}

