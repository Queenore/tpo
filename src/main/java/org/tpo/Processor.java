package org.tpo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tpo.Task.ExtendedTask;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.SynchronousQueue;

public class Processor extends Thread {
    private static final Logger LOGGER = LoggerFactory.getLogger(Processor.class);
    private final ExecutorService executor;
    private final SynchronousQueue<ExtendedTask> runningQueue;
    private TaskInProcess taskInProcess;

    public Processor(SynchronousQueue<ExtendedTask> runningQueue) {
        this.executor = Executors.newSingleThreadExecutor();
        this.runningQueue = runningQueue;
    }

    private void displase(ExtendedTask task) {
        LOGGER.info(
                taskInProcess.getTask().getPriority().name()
                        + " -> "
                        + task.getPriority().name()
        );

        ExtendedTask prevTask = taskInProcess.getTask();
        // TODO: preempt prevTask

        taskInProcess.cancel();
        putTask(task);
    }

    private void putTask(ExtendedTask task) {
        LOGGER.info("Executing: priority=" + task.getPriority().name());
        taskInProcess = new TaskInProcess(
                task,
                executor.submit(task)
        );
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                ExtendedTask nextTask = runningQueue.take();
                while (true) {
                    if (taskInProcess == null || taskInProcess.isDone()) {
                        putTask(nextTask);
                        break;
                    } else if (taskInProcess.getTask().getPriority().compareTo(nextTask.getPriority()) > 0) {
                        displase(nextTask);
                        break;
                    }
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    private class TaskInProcess {
        ExtendedTask task;
        Future<?> future;

        public TaskInProcess(ExtendedTask task, Future<?> future) {
            this.task = task;
            this.future = future;
        }

        public ExtendedTask getTask() {
            return task;
        }

        public Future<?> getFuture() {
            return future;
        }

        public void cancel() {
            future.cancel(true);
        }

        public boolean isDone() {
            return future.isDone();
        }
    }
}
