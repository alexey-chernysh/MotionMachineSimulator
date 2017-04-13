/*
 * Copyright (c) 2017. Alexey Chernysh, Russia, Krasnoyarsk
 */

package motionmachinesimulator.Processor;

public class CurrentPosition {

    private static CNCPoint2D position;

    private static CurrentPosition ourInstance = new CurrentPosition();

    public static CurrentPosition getInstance() {
        position = new CNCPoint2D();
        return ourInstance;
    }

    private CurrentPosition() {
        reset();
    }

    public static CNCPoint2D get() {
        return new CNCPoint2D(position.x, position.y);
    }

    public static void set(CNCPoint2D newPosition) {
        position.x = newPosition.x;
        position.y = newPosition.y;
    }

    public static void reset(){
        position.x = 0.0;
        position.y = 0.0;
    }

}

