package com.adsology;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;

public class SenderVerticle extends AbstractVerticle {
    private final String ADDRESS;

    public SenderVerticle(String ADDRESS) {
        this.ADDRESS = ADDRESS;
    }

    @Override
    public void start(Future<Void> startFuture) throws Exception {
        vertx.eventBus().send(ADDRESS, "ping");
        System.out.println("Sender send");
    }
}
