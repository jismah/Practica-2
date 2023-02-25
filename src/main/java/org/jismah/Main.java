package org.jismah;

import io.javalin.http.staticfiles.Location;
import io.javalin.Javalin;
import org.jismah.controladores.*;


public class Main {
    public static void main(String[] args) {

        // INICIALIZANDO SERVIDOR
        Javalin app = Javalin.create(config ->{
           config.staticFiles.add(staticFiles-> {
             staticFiles.hostedPath = "/";
             staticFiles.directory = "/public";
             staticFiles.location = Location.CLASSPATH;
           });
        });

        app.start(8080);

        System.out.println("* Servidor Arriba!");

        app.get("/", ctx -> ctx.result("Welcome to E-Commerce Server!"));

        //Manejo de plantillas.
        new PlantillasControlador(app).aplicarRutas();

        //Manejadores de Endpoints
        new Manejadores(app).aplicarRutas();


    }
}