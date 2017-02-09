/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package motionmachinesimulator.Processor;

/**
 *
 * @author alexey
 */
public class StraightMotion extends Motion {
    
    private int[] intScale; // normalized int scale factor. 
                            // abs(intScale) = Integer.MAX_VALUE;
    private int lengthIncrement;
    
    public StraightMotion(double[] change, double vel){
        super(change, vel);
        this.setLength();
        this.setScales();
        long motionDuration = (long)(ProcessorSettings.fpgaClock*this.length/this.getVelocityPerSec());
        lengthIncrement = (int)(ProcessorSettings.stepPulseBitMask*this.length/motionDuration);
        // check
        long tmp1 = (motionDuration*lengthIncrement)>>(ProcessorSettings.stepPulseBitNumber-1);
    }
    
    @Override
    public void setLength(){
        double result = 0.0d;
        if(this.positionChange != null){
            for(double tmp:this.positionChange )
                result += tmp * tmp;
        }
        
        this.length = Math.sqrt(result);
    }
    
    private void setScales(){
        if(this.positionChange != null){
            int N = this.positionChange.length;
            for(int i = 0; i<N; i++)
                if(this.length > 0.0d){
                    this.intScale[i] = (int)(Integer.MAX_VALUE * this.positionChange[i]/length);
                }
        }
    }

    @Override
    void execute() {
        long currentLength = 0L;
        long time = 0;
/*
        while(currentLength <= longLength){

            time++;
            currentLength += lengthIncrement;
        }
*/
    }
    
}
