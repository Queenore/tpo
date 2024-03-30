package org.tpo.Task;

import org.tpo.Stateful.ToWaitStateChanger;

import java.util.Random;

public class ExtendedTask extends Task {
    static final Random RANDOM = new Random();

    private final ToWaitStateChanger stateChanger;
    private long result;
    private final long limit = RANDOM.nextInt(1000) + 50000;
    private int snapshot;
    private boolean isWaiting = false;
    private long interruptTime;
    private long waitingTime;

    public ExtendedTask(Priority priority, int id, ToWaitStateChanger stateChanger) {
        super(priority, id);
        setRunnable(getExtendedTask());
        this.stateChanger = stateChanger;
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

    private Runnable getExtendedTask() {
        return () -> {
            LOGGER.info("Continue: " + snapshot);
            for (; snapshot < limit; snapshot++) {
                if (Thread.currentThread().isInterrupted()) {
                    return;
                }
                if (isWaitAction()) {
                    LOGGER.info("Waiting: " + snapshot);
                    isWaiting = true;
                    waitingTime = RANDOM.nextInt(2000) + 2000;
                    interruptTime = System.currentTimeMillis();
                    stateChanger.putInWaitState(this);
                    return;
                }
                result += snapshot;
            }
            LOGGER.info("Result: " + result);
        };
    }

    private static boolean isWaitAction() { // With 0,1% probability.
        int rand = RANDOM.nextInt(1000000);
        return rand > 999990;
    }
}
