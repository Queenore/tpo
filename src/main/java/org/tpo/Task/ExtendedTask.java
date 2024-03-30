package org.tpo.Task;

import org.tpo.Stateful.WaitStateChanger;

public class ExtendedTask extends Task {
    private final WaitStateChanger stateChanger;
    private final long limit = RANDOM.nextInt(1000) + 50000;
    private long result;
    private int index;
    private boolean isWaiting = false;
    private long interruptTime;
    private long waitingTime;

    public ExtendedTask(Priority priority, int id, WaitStateChanger stateChanger) {
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
            LOGGER.info("Continue: " + index);
            while(index < limit) {
                if (Thread.currentThread().isInterrupted()) {
                    return;
                }
                if (isWaitAction()) {
                    LOGGER.info("Waiting: " + index);
                    isWaiting = true;
                    waitingTime = getWaitingTime();
                    interruptTime = System.currentTimeMillis();
                    stateChanger.putInWaitState(this);
                    return;
                }
                result += index;
                index++;
            }
            LOGGER.info("Result: " + result);
        };
    }

    private static boolean isWaitAction() { // With 0,1% probability.
        int rand = RANDOM.nextInt(1000000);
        return rand > 999990;
    }

    private static int getWaitingTime() {
        // From 2000 to 4000 millis.
        return RANDOM.nextInt(2000) + 2000;
    }
}
