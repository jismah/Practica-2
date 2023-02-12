package org.jismah.controladores;

import io.javalin.Javalin;
import org.jismah.util.BaseControlador;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HandlerData extends BaseControlador {
    public HandlerData(Javalin app) {
        super(app);
    }

    @Override
    public void aplicarRutas() {
        /**
         * Ejemplo para leer por parametros de consulta (query param)
         * http://localhost:8080/producto?id=01&nombre=Jabon
         */
        app.post("/producto", ctx -> {
            List<String> product = new ArrayList<>();

            //listando la informacion.
            ctx.formParamMap().forEach((key, lista) -> {
                product.add(String.format("[%s] = [%s]", key, String.join(",", lista)));
            });

            System.out.println(Arrays.toString(product.toArray()) + "\n");

            ctx.redirect("/index.html");
        });

    }
}
