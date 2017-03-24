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

    public ArcMotion(double[] change, double[] center, ArcMotion.DIRECTION dir) throws Exception {
        super(change);

        this.centerOffset = center; // should be non zero for arc motion
        this.direction = dir;

        if(this.centerOffset != null){
            if(this.centerOffset.length != ControllerSettings.DIM) {
                throw new Exception("Arc center offset's X & Y coordinates needed only");
            }
            this.radius = Math.sqrt(this.centerOffset[0]*this.centerOffset[0] + this.centerOffset[1]*this.centerOffset[1]);
            if(this.radius <= 0.0) throw new Exception("Zero radius arc not supported");
            this.startAngle = Math.atan2(this.centerOffset[1],this.centerOffset[0]);
            this.currentAngle = this.startAngle;
            this.endAngle = Math.atan2(this.positionChange[1],this.positionChange[0]);
            this.angle = this.endAngle - this.startAngle;
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

        this.wayLengthXY = this.radius *this.angle;
        this.wayLength = Math.sqrt(this.positionChange[2]*this.positionChange[2]
                + this.wayLengthXY*this.wayLengthXY);

        if( this.wayLength <= 0.0)
            throw new Exception("Null motion not supported");

        this.Kz = this.positionChange[2]/this.wayLength;

    }

    @Override
    void onPositionChange() {
        double angleChange = this.currentWayLength/this.radius;
        if(this.direction == DIRECTION.CCW) angleChange = - angleChange;
        this.currentAngle = this.startAngle + angleChange;
        this.currentRelativePosition[0] = this.centerOffset[0] + this.radius * Math.cos(this.currentAngle);
        this.currentRelativePosition[1] = this.centerOffset[1] + this.radius * Math.sin(this.currentAngle);
        this.currentRelativePosition[2] = this.currentWayLength * this.Kz;
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
                endPoint[i]  = fromPoint[i] + positionChange[i];
            }
            double angleChange = this.currentWayLength/this.radius;
            if(this.direction == DIRECTION.CCW) angleChange = - angleChange;
            int[] p1 = TrajectoryView.transfer(tmpPoint1);
            int[] p2 = TrajectoryView.transfer(tmpPoint2);
            int x1 = Math.min(p1[0],p2[0]);
            int y1 = Math.min(p1[1],p2[1]);
            int x2 = Math.max(p1[0],p2[0]) - x1;
            int y2 = Math.max(p1[1],p2[1]) - y1;
            g.setColor(TrajectoryView.color1);
            g.drawArc(x1, y1, x2, y2,
                    (int)(180.0*startAngle/Math.PI),
                    (int)(180.0*angleChange/Math.PI));
            g.setColor(TrajectoryView.color2);
            g.drawArc(x1, y1, x2, y2,
                    (int)(180.0*(startAngle+angleChange)/Math.PI),
                    (int)(180.0*(angle-angleChange)/Math.PI));
            return endPoint;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public enum DIRECTION {
        CW, // clockwise
        CCW // counterclockwise
    }
}
