package motionmachinesimulator.Processor;

import java.util.ArrayList;

class Task extends ArrayList<CNCMotion> {

    Task(){
        fillDebugTask();
        System.out.println("Task way length = " + this.getWayLength() + " m.");    }

    private void fillDebugTask(){
        // debug sequence
        CNCPoint2D point1 = new CNCPoint2D( 0.0, 0.045);
        CNCPoint2D point2 = new CNCPoint2D( 0.01, 0.01);
        CNCPoint2D center1 = new CNCPoint2D(0.01, 0.00);
        CNCPoint2D point3 = new CNCPoint2D( 0.03,  0.0);
        CNCPoint2D point4 = new CNCPoint2D( 0.01, -0.01);
        CNCPoint2D center2 = new CNCPoint2D(0.00, -0.01);
        CNCPoint2D point5 = new CNCPoint2D(0.00, -0.03);
        CNCPoint2D point6 = new CNCPoint2D(-0.01, -0.01);
        CNCPoint2D center3 = new CNCPoint2D(-0.01, 0.0);
        CNCPoint2D point7 = new CNCPoint2D(-0.045, 0.0);
        CNCPoint2D point8 = new CNCPoint2D(0.005, -0.005);

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
            ExecutionState.getInstance().setState(ExecutionState.EXECUTION_STATE.READY_TO_START);
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
        CurrentPosition.getInstance().reset();
        ExecutionState.getInstance().setState(ExecutionState.EXECUTION_STATE.READY_TO_START);
    }

}
