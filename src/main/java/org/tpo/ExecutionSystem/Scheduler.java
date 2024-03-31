package org.tpo.ExecutionSystem;

public class Scheduler extends TaskQueuesManager {

    public Scheduler() {
        new Processor(getRunningQueue(), this).start();
    }
}
