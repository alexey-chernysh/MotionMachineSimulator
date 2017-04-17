package motionmachinesimulator.Processor;

import java.awt.*;

public abstract class CNCMotion extends CNCAction {
    // environment access
    private ExecutionDirection executionDirection = ExecutionDirection.getInstance();
    private ExecutionState executionState = ExecutionState.getInstance();
    private CurrentPosition currentPosition = CurrentPosition.getInstance();

    private MOTION_PHASE phase;
    private MOTION_TYPE motion_type;

    private double stepSizeBeforeAcceleration;
    private double stepSizeAfterDeceleration;
    private double stepSizeConstantVelocity;
    private double stepSizeIncrement;

    protected final CNCPoint2D relativeEndPoint; // all in meters
    protected CNCPoint2D currentRelativePosition;

    protected double wayLength; // all in meters
    protected double wayLengthCurrent;
    private double wayLengthAcceleration;
    private double wayLengthDeceleration;
    private double wayLengthConstantVelocity;

    /**
     * @param endPoint - relative position change after motion
     */
    CNCMotion(CNCPoint2D endPoint,
              MOTION_TYPE type,
              double startVel,
              double endVel) throws Exception {

        if (endPoint == null) throw new Exception("Null motion not supported");
        else relativeEndPoint = endPoint;

        motion_type = type;

        stepSizeConstantVelocity = ControllerSettings.getTargetStepSize(motion_type);

        if(startVel >= 0.0)stepSizeBeforeAcceleration = ControllerSettings.getStep4Velocity(startVel);
        else throw new Exception("Velocity should be positive");

        if(endVel >= 0.0) stepSizeAfterDeceleration = ControllerSettings.getStep4Velocity(endVel);
        else throw new Exception("Velocity should be positive");

        stepSizeIncrement = ControllerSettings.getStepIncrement4Acceleration();

        currentRelativePosition = new CNCPoint2D();

        phase = MOTION_PHASE.ACCELERATION;
    }

    public void buildVelocityPlan() {
        wayLengthCurrent = 0.0;
        wayLengthAcceleration = ControllerSettings.getWayLength4StepChange(stepSizeConstantVelocity, stepSizeBeforeAcceleration);
        wayLengthDeceleration = ControllerSettings.getWayLength4StepChange(stepSizeConstantVelocity, stepSizeAfterDeceleration);

        wayLengthConstantVelocity = wayLength - wayLengthAcceleration - stepSizeAfterDeceleration;
        if(wayLengthConstantVelocity < 0.0){
            // motion too short, processing without constant velocity state
            wayLengthAcceleration += wayLengthConstantVelocity/2;
            wayLengthDeceleration += wayLengthConstantVelocity/2;
            wayLengthConstantVelocity = 0.0;
        }

    }

    abstract CNCPoint2D onFastTimerTick(double dl); //return new relative position

    public abstract CNCPoint2D paint(Graphics g, CNCPoint2D fromPoint);

    public void run(CNCPoint2D startPos){
        double currentDistanceToTarget = wayLength;
        CNCPoint2D currentAbsPos;
        CNCPoint2D relPos;
        double stepSizeCurrent;

        if(executionDirection.isForward())stepSizeCurrent =  stepSizeBeforeAcceleration;
        else stepSizeCurrent = stepSizeAfterDeceleration;

        do{
            if(executionState.getState() == ExecutionState.EXECUTION_STATE.ON_THE_RUN){

                if(executionDirection.isForward()) relPos = onFastTimerTick(stepSizeCurrent);
                else relPos = onFastTimerTick(-stepSizeCurrent);

                currentAbsPos = startPos.add(relPos);
                currentPosition.set(currentAbsPos);
                ControllerSettings.setCurrentStepSIze(stepSizeCurrent);

                switch (phase){
                    case PAUSED:
                        break;
                    case ACCELERATION:
                        if(executionDirection.isForward()){
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
                        if(executionDirection.isForward()){
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

                if(executionDirection.isForward()){
                    currentDistanceToTarget = wayLength - wayLengthCurrent;
                    if(currentDistanceToTarget<wayLengthDeceleration)
                        phase = MOTION_PHASE.DECELERATION;
                } else {
                    currentDistanceToTarget = wayLengthCurrent;
                    if(currentDistanceToTarget<wayLengthAcceleration)
                        phase = MOTION_PHASE.ACCELERATION;
                }

            }; // else System.out.print("+");

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