package motionmachinesimulator.Processor;

import java.util.LinkedList;

/**
 * Created by Sales on 16.02.2017.
 * Functions needed to be implemented:
 *  start/resume
 *  stop/pause
 *  direction change (forward/backward)
 *  velocity change (acceleration/deceleration)
 */
public class MotionController extends ControllerState {

    private LinkedList<Motion> currentTask = new LinkedList<Motion>();
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
            StraightMotion straightMotion1 = new StraightMotion(point1);
            currentTask.addLast(straightMotion1);
            ArcMotion arcMotion1 = new ArcMotion(point2, center1, ArcMotion.DIRECTION.CW);
            currentTask.addLast(arcMotion1);
            StraightMotion straightMotion2 = new StraightMotion(point3);
            currentTask.addLast(straightMotion2);
            ArcMotion arcMotion2 = new ArcMotion(point4, center2, ArcMotion.DIRECTION.CW);
            currentTask.addLast(arcMotion2);
            StraightMotion straightMotion3 = new StraightMotion(point5);
            currentTask.addLast(straightMotion3);
            ArcMotion arcMotion3 = new ArcMotion(point6, center3, ArcMotion.DIRECTION.CW);
            currentTask.addLast(arcMotion3);
            StraightMotion straightMotion4 = new StraightMotion(point7);
            currentTask.addLast(straightMotion4);
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
        MotionController.setTaskState(TASK_STATE.STARTED);
    }

    public void resumeBackwardExecution() {
        forwardDirection = false;
        if(!controllerThread.isAlive()) {
            controllerThread = new Thread(this);
            controllerThread.start();
        }
        MotionController.setTaskState(TASK_STATE.STARTED);
    }

    private double stepSize = 0.005/1000.0;

    public void pauseExecution() {
        MotionController.setTaskState(TASK_STATE.PAUSED);
    }
    public void velocityUp() {
        stepSize = stepSize * 1.1;
    }
    public void velocityDown() {
        stepSize = stepSize * 0.9;
    }

    @Override
    public void run() {
        for(Motion motion: currentTask){
            do{
                if(MotionController.getTaskState() == TASK_STATE.STARTED){
                    if(this.forwardDirection)
                        motion.onFastTimerForwardTick(stepSize);
                    else
                        motion.onFastTimerBackwardTick(stepSize);

                    try {
                        Thread.sleep(1);
                    } catch (InterruptedException ie) {
                        ie.printStackTrace();
                    }

                } else {
                    System.out.println(" Motion controller empty run ");
                }
            }while(motion.isOnTheRun());
        }
        MotionController.setTaskState(TASK_STATE.PAUSED);
        resetTask();
    }

    private boolean taskStarted = false;
    private Motion currentMotion;

    public void go(){
        do{
            currentMotion = currentTask.getFirst();
            while((!endOfLastReached()||(!rewindedToStartOfFirst()))){
                taskStarted = true;

            }
            if(taskEjected()) break;
        }while(true);

    }

    private boolean rewindedToStartOfFirst() {
        if(currentMotion != null) {
            Motion firstMotion = currentTask.getFirst();
            return currentMotion.equals(firstMotion) && taskStarted && !currentMotion.isOnTheRun();
        } else return false;
    }

    private boolean endOfLastReached() {
        if(currentMotion != null) {
            Motion lastMotion = currentTask.getLast();
            return currentMotion.equals(lastMotion) && taskStarted && !currentMotion.isOnTheRun();
        } else return false;
    }

    private boolean ejectFlag = false;

    public void ejectTask(){
        ejectFlag = true;
    }

    private boolean taskEjected() {
        return ejectFlag;
    }

    private void resetTask(){
        for(Motion motion: currentTask){
            motion.setPhaseStateNotExecuted();
        }
        ControllerState.resetCurrentPosition();
    }

    public LinkedList<Motion> getCurrentTask() {
        return currentTask;
    }

}
