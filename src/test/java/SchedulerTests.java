import org.junit.jupiter.api.Test;
import org.tpo.ExecutionSystem.Scheduler;
import org.tpo.Task.ExtendedTask;
import org.tpo.Task.Priority;
import org.tpo.Task.Task;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import static org.junit.jupiter.api.Assertions.*;

public class SchedulerTests {

    @Test
    public void testSchedulerSuccess() throws InterruptedException {
        Scheduler scheduler = new Scheduler();
        Queue<Task>[] tasks = new ConcurrentLinkedQueue[4];
        for (int i = 0; i < 4; i++) {
            ConcurrentLinkedQueue<Task> priority_tasks = new ConcurrentLinkedQueue<>();
            priority_tasks.add(new ExtendedTask(Priority.values()[i], i, scheduler));
            tasks[i] = priority_tasks;
        }

        scheduler.setTasks(tasks);
        scheduler.start();
        Thread.sleep(2000);
        scheduler.interrupt();

        // Check if tasks have been passed through scheduler
        for (int i = 0; i < 4; i++) {
            assertTrue(tasks[i].isEmpty());
        }
    }

    @Test
    public void testSchedulerFailed(){
        Scheduler scheduler = new Scheduler();

        // Check if exception is thrown when tasks are null
        scheduler.start();
        assertThrows(IllegalThreadStateException.class, scheduler::start);
    }

}
