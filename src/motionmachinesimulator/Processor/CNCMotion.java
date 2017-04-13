package motionmachinesimulator.Processor;

import java.awt.*;

public abstract class CNCMotion extends CNCAction {
    // environment access
    private MotionController controller;
    private Task task;

    // global constants
    private double systemStepIncrement;

    // general params
    protected final CNCPoint2D relativeEndPoint; // all in meters
    protected CNCPoint2D currentRelativePosition;

    protected double wayLength; // all in meters
    protected double currentWayLength;

    private MOTION_PHASE phase;

    private MOTION_TYPE motion_type;
    private double startStepSize;
    private double startAccelerationWayLength;
    private double endStepSize;
    private double endDecelerationWayLength;

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
            this.startStepSize = ControllerSettings.getStep4Velocity(startVel);
            this.startAccelerationWayLength
                    = ControllerSettings.getWayLength4StepChange(ControllerSettings.getTargetStepSize(this.motion_type), startStepSize);
            System.out.println("Start acceleration way length = " + this.startAccelerationWayLength + " m.");
        } else throw new Exception("Velocity should be positive");

        if(endVel >= 0.0){
            this.endStepSize = ControllerSettings.getStep4Velocity(endVel);
            this.endDecelerationWayLength
                    = ControllerSettings.getWayLength4StepChange(ControllerSettings.getTargetStepSize(this.motion_type), endStepSize);
            System.out.println("End deceleration way length = " + this.endDecelerationWayLength + " m.");
        } else throw new Exception("Velocity should be positive");

        this.phase = MOTION_PHASE.START_VELOCITY_CHANGE;

    }

    abstract CNCPoint2D onFastTimerTick(double dl); //return new relative position

    public abstract CNCPoint2D paint(Graphics g, CNCPoint2D fromPoint);

    public void run(CNCPoint2D startPos){
        controller = MotionController.getInstance();
        task = controller.getCurrentTask();
        systemStepIncrement = ControllerSettings.getStepIncrement4Acceleration();

        double currentDistanceToTarget = Double.MAX_VALUE;
        CNCPoint2D currentAbsPos = new CNCPoint2D();
        double targetStepSize = ControllerSettings.getTargetStepSize(motion_type);
        double currentStepSize;

        if(controller.isForwardDirection()){
            currentStepSize =  this.startStepSize;
        } else {
            currentStepSize = this.endStepSize;
        }

        do{
            CNCPoint2D relPos;
            if(task.getState() == Task.TASK_STATE.ON_THE_RUN){

                if(controller.isForwardDirection()) relPos = this.onFastTimerTick(currentStepSize);
                else relPos = this.onFastTimerTick(-currentStepSize);
                currentAbsPos = startPos.add(relPos);
                CurrentPosition.getInstance().set(currentAbsPos);
                ControllerSettings.setCurrentStepSIze(currentStepSize);

                if(controller.isForwardDirection()){
                    currentDistanceToTarget = this.wayLength - this.currentWayLength;
                } else {
                    currentDistanceToTarget = this.currentWayLength;
                };

                if(currentStepSize < targetStepSize) currentStepSize += systemStepIncrement;
                if(currentStepSize > targetStepSize) currentStepSize -= systemStepIncrement;

                switch (this.phase){
                    case PAUSED:
                        break;
                    case START_VELOCITY_CHANGE:
                        if(controller.isForwardDirection()){
                            targetStepSize = ControllerSettings.getTargetStepSize(motion_type);
                            if(currentStepSize >= targetStepSize){
                                currentStepSize = targetStepSize;
                                this.phase = MOTION_PHASE.CONSTANT_VELOCITY;
                            }
                        } else {
                            targetStepSize = this.startStepSize;
                            if(currentStepSize <= targetStepSize){
                                currentStepSize = targetStepSize;
                            }
                        }
                        break;
                    case CONSTANT_VELOCITY:
                        targetStepSize = ControllerSettings.getTargetStepSize(motion_type);
                        if(controller.isForwardDirection()){
                            if(currentDistanceToTarget <= endDecelerationWayLength){
                                this.phase = MOTION_PHASE.END_VELOCITY_CHANGE;
                                targetStepSize = this.endStepSize;
                            }
                        } else {
                            if(currentDistanceToTarget <= startAccelerationWayLength){
                                this.phase = MOTION_PHASE.END_VELOCITY_CHANGE;
                                targetStepSize = this.startStepSize;
                            }
                        }
                        break;
                    case END_VELOCITY_CHANGE:
                        if(controller.isForwardDirection()){
                            targetStepSize = this.endStepSize;
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