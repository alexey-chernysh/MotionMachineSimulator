package motionmachinesimulator.Processor;

import java.awt.*;

/**
 * Created by Sales on 16.02.2017.
 */

abstract class Motion implements Runnable {

    public static final int DIM = 3;
    protected double wayLengthXY;
    protected double wayLength;

    // general params
    protected double[] positionChange;
    protected double velocity;

    /**
     * @param change - relative position chenge after motion
     * @param vel - motion velocity
     * @throws Exception
     */
    Motion(double[] change, double vel) throws Exception {
        this.positionChange = change;

        if(this.positionChange != null){
            if(this.positionChange.length != Motion.DIM){
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
