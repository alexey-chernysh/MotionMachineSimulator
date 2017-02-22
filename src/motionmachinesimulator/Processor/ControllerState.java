package motionmachinesimulator.Processor;

public class ControllerState {

    public ControllerState() {
        for(int i = 0; i< ControllerSettings.DIM; i++) currentPosition[i] = 0.0;
    }

    private static double[] currentPosition = new double[ControllerSettings.DIM];

    public static double[] getCurrentPosition() {
        return ControllerState.currentPosition;
    }
    public static void setCurrentPosition(double[] newPosition) {
        ControllerState.currentPosition = newPosition;
    }

    private static MOTION_STATE motionState = MOTION_STATE.PAUSED;

    static MOTION_STATE getMotionState() {
        return motionState;
    }
    static void setMotionState(MOTION_STATE newState) {
        motionState = newState;
    }

    enum MOTION_STATE {
        STARTED,
        PAUSED,
    }
/*
    private static DIRECTION_STATE directionState = DIRECTION_STATE.FORWARD;

    public static DIRECTION_STATE getDirectionState() {
        return directionState;
    }
    public static void setDirectionState(DIRECTION_STATE newDirectionState) {
        ControllerState.directionState = newDirectionState;
    }

    enum DIRECTION_STATE {
        FORWARD,
        BACKWARD
    }
    */
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
