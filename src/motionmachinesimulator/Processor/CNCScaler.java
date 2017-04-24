/*
 * Copyright (c) 2017. Alexey Chernysh, Russia, Krasnoyarsk
 */

package motionmachinesimulator.Processor;

class CNCScaler {
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

    public static final int shift = 31;
    private static final long scaleFactor = 1L<< shift;
    private static final double doubleScaleFactor = scaleFactor;

   static double long2double(long x){
        return x/doubleScaleFactor;
    }

    static long double2long(double x){
        return (long)(x*doubleScaleFactor);
    }

}
