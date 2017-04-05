package motionmachinesimulator.Processor;

import java.awt.*;

public abstract class Motion {

    // general params
    protected double[] relativeEndPoint; // all in meters
    protected double[] currentRelativePosition;

    protected double wayLength; // all in meters
    protected double wayLengthXY;
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
    Motion(double[] endPoint,
           MOTION_TYPE type,
           double startVel,
           double endVel) throws Exception {
        this.relativeEndPoint = endPoint;

        if (this.relativeEndPoint != null) {
            if (this.relativeEndPoint.length != ControllerSettings.DIM) {
                throw new Exception("Position change X, Y, Z coordinates needed only");
            }
        } else throw new Exception("Null motion not supported");

        this.currentRelativePosition = new double[ControllerSettings.DIM];
        for(int i=0; i<ControllerSettings.DIM;i++)
            this.currentRelativePosition[i] = 0.0;

        this.currentWayLength = 0.0;

        this.motion_type = type;

        if(startVel >= 0.0){
            this.startStepSize = ControllerSettings.getStep4Velocity(startVel);
            this.startAccelerationWayLength
                    = ControllerSettings.getWayLength4StepChange(ControllerSettings.getStepSize(this.motion_type), startStepSize);
            System.out.println("Start acceleration way length = " + this.startAccelerationWayLength + " m.");
        } else throw new Exception("Velocity should be positive");

        if(endVel >= 0.0){
            this.endStepSize = ControllerSettings.getStep4Velocity(endVel);
            this.endDecelerationWayLength
                    = ControllerSettings.getWayLength4StepChange(ControllerSettings.getStepSize(this.motion_type), endStepSize);
            System.out.println("End deceleration way length = " + this.endDecelerationWayLength + " m.");
        } else throw new Exception("Velocity should be positive");

        this.phase = MOTION_PHASE.START_VELOCITY_CHANGE;

    }

    abstract double[] onFastTimerTick(double dl); //return new relative position

    public abstract double[] paint(Graphics g, double[] fromPoint);

    public void run(double[] startPos){
        final MotionController controller = MotionController.getInstance();
        final Task task = controller.getCurrentTask();
        double currentDistanceToTarget = Double.MAX_VALUE;
        double[] currentAbsPos = new double[ControllerSettings.DIM];
        final double systemStepIncrement = ControllerSettings.getStepIncrement4Acceleration();
        double currentStepIncrement = systemStepIncrement;
        double targetStepSize = ControllerSettings.getStepSize(motion_type);
        double currentStepSize;
        if(controller.isForwardDirection()){
            currentStepSize =  this.startStepSize;
        } else {
            currentStepSize = this.endStepSize;
        }
        do{
            if(EjectFlag.taskShouldBeEjected()) break;  // TODO change EjectFlag algorithm. wrong operation
            double[] relPos;
            if(task.getState() == Task.TASK_STATE.ON_THE_RUN){

                if(controller.isForwardDirection()) relPos = this.onFastTimerTick(currentStepSize);
                else relPos = this.onFastTimerTick(-currentStepSize);
                for(int i=0; i<ControllerSettings.DIM;i++){
                    currentAbsPos[i] = startPos[i] + relPos[i];
                }
                CurrentPosition.set(currentAbsPos);
                ControllerSettings.setCurrentStepSIze(currentStepSize);

                if(controller.isForwardDirection()){
                    currentDistanceToTarget = this.wayLength - this.currentWayLength;
                } else {
                    currentDistanceToTarget = this.currentWayLength;
                };

                if(currentStepSize < targetStepSize) currentStepSize += currentStepIncrement;
                if(currentStepSize > targetStepSize) currentStepSize -= currentStepIncrement;

                switch (this.phase){
                    case PAUSED:
                        break;
                    case START_VELOCITY_CHANGE:
                        if(controller.isForwardDirection()){
                            targetStepSize = ControllerSettings.getStepSize(motion_type);
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
                        targetStepSize = ControllerSettings.getStepSize(motion_type);
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
                            targetStepSize = ControllerSettings.getStepSize(motion_type);
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
}