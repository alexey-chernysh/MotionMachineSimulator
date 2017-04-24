/*
 * Copyright (c) 2017. Alexey Chernysh, Russia, Krasnoyarsk
 */

package motionmachinesimulator.Processor;

import motionmachinesimulator.LongInt.Trigonometric;
import motionmachinesimulator.Views.TrajectoryView;

import java.awt.*;

import static motionmachinesimulator.LongInt.Trigonometric.cosInt9;
import static motionmachinesimulator.LongInt.Trigonometric.sinInt9;

public class CNCMotionArc extends CNCMotion {

    // arc params
    private CNCPoint centerOffset;
    private CNCMotionArc.DIRECTION direction;

    //arc vars
    private final double radius;
    private final long   radiusInt;
    private final long   oneDividedByRadiusScaled;
    private double      angle;
    private long        angleScaled;
    private double startAngle;
    private long   startAngleScaled;
    private double   endAngle;
    private long     endAngleScaled;
    private long currentAngleScaled;

    private static final double twoPi = 2.0*Math.PI;

    public CNCMotionArc(CNCPoint change,
                        CNCPoint center,
                        CNCMotionArc.DIRECTION dir,
                        MOTION_TYPE type,
                        double startVel,
                        double endVel) throws Exception {
        super(change, type, startVel, endVel);

        centerOffset = center; // should be non zero for arc motion
        direction = dir;

        if(this.centerOffset != null){
            radiusInt = this.centerOffset.getDistance();
            radius = CNCScaler.long2double(radiusInt);
            if(radius <= 0.0) throw new Exception("Zero radius arc not supported");
            oneDividedByRadiusScaled = (long)(Trigonometric.scale/radius);
            startAngle = Math.atan2(-centerOffset.getY(),-centerOffset.getX());
            endAngle = Math.atan2(relativeEndPoint.getY() - centerOffset.getY(),
                                  relativeEndPoint.getX() - centerOffset.getX());
            switch (direction){
                case CW:
                    while(endAngle >= startAngle ) endAngle -= twoPi;
                    while((startAngle - endAngle) > twoPi ) endAngle += twoPi;
                    break;
                case CCW:
                    while(endAngle <= startAngle ) endAngle += twoPi;
                    while((endAngle - startAngle) > twoPi ) endAngle -= twoPi;
                    break;
                default:
                    throw new Exception("Unsupported arc direction");
            }
            angle = endAngle - startAngle;

        } else throw new Exception("Null radius not supported");

        angleScaled = Trigonometric.getLongFromDoubleAngle(angle);
        startAngleScaled = Trigonometric.getLongFromDoubleAngle(startAngle);
        endAngleScaled = Trigonometric.getLongFromDoubleAngle(endAngle);
        currentAngleScaled = startAngleScaled;

        wayLength = (long)(radiusInt * Math.abs(angle));

        if( wayLength <= 0)
            throw new Exception("Null motion not supported");

        System.out.print("CNCMotionArc:");
        System.out.print(" dX = " + this.relativeEndPoint.getXinMeters());
        System.out.print(" dY = " + this.relativeEndPoint.getYinMeters());
        System.out.print(" startAngle = " + this.startAngle);
        System.out.print(" endAngle = " + this.endAngle);
        System.out.print(" angle = " + this.angle);
        System.out.println(" wayLength = " + this.wayLength);
    }

    @Override
    void onFastTimerTick(long dl) {
        wayLengthCurrent += dl;
        long angleChange = (wayLengthCurrent*oneDividedByRadiusScaled)>>CNCScaler.shift;
        // debug data
        long angleChangeControl = (wayLength*oneDividedByRadiusScaled)>>CNCScaler.shift;
        double check = angleChangeControl/((double)angleScaled);
        if(direction == DIRECTION.CW) angleChange = - angleChange;
        currentAngleScaled = startAngleScaled + angleChange;
        currentRelativePosition.setX((radiusInt * cosInt9(currentAngleScaled))>>Trigonometric.shift - centerOffset.getX());
        currentRelativePosition.setY((radiusInt * sinInt9(currentAngleScaled))>>Trigonometric.shift - centerOffset.getY());
    }

    @Override
    public CNCPoint paint(Graphics g, CNCPoint fromPoint) {
        try {
            CNCPoint radiusOffset = new CNCPoint(radiusInt, radiusInt);
            CNCPoint leftBottomPoint = fromPoint.add(centerOffset).sub(radiusOffset);
            CNCPoint rightTopPoint = fromPoint.add(centerOffset).add(radiusOffset);

            CNCPoint endPoint  = fromPoint.add(relativeEndPoint);

            double phase = ((double)this.wayLengthCurrent)/this.wayLength;
            double angleChange = angle/phase;
            if(this.direction == DIRECTION.CCW) angleChange = - angleChange;
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
