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
        double x1 = Math.PI/6.0;
        System.out.println("x = " + x1);
        double y1 = sinDouble11(x1);
        System.out.println("sin_dbl = " + y1);
        double z = Math.sin(x1);
        double error1 = Math.abs(y1-z);
        System.out.println("ERROR MEASUREMENT: " + error1);
        double y2 = sinInt9((int)(x1*scale))/((double)(scale));
        System.out.println("sin_int = " + y2);
        double error2 = Math.abs(y2-z);
        System.out.println("ERROR MEASUREMENT: " + error2);
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