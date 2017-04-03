package motionmachinesimulator.Processor;

import java.awt.*;

public abstract class Motion {

    // general params
    protected double[] relativeEndPoint; // all in meters
    protected double[] currentRelativePosition;

    protected double wayLength; // all in meters
    protected double wayLengthXY;
    protected double currentWayLength;

    private double targetVelocity;
    private double startVelocity;
    private double endVelocity;

    /**
     * @param endPoint - relative position change after motion
     */
    Motion(double[] endPoint,
           double motionVelocity,
           double startVel,
           double endVel) throws Exception {
        this.relativeEndPoint = endPoint;
        this.targetVelocity = motionVelocity;
        this.startVelocity = startVel;
        this.endVelocity = endVel;

        if (this.relativeEndPoint != null) {
            if (this.relativeEndPoint.length != ControllerSettings.DIM) {
                throw new Exception("Position change X, Y, Z coordinates needed only");
            }
        } else throw new Exception("Null motion not supported");

        this.currentRelativePosition = new double[ControllerSettings.DIM];
        for(int i=0; i<ControllerSettings.DIM;i++)
            this.currentRelativePosition[i] = 0.0;

        this.currentWayLength = 0.0;
    }

    abstract double[] onFastTimerTick(double dl); //return new relative position

    public abstract double[] paint(Graphics g, double[] fromPoint);

    public void run(double[] startPos){
        double currentDistanceToTarget = Double.MAX_VALUE;
        double[] currentAbsPos = new double[ControllerSettings.DIM];
        MotionController controller = MotionController.getInstance();
        Task currentTask = MotionController.getInstance().getCurrentTask();
        double stepSize = MotionController.getInstance().getStepSize();
        do{ // linear velocity phase
            if(EjectFlag.taskShouldBeEjected()) break;
            double[] relPos;
            if(currentTask.getState() == Task.TASK_STATE.ON_THE_RUN){
                if(controller.isForwardDirection()) relPos = this.onFastTimerTick(stepSize);
                else relPos = this.onFastTimerTick(-stepSize);
                for(int i=0; i<ControllerSettings.DIM;i++){
                    currentAbsPos[i] = startPos[i] + relPos[i];
                }
                CurrentPosition.set(currentAbsPos);
                currentDistanceToTarget = controller.isForwardDirection() ?
                        this.wayLength - this.currentWayLength :
                        this.currentWayLength;
                try {
                    Thread.sleep(MotionController.getInstance().getIntervalInMillis());
                } catch (InterruptedException ie) {
                    ie.printStackTrace();
                }
            } else System.out.print("+");
        } while (Math.abs(currentDistanceToTarget) > stepSize);
    }
}