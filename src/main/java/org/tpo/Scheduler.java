package org.tpo;

import org.tpo.Task.ExtendedTask;

import java.util.Comparator;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.SynchronousQueue;

public class Scheduler {
    private static final Comparator<ExtendedTask> COMPARATOR = Comparator.comparing(ExtendedTask::getPriority);

    private final PriorityBlockingQueue<ExtendedTask> readyQueue = new PriorityBlockingQueue<>(2, COMPARATOR);
    private final PriorityBlockingQueue<ExtendedTask> waitingQueue = new PriorityBlockingQueue<>(1 << 6, COMPARATOR);
    private final PriorityBlockingQueue<ExtendedTask> suspendedQueue = new PriorityBlockingQueue<>(1 << 6, COMPARATOR);
    private final SynchronousQueue<ExtendedTask> runningQueue = new SynchronousQueue<>();

    private final Transporter suspendedToReady;
    private final Transporter readyToRunning;

    private final Processor processor = new Processor(runningQueue);

    public Scheduler() {
        this.suspendedToReady = new Transporter(suspendedQueue, readyQueue);
        this.readyToRunning = new Transporter(readyQueue, runningQueue);

        this.suspendedToReady.start();
        this.readyToRunning.start();
        this.processor.start();
    }

    public void put(ExtendedTask task) {
        suspendedQueue.put(task);
    }
}
