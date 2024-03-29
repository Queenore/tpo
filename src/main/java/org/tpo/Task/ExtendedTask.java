package org.tpo.Task;

public class ExtendedTask implements Runnable {
    private Priority priority;
    private final Runnable runnable;

    public ExtendedTask(Runnable runnable, Priority priority) {
        this.runnable = runnable;
        this.priority = priority;
    }

    public Priority getPriority() {
        return priority;
    }

    @Override
    public void run() {
        runnable.run();
    }
}
