/*
 * Copyright (c) 2017. Alexey Chernysh, Russia, Krasnoyarsk
 */

package motionmachinesimulator.Processor;

public class CurrentPosition {

    private CNCPoint2D position;

    private static CurrentPosition ourInstance = new CurrentPosition();

    public static CurrentPosition getInstance() {
        return ourInstance;
    }

    private CurrentPosition() {
        this.position = new CNCPoint2D();
    }

    public CNCPoint2D get() {
        return new CNCPoint2D(position.x, position.y);
    }

    public void set(CNCPoint2D newPosition) {
        position.x = newPosition.x;
        position.y = newPosition.y;
    }

    public void reset(){
        position.x = 0.0;
        position.y = 0.0;
    }

}

