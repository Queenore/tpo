package org.tpo.Stateful;

import org.tpo.Task.Task;

public interface StateChanger extends Waitable {
    void putInReadyStateBlocking(Task task) throws InterruptedException;
    void putInReadyStateNonBlocking(Task task) throws InterruptedException;
}
