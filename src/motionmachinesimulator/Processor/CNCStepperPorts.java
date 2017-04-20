/*
 * Copyright (c) 2017. Alexey Chernysh, Russia, Krasnoyarsk
 */

package motionmachinesimulator.Processor;

public class CNCStepperPorts {

    private static CNCPoint2DInt position = new CNCPoint2DInt();

    private static boolean dirX = false;
    private static boolean dirY = false;

    private static boolean stepX = false;
    private static boolean stepY = false;

    private static final int stepBitPosition = 1;
    private static final int stepBitMask = ((int)1)<<(stepBitPosition-1);

    public static void setNewPosition(int x, int y){

        dirX  = (x >= position.x);
        stepX = (x & stepBitMask) > 0;
        position.x =  x;

        dirY  = (y >= position.y);
        stepY = (y & stepBitMask) > 0;
        position.y =  y;
    }

    public static CNCPoint2DInt getPosition(){
        return new CNCPoint2DInt(position.x, position.y);
    }

    public static void reset() {
        position.x = 0;
        position.y = 0;
    }
}
