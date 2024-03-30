package org.tpo.Task;

public class BaseTask extends ExtendedTask {

    public BaseTask(Priority priority, int id) {
        super(getBaseTask(), priority, id);
    }

    public static Runnable getBaseTask() {
        return () -> {
            long counter = 0;
            long limit = RANDOM.nextInt(1000000) + 100000000;
            for (long i = 0; i < limit; i++) {
                if (Thread.currentThread().isInterrupted()) {
                    return;
                }
                counter += RANDOM.nextInt(123) + i;
            }
            LOGGER.info("Result: " + counter);
        };
    }
}
