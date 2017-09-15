/*
 * Copyright (c) 2017. Alexey Chernysh, Russia, Krasnoyarsk
 */

package motionmachinesimulator.Processor;

class ExecutionState {

    private static boolean running;
    private static boolean resuming;
    private static boolean pausing;
    private static boolean forward;

    private static long resumingStepSize;
    private static long pausingStepSize;
    private static long startStopStepSize;
    private static long stepIncrement;

    private static ExecutionState ourInstance = new ExecutionState();

    static ExecutionState getInstance() {
        return ourInstance;
    }

    private ExecutionState() {
        running = false;
        resuming = false;
        pausing = false;
        forward = true;

        resumingStepSize = 0;
        pausingStepSize = 0;
        startStopStepSize = 0;
        stepIncrement = 0;

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
            long result = pausingStepSize;
            pausingStepSize -= stepIncrement;
            if(result > startStopStepSize) return result;
            else {
                running = false;
                pausing = false;
                return startStopStepSize;
            }
        } else return currentStepSize;
    }

    static boolean isForward() {
        return forward;
    }

    static void setForward() {
        forward = true;
    }

    static void setBackward() {
        forward = false;
    }

}
