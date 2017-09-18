package com.adsology;

import io.vertx.core.Vertx;
import io.vertx.core.json.Json;
import io.vertx.core.logging.Logger;
import io.vertx.core.logging.LoggerFactory;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;

public class Main {

    private static final Logger LOG = LoggerFactory.getLogger(Main.class);
    private static Vertx vertx;
    public static void main(String[] args) {
        vertx = Vertx.vertx();
        Router router = Router.router(vertx);
        router.get("/start/:senders/:receivers/:messages/:mps").handler(Main::countTime);

        vertx.createHttpServer()
                .requestHandler(router::accept).listen(8080);


    }

    private static void countTime(RoutingContext routingContext) {
        LOG.info("Received request " + routingContext.request().path());

        vertx.deployVerticle(new ConsumeVerticle("test"));
        vertx.deployVerticle(new SenderVerticle("test"));

        routingContext.response()
                .putHeader("content-type", "application/json; charset=utf-8")
                .end(Json.encodePrettily("Time is here"));
    }
}
