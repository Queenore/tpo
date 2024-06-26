package org.tpo.ExecutionSystem;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tpo.Stateful.StateChanger;
import org.tpo.Task.Task;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Processor extends Thread {
    private static final Logger LOGGER = LoggerFactory.getLogger(Processor.class);

    private final ExecutorService executor;
    private final StateChanger stateChanger;
    private final BlockingQueue<Task> readyQueue;

    private TaskInProcess taskInProcess;

    public Processor(BlockingQueue<Task> readyQueue, StateChanger stateChanger) {
        this.executor = Executors.newSingleThreadExecutor();
        this.readyQueue = readyQueue;
        this.stateChanger = stateChanger;
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                Task nextTask = readyQueue.take();
                while (true) {
                    if (taskInProcess == null || taskInProcess.isCancelled() || taskInProcess.isDone()) {
                        putTask(nextTask);
                        break;
                    } else if (
                            taskInProcess.getTask().getPriority()
                                    .compareTo(nextTask.getPriority()) > 0
                    ) {
                        preempt(nextTask);
                        break;
                    }
                    Thread.yield();
                }
                Thread.yield();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    private void preempt(Task task) throws InterruptedException {
        LOGGER.info(
                taskInProcess.getTask().getPriority().name()
                        + " -> "
                        + task.getPriority().name()
        );

        taskInProcess.cancel();
        Task prevTask = taskInProcess.getTask();

        putTask(task);
        stateChanger.putInReadyStateNonBlocking(prevTask);
    }

    private void putTask(Task task) {
        LOGGER.info("Executing: priority=" + task.getPriority().name() + ", id=" + task.getId());

        taskInProcess = new TaskInProcess(
                task,
                executor.submit(task)
        );
    }

    private static class TaskInProcess {
        Task task;
        Future<?> future;

        public TaskInProcess(Task task, Future<?> future) {
            this.task = task;
            this.future = future;
        }

        public Task getTask() {
            return task;
        }

        public void cancel() {
            future.cancel(true);
        }

        public boolean isDone() {
            return future.isDone();
        }

        public boolean isCancelled() {
            return future.isCancelled();
        }
    }
}
