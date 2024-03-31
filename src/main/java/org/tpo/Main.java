package org.tpo;

import org.tpo.ExecutionSystem.Scheduler;
import org.tpo.Task.Task;

import java.util.ArrayDeque;
import java.util.Queue;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        Scheduler scheduler = new Scheduler();

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