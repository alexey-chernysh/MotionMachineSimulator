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

    private final static int shift = 30;
    private final static long one = 1;
    public final static long scale = one<<shift;
    private final static double log2 = Math.log(2.0);
    private final static int n1 = shift-(int)(Math.log(Math.abs(b1))/log2);
    private final static long scale1 = one<<n1;
    private final static int n3 = shift-(int)(Math.log(Math.abs(b3))/log2);
    private final static long scale3 = one<<n3;
    private final static int n5 = shift-(int)(Math.log(Math.abs(b5))/log2);
    private final static long scale5 = one<<n5;
    private final static int n7 = shift-(int)(Math.log(Math.abs(b7))/log2);
    private final static long scale7 = one<<n7;
    private final static int n9 = shift-(int)(Math.log(Math.abs(b9))/log2);
    private final static long scale9 = one<<n9;
    private final static int n11 = shift-(int)(Math.log(Math.abs(b11))/log2);
    private final static long scale11 = one<<n11;
    private final static long c1  = (long)(b1  * scale1);
    private final static long c3  = (long)(b3  * scale3);
    private final static long c5  = (long)(b5  * scale5);
    private final static long c7  = (long)(b7  * scale7);
    private final static long c9  = (long)(b9  * scale9);
    private final static long c11 = (long)(b11 * scale11);

    public static int sin_int32(int angle) {
    /* in int32 format - PI = Ox0100 0000 0000 0000 0000 0000 0000 0000
        sin(PI = Ox0100 0000 0000 0000 0000 0000 0000 0000) = Ox0100 0000 0000 0000 0000 0000 0000 0000
    * */
        long tmp = angle;
//        System.out.println("z, scaled = " + tmp);
        long x2 = (tmp * tmp)>>shift;
//        System.out.println("z*x, scaled = " + x2);
        long result = (x2 * c11)>>(n11-n9+shift);
//        System.out.println("result, scaled = " + result);
        result = (x2 * (c9 + result))>>(n9-n7+shift);
//        System.out.println("result, scaled = " + result);
        result = (x2 * (c7 + result))>>(n7-n5+shift);
//        System.out.println("result, scaled = " + result);
        result = (x2 * (c5 + result))>>(n5-n3+shift);
//        System.out.println("result, scaled = " + result);
        result = (x2 * (c3 + result))>>(n3-n1+shift);
//        System.out.println("result, scaled = " + result);
        result = (tmp * (c1 + result))>>n1;
        return (int)(result);
    }

}