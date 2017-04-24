/*
 * Copyright (c) 2017. Alexey Chernysh, Russia, Krasnoyarsk
 */

package motionmachinesimulator.Processor;

/**
 * Created by Sales on 21.04.2017.
 */
public class CNCScaleForLong {
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

    public static final int nFactor = 37;
    public static final long scaleFactor = 1L<<nFactor;

    public static double getDoubleFromLong(long x){
        return ((double)x)/scaleFactor;
    }

    public static long getLongFromDouble(double x){
        return (long)(x*scaleFactor);
    }

}
