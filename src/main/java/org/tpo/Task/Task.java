package org.tpo.Task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Random;
import java.util.concurrent.Callable;

public abstract class Task implements Callable<Long> {
    static final Logger LOGGER = LoggerFactory.getLogger(Task.class);
    static final Random RANDOM = new Random();

    private final Priority priority;
    private final int id;

    private Callable<Long> callable = null;

    private volatile boolean isCompleted = false;

    public Task(Priority priority, int id) {
        this.priority = priority;
        this.id = id;
    }

    public Priority getPriority() {
        return priority;
    }

    public int getId() {
        return id;
    }

    public boolean isCompleted() {
        return isCompleted;
    }

    public void setCallable(Callable<Long> callable) {
        this.callable = callable;
    }

    @Override
    public Long call() throws Exception {
        if (callable == null) {
            throw new IllegalStateException();
        }

        Long result = callable.call();
        isCompleted = true;
        return result;
    }
}
