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

    private static double processorFrequency = 200000.0; // 100 kHz

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
        try {
            StraightMotion straightMotion1 = new StraightMotion(point1);
            currentTask.addLast(straightMotion1);
            StraightMotion straightMotion2 = new StraightMotion(point2);
            currentTask.addLast(straightMotion2);
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

    public void resumeExecution() {
        this.setTaskState(TASK_STATE.STARTED);
        if(!controllerThread.isAlive()) {
            controllerThread = new Thread(this);
            controllerThread.start();
        }
    }

    public void pauseExecution() {
        this.setTaskState(TASK_STATE.PAUSED);
    }

    @Override
    public void run() {
        for(Motion motion: currentTask){
            do{
                motion.onFastTimerForwardTick(0.1);
                try {
                    this.sleep(10);
                } catch (InterruptedException ie) {
                    ie.printStackTrace();
                }
            }while(motion.isOnTheRun());
        }
        setTaskState(TASK_STATE.PAUSED);
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
