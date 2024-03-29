package org.tpo;

import org.tpo.Task.ExtendedTask;

import java.util.concurrent.BlockingQueue;

class Transporter extends Thread {
    BlockingQueue<ExtendedTask> producer;
    BlockingQueue<ExtendedTask> consumer;

    public Transporter(BlockingQueue<ExtendedTask> producer,
                       BlockingQueue<ExtendedTask> consumer) {
        this.producer = producer;
        this.consumer = consumer;
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
