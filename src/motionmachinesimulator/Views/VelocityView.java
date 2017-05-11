/*
 * Copyright (c) 2017. Alexey Chernysh, Russia, Krasnoyarsk
 */

package motionmachinesimulator.Views;

import java.awt.*;

public class VelocityView {

    private static final Color color1 = new Color(255,0,0);
    private static VelocityHistory velocityHistory = VelocityHistory.getInstance();

    public static void paint(Graphics g, int width, int height){

        g.setColor(color1);
        for(VelocityHistory.VelocityRecord record: velocityHistory.history){
            int x = (int)(width*record.getWayLength());
            int y = (int)(height*record.getVelocity()/1000.0);
            g.drawLine(x, height, x, height-y);
        }
    }
}
