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

public class StraightMotion extends Motion {

    //general vars
    private double[] K = new double[ControllerSettings.DIM];

    StraightMotion(double[] change) throws Exception {
        super(change);

        this.wayLengthXY = Math.sqrt(this.positionChange[0]*this.positionChange[0] +
                                        +this.positionChange[1]*this.positionChange[1]);
        this.wayLength = Math.sqrt(this.positionChange[2]*this.positionChange[2]
                                 + this.wayLengthXY*this.wayLengthXY);

        if( this.wayLength <= 0.0)
            throw new Exception("Null motion not supported");

        for(int i = 0; i< ControllerSettings.DIM; i++)
            this.K[i] = this.positionChange[i]/this.wayLength;

        System.out.println("StraightMotion: ");
        System.out.println("wayLengthXY = " + this.wayLengthXY);
        System.out.println("wayLength = " + this.wayLength);
    }

    @Override
    public double[] paint(Graphics g, double[] fromPoint) {
        try {
            double[] innerPoint = new double[ControllerSettings.DIM];
            double[]   endPoint = new double[ControllerSettings.DIM];
            double phase = this.getPhase();
            for (int i = 0; i< ControllerSettings.DIM; i++) {
                double change = this.positionChange[i];
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
//            System.out.println("Start = " + fromPoint[0] + ", " + fromPoint[1]
//                    + " Inner = " + innerPoint[0] + ", " + innerPoint[1]
//                    + " End = " + endPoint[0] + ", " + endPoint[1]);
            return endPoint;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    void onPositionChange() {
        for(int i = 0; i< ControllerSettings.DIM; i++){
            this.currentRelativePosition[i] = this.currentWayLength*this.K[i];
        }
    }
}
