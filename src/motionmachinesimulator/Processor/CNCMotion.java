package motionmachinesimulator.Processor;

import java.awt.*;

public abstract class CNCMotion extends CNCAction {

    // environment access
    private ExecutionState executionState = ExecutionState.getInstance();

    private MOTION_PHASE phase;

    private final long stepSizeBeforeAcceleration;
    private final long stepSizeAfterDeceleration;
    private final long stepSizeConstantVelocity;
    private final long stepSizeIncrement;

    final CNCPoint relativeEndPoint;
    CNCPoint currentRelativePosition;

    long wayLength;
    long wayLengthCurrent;

    private long wayLengthAcceleration;
    private long wayLengthDeceleration;

    /**
     * @param endPoint - relative position change after motion
     */
    CNCMotion(CNCPoint endPoint,
              MOTION_TYPE type,
              double startVel,
              double endVel) throws Exception {

        if (endPoint == null) throw new Exception("Null motion not supported");
        else relativeEndPoint = endPoint;

        stepSizeConstantVelocity = ControllerSettings.getStepSize(type);

        if(startVel >= 0.0) stepSizeBeforeAcceleration = ControllerSettings.getStep4Velocity(startVel);
        else throw new Exception("Velocity should be positive");

        if(endVel >= 0.0) stepSizeAfterDeceleration = ControllerSettings.getStep4Velocity(endVel);
        else throw new Exception("Velocity should be positive");

        stepSizeIncrement = ControllerSettings.getStepIncrement4Acceleration();

        currentRelativePosition = new CNCPoint();

        phase = MOTION_PHASE.ACCELERATION;
    }

    void calcWayLength() {
        wayLengthCurrent = 0;
        wayLengthAcceleration = ControllerSettings.getWayLength4StepChange(stepSizeBeforeAcceleration, stepSizeConstantVelocity);
        wayLengthDeceleration = ControllerSettings.getWayLength4StepChange(stepSizeConstantVelocity, stepSizeAfterDeceleration);

        long wayLengthConstantVelocity = wayLength - wayLengthAcceleration - wayLengthDeceleration;
        if(wayLengthConstantVelocity < 0){
            // motion too short, processing without constant velocity state
            wayLengthAcceleration += wayLengthConstantVelocity /2;
            wayLengthDeceleration += wayLengthConstantVelocity /2;
        }

    }

    abstract void onFastTimerTick(long dl); //return new relative position

    public abstract CNCPoint paint(Graphics g, CNCPoint fromPoint);

    private long currentDistanceToTarget;
    private long stepSizeCurrent;
    private CNCPoint startPos;

    void start(){
        currentDistanceToTarget = wayLength;

        if(ExecutionDirection.isForward())
            stepSizeCurrent =  stepSizeBeforeAcceleration;
        else
            stepSizeCurrent = stepSizeAfterDeceleration;

        if(ExecutionDirection.isForward())
            startPos = CNCStepperPorts.getPosition();
    }

    void run(){
        start();
        do{
            if(executionState.getState() == ExecutionState.EXECUTION_STATE.ON_THE_RUN){

                if(ExecutionDirection.isForward())
                    wayLengthCurrent += stepSizeCurrent;
                else
                    wayLengthCurrent -= stepSizeCurrent;
                onFastTimerTick(wayLengthCurrent);

                CNCStepperPorts.setPosition(startPos.add(currentRelativePosition));
                ControllerSettings.setCurrentStepSIze(stepSizeCurrent);

                switch (phase){
                    case PAUSED:
                        break;
                    case ACCELERATION:
                        if(ExecutionDirection.isForward()){
                            if(stepSizeCurrent < stepSizeConstantVelocity){
                                stepSizeCurrent += stepSizeIncrement;
                            } else {
                                stepSizeCurrent = stepSizeConstantVelocity;
                                phase = MOTION_PHASE.CONSTANT_VELOCITY;
                            }
                        } else {
                            if(stepSizeCurrent > stepSizeBeforeAcceleration) stepSizeCurrent -= stepSizeIncrement;
                            else stepSizeCurrent = stepSizeBeforeAcceleration;
                        }
                        break;
                    case CONSTANT_VELOCITY:
                            stepSizeCurrent = stepSizeConstantVelocity;
                        break;
                    case DECELERATION:
                        if(ExecutionDirection.isForward()){
                            if(stepSizeCurrent > stepSizeAfterDeceleration) stepSizeCurrent -= stepSizeIncrement;
                            else stepSizeCurrent = stepSizeAfterDeceleration;
                        } else {
                            if(stepSizeCurrent < stepSizeConstantVelocity)stepSizeCurrent += stepSizeIncrement;
                            else {
                                stepSizeCurrent = stepSizeConstantVelocity;
                                phase = MOTION_PHASE.CONSTANT_VELOCITY;
                            }

                        }
                        break;
                    default:
                }

                if(ExecutionDirection.isForward()){
                    currentDistanceToTarget = wayLength - wayLengthCurrent;
                    if(currentDistanceToTarget<wayLengthDeceleration)
                        phase = MOTION_PHASE.DECELERATION;
                } else {
                    currentDistanceToTarget = wayLengthCurrent;
                    if(currentDistanceToTarget<wayLengthAcceleration)
                        phase = MOTION_PHASE.ACCELERATION;
                }

            }

            try {
                Thread.sleep(ControllerSettings.intervalInMillis);
            } catch (InterruptedException ie) {
                ie.printStackTrace();
            }

        } while (Math.abs(currentDistanceToTarget) > stepSizeCurrent);
    }

    enum MOTION_TYPE {
        FREE_RUN,
        WORKING
    }

    enum MOTION_PHASE {
        PAUSED,
        ACCELERATION,
        CONSTANT_VELOCITY,
        DECELERATION,
    }

}