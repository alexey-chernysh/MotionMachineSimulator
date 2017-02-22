package motionmachinesimulator.Processor;

import java.awt.*;

public abstract class Motion implements Runnable {

    // general params
    protected double[] positionChange; // in meters

    protected double velocityXY; // in meter/sec
    protected double velocity;

    protected double duration;  // in sec
    protected int nTicks;

    protected double wayLengthXY; // in meters
    protected double wayLength;
    protected double currentWayLength;

    /**
     * @param change - relative position chenge after motion
     * @param vel    - motion velocity
     * @throws Exception
     */
    Motion(double[] change, double vel) throws Exception {
        this.positionChange = change;

        if (this.positionChange != null) {
            if (this.positionChange.length != ControllerSettings.DIM) {
                throw new Exception("Position change X, Y, Z coordinates needed only");
            }
        } else throw new Exception("Null motion not supported");

        this.velocity = vel;
        if (this.velocity <= 0.0) {
            throw new Exception("Null or negative velocity for motion");
        }

        this.setPhaseStateNotExecuted();
    }

    public abstract double[] paint(Graphics g, double[] fromPoint);

    public void setPhaseStateNotExecuted() {
        this.currentWayLength = 0;
    }

    public double getPhase() {  // 0..1 - execution state, -1 - yet not executed
        return this.currentWayLength/this.wayLength;
    }

}