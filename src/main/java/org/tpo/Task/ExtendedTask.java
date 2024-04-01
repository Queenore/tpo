package org.tpo.Task;

import org.tpo.Stateful.Waitable;

public class ExtendedTask extends Task {
    private final Waitable waitStateProducer;
    private final long limit = RANDOM.nextInt(1000) + 50000;
    private long result;
    private volatile boolean isReady;
    private int index;
    private boolean isWaiting = false;
    private long interruptTime;
    private long waitingTime;

    public ExtendedTask(Priority priority, int id, Waitable waitStateProducer) {
        super(priority, id);
        setRunnable(getExtendedTask());
        this.waitStateProducer = waitStateProducer;
    }

    public ExtendedTask(Runnable runnable, Priority priority, int id, Waitable waitStateProducer) {
        super(priority, id);
        setRunnable(runnable);
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

    private Runnable getExtendedTask() {
        return () -> {
            LOGGER.info("Continue: " + index);

            while(index < limit) {

                if (Thread.currentThread().isInterrupted()) {
                    return;
                }

                if (isWaitAction()) {
                    LOGGER.info("Waiting: " + index);

                    // Set characteristics for wait state.
                    setWaitState();

                    try {
                        waitStateProducer.putInWaitState(this);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }

                    return;
                }

                result += index;
                index++;
            }
            isReady = true;
            LOGGER.info("Task done with result=" + result + ", id=" + getId());
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

    public boolean isReady() {
        return isReady;
    }
}
