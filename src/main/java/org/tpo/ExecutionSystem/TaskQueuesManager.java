package org.tpo.ExecutionSystem;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tpo.Stateful.StateChanger;
import org.tpo.Task.Task;
import org.tpo.Transporter.SimpleTransporter;
import org.tpo.Transporter.WaitingTransporter;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.SynchronousQueue;

public class TaskQueuesManager implements StateChanger {
    private static final Logger LOGGER = LoggerFactory.getLogger(TaskQueuesManager.class);
    private static final int READY_QUEUE_CAPACITY = 10;
    private static final int MAX_QUEUE_CAPACITY = 1 << 6;

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

    public void put(Task task) throws InterruptedException {
        suspendedQueue.put(task);
    }

    @Override
    public void putInWaitState(Task task) throws InterruptedException {
        waitingQueue.put(task);
    }

    @Override
    public synchronized void putInReadyStateBlocking(Task task) throws InterruptedException {
        LOGGER.info(" SIZE = " + readyQueue.size());
        while (readyQueue.size() >= READY_QUEUE_CAPACITY) {
            Thread.yield();
        }
        readyQueue.put(task);
    }

    @Override
    public synchronized void putInReadyStateNonBlocking(Task task) throws InterruptedException {
        readyQueue.put(task);
    }

    @Override
    public void putInRunningState(Task task) throws InterruptedException {
        runningQueue.put(task);
    }
}
