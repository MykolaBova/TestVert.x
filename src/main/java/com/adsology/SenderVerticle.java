package com.adsology;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.Handler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

public class SenderVerticle extends AbstractVerticle {
    private static Logger logger = LogManager.getLogger(SenderVerticle.class);

    private final List<String> addresses;
    private final long delay;
    private final int loop;

    public SenderVerticle(int delay, int loop, List<String> addresses) {
        this.addresses = addresses;
        this.delay = delay;
        this.loop = loop;
    }

    @Override
    public void start(Future<Void> startFuture) throws Exception {
        vertx.setPeriodic(delay, new Handler<Long>() {
            int count = 0;

            @Override
            public void handle(Long event) {
                vertx.setPeriodic(delay / addresses.size(), new Handler<Long>() {
                    int addressIndex = 0;

                    @Override
                    public void handle(Long event) {
                        vertx.eventBus().send(addresses.get(addressIndex), "ping");
                        logger.info("Sender send message to {}", addresses.get(addressIndex));
                        if (++addressIndex == addresses.size()) {
                            vertx.cancelTimer(event);
                        }
                    }
                });


                if (++count > loop) {
                    System.out.println("sender finish");
                    vertx.cancelTimer(event);
                }
            }
        });
    }
}
