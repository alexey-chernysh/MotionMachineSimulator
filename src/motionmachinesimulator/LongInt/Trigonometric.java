/*
 * Copyright (c) 2017. Alexey Chernysh, Russia, Krasnoyarsk
 */

package motionmachinesimulator.LongInt;

public class Trigonometric {
    public static int sin_int32(int angle /* in int32 format - PI = Ox0100 0000 0000 0000 0000 0000 0000 0000 */ ){
        long tmp = angle;
        int result = 0;
        return result;
    }

    private final static double a1 = 1.0/factorial(1);
    private final static double a3 = 1.0/factorial(3);
    private final static double a5 = 1.0/factorial(5);
    private final static double a7 = 1.0/factorial(7);
    private final static double a9 = 1.0/factorial(9);
    private final static double a11 = 1.0/factorial(11);
    private final static double a13 = 1.0/factorial(13);
    private final static double a15 = 1.0/factorial(15);
    private final static double a17 = 1.0/factorial(17);
    private final static double a19 = 1.0/factorial(19);

    public static double my_sin_double(double x){
        double x2 = x*x;
        double result = x*(a1 - x2*(a3 + x2*(a5 - x2*(a7 + x2*(a9 - x2*(a11 + x2*(a13 - x2*(a15 + x2*(a17 - x2*a19)))))))));
        return result;
    }

    private static double factorial(int N){
        double result = 1.0;
        for (int i=1; i<=N; i++){
            double t = i;
            result = result * t;
        }
        return result;
    }
}
