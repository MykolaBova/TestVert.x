package com.adsology;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SenderVerticle extends AbstractVerticle {
    private static Logger logger = LogManager.getLogger(SenderVerticle.class);

    private final String ADDRESS;

    public SenderVerticle(String ADDRESS) {
        this.ADDRESS = ADDRESS;
    }

    @Override
    public void start(Future<Void> startFuture) throws Exception {
        vertx.eventBus().send(ADDRESS, "ping");
        logger.info("Sender send message to {}", ADDRESS);
    }
}
