package motionmachinesimulator.Processor;

import java.util.ArrayList;

public class CNCTask extends ArrayList<CNCMotion> {

    CNCTask(){
        fillDebugTask();
        this.buildAllVelocityPlans();
        System.out.println("CNCTask way length = " + this.getWayLength() + " m.");
    }

    private void fillDebugTask(){
        // debug sequence
        CNCPoint point1 = new CNCPoint( 0.0, 0.045);
        CNCPoint point2 = new CNCPoint( 0.01, 0.01);
        CNCPoint center1 = new CNCPoint(0.01, 0.00);
        CNCPoint point3 = new CNCPoint( 0.03,  0.0);
        CNCPoint point4 = new CNCPoint( 0.01, -0.01);
        CNCPoint center2 = new CNCPoint(0.00, -0.01);
        CNCPoint point5 = new CNCPoint(0.00, -0.03);
        CNCPoint point6 = new CNCPoint(-0.01, -0.01);
        CNCPoint center3 = new CNCPoint(-0.01, 0.0);
        CNCPoint point7 = new CNCPoint(-0.045, 0.0);
        CNCPoint point8 = new CNCPoint(0.005, -0.005);

        CNCMotion.MOTION_TYPE t1 = CNCMotion.MOTION_TYPE.WORKING;
        CNCMotion.MOTION_TYPE t2 = CNCMotion.MOTION_TYPE.FREE_RUN;

        try {
            double v = ControllerSettings.getStartVelocity();
            double v1 = ControllerSettings.getWorkingVelocity();
            CNCMotionStraight straightMotion1 = new CNCMotionStraight(point1, t1, v1, v);
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
            ExecutionState.setStopped();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @return sum of length for all motions in task
     */
    public double getWayLength(){
        double taskWayLength = 0.0;
        for(CNCMotion motion: this){
            taskWayLength += CNCScaler.long2double(motion.wayLength);
        }
        return taskWayLength;
    }

    private void buildAllVelocityPlans(){
        for(CNCMotion motion: this){
            motion.calcWayLength();
        }
    }

    void reset(){
        for(CNCMotion motion: this){
            motion.wayLengthCurrent = 0;
        }
        CNCStepperPorts.reset();
        ExecutionState.setStopped();
    }

    public double getWayLengthCurrent(){
        double result = 0.0;
        for(CNCMotion motion:this)
            result += CNCScaler.long2double(motion.wayLengthCurrent);
        return result;
    }
}
