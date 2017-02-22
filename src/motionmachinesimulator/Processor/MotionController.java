package motionmachinesimulator.Processor;

import java.util.Iterator;
import java.util.LinkedList;

/**
 * Created by Sales on 16.02.2017.
 * Functions needed to be implemented:
 *  start/resume
 *  stop/pause
 *  direction change (forward/backward)
 *  velocity change (acceleration/deceleration)
 */
public class MotionController extends ControllerState implements Runnable {

    private LinkedList currentTask = new LinkedList<Motion>();

    private static double processorFrequency = 100000.0; // 100 kHz

    private static MotionController ourInstance = new MotionController();

    public static MotionController getInstance() {
        return ourInstance;
    }

    private MotionController() {
        super();
        // debug sequence
        double[] point1 = { 0.0,  0.055,0.0};
        double[] point2 = { 0.05, 0.0,  0.0};
        double[] point3 = { 0.0, -0.05, 0.0};
        double[] point4 = {-0.055,0.0,  0.0};
        try {
            StraightMotion straightMotion1 = new StraightMotion(point1,2000.0);
            currentTask.addLast(straightMotion1);
            StraightMotion straightMotion2 = new StraightMotion(point2,2000.0);
            currentTask.addLast(straightMotion2);
            StraightMotion straightMotion3 = new StraightMotion(point3,2000.0);
            currentTask.addLast(straightMotion3);
            StraightMotion straightMotion4 = new StraightMotion(point4,2000.0);
            currentTask.addLast(straightMotion4);
        } catch (Exception e) {
            e.printStackTrace();
        }
        new Thread(this).start();
    }

    public void resume(){
        this.setMotionState(MOTION_STATE.STARTED);
    }
    public void pause() {
        this.setMotionState(MOTION_STATE.PAUSED);
    }

    @Override
    public void run() {
    }

    static double getProcessorFrequency() {
        return processorFrequency;
    }

}
