package com.adsology;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import io.vertx.core.eventbus.EventBus;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SenderVerticle extends AbstractVerticle {
    private static Logger logger = LogManager.getLogger(SenderVerticle.class);

    private final String[] addresses;
    private final long delay;
    private final int loop;

    public SenderVerticle(int delay, int loop, String... addresses) {
        this.addresses = addresses;
        this.delay = delay;
        this.loop = loop;
    }

    @Override
    public void start(Future<Void> startFuture) throws Exception {
        EventBus eb = vertx.eventBus();

        vertx.setPeriodic(delay, new Handler<Long>() {
            int count = 0;

            @Override
            public void handle(Long event) {
                for (int i = 0; i < addresses.length; i++) {
                    vertx.eventBus().send(addresses[i], "ping");
                    logger.info("Sender send message to {}", addresses[i]);
                }

                if (++count > loop) {
                    System.out.println("sender finish");
                    vertx.cancelTimer(event);
                }
            }
        });
/*
        vertx.eventBus().send(addresses, "ping");
//        vertx.setPeriodic(delay, sender -> {
//            vertx.eventBus().send(addresses, "ping");
//        });

        logger.info("Sender send message to {}", addresses);
        */
    }
}
