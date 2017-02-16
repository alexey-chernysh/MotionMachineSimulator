package motionmachinesimulator.Processor;

import java.util.Iterator;
import java.util.LinkedList;

/**
 * Created by Sales on 16.02.2017.
 */
public class MotionProcess implements Runnable {

    private MotionProcess.STATE currentState = MotionProcess.STATE.EMPTY;
    private LinkedList currentTask = new LinkedList<Motion>();

    private double processorFrequency = 100000.0; // 100 kHz

    private static MotionProcess ourInstance = new MotionProcess();

    public static MotionProcess getInstance() {
        return ourInstance;
    }

    private MotionProcess() {
        // debug sequence
        double[] point1 = {1.0,1.0,0.0};
        double[] center1 = {1.0,0.0};
        double[] point2 = {2.0,0.0,0.0};
        try {
            StraightMotion straightMotion1 = new StraightMotion(point1,2000.0);
            currentTask.addLast(straightMotion1);
            ArcMotion arcMotion1 = new ArcMotion(point2,center1,2000.0, ArcMotion.DIRECTION.CW);
            currentTask.addLast(arcMotion1);
            this.currentState = STATE.NEW;
        } catch (Exception e) {
            e.printStackTrace();
        }
        new Thread(this).start();
    }

    private void tickForward(){


    };

    public void go() {
        // find current position
        Iterator<Motion> iter = currentTask.iterator();
        Motion motion = iter.next();
        while ((iter.hasNext()) && (motion.getPhase() >= 1))
            motion = iter.next();
        if (motion.getPhase() >= 1){ // execution finished, start from first again
            rewindToStart();
            iter = currentTask.iterator();
            motion = iter.next();
        }

        this.currentState = STATE.STARTED;
        while (iter.hasNext()){
            iter.next().execute();
        }

        this.currentState = STATE.FINISHED;
    }

    private void rewindToStart(){
        Iterator<Motion> iter = currentTask.iterator();
        Motion motion = iter.next();
        while ((iter.hasNext()) && (motion.getPhase() >= 1)) {
            motion.setPhaseStateNotExecuted();
            motion = iter.next();
        }
    }

    public void pause(){
        this.currentState = STATE.PAUSED;
    }

    public MotionProcess.STATE getState(){ return this.currentState; }

    @Override
    public void run() {

    }

    enum STATE {
        EMPTY,
        NEW,
        STARTED,
        PAUSED,
        FINISHED
    }
}
