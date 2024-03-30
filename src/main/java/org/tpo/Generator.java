package org.tpo;

import org.tpo.ExecutionSystem.Scheduler;
import org.tpo.Task.BaseTask;
import org.tpo.Task.ExtendedTask;
import org.tpo.Task.Priority;
import org.tpo.Task.Task;

import java.util.Queue;
import java.util.Random;

public class Generator extends Thread {
    private static final Random RANDOM = new Random();
    private  final Scheduler scheduler;
    private final Queue<Task>[] tasks;

    public Generator(Scheduler scheduler, Queue<Task>[] tasks) {
        this.scheduler = scheduler;
        this.tasks = tasks;
    }

    @Override
    public void run() {
        int id = 1;
        while (!Thread.currentThread().isInterrupted()) {
            int priorityIndex = RANDOM.nextInt(4);
            Priority priority = Priority.values()[priorityIndex];

            Task newTask = RANDOM.nextInt(2) > 0
                    ? new BaseTask(priority, id++)
                    : new ExtendedTask(priority, id++, scheduler);

            tasks[priorityIndex].add(newTask);

            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}
