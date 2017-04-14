/*
 * Copyright (c) 2017. Alexey Chernysh, Russia, Krasnoyarsk
 */

package motionmachinesimulator.Processor;

public class ExecutionDirection {

    private boolean forward;

    private static ExecutionDirection ourInstance = new ExecutionDirection();

    public static ExecutionDirection getInstance() {
        return ourInstance;
    }

    private ExecutionDirection() {
        forward = true;
    }

    public boolean isForward() {
        return forward;
    }

    public boolean isBackward() {
        return !forward;
    }

    public void setForward() {
        this.forward = true;
    }

    public void setBackward() {
        this.forward = false;
    }

}
