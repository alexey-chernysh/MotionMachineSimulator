/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package motionmachinesimulator.Processor;

/**
 * @author alexey
 */

class Motion {

    private static final int dim = 3;

    // general params
    protected double[] positionChange;
    private double velocity;


    // arc params
    protected double[] centerOffset;
    protected double length; // metric path length of motion
    private DIRECTION direction;

    //general vars
    private double[] K = new double[dim];
    private double wayLengthXY;
    private double wayLength;
    //arc vars
    private double Radius;
    private double beginAngle;
    private double   endAngle;
    private double      angle;

    private static final double twoPi = 2.0*Math.PI;

    public Motion(double[] change, double[] center, double vel, DIRECTION dir) throws Exception {

        this.positionChange = change;

        if(this.positionChange != null){
            if(this.positionChange.length != dim){
                throw new Exception("Position change X, Y, Z coordinates needed only");
            }
        } else throw new Exception("Null motion not supported");

        this.centerOffset = center; // should be non zero for arc motion

        if(this.centerOffset != null){
            if(this.centerOffset.length != 2) {
                throw new Exception("Arc center offset's X & Y coordinates needed only");
            };
            this.Radius = Math.sqrt(this.centerOffset[0]*this.centerOffset[0] + this.centerOffset[1]*this.centerOffset[1])
            if(this.Radius <= 0.0) throw new Exception("Zero radius arc not supported");
            this.beginAngle = Math.atan2(this.centerOffset[1],this.centerOffset[0]);
            this.endAngle = Math.atan2(this.positionChange[1],this.positionChange[0]);
            this.angle = this.endAngle - this.beginAngle;
            switch (this.direction){
                case CW:
                    while(this.angle >= 0.0 ) this.angle -= twoPi;
                    while(this.angle < -twoPi ) this.angle += twoPi;
                    break;
                case CCW:
                    while(this.angle <= 0.0 ) this.angle += twoPi;
                    while(this.angle > twoPi ) this.angle -= twoPi;
                    break;
                default:
                    throw new Exception("Unsupported arc direction");
            }
            this.wayLengthXY = this.Radius*this.angle;
        } else {
            this.wayLengthXY = Math.sqrt(this.positionChange[0]*this.positionChange[0] +
                                        +this.positionChange[1]*this.positionChange[1]);
        };
        this.wayLength = Math.sqrt(this.positionChange[2]*this.positionChange[2]
                                 + this.wayLengthXY*this.wayLengthXY);
        if( this.wayLength <= 0.0)
            throw new Exception("Null motion not supported");
        for(int i=0; i<dim; i++)
            this.K[i] = this.positionChange[i]/this.wayLength;

        this.velocity = vel;
        if(this.velocity <= 0.0){
            throw new Exception("Null or negative velocity for motion");
        }
        this.direction = dir;
    }
    
    public double getVelocity(){ return this.velocity; };
    public double getVelocityPerSec(){ return this.velocity/60.0; };
    
    private void setLength(){};
    public double getLength(){
        return this.length;
    }

    public void execute(){};

    public enum DIRECTION {
        CW, // clockwise
        CCW // counterclockwise
    };
}
