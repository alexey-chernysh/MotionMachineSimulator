package motionmachinesimulator.Views;

import motionmachinesimulator.Processor.*;

import java.awt.*;
import java.util.ArrayList;

public class TrajectoryView {

    private static double scale = 3000.0;
    private static double[][] rotationMatrix = {{1.0, 0.0, 0.0},{0.0,-1.0, 0.0},{0.0, 0.0, 0.0}};
    private static double[] offsetVector = {0.008,0.062, 0.0};

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
        ArrayList<CNCMotion> task = mc.getCurrentTask();
        //  draw trajectory
        double[] startPoint = {0.0, 0.0, 0.0};
        for(CNCMotion currentMotion : task){
            startPoint = currentMotion.paint(g, startPoint);
        }
        // draw current position
        double[] currentPosition = CurrentPosition.get();
        int[] x = new int[0];
        try {
            x = TrajectoryView.transfer(currentPosition);
            g.setColor(TrajectoryView.color1);
            g.drawLine(x[0]-5,x[1]-5,x[0]+5,x[1]+5);
            g.drawLine(x[0]-5,x[1]+5,x[0]+5,x[1]-5);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void setScale(double newScale) {
        TrajectoryView.scale = newScale;
    }

}
