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

    private ArrayList<Motion> currentTask = new ArrayList<Motion>();
    private Thread controllerThread;
    private boolean forwardDirection = true;

    private static MotionController ourInstance = new MotionController();

    public static MotionController getInstance() {
        return ourInstance;
    }

    private MotionController() {
        super();
        // debug sequence
        double[] point1 = { 0.0, 0.045, 0.0};
        double[] point2 = { 0.01, 0.01, 0.0};
        double[] center1 = {0.01, 0.00, 0.0};
        double[] point3 = { 0.03,  0.0, 0.0};
        double[] point4 = { 0.01, -0.01, 0.0};
        double[] center2 = {0.00, -0.01, 0.0};
        double[] point5 = {0.00, -0.03, 0.0};
        double[] point6 = {-0.01, -0.01, 0.0};
        double[] center3 = {-0.01, 0.0, 0.0};
        double[] point7 = {-0.045, 0.0, 0.0};
        try {
            double v = ControllerSettings.getWorkingVelocity();
            StraightMotion straightMotion1 = new StraightMotion(point1, v, v, v);
            currentTask.add(straightMotion1);
            ArcMotion arcMotion1 = new ArcMotion(point2, center1, ArcMotion.DIRECTION.CW, v, v, v);
            currentTask.add(arcMotion1);
            StraightMotion straightMotion2 = new StraightMotion(point3, v, v, v);
            currentTask.add(straightMotion2);
            ArcMotion arcMotion2 = new ArcMotion(point4, center2, ArcMotion.DIRECTION.CW, v, v, v);
            currentTask.add(arcMotion2);
            StraightMotion straightMotion3 = new StraightMotion(point5, v, v, v);
            currentTask.add(straightMotion3);
            ArcMotion arcMotion3 = new ArcMotion(point6, center3, ArcMotion.DIRECTION.CW, v, v, v);
            currentTask.add(arcMotion3);
            StraightMotion straightMotion4 = new StraightMotion(point7, v, v, v);
            currentTask.add(straightMotion4);
            this.setTaskState(TASK_STATE.READY_TO_START);
        } catch (Exception e) {
            e.printStackTrace();
        }
        controllerThread = new Thread(this);
        controllerThread.start();
    }

    public void resumeForwardExecution() {
        forwardDirection = true;
        if(!controllerThread.isAlive()) {
            controllerThread = new Thread(this);
            controllerThread.start();
        }
        this.setTaskState(TASK_STATE.ON_THE_RUN);
    }

    public void resumeBackwardExecution() {
        forwardDirection = false;
        if(!controllerThread.isAlive()) {
            controllerThread = new Thread(this);
            controllerThread.start();
        }
        this.setTaskState(TASK_STATE.ON_THE_RUN);
    }

    private int intervalInMillis = 1; // min available
    private double stepSize = getStep4Velocity(ControllerSettings.getWorkingVelocity(),
                                              (intervalInMillis/1000.0));

    public void pauseExecution() {
        this.setTaskState(TASK_STATE.PAUSED);
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
        Motion currentMotion;
        double[][] startPos = new double[taskSize][ControllerSettings.DIM];
        do{
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
            this.setTaskState(TASK_STATE.READY_TO_START);
            resetTask();
        }while(true);
    }

    void motionRun(Motion motion, double[] startPos){
//        double currentStepSize = stepSize;
        double currentDistanceToTarget = Double.MAX_VALUE;
        double[] currentAbsPos = new double[ControllerSettings.DIM];
        do{ // linear velocity phase
            if(taskShouldBeEjected()) break;
            double[] relPos;
            if(this.getTaskState() == TASK_STATE.ON_THE_RUN){
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

    private static boolean ejectFlag = false;
    public void ejectTask(){
        ejectFlag = true;
    }
    private boolean taskShouldBeEjected() {
        boolean result = ejectFlag;
        ejectFlag = false;
        return result;
    }

    private void resetTask(){
        for(Motion motion: currentTask){
            motion.currentWayLength = 0.0;
        }
        CurrentPosition.reset();
    }

    public ArrayList<Motion> getCurrentTask() {
        return currentTask;
    }

    static double getStep4Velocity(double velocity, double timeInterval){
        return velocity*timeInterval;
    }

    static double getStepIncrement(double acceleration, double timeInterval){
        double velocityIncrement = acceleration * timeInterval;
        return (velocityIncrement*timeInterval);
    }

    double getTaskWayLength(){
        double taskWayLength = 0.0;
        for(Motion motion: currentTask){
            taskWayLength += motion.wayLength;
        }
        return taskWayLength;
    }

    public double getCurrentVelocity() {
        double velMeterPerSec = stepSize/(intervalInMillis/1000.0);
        return velMeterPerSec*60*1000; // mm in min
    }
}
