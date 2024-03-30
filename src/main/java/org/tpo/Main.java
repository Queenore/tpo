package org.tpo;

import org.tpo.ExecutionSystem.Scheduler;
import org.tpo.Task.BaseTask;
import org.tpo.Task.ExtendedTask;
import org.tpo.Task.Priority;
import org.tpo.Task.Task;

import java.util.ArrayDeque;
import java.util.Queue;
import java.util.Random;

public class Main {
    private static final Random RANDOM = new Random();

    public static void main(String[] args) {
        Scheduler scheduler = new Scheduler();

//        testRandomTaskLifetimeGeneration(scheduler);

        testWithTaskList(scheduler);

    }

    private static void testRandomTaskLifetimeGeneration(Scheduler scheduler) throws InterruptedException {
        int id = 1;
        while (!Thread.currentThread().isInterrupted()) {
            Priority priority = Priority.values()[RANDOM.nextInt(4)];

            scheduler.put(
                    RANDOM.nextInt(2) > 0
                            ? new BaseTask(priority, id++)
                            : new ExtendedTask(priority, id++, scheduler)
            );

            Thread.sleep(RANDOM.nextInt(600) + 400);
        }
    }

    private static void testWithTaskList(Scheduler scheduler) {
        Queue<Task>[] tasks = new ArrayDeque[4];
        for (int i = 0; i < 4; i++) {
            tasks[i] = new ArrayDeque<>();
        }

        Generator generator = new Generator(scheduler, tasks);
        generator.start();

        while (!Thread.currentThread().isInterrupted()) {
            Task currTask = null;
            while (currTask == null) {
                for (int i = 0; i < 4; i++) {
                    currTask = tasks[i].peek();
                    if (currTask != null) {
                        scheduler.put(currTask);
                        tasks[i].poll();
                        break;
                    }
                }
            }
        }
    }
}