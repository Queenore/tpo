package org.tpo.Task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tpo.Main;

import java.util.Random;

public class ExtendedTask implements Runnable {
    static final Logger LOGGER = LoggerFactory.getLogger(Main.class);
    static final Random RANDOM = new Random();

    private Priority priority;
    private final Runnable runnable;
    private final int id;

    public ExtendedTask(Priority priority, int id) {
        this.priority = priority;
        this.id = id;
        this.runnable = getExtendedTask();
    }

    public ExtendedTask(Runnable runnable, Priority priority, int id) {
        this.priority = priority;
        this.id = id;
        this.runnable = runnable;
    }

    public Priority getPriority() {
        return priority;
    }

    public int getId() {
        return id;
    }

    @Override
    public void run() {
        runnable.run();
    }

    private Runnable getExtendedTask() {
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
