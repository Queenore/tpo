package org.tpo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tpo.Task.ExtendedTask;
import org.tpo.Task.Priority;

import java.util.Random;

public class Main {
    private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);
    public static void main(String[] args) throws InterruptedException {
        Scheduler scheduler = new Scheduler();

        Random random = new Random();
        while (true) {
            int randInt = random.nextInt(4);
            ExtendedTask task = new ExtendedTask(
                    getTask(random),
                    Priority.values()[randInt]
            );
            scheduler.put(task);
            Thread.sleep(random.nextInt(2000));
        }
    }

    private static Runnable getTask(Random random) {
        return () -> {
            long counter = 0;
            long limit = random.nextInt(1000000) + 100000000;
            for (long i = 0; i < limit; i++) {
                counter += i;
            }
            LOGGER.info("Result: " + counter);
        };
    }
}