/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package motionmachinesimulator.Processor;

import motionmachinesimulator.Views.TrajectoryView;

import java.awt.*;

/**
 * @author alexey
 */

public class CNCMotionStraight extends CNCMotion {

    //general vars
    private double Kx;
    private double Ky;

    CNCMotionStraight(CNCPoint2D change,
                      MOTION_TYPE type,
                      double startVel,
                      double endVel) throws Exception {
        super(change, type, startVel, endVel);

        this.wayLength = change.distance();

        if( this.wayLength <= 0.0)
            throw new Exception("Null motion not supported");

        this.Kx = this.relativeEndPoint.x/this.wayLength;
        this.Ky = this.relativeEndPoint.y/this.wayLength;

        System.out.print("CNCMotionStraight:");
        System.out.print(" dX = " + this.relativeEndPoint.x);
        System.out.print(" dY = " + this.relativeEndPoint.y);
        System.out.println(" wayLength = " + this.wayLength);
    }

    @Override
    public CNCPoint2D paint(Graphics g, CNCPoint2D fromPoint) {
        try {
            double phase = this.wayLengthCurrent /this.wayLength;
            CNCPoint2D innerPoint = relativeEndPoint.mul(phase).add(fromPoint);
            CNCPoint2D   endPoint = relativeEndPoint.add(fromPoint);;
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
    CNCPoint2DInt onFastTimerTick(double dl) {
        this.wayLengthCurrent += dl;
        this.currentRelativePosition.x = this.wayLengthCurrent * this.Kx;
        this.currentRelativePosition.y = this.wayLengthCurrent * this.Ky;
        return this.currentRelativePosition;
    }
}
