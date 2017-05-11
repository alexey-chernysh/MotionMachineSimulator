/*
 * Copyright (c) 2017. Alexey Chernysh, Russia, Krasnoyarsk
 */

package motionmachinesimulator.LongInt;

/**
 * Created by Sales on 11.05.2017.
 */
public class TrigonometricDouble {

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

    private final static double b0 = 0.999999999845705468807;
    private final static double b2 = -0.499999995116631679318;
    private final static double b4 = 0.0416666418861910415758;
    private final static double b6 = -0.00138884324659041369860;
    private final static double b8 = 0.0000247637740463291155877;
    private final static double b10 = -0.000000261150902081161051732;

    public static double cosDouble10(double x) {
        // Polynomial sine approximation at -Pi/2..Pi/2, error 1e-10
        double x2 = x * x;
        return b0 + x2*(b2 + x2*(b4 + x2*(b6 + x2*(b8 + x2*b10))));
    }
}
