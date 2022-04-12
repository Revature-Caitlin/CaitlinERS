package com.revature.main;

import com.revature.controller.*;
import io.javalin.Javalin;

public class Driver {
    public static void main(String[] args) {
        Javalin app = Javalin.create((config) -> {
            config.enableCorsForAllOrigins();
        });

        map(app, new AuthenticationController(), new ReimbursementController(), new ExceptionController(), new UserController());

        app.start(8081);
    }

    private static void map(Javalin app, Controller... controllers) {
        for (Controller c : controllers) {
            c.mapEndpoints(app);
        }
    }
}
