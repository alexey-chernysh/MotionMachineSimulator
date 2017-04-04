/*
 * Copyright (c) 2017. Alexey Chernysh, Russia, Krasnoyarsk
 */

package motionmachinesimulator.Tests;

import org.junit.jupiter.api.Test;

import static motionmachinesimulator.Processor.ControllerSettings.getWayLength4StepChange;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;

public class ControllerSettingsTest {

    // Test for getWayLength4StepChange method. actual for stepIncrement = 1.0 only
    @Test
    public void test1() throws Exception {
        // Action 1
        double actualResult = getWayLength4StepChange(2.0,2.0);
        // Assert
        assertThat(actualResult, equalTo(2.0));
        // Action 2
        actualResult = getWayLength4StepChange(2.0,3.0);
        // Assert
        assertThat(actualResult, equalTo(5.0));
        // Action 3
        actualResult = getWayLength4StepChange(3.0,2.0);
        // Assert
        assertThat(actualResult, equalTo(5.0));
        // Action 4
        actualResult = getWayLength4StepChange(2.0,4.0);
        // Assert
        assertThat(actualResult, equalTo(9.0));
    }

}