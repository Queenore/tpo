import org.junit.jupiter.api.Test;
import org.tpo.ExecutionSystem.Scheduler;
import org.tpo.Generator;
import org.tpo.Task.Task;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class GeneratorTests {

    @Test
    public void testGeneratorSuccess() throws InterruptedException {
        Scheduler scheduler = new Scheduler();
        Queue<Task>[] tasks = new ConcurrentLinkedQueue[4];
        for (int i = 0; i < 4; i++) {
            tasks[i] = new ConcurrentLinkedQueue<>();
        }

        Generator generator = new Generator(scheduler, tasks);
        generator.start();
        Thread.sleep(2000);

        // Check if tasks have been generated
        for (int i = 0; i < 4; i++) {
            assertFalse(tasks[i].isEmpty());
        }
    }

    @Test
    public void testGeneratorFailureWithInterrupt() throws InterruptedException {
        Scheduler scheduler = new Scheduler();
        Queue<Task>[] tasks = new Queue[4];
        for (int i = 0; i < 4; i++) {
            tasks[i] = new LinkedList<>();
        }

        Generator generator = new Generator(scheduler, tasks);
        generator.start();
        generator.interrupt();
        generator.join();

        // Check if tasks haven't been generated
        for (int i = 0; i < 4; i++) {
            assertTrue(tasks[i].isEmpty());
        }
    }
}