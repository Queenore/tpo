package org.tpo.Task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Random;

public abstract class Task implements Runnable {
    static final Logger LOGGER = LoggerFactory.getLogger(Task.class);
    static final Random RANDOM = new Random();

    private final Priority priority;
    private final int id;

    private Runnable runnable = null;

    public Task(Priority priority, int id) {
        this.priority = priority;
        this.id = id;
    }

    public void setRunnable(Runnable runnable) {
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
        if (runnable == null) {
            throw new IllegalStateException();
        }
        runnable.run();
    }
}
