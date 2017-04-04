/*
 * Copyright (c) 2017. Alexey Chernysh, Russia, Krasnoyarsk
 */

package motionmachinesimulator.Processor;

public class CurrentPosition {

    private static double x;
    private static double y;

    private static CurrentPosition ourInstance = new CurrentPosition();

    public static CurrentPosition getInstance() {
        return ourInstance;
    }

    private CurrentPosition() {
        reset();
    }

    public static double[] get() {
        return new double[]{x,y,0.0};
    }

    public static void set(double[] newPosition) {
        x = newPosition[0];
        y = newPosition[1];
    }

    public static void reset(){
        x = 0.0;
        y = 0.0;
    }

}

