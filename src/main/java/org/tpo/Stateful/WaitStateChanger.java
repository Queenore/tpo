package org.tpo.Stateful;

import org.tpo.Task.Task;

public interface WaitStateChanger {
    void putInWaitState(Task task);
}
