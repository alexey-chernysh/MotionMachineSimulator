package motionmachinesimulator.Processor;

import java.util.Iterator;
import java.util.LinkedList;

/**
 * Created by Sales on 16.02.2017.
 */
public class MotionProcess extends ExecutionState implements Runnable {

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
            this.setState(STATE.NEW);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void go() {
        new Thread(this).start();
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
        this.setState(STATE.PAUSED);
    }

    @Override
    public void run() {
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

        this.setState(STATE.STARTED);
        while (iter.hasNext()){
            Motion currentMotion = iter.next();
            Thread currentMotionThread = new Thread(currentMotion);
            currentMotionThread.start();
            try {
                currentMotionThread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        this.setState(STATE.FINISHED);
    }

}
