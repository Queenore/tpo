package org.tpo.Stateful;

import org.tpo.Task.Task;

public interface BaseStateChanger extends WaitStateChanger {
    void putInReadyState(Task task);
    void terminate(Task task);
}
