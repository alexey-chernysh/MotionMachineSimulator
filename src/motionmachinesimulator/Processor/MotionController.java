package motionmachinesimulator.Processor;

import java.util.ArrayList;

/**
 * Created by Sales on 16.02.2017.
 * Functions needed to be implemented:
 *  start/resume
 *  stop/pause
 *  direction change (forward/backward)
 *  velocity change (acceleration/deceleration)
 */

public class MotionController extends ControllerState {

    private Task currentTask;
    private Thread controllerThread;
    private boolean forwardDirection = true;
    public EjectFlag ejectFlag;

    private static MotionController ourInstance = new MotionController();

    public static MotionController getInstance() {
        return ourInstance;
    }

    private MotionController() {
        super();
        ejectFlag = new EjectFlag();
        currentTask = new Task();
        controllerThread = new Thread(this);
        controllerThread.start();
    }

    public void resumeForwardExecution() {
        forwardDirection = true;
        checkThresdState();
        this.currentTask.setState(Task.TASK_STATE.ON_THE_RUN);
    }

    public void resumeBackwardExecution() {
        forwardDirection = false;
        checkThresdState();
        this.currentTask.setState(Task.TASK_STATE.ON_THE_RUN);
    }

    private void checkThresdState(){
        if(!controllerThread.isAlive()) {
            controllerThread = new Thread(this);
            controllerThread.start();
        }
    }

    private int intervalInMillis = 1; // min available
    private double stepSize = getStep4Velocity(ControllerSettings.getWorkingVelocity(),
                                              (intervalInMillis/1000.0));

    public void pauseExecution() {
        this.currentTask.setState(Task.TASK_STATE.PAUSED);
    }
    public void velocityUp() {
        stepSize = stepSize * 1.1;
    }
    public void velocityDown() {
        stepSize = stepSize * 0.9;
    }

    @Override
    public void run() {
        final int taskSize = currentTask.size();
        double[][] startPos = new double[taskSize][ControllerSettings.DIM];
        do{
            Motion currentMotion;
            int currentMotionNum = 0;
            while((currentMotionNum>=0)&&(currentMotionNum<taskSize)){
                System.out.println(" Motion num =  " + currentMotionNum);
                currentMotion = currentTask.get(currentMotionNum);
                if(this.forwardDirection) startPos[currentMotionNum] = CurrentPosition.get();
                motionRun(currentMotion, startPos[currentMotionNum]);
                if(this.forwardDirection) {
                    currentMotion.currentWayLength = currentMotion.wayLength;
                    currentMotionNum++;
                } else {
                    currentMotion.currentWayLength = 0.0;
                    currentMotionNum--;
                }
            }
            this.currentTask.setState(Task.TASK_STATE.READY_TO_START);
            currentTask.reset();
        }while(true);
    }

    void motionRun(Motion motion, double[] startPos){
//        double currentStepSize = stepSize;
        double currentDistanceToTarget = Double.MAX_VALUE;
        double[] currentAbsPos = new double[ControllerSettings.DIM];
        do{ // linear velocity phase
            if(ejectFlag.taskShouldBeEjected()) break;
            double[] relPos;
            if(this.currentTask.getState() == Task.TASK_STATE.ON_THE_RUN){
                if(this.forwardDirection) relPos = motion.onFastTimerTick(stepSize);
                else relPos = motion.onFastTimerTick(-stepSize);
                for(int i=0; i<ControllerSettings.DIM;i++){
                    currentAbsPos[i] = startPos[i] + relPos[i];
                }
                CurrentPosition.set(currentAbsPos);
                currentDistanceToTarget = this.forwardDirection ?
                        motion.wayLength - motion.currentWayLength :
                        motion.currentWayLength;
                try {
                    Thread.sleep(intervalInMillis);
                } catch (InterruptedException ie) {
                    ie.printStackTrace();
                }
            } else System.out.print("+");
        } while (Math.abs(currentDistanceToTarget) > stepSize);
    }

    public Task getCurrentTask() {
        return currentTask;
    }

    static double getStep4Velocity(double velocity, double timeInterval){
        return velocity*timeInterval;
    }

    static double getStepIncrement(double acceleration, double timeInterval){
        double velocityIncrement = acceleration * timeInterval;
        return (velocityIncrement*timeInterval);
    }

    public double getCurrentVelocity() {
        double velMeterPerSec = stepSize/(intervalInMillis/1000.0);
        return velMeterPerSec*60*1000; // mm in min
    }
}
