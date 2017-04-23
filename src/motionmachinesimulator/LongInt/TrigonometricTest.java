/*
 * Copyright (c) 2017. Alexey Chernysh, Russia, Krasnoyarsk
 */

package motionmachinesimulator.LongInt;

import org.junit.Test;

import static motionmachinesimulator.LongInt.Trigonometric.*;

/**
 * Created by Sales on 14.04.2017.
 */
public class TrigonometricTest {

    @Test
    public void error_estimation() {
        // error measurement
        double x1 = Math.PI/4.0;
        System.out.println("x = " + x1);
        System.out.println("-1    = " + Integer.toBinaryString(-1));
        System.out.println("scale =   " + Long.toBinaryString(scale));
        double ys1 = sinDouble11(x1);
        System.out.println("sin_dbl = " + ys1);
        double yc1 = cosDouble10(x1);
        System.out.println("cos_dbl = " + yc1);
        System.out.println("sin_dbl^2 + cos_dbl^2 = " + (ys1*ys1+yc1*yc1));
        double z = Math.sin(x1);
        double error1 = Math.abs(ys1-z);
        System.out.println("ERROR MEASUREMENT: " + error1);
        double ys2 = sinInt9((int)(x1*scale))/((double)(scale));
        System.out.println("sin_int = " + ys2);
        double error2 = Math.abs(ys2-z);
        System.out.println("ERROR MEASUREMENT: " + error2);
        double yc2 = cosInt10((int)(x1*scale))/((double)(scale));
        System.out.println("cos_int = " + yc2);
        double error3 = Math.abs(yc2-Math.cos(x1));
        System.out.println("ERROR MEASUREMENT: " + error3);
    }

    @Test
    public void error_estimation_integer() {
        // error measurement
        long N = scale;
        long target = scale;
        long error_sum = 0;
        for(int i=0;i<=N;i++){
            long s = sinInt9(i);
            long c = cosInt10(i);
            long res = (s*s + c*c)>>(shift);
            long error = Math.abs(res - target);
            /*
            System.out.println("res    = " + Long.toBinaryString(res));
            System.out.println("target = " + Long.toBinaryString(target));
            System.out.println("error = " + error);
            */
            error_sum += error;
        }
        System.out.println("N         = " + N);
        System.out.println("error sum = " + error_sum);
    }

    @Test
    public void full_range_test() {
        // error measurement
        long N = 200;
        double lowLimit = -2.0*Math.PI;
        double hiLimit  =  2.0*Math.PI;
        long target = scale;
        long error_sum = 0;
        for(int i=0;i<=N;i++){
            double x = lowLimit + i*(hiLimit-lowLimit)/N;
            long j = (long)((x*scaledPi)/Math.PI);
            long s = sinInt9(j);
            long c = cosInt10(j);
            long res = (s*s + c*c)>>(shift);
            long error = Math.abs(res - target);

            System.out.println("x    = " + x);
            System.out.println("res    = " + Long.toBinaryString(res));
            System.out.println("target = " + Long.toBinaryString(target));
            System.out.println("error = " + error);

            error_sum += error;
        }
        System.out.println("N         = " + N);
        System.out.println("error sum = " + error_sum);
    }

    @Test
    public void scaling_test() {
        double lowLimit = -Math.PI/2;
        for(int i=0;i<5;i++){
            double x = lowLimit + i*Math.PI/4;
            long j = getLongFromDoubleAngle(x);
            long s = sinInt9(j);
            long c = cosInt10(j);
            double ds = ((double)s)/scale;
            double dc = ((double)c)/scale;

            System.out.println("x      = " + x);
            System.out.println("sin(x) = " + ds);
            System.out.println("cos(x) = " + dc);
            System.out.println("norm = " + (ds*ds+dc*dc));
            System.out.println();
        }
    }

    @Test
    public void time_estimation() {
        // time measurement
        long N = 100000000; // number of call
        double dx = Math.PI/N;

        // measurement 1
        double x = 0.0;
        double y;
        double z = 0.0;
        long start = System.currentTimeMillis();
        for(long i=0; i<N; i++){
            y = Math.sin(x);
            z += y;
            x += dx;
        }
        long end = System.currentTimeMillis();
        long time1 = end - start;
        System.out.println("z = " + z);
        System.out.println("MEASUREMENT: " + N + " calculation of Math.sin() took " + time1 + " MilliSeconds");
        double oneSin1 = ((double)time1)*1000/N;
        System.out.println("MEASUREMENT: calculation of ONE Math.sin() took " + oneSin1 + " MicroSeconds");
        System.out.println("MEASUREMENT: it means, that it can be executed " + 1.0/oneSin1 + " times in MicroSeconds");
        System.out.println();

        // measurement 2
        x = 0.0;
        z = 0.0;
        start = System.currentTimeMillis();
        for(int i=0; i<N; i++){
            y = sinDouble11(x);
            z += y;
            x += dx;
        }
        end = System.currentTimeMillis();
        long time2 = end - start;
        System.out.println("z = " + z);
        System.out.println("MEASUREMENT: " + N + " calculation of my sin() took " + time2 + " MilliSeconds");
        double oneSin2 = ((double)time2)*1000/N;
        System.out.println("MEASUREMENT: calculation of ONE my sin() took " + oneSin2 + " MicroSeconds");
        System.out.println("MEASUREMENT: it means, that it can be executed " + 1.0/oneSin2 + " times in MicroSeconds");
        System.out.println("MEASUREMENT: acceleration in near " + oneSin1/oneSin2 + " times");
        System.out.println();

        // measurement 3
        long lz = 0;
        int ly;
        start = System.currentTimeMillis();
        for(int i=0; i<N; i++){
            ly = sinInt9(i);
            lz += ly;
        }
        end = System.currentTimeMillis();
        long time3 = end - start;
        System.out.println("z = " + lz);
        System.out.println("MEASUREMENT: " + N + " calculation of my sin() took " + time3 + " MilliSeconds");
        double oneSin3 = ((double)time3)*1000/N;
        System.out.println("MEASUREMENT: calculation of ONE my sin() took " + oneSin3 + " MicroSeconds");
        System.out.println("MEASUREMENT: it means, that it can be executed " + 1.0/oneSin3 + " times in MicroSeconds");
        System.out.println("MEASUREMENT: acceleration in near " + oneSin1/oneSin3 + " times");
        System.out.println();

    }

}