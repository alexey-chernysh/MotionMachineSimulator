package motionmachinesimulator.Processor;

import java.util.ArrayList;

/**
 * Created by Sales on 31.03.2017.
 */
public class Task extends ArrayList<Motion> {

    public Task(){
        fillDebugTask();
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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

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

}
