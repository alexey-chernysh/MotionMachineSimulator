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
    private double[] K = new double[ControllerSettings.DIM];

    CNCMotionStraight(double[] change,
                      MOTION_TYPE type,
                      double startVel,
                      double endVel) throws Exception {
        super(change, type, startVel, endVel);

        this.wayLengthXY = Math.sqrt(this.relativeEndPoint[0]*this.relativeEndPoint[0] +
                                        +this.relativeEndPoint[1]*this.relativeEndPoint[1]);
        this.wayLength = Math.sqrt(this.relativeEndPoint[2]*this.relativeEndPoint[2]
                                 + this.wayLengthXY*this.wayLengthXY);

        if( this.wayLength <= 0.0)
            throw new Exception("Null motion not supported");

        for(int i = 0; i< ControllerSettings.DIM; i++)
            this.K[i] = this.relativeEndPoint[i]/this.wayLength;

        System.out.print("CNCMotionStraight:");
        System.out.print(" dX = " + this.relativeEndPoint[0]);
        System.out.print(" dY = " + this.relativeEndPoint[1]);
        System.out.println(" wayLength = " + this.wayLength);
    }

    @Override
    public double[] paint(Graphics g, double[] fromPoint) {
        try {
            double[] innerPoint = new double[ControllerSettings.DIM];
            double[]   endPoint = new double[ControllerSettings.DIM];
            double phase = this.currentWayLength/this.wayLength;
            for (int i = 0; i< ControllerSettings.DIM; i++) {
                double change = this.relativeEndPoint[i];
                innerPoint[i] = fromPoint[i] + phase*change;
                endPoint[i] = fromPoint[i] + change;
            }
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
    double[] onFastTimerTick(double dl) {
        this.currentWayLength += dl;
        for(int i = 0; i< ControllerSettings.DIM; i++){
            this.currentRelativePosition[i] = this.currentWayLength * this.K[i];
        }
        return this.currentRelativePosition;
    }
}
