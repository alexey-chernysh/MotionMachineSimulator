/*
 * Copyright (c) 2017. Alexey Chernysh, Russia, Krasnoyarsk
 */

package motionmachinesimulator.Processor;

import motionmachinesimulator.LongInt.TrigonometricInt;
import motionmachinesimulator.Views.TrajectoryView;

import java.awt.*;

public class CNCMotionStraight extends CNCMotion {

    //general vars
    private final long Kx;
    private final long Ky;

    CNCMotionStraight(CNCPoint relEndPoint,
                      MOTION_TYPE type,
                      double startVel,
                      double endVel) throws Exception {
        super(relEndPoint, type, startVel, endVel);

        Kx = (long)((((double)relativeEndPoint.getX())/wayLength)* TrigonometricInt.scale);
        Ky = (long)((((double)relativeEndPoint.getY())/wayLength)* TrigonometricInt.scale);

        System.out.print("CNCMotionStraight:");
        System.out.print(" dX = " + this.relativeEndPoint.getXinMeters());
        System.out.print(" dY = " + this.relativeEndPoint.getYinMeters());
        System.out.println(" wayLength = " + this.wayLength);
    }

    @Override
    long calcWayLength() {
        return relativeEndPoint.getDistance();
    }

    @Override
    public CNCPoint paint(Graphics g, CNCPoint fromPoint) {
        try {
            double phase = ((double)this.wayLengthCurrent)/this.wayLength;
            CNCPoint innerPoint = relativeEndPoint.mul(phase).add(fromPoint);
            CNCPoint endPoint = relativeEndPoint.add(fromPoint);
            int[] p1 = TrajectoryView.transfer(fromPoint);
            int[] p2 = TrajectoryView.transfer(innerPoint);
            int[] p3 = TrajectoryView.transfer(endPoint);
            g.setColor(TrajectoryView.color1);
            g.drawLine(p1[0],p1[1],p2[0],p2[1]);
            g.setColor(TrajectoryView.color2);
            g.drawLine(p2[0],p2[1],p3[0],p3[1]);
            return endPoint;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    void onFastTimerTick(long wayLengthCurrent_) {
        currentRelativePosition.setX((wayLengthCurrent_ * Kx)>> TrigonometricInt.shift);
        currentRelativePosition.setY((wayLengthCurrent_ * Ky)>> TrigonometricInt.shift);
    }
}
