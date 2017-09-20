/*
 * Copyright (c) 2017. Alexey Chernysh, Russia, Krasnoyarsk
 */

package motionmachinesimulator.Processor;

import motionmachinesimulator.LongInt.TrigonometricInt;
import motionmachinesimulator.Views.TrajectoryView;

import java.awt.*;

import static motionmachinesimulator.LongInt.TrigonometricInt.cosInt9;
import static motionmachinesimulator.LongInt.TrigonometricInt.sinInt9;

public class CNCMotionArc extends CNCMotion {

    // arc params
    private CNCPoint centerOffset;
    private CNCMotionArc.DIRECTION direction;

    //arc vars
    private final long radiusLong;
    private final long oneDividedByRadiusScaled;

    private final double angle;
    private final long angleScaled;
    private final long startAngleScaled;
    private long currentAngleScaled;

    private static final double twoPi = 2.0*Math.PI;

    // TODO добавить ограничене на максимальную скорость в зависимости от максимального ускоения a = v*v/r => vmax = sqrt(amax*r)

    CNCMotionArc(CNCPoint change,
                 CNCPoint center,
                 CNCMotionArc.DIRECTION dir,
                 MOTION_TYPE type,
                 double startVel,
                 double endVel) throws Exception {
        super(change, type, startVel, endVel);

        centerOffset = center; // should be non zero for arc motion
        direction = dir;

        double startAngle;
        double   endAngle;

        if(this.centerOffset != null){
            radiusLong = this.centerOffset.getDistance();
            if(radiusLong <= 0) throw new Exception("Zero radius arc not supported");
            oneDividedByRadiusScaled = (long)(TrigonometricInt.scale/CNCScaler.long2double(radiusLong));
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

        angleScaled = TrigonometricInt.getLongFromDoubleAngle(angle);
        startAngleScaled = TrigonometricInt.getLongFromDoubleAngle(startAngle);
        currentAngleScaled = startAngleScaled;

        System.out.print("CNCMotionArc:");
        System.out.print(" dX = " + this.relativeEndPoint.getXinMeters());
        System.out.print(" dY = " + this.relativeEndPoint.getYinMeters());
        System.out.print(" startAngle = " + startAngle);
        System.out.print(" endAngle = " + endAngle);
        System.out.print(" angle = " + angle);
        System.out.println(" wayLength = " + this.wayLength);
    }

    @Override
    long calcWayLength() {
        return (long)(radiusLong * Math.abs(angle));
    }

    @Override
    void onFastTimerTick(long wayLengthCurrent_) {
        long angleChange = (wayLengthCurrent_*oneDividedByRadiusScaled)>>CNCScaler.shift;
        if(direction == DIRECTION.CW) angleChange = - angleChange;
        currentAngleScaled = startAngleScaled + angleChange;
        // TODO temporary variables should be removed
        long tmpX2 = (radiusLong * cosInt9(currentAngleScaled)) >> TrigonometricInt.shift;
        currentRelativePosition.setX(centerOffset.getX() + tmpX2);
        long tmpY2 = (radiusLong * sinInt9(currentAngleScaled)) >> TrigonometricInt.shift;
        currentRelativePosition.setY(centerOffset.getY() + tmpY2);
    }

    @Override
    public CNCPoint paint(Graphics g, CNCPoint fromPoint) {
        try {
            CNCPoint radiusOffset = new CNCPoint(radiusLong, radiusLong);
            CNCPoint leftBottomPoint = fromPoint.add(centerOffset).sub(radiusOffset);
            CNCPoint rightTopPoint = fromPoint.add(centerOffset).add(radiusOffset);
            CNCPoint endPoint  = fromPoint.add(relativeEndPoint);

            int[] p1 = TrajectoryView.transfer(leftBottomPoint);
            int[] p2 = TrajectoryView.transfer(rightTopPoint);

            double phase = ((double)this.wayLengthCurrent)/this.wayLength;
            double angleChange = phase*(angleScaled/ TrigonometricInt.doubleScale);
            if(this.direction == DIRECTION.CCW) angleChange = - angleChange;
            int x1 = Math.min(p1[0],p2[0]);
            int y1 = Math.min(p1[1],p2[1]);
            int x2 = Math.max(p1[0],p2[0]) - x1;
            int y2 = Math.max(p1[1],p2[1]) - y1;
            g.setColor(TrajectoryView.color1);
            int angle1 = rad2grad(startAngleScaled/ TrigonometricInt.doubleScale);
            int angle2 = rad2grad(angleChange);
            g.drawArc(x1, y1, x2, y2, angle1, angle2);
            g.setColor(TrajectoryView.color2);
            int angle3 = rad2grad(startAngleScaled/ TrigonometricInt.doubleScale+angleChange);
            int angle4 = rad2grad(angleScaled/ TrigonometricInt.doubleScale-angleChange);
            g.drawArc(x1, y1, x2, y2, angle3, angle4);
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
