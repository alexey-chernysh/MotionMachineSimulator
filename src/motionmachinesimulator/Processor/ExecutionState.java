package motionmachinesimulator.Processor;

/**
 * Created by alexey on 16.02.17.
 */
public class ExecutionState {

    private static STATE currentState = STATE.EMPTY;
    private static double[] currentPosition = new double[ProcessorSettings.DIM];

    public ExecutionState() {
        for(int i = 0; i< ProcessorSettings.DIM; i++) currentPosition[i] = 0.0;
    }

    public static  synchronized STATE getState() {
        return currentState;
    }

    public static  synchronized void setState(STATE newState) {
        currentState = newState;
    }

    public static double[] getCurrentPosition() {
        return ExecutionState.currentPosition;
    }

    public static void setCurrentPosition(double[] newPosition) {
        ExecutionState.currentPosition = newPosition;
    }

    enum STATE {
        EMPTY,
        NEW,
        STARTED,
        PAUSED,
        FINISHED
    }
}
