package org.tpo.Task;

import java.util.concurrent.Callable;

public class BaseTask extends Task {

    public BaseTask(Priority priority, int id) {
        super(priority, id);
        setCallable(getBaseTask());
    }

    public Callable<Long> getBaseTask() {
        return () -> {
            long result = 0;
            long limit = RANDOM.nextInt(1000000) + 100000000;

            for (long i = 0; i < limit; i++) {
                if (Thread.currentThread().isInterrupted()) {
                    throw new InterruptedException();
                }
                result += i;
            }

            LOGGER.info("Task done with result=" + result + ", id=" + getId());

            return result;
        };
    }
}
