/*
 * Copyright (c) 2017. Alexey Chernysh, Russia, Krasnoyarsk
 */

package motionmachinesimulator.Processor;

class ExecutionState {

    private boolean running;
    private boolean resuming;
    private boolean suspending;

    private static ExecutionState ourInstance = new ExecutionState();

    static ExecutionState getInstance() {
        return ourInstance;
    }

    private ExecutionState() {
        running = false;
        resuming = false;
        suspending = false;
    }

    void setRunning() {
        this.running = true;
    }
    void setStopped() { this.running = false; }

    boolean isRunning() {
        return running;
    }

    boolean isPaused() {
        return !running;
    }

    public boolean isResuming() {
        return resuming;
    }

    public boolean isSuspending() {
        return suspending;
    }

}
