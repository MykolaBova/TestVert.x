package com.adsology;

import io.vertx.core.Vertx;
import io.vertx.core.json.Json;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Main {

    private static Logger logger = LogManager.getLogger(Main.class);
    private static Vertx vertx;
    public static void main(String[] args) {
        vertx = Vertx.vertx();
        Router router = Router.router(vertx);
        router.get("/start/:senders/:receivers/:messages/:mps").handler(Main::doAllJob);

        vertx.createHttpServer()
                .requestHandler(router::accept).listen(8080);


    }

    private static void doAllJob(RoutingContext routingContext) {
        logger.info("Received request {}", routingContext.request().path());

        String senders = routingContext.request().getParam("senders");
        String receivers = routingContext.request().getParam("receivers");
        String messages = routingContext.request().getParam("messages");
        String messagesPerSecond = routingContext.request().getParam("mps");

        logger.info("Need to create {} senders and {} receivers for {} messages", senders, receivers, messages);
        vertx.deployVerticle(new ConsumeVerticle("test"));
        vertx.deployVerticle(new SenderVerticle("test"));

        routingContext.response()
                .putHeader("content-type", "application/json; charset=utf-8")
                .end(Json.encodePrettily("Time is here"));
    }
}
