package motionmachinesimulator.Processor;

import java.awt.*;

public abstract class CNCMotion extends CNCAction {
    // environment access
    private ExecutionDirection executionDirection;
    private ExecutionState executionState;

    private MOTION_PHASE phase;
    private MOTION_TYPE motion_type;

    private final double stepSizeBeforeAcceleration;
    private final double stepSizeAfterDeceleration;
    private final double stepSizeConstantVelocity;
    private final double stepSizeIncrement;

    protected final CNCPoint2D relativeEndPoint; // all in meters
    protected CNCPoint2D currentRelativePosition;

    protected double wayLength; // all in meters
    protected double currentWayLength;
    private double wayLengthAcceleration;
    private double wayLengthDeceleration;
    protected double wayLengthConstantVelocity;

    /**
     * @param endPoint - relative position change after motion
     */
    CNCMotion(CNCPoint2D endPoint,
              MOTION_TYPE type,
              double startVel,
              double endVel) throws Exception {
        this.relativeEndPoint = endPoint;

        if (this.relativeEndPoint == null)
            throw new Exception("Null motion not supported");

        this.currentRelativePosition = new CNCPoint2D();

        this.currentWayLength = 0.0;

        this.motion_type = type;

        if(startVel >= 0.0){
            this.stepSizeBeforeAcceleration = ControllerSettings.getStep4Velocity(startVel);
            this.wayLengthAcceleration
                    = ControllerSettings.getWayLength4StepChange(ControllerSettings.getTargetStepSize(this.motion_type), stepSizeBeforeAcceleration);
            System.out.println("Start acceleration way length = " + this.wayLengthAcceleration + " m.");
        } else throw new Exception("Velocity should be positive");

        if(endVel >= 0.0){
            this.stepSizeAfterDeceleration = ControllerSettings.getStep4Velocity(endVel);
            this.wayLengthDeceleration
                    = ControllerSettings.getWayLength4StepChange(ControllerSettings.getTargetStepSize(this.motion_type), stepSizeAfterDeceleration);
            System.out.println("End deceleration way length = " + this.wayLengthDeceleration + " m.");
        } else throw new Exception("Velocity should be positive");

        this.stepSizeConstantVelocity = ControllerSettings.getTargetStepSize(this.motion_type);

        this.stepSizeIncrement = ControllerSettings.getStepIncrement4Acceleration();
        this.phase = MOTION_PHASE.START_VELOCITY_CHANGE;
        this.executionDirection = ExecutionDirection.getInstance();
        this.executionState = ExecutionState.getInstance();
    }

    abstract CNCPoint2D onFastTimerTick(double dl); //return new relative position

    public abstract CNCPoint2D paint(Graphics g, CNCPoint2D fromPoint);

    public void run(CNCPoint2D startPos){
        double currentDistanceToTarget = Double.MAX_VALUE;
        CNCPoint2D currentAbsPos = new CNCPoint2D();
        double targetStepSize = ControllerSettings.getTargetStepSize(motion_type);
        double currentStepSize;

        if(executionDirection.isForward()){
            currentStepSize =  this.stepSizeBeforeAcceleration;
        } else {
            currentStepSize = this.stepSizeAfterDeceleration;
        }

        do{
            CNCPoint2D relPos;
            if(executionState.getState() == ExecutionState.EXECUTION_STATE.ON_THE_RUN){

                if(executionDirection.isForward()) relPos = this.onFastTimerTick(currentStepSize);
                else relPos = this.onFastTimerTick(-currentStepSize);
                currentAbsPos = startPos.add(relPos);
                CurrentPosition.getInstance().set(currentAbsPos);
                ControllerSettings.setCurrentStepSIze(currentStepSize);

                if(executionDirection.isForward()){
                    currentDistanceToTarget = this.wayLength - this.currentWayLength;
                } else {
                    currentDistanceToTarget = this.currentWayLength;
                };

                if(currentStepSize < targetStepSize) currentStepSize += stepSizeIncrement;
                if(currentStepSize > targetStepSize) currentStepSize -= stepSizeIncrement;

                switch (this.phase){
                    case PAUSED:
                        break;
                    case START_VELOCITY_CHANGE:
                        if(executionDirection.isForward()){
                            targetStepSize = ControllerSettings.getTargetStepSize(motion_type);
                            if(currentStepSize >= targetStepSize){
                                currentStepSize = targetStepSize;
                                this.phase = MOTION_PHASE.CONSTANT_VELOCITY;
                            }
                        } else {
                            targetStepSize = this.stepSizeBeforeAcceleration;
                            if(currentStepSize <= targetStepSize){
                                currentStepSize = targetStepSize;
                            }
                        }
                        break;
                    case CONSTANT_VELOCITY:
                        targetStepSize = ControllerSettings.getTargetStepSize(motion_type);
                        if(executionDirection.isForward()){
                            if(currentDistanceToTarget <= wayLengthDeceleration){
                                this.phase = MOTION_PHASE.END_VELOCITY_CHANGE;
                                targetStepSize = this.stepSizeAfterDeceleration;
                            }
                        } else {
                            if(currentDistanceToTarget <= wayLengthAcceleration){
                                this.phase = MOTION_PHASE.END_VELOCITY_CHANGE;
                                targetStepSize = this.stepSizeBeforeAcceleration;
                            }
                        }
                        break;
                    case END_VELOCITY_CHANGE:
                        if(executionDirection.isForward()){
                            targetStepSize = this.stepSizeAfterDeceleration;
                            if(currentStepSize >= targetStepSize){
                                currentStepSize = targetStepSize;
                            }
                        } else {
                            targetStepSize = ControllerSettings.getTargetStepSize(motion_type);
                            if(currentStepSize <= targetStepSize){
                                currentStepSize = targetStepSize;
                                this.phase = MOTION_PHASE.CONSTANT_VELOCITY;
                            }
                        }
                        break;
                    default:
                }

            } else System.out.print("+");

            try {
                Thread.sleep(ControllerSettings.intervalInMillis);
            } catch (InterruptedException ie) {
                ie.printStackTrace();
            }

        } while (Math.abs(currentDistanceToTarget) > currentStepSize);
    }

    enum MOTION_TYPE {
        FREE_RUN,
        WORKING
    }

    enum MOTION_PHASE {
        PAUSED,
        START_VELOCITY_CHANGE,
        CONSTANT_VELOCITY,
        END_VELOCITY_CHANGE,
    }

    enum VELOCITY_STATE {
        ACCELERATING,
        DECELERATING,
        CONSTANT
    }
}