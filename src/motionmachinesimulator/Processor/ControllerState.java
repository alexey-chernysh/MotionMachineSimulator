package motionmachinesimulator.Processor;

public class ControllerState extends Thread {

    ControllerState() {
    }

    private MOTION_DIRECTION direction = MOTION_DIRECTION.FORWARD;

    MOTION_DIRECTION getDirection() {
        return this.direction;
    }
    void setDirection(MOTION_DIRECTION newDirectionState) {
        this.direction = newDirectionState;
    }

    enum MOTION_DIRECTION {
        FORWARD,
        BACKWARD
    }

}
