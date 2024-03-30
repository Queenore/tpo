package org.tpo.Transporter;

import org.tpo.Task.Task;

import java.util.concurrent.BlockingQueue;

public class SimpleTransporter extends Transporter{
    public SimpleTransporter(BlockingQueue<Task> producer, BlockingQueue<Task> consumer) {
        super(producer, consumer);
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                consumer.put(producer.take());
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}
