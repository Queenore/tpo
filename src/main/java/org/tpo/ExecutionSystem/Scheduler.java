package org.tpo.ExecutionSystem;

import org.tpo.Task.Task;

import java.util.Queue;

public class Scheduler extends TaskQueuesManager {

    private Queue<Task>[] tasks;

    public Scheduler() {
        new Processor(getRunningQueue(), this).start();
    }

    public void setTasks(Queue<Task>[] tasks){
        this.tasks = tasks;
    }

    @Override
    public void run() {
        if (tasks==null) {
            throw new IllegalStateException();
        }

        while (!Thread.currentThread().isInterrupted()) {
            Task currTask = null;

            while (currTask == null) {
                for (int i = 0; i < 4; i++) {
                    currTask = tasks[i].peek();
                    if (currTask != null) {
                        try {
                            put(currTask);
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                        }
                        tasks[i].poll();
                        break;
                    }
                }
            }

            Thread.yield();
        }
    }
}
