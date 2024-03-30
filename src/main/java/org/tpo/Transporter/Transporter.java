package org.tpo.Transporter;

import org.tpo.Task.Task;

import java.util.concurrent.BlockingQueue;

abstract class Transporter extends Thread {
    BlockingQueue<Task> producer;
    BlockingQueue<Task> consumer;

    public Transporter(BlockingQueue<Task> producer,
                       BlockingQueue<Task> consumer) {
        this.producer = producer;
        this.consumer = consumer;
    }
}
