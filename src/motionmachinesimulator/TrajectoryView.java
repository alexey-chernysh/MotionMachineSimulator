package motionmachinesimulator;

import motionmachinesimulator.Processor.Motion;
import motionmachinesimulator.Processor.ProcessorSettings;

import java.awt.*;
import java.util.LinkedList;

/**
 * Created by Sales on 17.02.2017.
 */
public class TrajectoryView {

    private static double scale = 1.0;
    private static double[][] rotationMatrix = {{1.0, 0.0, 0.0},{0.0, 1.0, 0.0},{0.0, 0.0, 0.0}};
    private static double[] offsetVector = {0.0, 0.0, 0.0};

    private static double[] transfer(double[] input) throws Exception {
        if(input != null){
            if(input.length != ProcessorSettings.DIM){
                throw new Exception("Point X, Y and Z coordinates needed only");
            }
            double[] result = new double[ProcessorSettings.DIM];
            for (int i=0; i<ProcessorSettings.DIM; i++){
                double tmp = offsetVector[i];
                for(int j=0; j<ProcessorSettings.DIM; j++){
                    tmp += input[j]*rotationMatrix[i][j];
                }
                result[i] = scale*tmp;
            }
            return result;
        } else throw new Exception("Null point not supported");
    }

    public static void paint(Graphics g, LinkedList<Motion> task){
        double[] startPoint = {0.0, 0.0, 0.0};
        for(Motion currentMotion : task){
            try {
                if(currentMotion instanceof StraightMotion) {
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
