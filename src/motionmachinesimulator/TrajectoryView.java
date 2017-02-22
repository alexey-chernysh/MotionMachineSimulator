package motionmachinesimulator;

import motionmachinesimulator.Processor.Motion;
import motionmachinesimulator.Processor.ControllerSettings;
import motionmachinesimulator.Processor.StraightMotion;

import java.awt.*;
import java.util.LinkedList;

/**
 * Created by Sales on 17.02.2017.
 */
public class TrajectoryView {

    private static double scale = 1.0;
    private static double[][] rotationMatrix = {{1.0, 0.0, 0.0},{0.0, 1.0, 0.0},{0.0, 0.0, 0.0}};
    private static double[] offsetVector = {0.0, 0.0, 0.0};

    public static final Color color1 = new Color(255,0,0);
    public static final Color color2 = new Color(0,0,255);

    public static int[] transfer(double[] input) throws Exception {
        if(input != null){
            if(input.length != ControllerSettings.DIM){
                throw new Exception("Point X, Y and Z coordinates needed only");
            }
            int[] result = new int[ControllerSettings.DIM];
            for (int i = 0; i< ControllerSettings.DIM; i++){
                double tmp = offsetVector[i];
                for(int j = 0; j< ControllerSettings.DIM; j++){
                    tmp += input[j]*rotationMatrix[i][j];
                }
                result[i] = (int)(scale*tmp);
            }
            return result;
        } else throw new Exception("Null point not supported");
    }

    public static void paint(Graphics g, LinkedList<Motion> task){
        double[] startPoint = {0.0, 0.0, 0.0};
        for(Motion currentMotion : task){
            try {
                if(currentMotion instanceof StraightMotion) {
                    startPoint = currentMotion.paint(g, startPoint);
                } else throw new Exception("For arc yet not ready");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void setScale(double newScale) {
        TrajectoryView.scale = newScale;
    }

}
