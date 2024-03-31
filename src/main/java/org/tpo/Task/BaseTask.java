package org.tpo.Task;

public class BaseTask extends Task {

    public BaseTask(Priority priority, int id) {
        super(priority, id);
        setRunnable(getBaseTask());
    }

    public Runnable getBaseTask() {
        return () -> {
            long result = 0;
            long limit = RANDOM.nextInt(1000000) + 100000000;

            for (long i = 0; i < limit; i++) {
                if (Thread.currentThread().isInterrupted()) {
                    return;
                }
                result += i;
            }

            LOGGER.info("Task done with result=" + result + ", id=" + getId());
        };
    }
}
