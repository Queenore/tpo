package org.tpo.Stateful;

import org.tpo.Task.Task;

public interface ToWaitStateChanger {
    void putInWaitState(Task task);
}
