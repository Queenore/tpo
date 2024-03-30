package org.tpo.ExecutionSystem;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tpo.Stateful.BaseStateChanger;
import org.tpo.Task.Task;
import org.tpo.Transporter.SimpleTransporter;
import org.tpo.Transporter.WaitingTransporter;

import java.util.Comparator;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.SynchronousQueue;

public class Scheduler implements BaseStateChanger {
    private static final Logger LOGGER = LoggerFactory.getLogger(Scheduler.class);
    private static final Comparator<Task> COMPARATOR = Comparator.comparing(Task::getPriority);

    private final PriorityBlockingQueue<Task> readyQueue = new PriorityBlockingQueue<>(2, COMPARATOR);
    private final PriorityBlockingQueue<Task> waitingQueue = new PriorityBlockingQueue<>(1 << 6, COMPARATOR);
    private final PriorityBlockingQueue<Task> suspendedQueue = new PriorityBlockingQueue<>(1 << 6, COMPARATOR);
    private final Processor processor;

    public Scheduler() {
        SynchronousQueue<Task> runningQueue = new SynchronousQueue<>();

        new SimpleTransporter(readyQueue, runningQueue).start();
        new WaitingTransporter(waitingQueue, runningQueue).start();
        new SimpleTransporter(suspendedQueue, readyQueue).start();

        this.processor = new Processor(runningQueue, this);
        this.processor.start();
    }

    public void put(Task task) {
        suspendedQueue.put(task);
    }

    @Override
    public void putInWaitState(Task task) {
//        LOGGER.info("Put in wait state: id=" + task.getId());
        processor.toWaitState();
        waitingQueue.put(task);
    }

    @Override
    public void putInReadyState(Task task) {
        readyQueue.put(task);
    }

    @Override
    public void terminate(Task task) {
        LOGGER.info("Task done: id=" + task.getId());
    }
}
