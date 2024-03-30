package org.tpo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tpo.Task.ExtendedTask;
import org.tpo.Task.Priority;

import java.util.Random;

public class Main {
    private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);
    private static final Random RANDOM  = new Random();

    public static void main(String[] args) throws InterruptedException {
        Scheduler scheduler = new Scheduler();

        int id = 1;
        while (true) {
            int randInt = RANDOM.nextInt(4);

            scheduler.put(
                    new ExtendedTask(
                            getTask(),
                            Priority.values()[randInt],
                            id++
                    )
            );

            Thread.sleep(RANDOM.nextInt(1000) + 1500);
        }
    }

    private static Runnable getTask() {
        return () -> {
            long counter = 0;
            long limit = RANDOM.nextInt(1000000) + 100000000;
            for (long i = 0; i < limit; i++) {
                if (Thread.currentThread().isInterrupted()) {
                    return;
                }
                if (isWaitAction()) {
                    int waitOperationTime = RANDOM.nextInt(2000) + 4000;
                    // TODO: put in wait state
                }

                counter += RANDOM.nextInt(123) + i;
            }
            LOGGER.info("Result: " + counter);
        };
    }

    private static boolean isWaitAction() {
        int rand = RANDOM.nextInt(1000);
        return rand > 90;
    }
}