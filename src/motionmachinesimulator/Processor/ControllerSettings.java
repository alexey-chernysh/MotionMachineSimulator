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

    private static double startVelocity = 0.1/60.0; // m/sec 100 mm in  min
    public static double getStartVelocity(){ return startVelocity; }

    private static double freeRunVelocity = 5.0/60.0; // m/sec for 5.0 m/min
    public static long getStepSizeFreeRun() {
        return getStep4Velocity(freeRunVelocity);
    }
    public static double getFreeRunVelocity() {
        return freeRunVelocity;
    }

    private static double workingVelocity = 1.0/60.0; // m/sec for 1.0 m/min
    public static void setWorkingVelocity(double newValue){ workingVelocity = newValue;}
    public static double getWorkingVelocity() {
        return workingVelocity;
    }
    public static long getStepSizeWorking() {
        return getStep4Velocity(workingVelocity);
    }
    public static double getWorkingVelocityMMinMin() {
        double velMeterPerSec = getWorkingVelocity();
        return velMeterPerSec*60*1000; // mm in min
    }

    public static long getStepSize(CNCMotion.MOTION_TYPE motion_type) {
        switch (motion_type){
            case FREE_RUN: return getStepSizeFreeRun();
            case WORKING: return getStepSizeWorking();
            default: return 0;
        }
    }
    private static long currentStepSIze = 0;
    public static void setCurrentStepSIze(long currentStepSIze) {
        ControllerSettings.currentStepSIze = currentStepSIze;
    }

    public static double getCurrentVelocity() {
        return CNCScaler.long2double(currentStepSIze)/intervalInSec;
    }
    public static double getCurrentVelocityMMinMin() {
        double velMeterPerSec = ControllerSettings.getCurrentVelocity();
        return velMeterPerSec * 60 * 1000; // mm in min
    }

    private static double acceleration = 0.015; // m/sec/sec
    public static double getAcceleration() {
        return acceleration;
    }
    public static void setAcceleration(double value) {
        acceleration = value;
    }
    public static long getStepIncrement4Acceleration(){
        double velocityIncrement = acceleration * intervalInSec;
        return CNCScaler.double2long(velocityIncrement*intervalInSec);
    }

    public static long getStep4Velocity(double velocity){
        return CNCScaler.double2long(velocity*intervalInSec);
    }

    public static long getWayLength4StepChange(long stepSize1, long stepSize2) {
        boolean isAccereting = (stepSize2 >= stepSize1);
        long stepIncrement = getStepIncrement4Acceleration();
        long result = 0;
        long stepSize = stepSize1;
        if(isAccereting){
            while (stepSize < stepSize2){
                result += stepSize;
                stepSize += stepIncrement;
            }
        } else {
            while (stepSize > stepSize2){
                result += stepSize;
                stepSize -= stepIncrement;
            }
        }
        return result;
    }
}
