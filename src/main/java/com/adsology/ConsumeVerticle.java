package com.adsology;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ConsumeVerticle extends AbstractVerticle {

    private static Logger logger = LogManager.getLogger(ConsumeVerticle.class);

    private final String ADDRESS;

    public ConsumeVerticle(String address) {
        ADDRESS = address;
    }

    @Override
    public void start(Future<Void> startFuture) throws Exception {

        vertx.eventBus().consumer(ADDRESS, message -> {
            logger.info("Message {} received", message.body());
        });
    }
}
