package org.tpo;

import org.tpo.ExecutionSystem.Scheduler;
import org.tpo.Task.BaseTask;
import org.tpo.Task.ExtendedTask;
import org.tpo.Task.Priority;

import java.util.Random;

public class Main {
    private static final Random RANDOM = new Random();

    public static void main(String[] args) throws InterruptedException {
        Scheduler scheduler = new Scheduler();

        int id = 1;
        while (true) {
            Priority priority = Priority.values()[RANDOM.nextInt(4)];

            scheduler.put(
                    RANDOM.nextInt(2) > 0
                            ? new BaseTask(priority, id++)
                            : new ExtendedTask(priority, id++, scheduler)
            );

            Thread.sleep(RANDOM.nextInt(600) + 400);
        }
    }
}