package org.jismah.controladores;

import io.javalin.Javalin;
import io.javalin.http.Context;

public class Manejadores {

    private Javalin app;

    public Manejadores(Javalin app) {
        this.app = app;
    }

    public void aplicarRutas () {
        /**
         * Manejador que se aplica todas las llamadas que sean realizada.
         */
        app.before(ctx -> {
            //
            String message = String.format("Traffic IP: %s\nRuta: %s, Metodo: %s\n",
                    ctx.req().getRemoteHost(),
                    ctx.path(),
                    ctx.req().getMethod());
            //
            System.out.println(message);
        });
    }
}
