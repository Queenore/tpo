package org.tpo.Task;

public class ExtendedTask implements Runnable {
    private Priority priority;
    private final Runnable runnable;
    private final int id;

    public ExtendedTask(Runnable runnable, Priority priority, int id) {
        this.runnable = runnable;
        this.priority = priority;
        this.id = id;
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
}
