package motionmachinesimulator.Views;

import java.awt.*;
import java.util.LinkedList;

/**
 * Created by Sales on 03.03.2017.
 */

public class TimeLine {

    private LinkedList<Double> values = new LinkedList<Double>();

    private Color lineColor;
    double offsetX = 0.0;
    double offsetY = -100.0;
    double scaleX = 1.0;
    double scaleY = -1.0;
    double dX = 1.0;

    public TimeLine(Color color){
        this.lineColor = color;
    }

    public void addValue(double newValue){
        Double value = new Double(newValue);
        values.add(value);
    }

    public void paint(Graphics g){
        int lastX = (int)(offsetX);;
        int lastY = (int)(offsetY);
        g.setColor(lineColor);
        for(Double value : values){
            double x = lastX + dX;
            double y = value;
            int currentX = (int)(x*scaleX + offsetX);
            int currentY = (int)(y*scaleY + offsetY);
            g.drawLine(lastX, lastY, currentX, currentY);
            lastX = currentX;
            lastY = currentY;
        }
    }
}
