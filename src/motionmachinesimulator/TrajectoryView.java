package motionmachinesimulator;

import motionmachinesimulator.Processor.ProcessorSettings;

import java.awt.*;

/**
 * Created by Sales on 17.02.2017.
 */
public class TrajectoryView {

    private static double scale = 1.0;
    private static double[][] rotationMatrix = {{1.0, 0.0, 0.0},{0.0, 1.0, 0.0},{0.0, 0.0, 0.0}};
    private static double[][] transferMatrix = new double[ProcessorSettings.DIM][ProcessorSettings.DIM];
    private static double[] offsetVector = {0.0, 0.0, 0.0};

    private void getNewTransferMatrix(){
        for(int i=0; i<ProcessorSettings.DIM)
    }

    private static double[] transfer(double[] input) throws Exception {
        if(input != null){
            if(input.length != ProcessorSettings.DIM){
                throw new Exception("Point X, Y and Z coordinates needed only");
            }
        } else throw new Exception("Null point not supported");

    }

    public static void paint(Graphics g){

    }

    public static void setScale(double newScale) {
        TrajectoryView.scale = newScale;
    }

}
