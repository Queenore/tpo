package org.tpo.Transporter;

import org.tpo.Task.Task;

import java.util.concurrent.BlockingQueue;

public class SimpleTransporter extends Transporter{

    public SimpleTransporter(BlockingQueue<Task> producer, TaskTransporter transporter) {
        super(producer, transporter);
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                transporter.transport(producer.take());
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}
