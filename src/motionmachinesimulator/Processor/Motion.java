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
    private double startVelocity;
    private double endVelocity;

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

        if(startVel >= 0.0) this.startVelocity = startVel;
        else throw new Exception("Velocity should be positive");

        if(endVel >= 0.0) this.endVelocity = endVel;
        else throw new Exception("Velocity should be positive");

    }

    abstract double[] onFastTimerTick(double dl); //return new relative position

    public abstract double[] paint(Graphics g, double[] fromPoint);

    public double run(double[] startPos, double initialStepSize){
        double currentDistanceToTarget = Double.MAX_VALUE;
        double[] currentAbsPos = new double[ControllerSettings.DIM];
        MotionController controller = MotionController.getInstance();
        double currentStepSize = initialStepSize;
        double currentStepIncrement = ControllerSettings.getStepIncrement4Acceleration();
        do{ // linear velocity phase
            if(EjectFlag.taskShouldBeEjected()) break;
            double[] relPos;
            if(MotionController.getInstance().getCurrentTask().getState() == Task.TASK_STATE.ON_THE_RUN){
                if(controller.isForwardDirection()) relPos = this.onFastTimerTick(currentStepSize);
                else relPos = this.onFastTimerTick(-currentStepSize);
                ControllerSettings.setCurrentStepSIze(currentStepSize);
                double targetStepSize = ControllerSettings.getStepSizeWorking();
                if(currentStepSize < targetStepSize){
                    currentStepSize += currentStepIncrement;
                    if(currentStepSize > targetStepSize) currentStepSize = targetStepSize;
                }
                if(currentStepSize > targetStepSize){
                    currentStepSize -= currentStepIncrement;
                    if(currentStepSize < targetStepSize) currentStepSize = targetStepSize;
                }
                for(int i=0; i<ControllerSettings.DIM;i++){
                    currentAbsPos[i] = startPos[i] + relPos[i];
                }
                CurrentPosition.set(currentAbsPos);
                currentDistanceToTarget = controller.isForwardDirection() ?
                        this.wayLength - this.currentWayLength :
                        this.currentWayLength;
                try {
                    Thread.sleep(ControllerSettings.intervalInMillis);
                } catch (InterruptedException ie) {
                    ie.printStackTrace();
                }
            } else System.out.print("+");
        } while (Math.abs(currentDistanceToTarget) > currentStepSize);
        return currentStepSize;
    }

    enum MOTION_TYPE {
        FREE_RUN,
        WORKING
    }
}