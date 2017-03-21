package motionmachinesimulator.Processor;

import java.awt.*;

public class ArcMotion extends Motion {


    // arc params
    private double[] centerOffset;
    private ArcMotion.DIRECTION direction;

    //arc vars
    private double Radius;
    private double      angle;
    private double beginAngle;
    private double   endAngle;

    //general vars
    protected double Kz;

    private static final double twoPi = 2.0*Math.PI;

    public ArcMotion(double[] change, double[] center, double vel, ArcMotion.DIRECTION dir) throws Exception {
        super(change, vel);

        this.centerOffset = center; // should be non zero for arc motion
        this.direction = dir;

        if(this.centerOffset != null){
            if(this.centerOffset.length != 2) {
                throw new Exception("Arc center offset's X & Y coordinates needed only");
            }
            this.Radius = Math.sqrt(this.centerOffset[0]*this.centerOffset[0] + this.centerOffset[1]*this.centerOffset[1]);
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
        }

        this.wayLengthXY = this.Radius*this.angle;
        this.wayLength = Math.sqrt(this.positionChange[2]*this.positionChange[2]
                + this.wayLengthXY*this.wayLengthXY);

        if( this.wayLength <= 0.0)
            throw new Exception("Null motion not supported");

        this.velocityXY = this.velocity*this.velocityXY/this.wayLength;

        this.duration = this.wayLength/this.velocity;
        this.nTicks = (int)(this.duration* MotionController.getProcessorFrequency());
        double dL = this.velocity/ MotionController.getProcessorFrequency();

        this.Kz = this.positionChange[2]/this.wayLength;

    }

    @Override
    void prepare() {

    }

    @Override
    void onFastTimerTick() {

    }

    @Override
    public double[] paint(Graphics g, double[] fromPoint) {
        return new double[0];
    }

    public enum DIRECTION {
        CW, // clockwise
        CCW // counterclockwise
    }
}
