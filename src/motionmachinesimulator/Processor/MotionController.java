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
        double[] point1 = { 0.0,   0.055, 0.0};
        double[] point2 = { 0.05,  0.0,   0.0};
        double[] point3 = { 0.0,  -0.05,  0.0};
        double[] point4 = {-0.055, 0.0,   0.0};
        double[] center1 = {0.025, 0.00, 0.0};
        try {
            StraightMotion straightMotion1 = new StraightMotion(point1);
            currentTask.addLast(straightMotion1);
            ArcMotion arcMotion1 = new ArcMotion(point2, center1, ArcMotion.DIRECTION.CCW);
            currentTask.addLast(arcMotion1);
//            StraightMotion straightMotion2 = new StraightMotion(point2);
//            currentTask.addLast(straightMotion2);
            StraightMotion straightMotion3 = new StraightMotion(point3);
            currentTask.addLast(straightMotion3);
            StraightMotion straightMotion4 = new StraightMotion(point4);
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

    private double stepSize = 0.01/1000.0;

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

    private void resetTask(){
        for(Motion motion: currentTask){
            motion.setPhaseStateNotExecuted();
        }
    }

    public LinkedList<Motion> getCurrentTask() {
        return currentTask;
    }

}
