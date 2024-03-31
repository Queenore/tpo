package org.tpo.Stateful;

import org.tpo.Task.Task;

public interface Waitable {
    void putInWaitState(Task task) throws InterruptedException;
}
