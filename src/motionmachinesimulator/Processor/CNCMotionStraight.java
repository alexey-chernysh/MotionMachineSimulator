/*
 * Copyright (c) 2017. Alexey Chernysh, Russia, Krasnoyarsk
 */

package motionmachinesimulator.Processor;

import motionmachinesimulator.LongInt.Trigonometric;
import motionmachinesimulator.Views.TrajectoryView;

import java.awt.*;

public class CNCMotionStraight extends CNCMotion {

    //general vars
    private long Kx;
    private long Ky;

    CNCMotionStraight(CNCPoint change,
                      MOTION_TYPE type,
                      double startVel,
                      double endVel) throws Exception {
        super(change, type, startVel, endVel);

        this.wayLength = change.getDistance();

        if( this.wayLength <= 0.0)
            throw new Exception("Null motion not supported");

        Kx = (long)((((double)relativeEndPoint.x)/wayLength)* Trigonometric.scale);
        Ky = (long)((((double)relativeEndPoint.x)/wayLength)* Trigonometric.scale);

        System.out.print("CNCMotionStraight:");
        System.out.print(" dX = " + this.relativeEndPoint.x);
        System.out.print(" dY = " + this.relativeEndPoint.y);
        System.out.println(" wayLength = " + this.wayLength);
    }

    @Override
    public CNCPoint paint(Graphics g, CNCPoint fromPoint) {
        try {
            double phase = ((double)this.wayLengthCurrent)/this.wayLength;
            CNCPoint innerPoint = relativeEndPoint.mul(phase).add(fromPoint);
            CNCPoint endPoint = relativeEndPoint.add(fromPoint);;
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
    void onFastTimerTick(long dl) {
        wayLengthCurrent += dl;
        currentRelativePosition.x = (wayLengthCurrent * Kx)>>Trigonometric.shift;
        currentRelativePosition.y = (wayLengthCurrent * Ky)>>Trigonometric.shift;
    }
}
