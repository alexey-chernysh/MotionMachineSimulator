package motionmachinesimulator.Processor;

import java.awt.*;

public abstract class Motion {

    // general params
    protected double[] relativeEndPoint; // all in meters
    protected double[] currentRelativePosition;

    protected double wayLength; // all in meters
    protected double wayLengthXY;
    protected double currentWayLength;

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
            this.startAccelerationWayLength = ControllerSettings.getWayLength4StepChange(ControllerSettings.getStepSize(this.motion_type), startVel);
        } else throw new Exception("Velocity should be positive");

        if(endVel >= 0.0){
            this.endStepSize = ControllerSettings.getStep4Velocity(endVel);
            this.endDecelerationWayLength = ControllerSettings.getWayLength4StepChange(ControllerSettings.getStepSize(this.motion_type), endVel);
        } else throw new Exception("Velocity should be positive");

    }

    abstract double[] onFastTimerTick(double dl); //return new relative position

    public abstract double[] paint(Graphics g, double[] fromPoint);

    public void run(double[] startPos){
        double currentDistanceToTarget = Double.MAX_VALUE;
        double[] currentAbsPos = new double[ControllerSettings.DIM];
        MotionController controller = MotionController.getInstance();
        double currentStepSize = controller.isForwardDirection() ? this.startStepSize : this.endStepSize;
        double currentStepIncrement = ControllerSettings.getStepIncrement4Acceleration();
        do{ // linear velocity phase
            if(EjectFlag.taskShouldBeEjected()) break;  // TODO change EjectFlag algorithm. wrong operation
            double[] relPos;
            if(MotionController.getInstance().getCurrentTask().getState() == Task.TASK_STATE.ON_THE_RUN){

                if(controller.isForwardDirection()) relPos = this.onFastTimerTick(currentStepSize);
                else relPos = this.onFastTimerTick(-currentStepSize);
                for(int i=0; i<ControllerSettings.DIM;i++){
                    currentAbsPos[i] = startPos[i] + relPos[i];
                }
                CurrentPosition.set(currentAbsPos);
                ControllerSettings.setCurrentStepSIze(currentStepSize);

                double targetStepSize = ControllerSettings.getStepSize(motion_type);
                if(currentStepSize < targetStepSize){
                    currentStepSize += currentStepIncrement;
                    if(currentStepSize > targetStepSize) currentStepSize = targetStepSize;
                }
                if(currentStepSize > targetStepSize){
                    currentStepSize -= currentStepIncrement;
                    if(currentStepSize < targetStepSize) currentStepSize = targetStepSize;
                }

                if(controller.isForwardDirection()){
                    currentDistanceToTarget = this.wayLength - this.currentWayLength;
                } else {
                    currentDistanceToTarget = this.currentWayLength;
                };
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
}