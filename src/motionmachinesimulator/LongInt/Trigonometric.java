/*
 * Copyright (c) 2017. Alexey Chernysh, Russia, Krasnoyarsk
 */

package motionmachinesimulator.LongInt;

public class Trigonometric {
    public static int sin_int32(int angle /* in int32 format - PI = Ox0100 0000 0000 0000 0000 0000 0000 0000 */) {
        long tmp = angle;
        int result = 0;
        return result;
    }

    private final static double a1 = 1.0 / factorial(1);
    private final static double a3 = 1.0 / factorial(3);
    private final static double a5 = 1.0 / factorial(5);
    private final static double a7 = 1.0 / factorial(7);
    private final static double a9 = 1.0 / factorial(9);
    private final static double a11 = 1.0 / factorial(11);
    private final static double a13 = 1.0 / factorial(13);
    private final static double a15 = 1.0 / factorial(15);
    private final static double a17 = 1.0 / factorial(17);
    private final static double a19 = 1.0 / factorial(19);

    public static double my_sin_double1(double x) {
        double x2 = x * x;
        double result = x * (a1 - x2 * (a3 + x2 * (a5 - x2 * (a7 + x2 * (a9 - x2 * (a11 + x2 * (a13 - x2 * (a15 + x2 * (a17 - x2 * a19)))))))));
        return result;
    }

    private static double factorial(int N) {
        double result = 1.0;
        for (int i = 1; i <= N; i++) {
            double t = i;
            result = result * t;
        }
        return result;
    }

    /*
    Polinomial sine approximation at -Pi/2..Pi/2, error 1e-8
            2.59811044408555809666e-06x^9
            - 1.98047553009241346906e-04x^7
            + 8.33296401821855059586e-03x^5
            - 1.66666515202236230156e-01x^3
            + 9.99999982782301183838e-01x}
    */
    private final static double b1  =  0.999999999919514177715;
    private final static double b3  = -0.166666665687003985588;
    private final static double b5  =  0.00833332995329348954259;
    private final static double b7  = -0.000198407729337123660700;
    private final static double b9  =  0.00000275219407638750246502;
    private final static double b11 = -0.0000000238436840799723029176;

    public static double my_sin_double2(double x) {
        double x2 = x * x;
        return x * (b1 + x2 * (b3 + x2 * (b5 + x2 * (b7 + x2 * (b9 + x2 * b11)))));
    }
}