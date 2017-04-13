/*
 * Copyright (c) 2017. Alexey Chernysh, Russia, Krasnoyarsk
 */

package motionmachinesimulator.Processor;

import motionmachinesimulator.Views.TrajectoryView;

import java.awt.*;

public class CNCMotionArc extends CNCMotion {


    // arc params
    private CNCPoint2D centerOffset;
    private CNCMotionArc.DIRECTION direction;

    //arc vars
    private double radius;
    private double        angle;
    private double   startAngle;
    private double     endAngle;
    private double currentAngle;

    private static final double twoPi = 2.0*Math.PI;

    public CNCMotionArc(CNCPoint2D change,
                        CNCPoint2D center,
                        CNCMotionArc.DIRECTION dir,
                        MOTION_TYPE type,
                        double startVel,
                        double endVel) throws Exception {
        super(change, type, startVel, endVel);

        this.centerOffset = center; // should be non zero for arc motion
        this.direction = dir;

        if(this.centerOffset != null){
            this.radius = this.centerOffset.distance();
            if(this.radius <= 0.0) throw new Exception("Zero radius arc not supported");
            this.startAngle = Math.atan2(-this.centerOffset.y,-this.centerOffset.x);
            this.currentAngle = this.startAngle;
            this.endAngle = Math.atan2(this.relativeEndPoint.y - this.centerOffset.y,
                                       this.relativeEndPoint.x - this.centerOffset.x);
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

        this.wayLength = Math.abs(this.radius * this.angle);

        if( this.wayLength <= 0.0)
            throw new Exception("Null motion not supported");

        System.out.print("CNCMotionArc:");
        System.out.print(" dX = " + this.relativeEndPoint.x);
        System.out.print(" dY = " + this.relativeEndPoint.y);
        System.out.print(" startAngle = " + this.startAngle);
        System.out.print(" endAngle = " + this.endAngle);
        System.out.print(" angle = " + this.angle);
        System.out.println(" wayLength = " + this.wayLength);
    }

    @Override
    CNCPoint2D onFastTimerTick(double dl) {
        this.currentWayLength += dl;
        double angleChange = this.currentWayLength/this.radius;
        if(this.direction == DIRECTION.CW) angleChange = - angleChange;
        this.currentAngle = this.startAngle + angleChange;
        this.currentRelativePosition.x = this.centerOffset.x + this.radius * Math.cos(this.currentAngle);
        this.currentRelativePosition.y = this.centerOffset.y + this.radius * Math.sin(this.currentAngle);
        return this.currentRelativePosition;
    }

    @Override
    public CNCPoint2D paint(Graphics g, CNCPoint2D fromPoint) {
        try {
            CNCPoint2D radiusOffset = new CNCPoint2D(radius, radius);
            CNCPoint2D  leftBottomPoint = fromPoint.add(centerOffset).sub(radiusOffset);
            CNCPoint2D  rightTopPoint = fromPoint.add(centerOffset).add(radiusOffset);

            CNCPoint2D  endPoint  = fromPoint.add(relativeEndPoint);

            double angleChange = this.currentWayLength/this.radius;
            if(this.direction == DIRECTION.CW) angleChange = - angleChange;
            int[] p1 = TrajectoryView.transfer(leftBottomPoint);
            int[] p2 = TrajectoryView.transfer(rightTopPoint);
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
