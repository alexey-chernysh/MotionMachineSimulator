package motionmachinesimulator.Processor;

import java.util.ArrayList;

class Task extends ArrayList<Motion> {

    private TASK_STATE state;

    Task(){
        fillDebugTask();
        state = TASK_STATE.EMPTY;
    }

    private void fillDebugTask(){
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
            this.add(straightMotion1);
            ArcMotion arcMotion1 = new ArcMotion(point2, center1, ArcMotion.DIRECTION.CW, v, v, v);
            this.add(arcMotion1);
            StraightMotion straightMotion2 = new StraightMotion(point3, v, v, v);
            this.add(straightMotion2);
            ArcMotion arcMotion2 = new ArcMotion(point4, center2, ArcMotion.DIRECTION.CW, v, v, v);
            this.add(arcMotion2);
            StraightMotion straightMotion3 = new StraightMotion(point5, v, v, v);
            this.add(straightMotion3);
            ArcMotion arcMotion3 = new ArcMotion(point6, center3, ArcMotion.DIRECTION.CW, v, v, v);
            this.add(arcMotion3);
            StraightMotion straightMotion4 = new StraightMotion(point7, v, v, v);
            this.add(straightMotion4);
            this.setState(TASK_STATE.READY_TO_START);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @return sum of length for all motions in task
     */
    double getWayLength(){
        double taskWayLength = 0.0;
        for(Motion motion: this){
            taskWayLength += motion.wayLength;
        }
        return taskWayLength;
    }

    void reset(){
        for(Motion motion: this){
            motion.currentWayLength = 0.0;
        }
        CurrentPosition.reset();
    }

    TASK_STATE getState() {
        return this.state;
    }

    void setState(TASK_STATE newState) {
        this.state = newState;
    }

    enum TASK_STATE {
        EMPTY,
        READY_TO_START,
        ACCELERATING,
        ON_THE_RUN,
        DECELERATED,
        PAUSED,
        FINISHED
    }

}