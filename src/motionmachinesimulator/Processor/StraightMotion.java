/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package motionmachinesimulator.Processor;

import java.awt.*;

/**
 * @author alexey
 */

class StraightMotion extends Motion {

    //general vars
    private double[] K = new double[ProcessorSettings.DIM];
    private double dL;
    private double currentWayLength;

    public StraightMotion(double[] change, double vel) throws Exception {
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
        this.nTicks = (int)(this.duration*MotionProcess.getProcessorFrequency());
        System.out.println("nTicks = " + this.nTicks);
        this.dL = this.velocity/MotionProcess.getProcessorFrequency();
        System.out.println("dL = " + this.dL);

        for(int i = 0; i< ProcessorSettings.DIM; i++)
            this.K[i] = this.positionChange[i]/this.wayLength;

    }

    @Override
    void paint(Graphics g) {

    }

    @Override
    public void run() {
        final double[] startPosition = MotionProcess.getCurrentPosition();
        this.currentWayLength = 0.0;
        double[] reachedPosition = new double[ProcessorSettings.DIM];
        for(int i=0; i<ProcessorSettings.DIM; i++)
            reachedPosition[i] = 0;
        while (this.currentWayLength < this.wayLength){
            this.currentWayLength += dL;
            for(int i=0; i<ProcessorSettings.DIM; i++)
                reachedPosition[i] = startPosition[i] + this.currentWayLength*this.K[i];
            MotionProcess.setCurrentPosition(reachedPosition);
        }
    }
}
