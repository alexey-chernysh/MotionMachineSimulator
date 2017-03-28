package motionmachinesimulator.Processor;

public class ControllerState extends Thread {

    ControllerState() {
    }

    private TASK_STATE taskState = TASK_STATE.EMPTY;

    TASK_STATE getTaskState() {
        return this.taskState;
    }
    void setTaskState(TASK_STATE newState) {
        this.taskState = newState;
    }

    enum TASK_STATE {
        EMPTY,
        READY_TO_START,
        ACCELERATING,
        ON_THE_RUN,
        DEACCELERATED,
        PAUSED,
        FINISHED
    }

    private DIRECTION_STATE directionState = DIRECTION_STATE.FORWARD;

    DIRECTION_STATE getDirectionState() {
        return this.directionState;
    }
    void setDirectionState(DIRECTION_STATE newDirectionState) {
        this.directionState = newDirectionState;
    }

    enum DIRECTION_STATE {
        FORWARD,
        BACKWARD
    }
/*
    private static VELOCITY_STATE velocityState = VELOCITY_STATE.CONSTANT;

    public static VELOCITY_STATE getVelocityState() {
        return velocityState;
    }
    public static void setVelocityState(VELOCITY_STATE newVelocityState) {
        ControllerState.velocityState = newVelocityState;
    }

    enum VELOCITY_STATE {
        CONSTANT,
        ACCELERATION,
        DECELERATION
    }
*/
}
