/*
 * Copyright (c) 2017. Alexey Chernysh, Russia, Krasnoyarsk
 */

package motionmachinesimulator.LongInt;

public class Trigonometric {

    private final static double b1  =  0.999999999919514177715;
    private final static double b3  = -0.166666665687003985588;
    private final static double b5  =  0.00833332995329348954259;
    private final static double b7  = -0.000198407729337123660700;
    private final static double b9  =  0.00000275219407638750246502;
    private final static double b11 = -0.0000000238436840799723029176;

    public static double my_sin_double(double x) {
        // Polynomial sine approximation at -Pi/2..Pi/2, error 1e-10
        double x2 = x * x;
        return x * (b1 + x2 * (b3 + x2 * (b5 + x2 * (b7 + x2 * (b9 + x2 * b11)))));
    }


    public final static long scale = 1<<30;
    // TODO change scaling factors for high order coefficients for increasing accuracy
    private final static long c1 = (long)(b1 * scale);
    private final static long c3 = (long)(b3 * scale);
    private final static long c5 = (long)(b5 * scale);
    private final static long c7 = (long)(b7 * scale);
    private final static long c9 = (long)(b9 * scale);
    private final static long c11 = (long)(b11 * scale);

    public static int sin_int32(int angle) {
    /* in int32 format - PI = Ox0100 0000 0000 0000 0000 0000 0000 0000
        sin(PI = Ox0100 0000 0000 0000 0000 0000 0000 0000) = Ox0100 0000 0000 0000 0000 0000 0000 0000
    * */
        long tmp = angle;
//        System.out.println("z, scaled = " + tmp);
        long x2 = (tmp * tmp)>>30;
//        System.out.println("z*x, scaled = " + x2);
        long result = (x2 * c11)>>30;
//        System.out.println("result, scaled = " + result);
        result = (x2 * (c9 + result))>>30;
//        System.out.println("result, scaled = " + result);
        result = (x2 * (c7 + result))>>30;
//        System.out.println("result, scaled = " + result);
        result = (x2 * (c5 + result))>>30;
//        System.out.println("result, scaled = " + result);
        result = (x2 * (c3 + result))>>30;
//        System.out.println("result, scaled = " + result);
        result = (tmp * (c1 + result))>>30;
        return (int)(result);
    }

}