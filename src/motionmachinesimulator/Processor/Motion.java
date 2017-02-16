package motionmachinesimulator.Processor;

import java.util.LinkedList;

/**
 * Created by Sales on 16.02.2017.
 */
abstract class Motion {

    public static final int DIM = 3;
    protected double wayLengthXY;
    protected double wayLength;

    // general params
    protected double[] positionChange;
    protected double velocity;

    public Motion(double[] change, double vel) throws Exception {
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

    public double getVelocity(){ return this.velocity; };
    public double getVelocityPerSec(){ return this.velocity/60.0; };
    public double getLength(){
        return this.wayLength;
    }

    abstract void execute();

}
