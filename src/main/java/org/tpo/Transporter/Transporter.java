package org.tpo.Transporter;

import org.tpo.Task.Task;

import java.util.concurrent.BlockingQueue;

abstract class Transporter extends Thread {
    BlockingQueue<Task> producer;
    TaskTransporter transporter;

    public Transporter(BlockingQueue<Task> producer,
                       TaskTransporter transporter) {
        this.producer = producer;
        this.transporter = transporter;
    }

    public interface TaskTransporter {
        void transport(Task task) throws InterruptedException;
    }
}
