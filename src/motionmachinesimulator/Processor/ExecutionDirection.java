/*
 * Copyright (c) 2017. Alexey Chernysh, Russia, Krasnoyarsk
 */

package motionmachinesimulator.Processor;

class ExecutionDirection {

    private boolean forward;

    private static ExecutionDirection ourInstance = new ExecutionDirection();

    static ExecutionDirection getInstance() {
        return ourInstance;
    }

    private ExecutionDirection() {
        forward = true;
    }

    boolean isForward() {
        return forward;
    }

    void setForward() {
        this.forward = true;
    }

    void setBackward() {
        this.forward = false;
    }

}
