package org.tpo.Transporter;

import org.tpo.Task.ExtendedTask;
import org.tpo.Task.Task;

import java.util.concurrent.BlockingQueue;

public class WaitingTransporter extends Transporter {

    public WaitingTransporter(BlockingQueue<Task> producer, TaskTransporter transporter) {
        super(producer, transporter);
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                ExtendedTask task = (ExtendedTask) producer.take();
                while (!task.isTaskReady()) {
                    Thread.yield();
                }
                transporter.transport(task);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}
