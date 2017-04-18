/*
 * Copyright (c) 2017. Alexey Chernysh, Russia, Krasnoyarsk
 */

package motionmachinesimulator.Views;

import motionmachinesimulator.Processor.ControllerSettings;

import java.awt.*;

/**
 * Created by Sales on 17.04.2017.
 */
public class VelocityView {

    public static final Color color1 = new Color(255,0,0);
    public static final Color color2 = new Color(0,0,255);
    private static VelocityHistory velocityHistory = VelocityHistory.getInstance();
    private static double workingVelocity = ControllerSettings.getWorkingVelocity();

    public static void paint(Graphics g, int width, int height){

        g.setColor(color1);
        for(VelocityHistory.VelocityRecord record: velocityHistory.history){
            int x = (int)(width*record.getWayLength());
            int y = (int)(height*record.getVelocity()/1000.0);
            g.drawLine(x, height, x, height-y);
        }
    }
}
