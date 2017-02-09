/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package motionmachinesimulator.Processor;

/**
 * @author alexey
 */
abstract class Motion {
    
    protected double[] positionChange;
    protected double length; // metric path length of motion
    private double velocity;
    
    public Motion(double[] change, double vel){
        this.positionChange = change;
        this.velocity = vel;
    }
    
    public double getVelocity(){ return this.velocity; };
    public double getVelocityPerSec(){ return this.velocity/60.0; };
    
    abstract void setLength();
    public double getLength(){
        return this.length;
    }

    abstract void execute();
}
