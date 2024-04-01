import org.junit.jupiter.api.Test;
import org.tpo.Task.BaseTask;
import org.tpo.Task.Priority;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class BaseTaskTests {

    @Test
    public void testBaseTaskExecution() {
        BaseTask task = new BaseTask(Priority.TWO, 1);
        Thread thread = new Thread(task.getBaseTask());

        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        assertFalse(thread.isAlive());
    }

    @Test
    public void testInterruptBaseTask() throws InterruptedException {
        BaseTask task = new BaseTask(Priority.TWO, 1);
        Thread thread = new Thread(task.getBaseTask());

        thread.start();
        thread.interrupt();

        assertTrue(thread.isAlive());
        thread.join();
    }
}