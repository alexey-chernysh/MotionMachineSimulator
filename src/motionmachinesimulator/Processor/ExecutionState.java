/*
 * Copyright (c) 2017. Alexey Chernysh, Russia, Krasnoyarsk
 */

package motionmachinesimulator.Processor;

public class ExecutionState {

    private EXECUTION_STATE state;

    private static ExecutionState ourInstance = new ExecutionState();

    public static ExecutionState getInstance() {
        return ourInstance;
    }

    private ExecutionState() {
        state = EXECUTION_STATE.EMPTY;
    }

    EXECUTION_STATE getState() {
        return this.state;
    }

    void setState(EXECUTION_STATE newState) {
        this.state = newState;
    }

    enum EXECUTION_STATE {
        EMPTY,
        READY_TO_START,
        ON_THE_RUN,
        PAUSED
    }

}
