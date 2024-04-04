package org.tpo.ExecutionSystem;

import org.tpo.Stateful.StateChanger;
import org.tpo.Task.Task;
import org.tpo.Transporter.SimpleTransporter;
import org.tpo.Transporter.WaitingTransporter;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.SynchronousQueue;

public class TaskQueuesManager extends Thread implements StateChanger  {
    private static final int READY_QUEUE_CAPACITY = 100;
    private static final int MAX_QUEUE_CAPACITY = 1 << 10;

    // Where are two ways to add element in readyQueue: blocking or non-blocking.
    private final BlockingQueue<Task> readyQueue = new ArrayBlockingQueue<>(MAX_QUEUE_CAPACITY);
    private final BlockingQueue<Task> waitingQueue = new ArrayBlockingQueue<>(MAX_QUEUE_CAPACITY);
    private final BlockingQueue<Task> suspendedQueue = new ArrayBlockingQueue<>(MAX_QUEUE_CAPACITY);
    private final SynchronousQueue<Task> runningQueue = new SynchronousQueue<>();

    public TaskQueuesManager() {
        new SimpleTransporter(readyQueue, this::putInRunningState).start();
        new SimpleTransporter(suspendedQueue, this::putInReadyStateBlocking).start();
        new WaitingTransporter(waitingQueue, this::putInReadyStateNonBlocking).start();
    }

    public SynchronousQueue<Task> getRunningQueue() {
        return runningQueue;
    }

    public BlockingQueue<Task> getReadyQueue() {
        return readyQueue;
    }

    public BlockingQueue<Task> getWaitingQueue() {
        return waitingQueue;
    }

    public BlockingQueue<Task> getSuspendedQueue() {
        return suspendedQueue;
    }

    public void put(Task task) throws InterruptedException {
        suspendedQueue.put(task);
    }

    @Override
    public void putInWaitState(Task task) throws InterruptedException {
        waitingQueue.put(task);
    }

    @Override
    public synchronized void putInReadyStateBlocking(Task task) throws InterruptedException {
        while (readyQueue.size() >= READY_QUEUE_CAPACITY) {
            Thread.yield();
        }
        readyQueue.put(task);
    }

    @Override
    public void putInReadyStateNonBlocking(Task task) throws InterruptedException {
        readyQueue.put(task);
    }

    @Override
    public void putInRunningState(Task task) throws InterruptedException {
        runningQueue.put(task);
    }
}
