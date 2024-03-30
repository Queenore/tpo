package org.tpo.Stateful;

import org.tpo.Task.Task;

public interface BaseStateChanger extends ToWaitStateChanger {
    void putInReadyState(Task task);

    void termitate(Task task);
}
