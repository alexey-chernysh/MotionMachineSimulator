package motionmachinesimulator.Views;

import motionmachinesimulator.Processor.*;

import java.awt.*;
import java.util.ArrayList;

public class TrajectoryView {

    private static double scale = 3000.0;
    private static double[][] rotationMatrix = {{1.0, 0.0},{0.0,-1.0}};
    private static double[] offsetVector = {0.008,0.062};

    public static final Color color1 = new Color(255,0,0);
    public static final Color color2 = new Color(0,0,255);

    public static int[] transfer(CNCPoint2D input) throws Exception {
        double[] inputVector = {input.x,input.y};
        if(input != null){
            int[] result = new int[2];
            for (int i = 0; i< 2; i++){
                double tmp = offsetVector[i];
                for(int j = 0; j< 2; j++){
                    tmp += inputVector[j]*rotationMatrix[i][j];
                }
                result[i] = (int)(scale*tmp);
            }
            return result;
        } else throw new Exception("Null point not supported");
    }

    public static void paint(Graphics g){
        CNCMotionController mc = CNCMotionController.getInstance();
        ArrayList<CNCMotion> task = mc.getCurrentTask();
        //  draw trajectory
        CNCPoint2D startPoint = new CNCPoint2D();
        for(CNCMotion currentMotion : task){
            startPoint = currentMotion.paint(g, startPoint);
        }
        // draw current position
        CNCPoint2DInt currentPositionInt = CNCStepperPorts.getPosition();
        CNCPoint2D currentPosition = currentPositionInt.toCNCPoint2D();
        int[] x;
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
