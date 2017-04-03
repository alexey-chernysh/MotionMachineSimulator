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
/*
    public static final double maxVelocityRequired = 30000; // 30 m/min
    public static final double fastTimerClock = 10000000; // 10MHz
    public static final double maxPulseFrequency = 200000; // 200KHz
    public static final double freqRatio = fastTimerClock /maxPulseFrequency;

    public static final double rackModule = 3.14; // module 1 = mm/tooth
    public static final int toothPerRevolution = 15;
    public static final double mmPerGearRevolution = rackModule*toothPerRevolution;
    public static final double gearBoxRatio = 5.0;
    public static final double mmPerMotorRevolution = mmPerGearRevolution/gearBoxRatio;

    public static final int stepPerMotorRevolution = 200; // 2-pole step motor
    public static final int microStepNumber = 20;
    public static final int microStepPerMotorRevolution
            = stepPerMotorRevolution*microStepNumber;

    public static final double stepSize // mm per microstep
            = mmPerMotorRevolution/microStepPerMotorRevolution;

    public static final double maxVelocityReachable = 60*stepSize*maxPulseFrequency/1000.0;

    public static final String gear_state =
            "MM per motor shaft revolution = "
                    + ControllerSettings.mmPerMotorRevolution
                    + ";  MicroStep per motor shaft revolution = "
                    + ControllerSettings.microStepPerMotorRevolution ;

    public static final String step_state =
            "MicroStep size = "
                    + ControllerSettings.stepSize
                    + " mm, Step size = "
                    + ControllerSettings.stepSize* ControllerSettings.microStepNumber
                    + " mm";

    public static final String accuracy_state =
            " Max velocity = "
                    + ControllerSettings.maxVelocityReachable
                    + " m/min";
*/
    public final static int intervalInMillis = 1; // min available
    public final static double intervalInSec = intervalInMillis/1000.0;

    private static double freeRunVelocity = 5.0/60.0; // m/sec for 5.0 m/min
    public static double getFreeRunVelocity() {
        return freeRunVelocity;
    }

    private static double workingVelocity = 1.0/60.0; // m/sec for 1.0 m/min
    public static void setWorkingVelocity(double newValue){ workingVelocity = newValue;}
    public static double getWorkingVelocity() {
        return workingVelocity;
    }
    public static double getStepSizeWorking() {
        return getStep4Velocity(workingVelocity);
    }
    public static double getWorkingVelocityMMinMin() {
        double velMeterPerSec = ControllerSettings.getWorkingVelocity();
        return velMeterPerSec*60*1000; // mm in min
    }

    private static double acceleration = 0.5/60.0; // m/sec/sec
    public static double getAcceleration() {
        return acceleration;
    }
    public static double getStepIncrement4Acceleration(){
        double velocityIncrement = acceleration * intervalInSec;
        return (velocityIncrement*intervalInSec);
    }

    public static final int stepPulseBitNumber = 33;
    public static final long stepPulseBitMask = 1L<<(stepPulseBitNumber-1);

    public static double getStep4Velocity(double velocity){
        return velocity*intervalInSec;
    }

}
