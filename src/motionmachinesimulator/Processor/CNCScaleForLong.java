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
    *   Тогда при частоте FPGA 5MHz и минимальной скорости
    *   0,01 м/мин => 0,0001666 м/сек, приращение 3*10-11,
    *   цена шага должна быть 1,3*10-13 или 1/(2^43)
    * */

    private static final int nFactor = 43;
    private static final long scaleFactor = ((long)1)<<nFactor;

    public static double getDoubleFromLong(long x){
        return ((double)x)/scaleFactor;
    }

    public static long getLongFromDouble(double x){
        return (long)(x*scaleFactor);
    }

}
