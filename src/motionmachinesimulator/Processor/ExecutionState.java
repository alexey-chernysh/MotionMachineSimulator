/*
 * Copyright (c) 2017. Alexey Chernysh, Russia, Krasnoyarsk
 */

package motionmachinesimulator.Processor;

class ExecutionState {

    private EXECUTION_STATE state;

    private static ExecutionState ourInstance = new ExecutionState();

    static ExecutionState getInstance() {
        return ourInstance;
    }

    private ExecutionState() {
        state = EXECUTION_STATE.PAUSED;
    }

    EXECUTION_STATE getState() {
        return this.state;
    }

    void setState(EXECUTION_STATE newState) {
        this.state = newState;
    }

    public boolean isPaused() {
        return EXECUTION_STATE.PAUSED == state;
    }

    enum EXECUTION_STATE {
        ON_THE_RUN,
        PAUSED
    }

}
