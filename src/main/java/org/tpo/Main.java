package org.tpo;

import org.tpo.ExecutionSystem.Scheduler;
import org.tpo.Task.Task;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Main {
    public static void main(String[] args) {
        Queue<Task>[] tasks = new ConcurrentLinkedQueue[4];
        for (int i = 0; i < 4; i++) {
            tasks[i] = new ConcurrentLinkedQueue<>();
        }

        Scheduler scheduler = new Scheduler();
        scheduler.setTasks(tasks);

        Generator generator = new Generator(scheduler, tasks);

        generator.start();
        scheduler.start();
    }
}