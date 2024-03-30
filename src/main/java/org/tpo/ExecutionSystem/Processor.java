package org.tpo.ExecutionSystem;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tpo.Stateful.BaseStateChanger;
import org.tpo.Task.Task;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.SynchronousQueue;

public class Processor extends Thread {
    private static final Logger LOGGER = LoggerFactory.getLogger(Processor.class);

    private final ExecutorService executor;
    private final BaseStateChanger stateChanger;
    private final SynchronousQueue<Task> runningQueue;

    private TaskInProcess taskInProcess;

    public Processor(SynchronousQueue<Task> runningQueue, BaseStateChanger stateChanger) {
        this.executor = Executors.newSingleThreadExecutor();
        this.runningQueue = runningQueue;
        this.stateChanger = stateChanger;
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                Task nextTask = runningQueue.take();
                while (true) {
                    if (taskInProcess == null || taskInProcess.isCancelled()) {
                        putTask(nextTask);
                        break;
                    } else if (taskInProcess.isDone())  {
                        terminate(nextTask);
                        break;
                    } else if (
                            taskInProcess.getTask().getPriority()
                                    .compareTo(nextTask.getPriority()) > 0
                    ) {
                        preempt(nextTask);
                        break;
                    }
                }
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    public void toWaitState() {
        taskInProcess.cancel();
    }

    private void terminate(Task nextTask) {
        stateChanger.terminate(taskInProcess.getTask());
        putTask(nextTask);
    }

    private void preempt(Task task) {
        LOGGER.info(
                taskInProcess.getTask().getPriority().name()
                        + " -> "
                        + task.getPriority().name()
        );

        taskInProcess.cancel();
        Task prevTask = taskInProcess.getTask();
        putTask(task);
        stateChanger.putInReadyState(prevTask);
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
