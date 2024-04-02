package org.tpo.Task;

import org.tpo.Stateful.Waitable;

import java.util.concurrent.Callable;

public class ExtendedTask extends Task {
    private final Waitable waitStateProducer;
    private final long limit = RANDOM.nextInt(1000) + 50000;
    private long result;
    private int index;
    private boolean isWaiting = false;
    private long interruptTime;
    private long waitingTime;

    public ExtendedTask(Priority priority, int id, Waitable waitStateProducer) {
        super(priority, id);
        setCallable(getExtendedTask());
        this.waitStateProducer = waitStateProducer;
    }

    public ExtendedTask(Callable<Long> callable, Priority priority, int id, Waitable waitStateProducer) {
        super(priority, id);
        setCallable(callable);
        this.waitStateProducer = waitStateProducer;
    }

    public boolean isTaskReady() {
        if (!isWaiting) {
            return true;
        }

        long diff = System.currentTimeMillis() - interruptTime;
        if (diff >= waitingTime) {
            isWaiting = false;
            return true;
        }
        return false;
    }

    public void setWaitState() {
        isWaiting = true;
        waitingTime = getWaitingTime();
        interruptTime = System.currentTimeMillis();
    }

    private Callable<Long> getExtendedTask() {
        return () -> {
            LOGGER.info("Continue: " + index);

            while (index < limit) {

                if (Thread.currentThread().isInterrupted()) {
                    throw new InterruptedException();
                }

                if (isWaitAction()) {
                    LOGGER.info("Waiting: " + index);

                    // Set characteristics for wait state.
                    setWaitState();

                    waitStateProducer.putInWaitState(this);

                    throw new InterruptedException();
                }

                result += index;
                index++;
            }

            LOGGER.info("Task done with result=" + result + ", id=" + getId());

            return result;
        };
    }

    private static boolean isWaitAction() { // With 0,1% probability.
        int rand = RANDOM.nextInt(1000000);
        return rand > 999990;
    }

    private static int getWaitingTime() {
        // From 3000 to 6000 millis.
        return RANDOM.nextInt(3000) + 3000;
    }
}
