package motionmachinesimulator.Processor;

import java.util.ArrayList;

class Task extends ArrayList<CNCMotion> {

    private TASK_STATE state;

    Task(){
        fillDebugTask();
        state = TASK_STATE.EMPTY;
        System.out.println("Task way length = " + this.getWayLength() + " m.");    }

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
        double[] point8 = {0.005, -0.005, 0.0};

        CNCMotion.MOTION_TYPE t1 = CNCMotion.MOTION_TYPE.WORKING;
        CNCMotion.MOTION_TYPE t2 = CNCMotion.MOTION_TYPE.FREE_RUN;

        try {
            double v = ControllerSettings.getStartVelocity();
            CNCMotionStraight straightMotion1 = new CNCMotionStraight(point1, t1, v, v);
            this.add(straightMotion1);
            CNCMotionArc arcMotion1 = new CNCMotionArc(point2, center1, CNCMotionArc.DIRECTION.CW, t1, v, v);
            this.add(arcMotion1);
            CNCMotionStraight straightMotion2 = new CNCMotionStraight(point3, t1, v, v);
            this.add(straightMotion2);
            CNCMotionArc arcMotion2 = new CNCMotionArc(point4, center2, CNCMotionArc.DIRECTION.CW, t1, v, v);
            this.add(arcMotion2);
            CNCMotionStraight straightMotion3 = new CNCMotionStraight(point5, t1, v, v);
            this.add(straightMotion3);
            CNCMotionArc arcMotion3 = new CNCMotionArc(point6, center3, CNCMotionArc.DIRECTION.CW, t1, v, v);
            this.add(arcMotion3);
            CNCMotionStraight straightMotion4 = new CNCMotionStraight(point7, t1, v, v);
            this.add(straightMotion4);
            CNCMotionStraight straightMotion5 = new CNCMotionStraight(point8, t2, v, v);
            this.add(straightMotion5);
            this.setState(TASK_STATE.READY_TO_START);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @return sum of length for all motions in task
     */
    private double getWayLength(){
        double taskWayLength = 0.0;
        for(CNCMotion motion: this){
            taskWayLength += motion.wayLength;
        }
        return taskWayLength;
    }

    void reset(){
        for(CNCMotion motion: this){
            motion.currentWayLength = 0.0;
        }
        CurrentPosition.reset();
        this.setState(Task.TASK_STATE.READY_TO_START);
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
        ON_THE_RUN,
        PAUSED
    }

}
