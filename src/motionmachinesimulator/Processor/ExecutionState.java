/*
 * Copyright (c) 2017. Alexey Chernysh, Russia, Krasnoyarsk
 */

package motionmachinesimulator.Processor;

class ExecutionState {

    private static boolean running;
    private static boolean resuming;
    private static boolean pausing;
    private static boolean forward;

    private static ExecutionState ourInstance = new ExecutionState();

    static ExecutionState getInstance() {
        return ourInstance;
    }

    private ExecutionState() {
        running = false;
        resuming = false;
        pausing = false;
        forward = true;
    }

    static void setRunning() {
        running = true;
    }
    static void setStopped() { running = false; }

    static boolean isRunning() {
        return running;
    }

    static boolean isPaused() {
        return !running;
    }

    static boolean isResuming() {
        return resuming;
    }

    static boolean isPausing() {
        return pausing;
    }

    static long resumingStepSize = 0;
    static long pausingStepSize = 0;
    static long startStopStepSize = 0;
    static long stepIncrement = 0;

    static void setResuming(){
        resuming = true;
        resumingStepSize = ControllerSettings.getStartStepSize();
        stepIncrement = ControllerSettings.getStepIncrement4Acceleration();
    }

    static long getResumingStepSize(long currentStepSize){
        if(resuming){
            long result = resumingStepSize;
            resumingStepSize += stepIncrement;
            if(result < currentStepSize) return result;
            else {
                resuming = false;
                return currentStepSize;
            }
        } else return currentStepSize;
    }

    static void setPausing(){
        resuming = false;
        pausing = true;
        pausingStepSize = ControllerSettings.getCurrentStepSize();
        stepIncrement = ControllerSettings.getStepIncrement4Acceleration();
        startStopStepSize = ControllerSettings.getStartStepSize();
    }

    static long getPausingStepSize(long currentStepSize){
        if(pausing){
            pausingStepSize -= stepIncrement;
            if(pausingStepSize > startStopStepSize){
                return pausingStepSize;
            } else {
                running = false;
                pausing = false;
                return startStopStepSize;
            }
        } else return currentStepSize;
    }

    public static boolean isForward() {
        return forward;
    }

    public static boolean isBackward() {
        return !forward;
    }

    public static void setForward() {
        forward = true;
    }

    public static void setBackward() {
        forward = false;
    }

}
