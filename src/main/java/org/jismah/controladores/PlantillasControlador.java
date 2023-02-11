package org.jismah.controladores;
import io.javalin.rendering.JavalinRenderer;
import org.jismah.util.BaseControlador;
import io.javalin.Javalin;
import org.jismah.entidades.Usuario;
//import io.javalin.plugin.rendering.JavalinRenderer;
//import io.javalin.plugin.rendering.template.JavalinThymeleaf;

import org.jetbrains.annotations.NotNull;

import java.util.*;

import static io.javalin.apibuilder.ApiBuilder.*;

public class PlantillasControlador extends BaseControlador {

    public PlantillasControlador(Javalin app) {
        super(app);
        registrandoPlantillas();
    }

    /**
     * Registrando la plantilla.
     */
    private void registrandoPlantillas(){
        //registrando los sistemas de plantilla.
//        JavalinRenderer.register(JavalinThymeleaf.INSTANCE, ".html");
    }

    @Override
    public void aplicarRutas() {
        app.routes(() -> {

            path("/admin", () -> {

                /**
                 * http://localhost:8080/admin
                 */
                get("/", ctx -> {
                    List<Usuario> listaUsuario = getUsuarios();

                    Map<String, Object> modelo = new HashMap<>();
                    modelo.put("Lista Usuarios", listaUsuario);

                    ctx.render("/index.html", modelo);
                });
            });


        });
    }

    @NotNull
    private List<Usuario> getUsuarios() {
        //listando los usuarios..
        List<Usuario> listaUsuario = new ArrayList<>();
        listaUsuario.add(new Usuario(1, "JohnRodriguez", "John", "123456"));
        listaUsuario.add(new Usuario(2, "GabrielCepeda", "Gabriel", "123456"));
        listaUsuario.add(new Usuario(3, "EduardoMartinez", "Eduardo", "123456"));
        return listaUsuario;
    }
}

