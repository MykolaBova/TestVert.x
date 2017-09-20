package com.adsology;

import io.vertx.core.Vertx;
import io.vertx.core.json.Json;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.RoutingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Collections;
import java.util.List;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.toList;

public class Main {

    private static Logger logger = LogManager.getLogger(Main.class);
    private static Vertx vertx;
    public static void main(String[] args) {
        vertx = Vertx.vertx();
        Router router = Router.router(vertx);
        router.get("/start/:senders/:receivers/:messages/:mps").handler(Main::doAllJob);

        vertx.createHttpServer()
                .requestHandler(router::accept).listen(8081);


    }

    private static void doAllJob(RoutingContext routingContext) {
        logger.info("Received request {}", routingContext.request().path());

        Integer senders = Integer.valueOf(routingContext.request().getParam("senders"));
        Integer receivers = Integer.valueOf(routingContext.request().getParam("receivers"));
        Integer messages = Integer.valueOf(routingContext.request().getParam("messages"));
        Integer messagesPerSecond = Integer.valueOf(routingContext.request().getParam("mps"));

        logger.info("Need to create {} senders and {} receivers for {} messages", senders, receivers, messages);

//        IntStream.range(0, receivers)
//                .forEach();

        // Create consumers
        List<String> addresses = IntStream.range(0, receivers)
                .mapToObj(String::valueOf)
                .collect(toList());
        for (int i = 0; i < receivers; i++) {
            vertx.deployVerticle(new ConsumeVerticle(addresses.get(i)));
        }

        // Create senders

        int sendDelay = messagesPerSecond * 1000 * senders;
        if (senders > receivers) {
            int loop = messages / senders;
            int index = 0;
            for (int i = 0; i < senders; i++) {
                vertx.deployVerticle(new SenderVerticle(
                        sendDelay,
                        loop,
                        Collections.singletonList(
                                addresses.get(index))));
                index = index < index - 1 ? ++index : 0 ;
            }
        } else if (senders < receivers) {
            int loop = messages / receivers;

            int receiversPerSender = receivers / senders;
            int lastIndex = 0;
            for (int i = 0; i < senders; i++) {
                vertx.deployVerticle(new SenderVerticle(
                        sendDelay,
                        loop,
                        addresses.subList(
                                lastIndex,
                                lastIndex + receiversPerSender)));
                lastIndex += receiversPerSender;
            }
        }

        routingContext.response()
                .putHeader("content-type", "application/json; charset=utf-8")
                .end(Json.encodePrettily("Time is here"));
    }
}
