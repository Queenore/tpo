import org.junit.jupiter.api.Test;
import org.tpo.ExecutionSystem.Scheduler;
import org.tpo.Task.ExtendedTask;
import org.tpo.Task.Priority;
import org.tpo.Task.Task;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TaskQueuesManagerTest {

    @Test
    void testPutInWaitState() throws InterruptedException {
        Scheduler scheduler = new Scheduler();
        BlockingQueue<Task> waitingQueue = scheduler.getWaitingQueue();

//        for (int i=0; i<10; i++) {
//
//        }
        ExtendedTask task = new ExtendedTask(Priority.ONE, 1, scheduler);
        task.setWaitState();
        scheduler.putInWaitState(task);

        assertEquals(1, waitingQueue.size());
        while (!task.isReady()){
            Thread.yield();
        }
        assertEquals(0, waitingQueue.size());
    }

    @Test
    void testPutInReadyStateBlocking() throws InterruptedException {
        Scheduler scheduler = new Scheduler();
        BlockingQueue<Task> readyQueue = scheduler.getReadyQueue();

//        for (int i=0; i<50; i++) {
//
//        }
        Task task = new ExtendedTask(Priority.ONE, 1, scheduler);
        scheduler.putInReadyStateBlocking(task);

        assertEquals(1, readyQueue.size());
        Thread.sleep(100);
        assertEquals(0, readyQueue.size());
    }

    @Test
    void testPutInReadyStateNonBlocking() throws InterruptedException {
        Scheduler scheduler = new Scheduler();
        BlockingQueue<Task> readyQueue = scheduler.getReadyQueue();

//        for (int i=0; i<50; i++) {
//
//        }
        Task task = new ExtendedTask(Priority.ONE, 1, scheduler);
        scheduler.putInReadyStateNonBlocking(task);

        assertEquals(1, readyQueue.size());
        Thread.sleep(100);
        assertEquals(0, readyQueue.size());
    }

    @Test
    void testPutInReadyStateNonBlocking_CountDown() throws InterruptedException {
        Scheduler scheduler = new Scheduler();
        CountDownLatch countDownLatch = new CountDownLatch(1);
        Task task = new ExtendedTask(() -> {
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            countDownLatch.countDown();
        },Priority.ONE, 1, scheduler);

        scheduler.putInReadyStateNonBlocking(task);
        countDownLatch.await();

        assertEquals(0, scheduler.getSuspendedQueue().size());
        assertEquals(0, scheduler.getReadyQueue().size());
        assertEquals(0, scheduler.getWaitingQueue().size());
    }

    @Test
    void testPutSuspended() throws InterruptedException {
        Scheduler scheduler = new Scheduler();
        BlockingQueue<Task> suspendedQueue = scheduler.getSuspendedQueue();

//        for (int i=0; i<50; i++) {
//
//        }
        Task task = new ExtendedTask(Priority.ONE, 1, scheduler);
        scheduler.put(task);

        assertEquals(1, suspendedQueue.size());
        Thread.sleep(100);
        assertEquals(0, suspendedQueue.size());
    }

    @Test
    void testPutSuspended_Priorities() throws InterruptedException {
        Scheduler scheduler = new Scheduler();
        BlockingQueue<Task> suspendedQueue = scheduler.getSuspendedQueue();

//        for (int i=0; i<50; i++) {
//
//        }
        Task task1 = new ExtendedTask(Priority.ONE, 1, scheduler);
        Task task2 = new ExtendedTask(Priority.THREE, 2, scheduler);
        scheduler.put(task1);
        scheduler.put(task2);

        assertEquals(2, suspendedQueue.size());
        Thread.sleep(100);
        assertEquals(0, suspendedQueue.size());
    }

}