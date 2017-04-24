/*
 * Copyright (c) 2017. Alexey Chernysh, Russia, Krasnoyarsk
 */

package motionmachinesimulator.Processor;

public class CNCStepperPorts {

    private static CNCPoint position = new CNCPoint();
/*
    private static boolean dirX = false;
    private static boolean dirY = false;

    private static boolean stepX = false;
    private static boolean stepY = false;

    private static final int stepBitPosition = 1;
    private static final long stepBitMask = ((int)1)<<(stepBitPosition-1);
*/
    public static void setPosition(CNCPoint newPosition){

//        dirX  = (newPosition.x >= position.x);
//        stepX = (newPosition.x & stepBitMask) > 0;
        position.setX(newPosition.getX());

//        dirY  = (newPosition.y >= position.y);
//        stepY = (newPosition.y & stepBitMask) > 0;
        position.setY(newPosition.getY());
    }

    public static CNCPoint getPosition(){
        return new CNCPoint(position.getX(), position.getY());
    }

    static void reset() {
        position.setX(0);
        position.setY(0);
    }
}
