package motionmachinesimulator.Processor;

import java.util.LinkedList;

/**
 * Created by Sales on 16.02.2017.
 */
public class MotionProcess {

    private MotionProcess.STATE currentState = MotionProcess.STATE.EMPTY;
    private LinkedList currentTask = new LinkedList<Motion>();

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
            currentTask.add(straightMotion1);
            ArcMotion arcMotion1 = new ArcMotion(point2,center1,2000.0, ArcMotion.DIRECTION.CW);
            currentTask.add(arcMotion1);
            this.currentState = STATE.READY;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void Go(){
        this.currentState = STATE.ONTHERUN;
        this.currentState = STATE.FINISHED;
    }
    public void Pause(){
        this.currentState = STATE.PAUSED;
    }

    public MotionProcess.STATE getState(){ return this.currentState; }

    enum STATE {
        EMPTY,
        READY,
        ONTHERUN,
        PAUSED,
        FINISHED
    }
}
