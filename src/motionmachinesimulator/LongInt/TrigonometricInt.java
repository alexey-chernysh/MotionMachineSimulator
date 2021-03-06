/*
 * Copyright (c) 2017. Alexey Chernysh, Russia, Krasnoyarsk
 */

package motionmachinesimulator.LongInt;

public class TrigonometricInt {

    public final static int shift = 30;
    public final static long scale = 1L<<shift;
    public final static double doubleScale = scale;

    public final static long scaledPi = (long)((Math.PI)*scale);
    public final static long scaledHalfPi = scaledPi/2;
    public final static long scaledHalfPiMinus = -scaledHalfPi;
    public final static long scaledPiMinus = -scaledPi;

    public static double getDoubleFromLongAngle(long x){
        return x/doubleScale;
    }
    public static long getLongFromDoubleAngle(double x){
        return (long)(x*doubleScale);
    }

    private final static double log2 = Math.log(2.0);
    private static int shiftNeeded(double x){
        return -(int)(Math.log(Math.abs(x))/log2);
    }

    private final static double dsk_1  =  0.999999982782301183838;
    private final static double dsk_3  = -0.166666515202236230156;
    private final static double dsk_5  =  0.00833296401821855059586;
    private final static double dsk_7  = -0.000198047553009241346906;
    private final static double dsk_9  =  0.00000259811044408555809666;

    private final static int n1 = shift+shiftNeeded(dsk_1);
    private final static long scale1 = 1L<<n1;
    private final static int n3 = shift+shiftNeeded(dsk_3);
    private final static long scale3 = 1L<<n3;
    private final static int n5 = shift+shiftNeeded(dsk_5);
    private final static long scale5 = 1L<<n5;
    private final static int n7 = shift+shiftNeeded(dsk_7);
    private final static long scale7 = 1L<<n7;
    private final static int n9 = shift+shiftNeeded(dsk_9);
    private final static long scale9 = 1L<<n9;

    private final static long k9_1  = (long)(dsk_1  * scale1);
    private final static long k9_3  = (long)(dsk_3  * scale3);
    private final static long k9_5  = (long)(dsk_5  * scale5);
    private final static long k9_7  = (long)(dsk_7  * scale7);
    private final static long k9_9  = (long)(dsk_9  * scale9);

    public static long sinInt9(long angle) {
    /* in int32 format - PI = Ox0100 0000 0000 0000 0000 0000 0000 0000
        sin(PI/2) = Ox0010 0000 0000 0000 0000 0000 0000 0000) = Ox0100 0000 0000 0000 0000 0000 0000 0000
    * */
//        long tmp = maskToPi(angle);
        long tmp = angle;
        if(tmp > scaledHalfPi)      tmp = scaledPi      - tmp;
        if(tmp < scaledHalfPiMinus) tmp = scaledPiMinus - tmp;
        long x2 = (tmp * tmp)>>shift;
        long result = (x2 * k9_9)>>(n9-n7+shift);
        result = (x2 * (k9_7 + result))>>(n7-n5+shift);
        result = (x2 * (k9_5 + result))>>(n5-n3+shift);
        result = (x2 * (k9_3 + result))>>(n3-n1+shift);
        result = (tmp * (k9_1 + result))>>n1;
        return result;
    }

    private final static double dck_0 = 0.999999999845705468807;
    private final static double dck_2 = -0.499999995116631679318;
    private final static double dck_4 = 0.0416666418861910415758;
    private final static double dck_6 = -0.00138884324659041369860;
    private final static double dck_8 = 0.0000247637740463291155877;
    private final static double dck_10 = -0.000000261150902081161051732;

    private final static int n0 = shift+shiftNeeded(dck_0);
    private final static long scale0 = 1L<<n0;
    private final static int n2 = shift+shiftNeeded(dck_2);
    private final static long scale2 = 1L<<n2;
    private final static int n4 = shift+shiftNeeded(dck_4);
    private final static long scale4 = 1L<<n4;
    private final static int n6 = shift+shiftNeeded(dck_6);
    private final static long scale6 = 1L<<n6;
    private final static int n8 = shift+shiftNeeded(dck_8);
    private final static long scale8 = 1L<<n8;
    private final static int n10 = shift+shiftNeeded(dck_10);
    private final static long scale10 = 1L<<n10;

    private final static long k10_0   = (long)(dck_0  * scale0);
    private final static long k10_2   = (long)(dck_2  * scale2);
    private final static long k10_4   = (long)(dck_4  * scale4);
    private final static long k10_6   = (long)(dck_6  * scale6);
    private final static long k10_8   = (long)(dck_8  * scale8);
    private final static long k10_10  = (long)(dck_10 * scale10);

    public static long cosInt9(long angle) {
        long tmp = angle;
        boolean positiveResult = true;
        if(tmp > scaledHalfPi){
            tmp = scaledPi - tmp;
            positiveResult =false;
        }
        if(tmp < scaledHalfPiMinus){
            tmp = scaledPiMinus - tmp;
            positiveResult = false;
        }
        long x2 = (tmp * tmp)>>shift;
        long result = (x2 * k10_10)>>(n10-n8+shift);
        result = (x2 * (k10_8 + result))>>(n8-n6+shift);
        result = (x2 * (k10_6 + result))>>(n6-n4+shift);
        result = (x2 * (k10_4 + result))>>(n4-n2+shift);
        result = (x2 * (k10_2 + result))>>(n2-n0+shift);
        result = (k10_0 + result)>>(n0-shift);
        if(positiveResult)return result;
        else return -result;
    }

}