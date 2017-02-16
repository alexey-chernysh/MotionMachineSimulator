/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package motionmachinesimulator.Processor;

/**
 * @author alexey
 */

class StraightMotion extends Motion {

    //general vars
    private double[] K = new double[Motion.DIM];

    public StraightMotion(double[] change, double vel) throws Exception {
        super(change, vel);

        this.wayLengthXY = Math.sqrt(this.positionChange[0]*this.positionChange[0] +
                                        +this.positionChange[1]*this.positionChange[1]);
        this.wayLength = Math.sqrt(this.positionChange[2]*this.positionChange[2]
                                 + this.wayLengthXY*this.wayLengthXY);

        if( this.wayLength <= 0.0)
            throw new Exception("Null motion not supported");

        for(int i = 0; i< DIM; i++)
            this.K[i] = this.positionChange[i]/this.wayLength;

    }

    @Override
    void execute() {

    }
}
