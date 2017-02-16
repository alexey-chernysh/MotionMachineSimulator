package motionmachinesimulator.Processor;

/**
 * Created by alexey on 16.02.17.
 */
public class ExecutionState {

    private static STATE currentState = STATE.EMPTY;

    public static  synchronized STATE getState() {
        return currentState;
    }

    public static  synchronized void setState(STATE newState) {
        currentState = newState;
    }

    enum STATE {
        EMPTY,
        NEW,
        STARTED,
        PAUSED,
        FINISHED
    }
}
