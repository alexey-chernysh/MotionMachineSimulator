package motionmachinesimulator.Processor;

import java.awt.*;

public abstract class Motion {

    // general params
    protected double[] positionChange; // all in meters
    protected double[] absoluteStartPosition;
    protected double[] currentRelativePosition;
    protected double[] currentAbsolutePositiom;

    protected double wayLength; // all in meters
    protected double wayLengthXY;
    protected double currentWayLength;

    protected boolean onTheRun;

    /**
     * @param change - relative position chenge after motion
     * @throws Exception
     */
    Motion(double[] change) throws Exception {
        this.positionChange = change;

        if (this.positionChange != null) {
            if (this.positionChange.length != ControllerSettings.DIM) {
                throw new Exception("Position change X, Y, Z coordinates needed only");
            }
        } else throw new Exception("Null motion not supported");

        this.currentRelativePosition = new double[ControllerSettings.DIM];
        this.currentAbsolutePositiom = new double[ControllerSettings.DIM];
        for(int i=0; i<ControllerSettings.DIM;i++)
            this.currentRelativePosition[i] = 0.0;

        this.currentWayLength = 0.0;
        this.setPhaseStateNotExecuted();
        this.onTheRun = false;
    }

    void onFastTimerForwardTick(double dl){
        if(!this.onTheRun){
            this.onTheRun = true;
            this.absoluteStartPosition = MotionController.getCurrentPosition();
        }
        this.currentWayLength += dl;
        onPositionChange();
        for(int i=0; i<ControllerSettings.DIM;i++)
            this.currentAbsolutePositiom[i] = this.absoluteStartPosition[i] + this.currentRelativePosition[i];
        MotionController.setCurrentPosition(this.currentAbsolutePositiom);
        if(this.currentWayLength >= this.wayLength)
            this.onTheRun = false;
    };

    void onFastTimerBackwardTick(double dl){
        this.onTheRun = true;
        this.currentWayLength -= dl;
        onPositionChange();
        if(this.currentWayLength < 0) this.onTheRun = false;
        else {
            for(int i=0; i<ControllerSettings.DIM;i++)
                this.currentAbsolutePositiom[i] = this.absoluteStartPosition[i] + this.currentRelativePosition[i];
        }
    }

    abstract void onPositionChange();

    public abstract double[] paint(Graphics g, double[] fromPoint);

    public void setPhaseStateNotExecuted() {
        this.currentWayLength = 0.0;
    }

    public boolean isOnTheRun(){ return onTheRun; }

}