package motionmachinesimulator.Views;

import motionmachinesimulator.Processor.Motion;
import motionmachinesimulator.Processor.ControllerSettings;
import motionmachinesimulator.Processor.MotionController;
import motionmachinesimulator.Processor.StraightMotion;

import java.awt.*;
import java.util.LinkedList;

public class TrajectoryView {

    private static double scale = 3000.0;
    private static double[][] rotationMatrix = {{1.0, 0.0, 0.0},{0.0,-1.0, 0.0},{0.0, 0.0, 0.0}};
    private static double[] offsetVector = {0.003,0.07, 0.0};

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

    public static void paint(Graphics g){
        MotionController mc = MotionController.getInstance();
        LinkedList<Motion> task = mc.getCurrentTask();
        double[] startPoint = {0.0, 0.0, 0.0};
        for(Motion currentMotion : task){
            try {
                if(currentMotion instanceof StraightMotion) {
                    startPoint = currentMotion.paint(g, startPoint);
                } else throw new Exception("Yet not ready for arc motion");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void setScale(double newScale) {
        TrajectoryView.scale = newScale;
    }

}
