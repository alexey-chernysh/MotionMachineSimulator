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

    public static double sinDouble11(double x) {
        // Polynomial sine approximation at -Pi/2..Pi/2, error 1e-10
        double x2 = x * x;
        return x * (b1 + x2 * (b3 + x2 * (b5 + x2 * (b7 + x2 * (b9 + x2 * b11)))));
    }

    private final static int shift = 30;
    private final static long one = 1;
    public final static long scale = one<<shift;
    private final static double log2 = Math.log(2.0);
    private static int shiftNeeded(double x){
        return -(int)(Math.log(Math.abs(x))/log2);
    }

    private final static double d1  =  0.999999982782301183838;
    private final static double d3  = -0.166666515202236230156;
    private final static double d5  =  0.00833296401821855059586;
    private final static double d7  = -0.000198047553009241346906;
    private final static double d9  =  0.00000259811044408555809666;

    private final static int n1 = shift+shiftNeeded(d1);
    private final static long scale1 = one<<n1;
    private final static int n3 = shift+shiftNeeded(d3);
    private final static long scale3 = one<<n3;
    private final static int n5 = shift+shiftNeeded(d5);
    private final static long scale5 = one<<n5;
    private final static int n7 = shift+shiftNeeded(d7);
    private final static long scale7 = one<<n7;
    private final static int n9 = shift+shiftNeeded(d9);
    private final static long scale9 = one<<n9;

    private final static long k9_1  = (long)(d1  * scale1);
    private final static long k9_3  = (long)(d3  * scale3);
    private final static long k9_5  = (long)(d5  * scale5);
    private final static long k9_7  = (long)(d7  * scale7);
    private final static long k9_9  = (long)(d9  * scale9);

    public static int sinInt9(int angle) {
    /* in int32 format - PI = Ox0100 0000 0000 0000 0000 0000 0000 0000
        sin(PI = Ox0100 0000 0000 0000 0000 0000 0000 0000) = Ox0100 0000 0000 0000 0000 0000 0000 0000
    * */
        long tmp = angle;
        long x2 = (tmp * tmp)>>shift;
        long result = (x2 * k9_9)>>(n9-n7+shift);
        result = (x2 * (k9_7 + result))>>(n7-n5+shift);
        result = (x2 * (k9_5 + result))>>(n5-n3+shift);
        result = (x2 * (k9_3 + result))>>(n3-n1+shift);
        result = (tmp * (k9_1 + result))>>n1;
        return (int)(result);
    }

}