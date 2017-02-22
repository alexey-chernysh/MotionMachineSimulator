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
    private double dL;

    StraightMotion(double[] change, double vel) throws Exception {
        super(change, vel);
        System.out.println("StraightMotion: ");

        this.wayLengthXY = Math.sqrt(this.positionChange[0]*this.positionChange[0] +
                                        +this.positionChange[1]*this.positionChange[1]);
        System.out.println("wayLengthXY = " + this.wayLengthXY);
        this.wayLength = Math.sqrt(this.positionChange[2]*this.positionChange[2]
                                 + this.wayLengthXY*this.wayLengthXY);
        System.out.println("wayLength = " + this.wayLength);

        if( this.wayLength <= 0.0)
            throw new Exception("Null motion not supported");

        this.velocityXY = this.velocity*this.velocityXY/this.wayLength;
        System.out.println("velocityXY = " + this.velocityXY);

        this.duration = this.wayLength/this.velocity;
        System.out.println("duration = " + this.duration);
        this.nTicks = (int)(this.duration* MotionController.getProcessorFrequency());
        System.out.println("nTicks = " + this.nTicks);
        this.dL = this.velocity/ MotionController.getProcessorFrequency();
        System.out.println("dL = " + this.dL);

        for(int i = 0; i< ControllerSettings.DIM; i++)
            this.K[i] = this.positionChange[i]/this.wayLength;

    }

    @Override
    public double[] paint(Graphics g, double[] fromPoint) {
        try {
            int[] p1 = TrajectoryView.transfer(fromPoint);
            double[] innerPoint = new double[ControllerSettings.DIM];
            for (int i = 0; i< ControllerSettings.DIM; i++) {
                innerPoint[i] = fromPoint[i] + this.getPhase()*this.positionChange[i];
            }
            int[] p2 = TrajectoryView.transfer(innerPoint);
            double[] endPoint = new double[ControllerSettings.DIM];
            for (int i = 0; i< ControllerSettings.DIM; i++) {
                endPoint[i] = fromPoint[i] + this.positionChange[i];
            }
            int[] p3 = TrajectoryView.transfer(endPoint);
            g.setColor(TrajectoryView.color1);
            g.drawLine(p1[0],p1[1],p2[0],p2[1]);
            g.setColor(TrajectoryView.color2);
            g.drawLine(p2[0],p2[1],p3[0],p3[1]);
            return endPoint;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void run() {
        final double[] startPosition = MotionController.getCurrentPosition();
        this.currentWayLength = 0.0;
        double[] reachedPosition = new double[ControllerSettings.DIM];
        for(int i = 0; i< ControllerSettings.DIM; i++)
            reachedPosition[i] = 0;
        while (this.currentWayLength < this.wayLength){
            this.currentWayLength += dL;
            for(int i = 0; i< ControllerSettings.DIM; i++)
                reachedPosition[i] = startPosition[i] + this.currentWayLength*this.K[i];
            MotionController.setCurrentPosition(reachedPosition);
        }
    }
}
