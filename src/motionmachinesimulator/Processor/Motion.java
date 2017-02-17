package motionmachinesimulator.Processor;

import java.awt.*;

/**
 * Created by Sales on 16.02.2017.
 */

abstract class Motion implements Runnable {

    // general params
    protected double[] positionChange; // in meters

    protected double velocityXY; // in meter/sec
    protected double velocity;

    protected double duration;  // in sec
    protected int nTicks;

    protected double wayLengthXY; // in meters
    protected double wayLength;

    /**
     * @param change - relative position chenge after motion
     * @param vel - motion velocity
     * @throws Exception
     */
    Motion(double[] change, double vel) throws Exception {
        this.positionChange = change;

        if(this.positionChange != null){
            if(this.positionChange.length != ProcessorSettings.DIM){
                throw new Exception("Position change X, Y, Z coordinates needed only");
            }
        } else throw new Exception("Null motion not supported");

        this.velocity = vel;
        if(this.velocity <= 0.0){
            throw new Exception("Null or negative velocity for motion");
        }
    };

    //execution state
    protected double phase = -1; // 0..1 - execution state, -1 - yet not executed

    public double getPhase() {
        return this.phase;
    }

    public void setPhaseStateNotExecuted(){
        this.phase = -1;
    }

    abstract void paint(Graphics g);
}
