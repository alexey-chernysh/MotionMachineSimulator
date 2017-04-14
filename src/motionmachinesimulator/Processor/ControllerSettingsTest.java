/*
 * Copyright (c) 2017. Alexey Chernysh, Russia, Krasnoyarsk
 */

package motionmachinesimulator.Processor;

import org.junit.jupiter.api.Test;

import static motionmachinesimulator.Processor.ControllerSettings.*;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;

public class ControllerSettingsTest {

    // Test for getWayLength4StepChange method. actual for stepIncrement = 1.0 only
    @Test
    public void test1() throws Exception {
        double a = getAcceleration();
        double dt = intervalInSec;
        double vel1 = getStartVelocity();
        double step1 = getStep4Velocity(vel1);
        double vel2 = getWorkingVelocity();
        double step2 = getStep4Velocity(vel2);
        // Action
        double actualResult = getWayLength4StepChange(step1, step2);
        // Assert
        assertThat(actualResult, equalTo(wayCheck1(dt, step1, step2, a)));
    }

    @Test
    public void test2() throws Exception {
        double ds = getStepIncrement4Acceleration();
        double dt = intervalInSec;
        double vel1 = getStartVelocity();
        double step1 = getStep4Velocity(vel1);
        double vel2 = getWorkingVelocity();
        double step2 = getStep4Velocity(vel2);
        // Action
        double actualResult = getWayLength4StepChange(step1, step2);
        // Assert
        assertThat(actualResult, equalTo(wayCheck2(dt, step1, step2, ds)));
    }

    double wayCheck1(double timeInterval, double startStep, double endStep, double accel){
        double startVel = startStep/timeInterval;
        double endVel = endStep/timeInterval;
        double velChange = endVel - startVel;
        double changeTime  = velChange/accel;
        double wayLength = changeTime*(startVel + endVel)/2.0;
        return wayLength;
    }

    double wayCheck2(double timeInterval, double startStep, double endStep, double stepChange){
        double accel = stepChange/timeInterval/timeInterval;
        double startVel = startStep/timeInterval;
        double endVel = endStep/timeInterval;
        double velChange = endVel - startVel;
        double changeTime  = velChange/accel;
        double wayLength = changeTime*(startVel + endVel)/2.0;
        return wayLength;
    }

}