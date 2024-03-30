package org.tpo;

import org.tpo.Task.ExtendedTask;

public interface StateChanger {
    void putInWaitState(ExtendedTask task);
    void putInReadyState(ExtendedTask task);
}
