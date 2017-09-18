package com.adsology;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;

public class ConsumeVerticle extends AbstractVerticle {

    private final String ADDRESS;

    public ConsumeVerticle(String address) {
        ADDRESS = address;
    }

    @Override
    public void start(Future<Void> startFuture) throws Exception {

        vertx.eventBus().consumer(ADDRESS, message -> {
            System.out.printf("Message %s received\n", message.body());
        });
    }
}
