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

    private static double processorFrequency = 100000.0; // 100 kHz

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
            StraightMotion straightMotion1 = new StraightMotion(point1,200.0);
            currentTask.addLast(straightMotion1);
            StraightMotion straightMotion2 = new StraightMotion(point2,200.0);
            currentTask.addLast(straightMotion2);
            StraightMotion straightMotion3 = new StraightMotion(point3,200.0);
            currentTask.addLast(straightMotion3);
            StraightMotion straightMotion4 = new StraightMotion(point4,200.0);
            currentTask.addLast(straightMotion4);
        } catch (Exception e) {
            e.printStackTrace();
        }
        new Thread(this).start();
    }

    public void resumeExecution(){
        this.setMotionState(MOTION_STATE.STARTED);
        this.start();
    }
    public void pauseExecution() {
        this.setMotionState(MOTION_STATE.PAUSED);
    }

    @Override
    public void run() {
        for(Motion motion: currentTask){
            motion.start();
            try {
                motion.join();
            } catch (InterruptedException ie) {
                ie.printStackTrace();
            }
        }
    }

    static double getProcessorFrequency() {
        return processorFrequency;
    }

    public LinkedList getCurrentTask() {
        return currentTask;
    }

}
