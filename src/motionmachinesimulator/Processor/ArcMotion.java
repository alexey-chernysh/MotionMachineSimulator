package motionmachinesimulator.Processor;

import motionmachinesimulator.Views.TrajectoryView;

import java.awt.*;

public class ArcMotion extends Motion {


    // arc params
    private double[] centerOffset;
    private ArcMotion.DIRECTION direction;

    //arc vars
    private double radius;
    private double        angle;
    private double   startAngle;
    private double     endAngle;
    private double currentAngle;

    //general vars
    protected double Kz;

    private static final double twoPi = 2.0*Math.PI;

    public ArcMotion(double[] change,
                     double[] center,
                     ArcMotion.DIRECTION dir,
                     MOTION_TYPE type,
                     double startVel,
                     double endVel) throws Exception {
        super(change, type, startVel, endVel);

        this.centerOffset = center; // should be non zero for arc motion
        this.direction = dir;

        if(this.centerOffset != null){
            if(this.centerOffset.length != ControllerSettings.DIM) {
                throw new Exception("Arc center offset's X & Y coordinates needed only");
            }
            this.radius = Math.sqrt(this.centerOffset[0]*this.centerOffset[0] + this.centerOffset[1]*this.centerOffset[1]);
            if(this.radius <= 0.0) throw new Exception("Zero radius arc not supported");
            this.startAngle = Math.atan2(-this.centerOffset[1],-this.centerOffset[0]);
            this.currentAngle = this.startAngle;
            this.endAngle = Math.atan2(this.relativeEndPoint[1]-this.centerOffset[1],this.relativeEndPoint[0]-this.centerOffset[0]);
            switch (this.direction){
                case CW:
                    while(this.endAngle >= this.startAngle ) this.endAngle -= twoPi;
                    while((this.startAngle - this.endAngle) > twoPi ) this.endAngle += twoPi;
                    break;
                case CCW:
                    while(this.endAngle <= this.startAngle ) this.endAngle += twoPi;
                    while((this.endAngle - this.startAngle) > twoPi ) this.endAngle -= twoPi;
                    break;
                default:
                    throw new Exception("Unsupported arc direction");
            }
            this.angle = this.endAngle - this.startAngle;
        }

        this.wayLengthXY = this.radius *this.angle;
        this.wayLength = Math.sqrt(this.relativeEndPoint[2]*this.relativeEndPoint[2]
                + this.wayLengthXY*this.wayLengthXY);

        if( this.wayLength <= 0.0)
            throw new Exception("Null motion not supported");

        this.Kz = this.relativeEndPoint[2]/this.wayLength;

        System.out.print("ArcMotion:");
        System.out.print(" dX = " + this.relativeEndPoint[0]);
        System.out.print(" dY = " + this.relativeEndPoint[1]);
        System.out.print(" startAngle = " + this.startAngle);
        System.out.print(" endAngle = " + this.endAngle);
        System.out.print(" angle = " + this.angle);
        System.out.println(" wayLength = " + this.wayLength);
    }

    @Override
    double[] onFastTimerTick(double dl) {
        this.currentWayLength += dl;
        double angleChange = this.currentWayLength/this.radius;
        if(this.direction == DIRECTION.CW) angleChange = - angleChange;
        this.currentAngle = this.startAngle + angleChange;
        this.currentRelativePosition[0] = this.centerOffset[0] + this.radius * Math.cos(this.currentAngle);
        this.currentRelativePosition[1] = this.centerOffset[1] + this.radius * Math.sin(this.currentAngle);
        this.currentRelativePosition[2] = this.currentWayLength * this.Kz;
        return this.currentRelativePosition;
    }

    @Override
    public double[] paint(Graphics g, double[] fromPoint) {
        try {
            double[]  tmpPoint1 = new double[ControllerSettings.DIM];
            double[]  tmpPoint2 = new double[ControllerSettings.DIM];
            double[]  endPoint  = new double[ControllerSettings.DIM];
            for (int i = 0; i< ControllerSettings.DIM; i++) {
                tmpPoint1[i] = fromPoint[i] + centerOffset[i] - radius;
                tmpPoint2[i] = fromPoint[i] + centerOffset[i] + radius;
                endPoint[i]  = fromPoint[i] + relativeEndPoint[i];
            }
            double angleChange = this.currentWayLength/this.radius;
            if(this.direction == DIRECTION.CW) angleChange = - angleChange;
            int[] p1 = TrajectoryView.transfer(tmpPoint1);
            int[] p2 = TrajectoryView.transfer(tmpPoint2);
            int x1 = Math.min(p1[0],p2[0]);
            int y1 = Math.min(p1[1],p2[1]);
            int x2 = Math.max(p1[0],p2[0]) - x1;
            int y2 = Math.max(p1[1],p2[1]) - y1;
            g.setColor(TrajectoryView.color1);
            g.drawArc(x1, y1, x2, y2, rad2grad(startAngle), rad2grad(angleChange));
            g.setColor(TrajectoryView.color2);
            g.drawArc(x1, y1, x2, y2, rad2grad(startAngle+angleChange), rad2grad(angle-angleChange));
            return endPoint;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private int rad2grad(double rad){ return (int)(180.0*rad/ Math.PI);}

    public enum DIRECTION {
        CW, // clockwise
        CCW // counterclockwise
    }
}
