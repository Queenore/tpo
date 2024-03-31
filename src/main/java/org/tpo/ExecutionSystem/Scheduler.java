package org.tpo.ExecutionSystem;

public class Scheduler extends TaskQueuesManager {

    public Scheduler() {
        Processor processor = new Processor(getRunningQueue(), this);
        processor.start();
    }
}
