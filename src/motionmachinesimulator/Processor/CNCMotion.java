package motionmachinesimulator.Processor;

import java.awt.*;

public abstract class CNCMotion extends CNCAction {

    // environment access
    private MOTION_PHASE phase;

    private final long stepSizeBeforeAcceleration;
    private final long stepSizeAfterDeceleration;
    private final long stepSizeIncrement;
    private final long stepSizeConstantVelocity;
    private long currentDistanceToTarget;
    private long stepSizeForTrapeciedalProfile;

    final CNCPoint relativeEndPoint;
    CNCPoint currentRelativePosition;
    private CNCPoint startPos;


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

    void prepareData(){
        currentDistanceToTarget = wayLength;
        if(ExecutionState.isForward()) {
            stepSizeForTrapeciedalProfile = stepSizeBeforeAcceleration;
            startPos = CNCStepperPorts.getPosition();
            phase = MOTION_PHASE.HEAD;
        } else {
            stepSizeForTrapeciedalProfile = stepSizeAfterDeceleration;
            phase = MOTION_PHASE.TAIL;
        }
    }

    boolean goByOneNanoStepForward(){ // return true if another step needed

        long stepSizeCurrent = stepSizeForTrapeciedalProfile;
        stepSizeCurrent = ExecutionState.getResumingStepSize(stepSizeCurrent);
        stepSizeCurrent = ExecutionState.getPausingStepSize(stepSizeCurrent);
        wayLengthCurrent += stepSizeCurrent;
        onFastTimerTick(wayLengthCurrent);

        CNCStepperPorts.setPosition(startPos.add(currentRelativePosition));
        ControllerSettings.setCurrentStepSize(stepSizeCurrent);

        switch (phase){
            case HEAD:
                if(stepSizeForTrapeciedalProfile < stepSizeConstantVelocity){
                    stepSizeForTrapeciedalProfile += stepSizeIncrement;
                } else {
                    stepSizeForTrapeciedalProfile = stepSizeConstantVelocity;
                    phase = MOTION_PHASE.BODY;
                }
                break;
            case BODY:
                stepSizeForTrapeciedalProfile = stepSizeConstantVelocity;
                break;
            case TAIL:
                if(stepSizeForTrapeciedalProfile > stepSizeAfterDeceleration) stepSizeForTrapeciedalProfile -= stepSizeIncrement;
                else stepSizeForTrapeciedalProfile = stepSizeAfterDeceleration;
                break;
            default:
        }

        currentDistanceToTarget = wayLength - wayLengthCurrent;
        if(currentDistanceToTarget<wayLengthDeceleration)
            phase = MOTION_PHASE.TAIL;

        return (Math.abs(currentDistanceToTarget) > stepSizeForTrapeciedalProfile);
    }

    boolean goByOneNanoStepBackward(){ // return true if another step needed

        long stepSizeCurrent = stepSizeForTrapeciedalProfile;
        stepSizeCurrent = ExecutionState.getResumingStepSize(stepSizeCurrent);
        stepSizeCurrent = ExecutionState.getPausingStepSize(stepSizeCurrent);
        wayLengthCurrent -= stepSizeCurrent;
        onFastTimerTick(wayLengthCurrent);

        CNCStepperPorts.setPosition(startPos.add(currentRelativePosition));
        ControllerSettings.setCurrentStepSize(stepSizeCurrent);

        switch (phase){
            case HEAD:
                if(stepSizeForTrapeciedalProfile > stepSizeBeforeAcceleration) stepSizeForTrapeciedalProfile -= stepSizeIncrement;
                else stepSizeForTrapeciedalProfile = stepSizeBeforeAcceleration;
                break;
            case BODY:
                stepSizeForTrapeciedalProfile = stepSizeConstantVelocity;
                break;
            case TAIL:
                if(stepSizeForTrapeciedalProfile < stepSizeConstantVelocity) stepSizeForTrapeciedalProfile += stepSizeIncrement;
                else {
                    stepSizeForTrapeciedalProfile = stepSizeConstantVelocity;
                    phase = MOTION_PHASE.BODY;
                }
                break;
            default:
        }

        currentDistanceToTarget = wayLengthCurrent;
        if(currentDistanceToTarget<wayLengthAcceleration) phase = MOTION_PHASE.HEAD;

        return (Math.abs(currentDistanceToTarget) > stepSizeForTrapeciedalProfile);
    }

    enum MOTION_TYPE {
        FREE_RUN,
        WORKING
    }

    enum MOTION_PHASE {
        HEAD,
        BODY,
        TAIL
    }

}