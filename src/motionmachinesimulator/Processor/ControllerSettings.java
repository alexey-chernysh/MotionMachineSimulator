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
public class ControllerSettings {

    public static final int DIM = 3;

    public static final double maxVelocityRequired = 30000; // 30 m/min
    public static final double fpgaClock = 50000000; // 50MHz
    public static final double maxPulseFrequency = 200000; // 200KHz
    public static final double freqRatio = fpgaClock/maxPulseFrequency;

    public static final double rackModule = 3.14; // mm/tooth
    public static final int toothPerRevolution = 15;
    public static final double mmPerGearRevolution = rackModule*toothPerRevolution;
    public static final double gearBoxRatio = 5.0; // no gear box
    public static final double mmPerMotorRevolution = mmPerGearRevolution/gearBoxRatio;

    public static final int stepPerMotorRevolution = 200; // 2-pole step motor
    public static final int microStepNumber = 20;
    public static final int microstepPerMotorRevolution 
            = stepPerMotorRevolution*microStepNumber;

    public static final double stepSize // mm per mirostep
            = mmPerMotorRevolution/microstepPerMotorRevolution;
    public static final double maxVelocityReachable = 60*stepSize*maxPulseFrequency/1000.0;
    
    public static final int stepPulseBitNumber = 33;
    public static final long stepPulseBitMask = 1L<<(stepPulseBitNumber-1);
    
    public static final short NofAxis = 3;
    
    public static final String gear_state = 
             "MM per motor shaft revolution = "
             + ControllerSettings.mmPerMotorRevolution
             + ";  MicroStep per motor shaft revolution = "
             + ControllerSettings.microstepPerMotorRevolution ;

    public static final String step_state =
                            "MicroStep size = "
                            + ControllerSettings.stepSize
                            + " mm, Step size = "
                            + ControllerSettings.stepSize* ControllerSettings.microStepNumber
                            + " mm";

    public static final String accuracy_state =
             " Max veloity = "
             + ControllerSettings.maxVelocityReachable
             + " m/min";

}
