package org.tpo.Transporter;

import org.tpo.Task.ExtendedTask;
import org.tpo.Task.Task;

import java.util.concurrent.BlockingQueue;

public class WaitingTransporter extends Transporter{
    public WaitingTransporter(BlockingQueue<Task> producer, BlockingQueue<Task> consumer) {
        super(producer, consumer);
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                ExtendedTask task = (ExtendedTask) producer.take();
                while (!task.isTaskReady()) {
                    Thread.yield();
                }
                consumer.put(task);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}
