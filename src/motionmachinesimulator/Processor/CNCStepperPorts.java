/*
 * Copyright (c) 2017. Alexey Chernysh, Russia, Krasnoyarsk
 */

package motionmachinesimulator.Processor;

/**
 * Created by Sales on 19.04.2017.
 */
public class CNCStepperPorts {

    private static long lastX = 0;
    private static long lastY = 0;

    private static boolean dirX = false;
    private static boolean dirY = false;

    private static boolean stepX = false;
    private static boolean stepY = false;

    private static final int stepBitPosition = 10;
    private static final long stepBitMask = ((long)1)<<(stepBitPosition-1);

    public static void setNewPosition(long x, long y){

        dirX  = (x >= lastX);
        stepX = (x & stepBitMask) > 0;
        lastX =  x;

        dirY  = (y >= lastY);
        stepY = (y & stepBitMask) > 0;
        lastY =  y;
    }

}
