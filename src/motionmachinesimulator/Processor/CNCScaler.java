/*
 * Copyright (c) 2017. Alexey Chernysh, Russia, Krasnoyarsk
 */

package motionmachinesimulator.Processor;

/**
 * Created by Sales on 21.04.2017.
 */
public class CNCScaler {
    /*
    *   Целочисленное масшатабироване использовано для ускорения
    *   арифметических оперций.
    *   Масштаб определяется из следующих соображений:
    *   минимальный размер приращения длины, (приращение на
    *   максимальной частоте для минимальной скорости)
    *   должно быть пластичным. т.е. быть не менее 255
    *   Тогда при частоте тактов 1MHz и минимальной скорости
    *   0,1 м/мин => 0,001666 м/сек, приращение 1,6*10-9,
    *   цена шага должна быть 1,3*10-13 или 1/(2^38)
    * */

    public static final int nFactor = 30;
    public static final long scaleFactor = 1L<<nFactor;
    private static final double doubleScaleFactor = scaleFactor;

    public static double long2double(long x){
        double tmp = x;
        return tmp/doubleScaleFactor;
    }

    public static long double2long(double x){
        return (long)(x*scaleFactor);
    }

}
