/*
 * Copyright (c) 2017. Alexey Chernysh, Russia, Krasnoyarsk
 */

package motionmachinesimulator.Tests;

import org.junit.jupiter.api.Test;

import static motionmachinesimulator.Processor.ControllerSettings.getWayLength4StepChange;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;

public class ControllerSettingsTest {
    @Test
    public void test1() throws Exception {
        // Action
        double actualResult = getWayLength4StepChange(1.0,2.0);
        // Assert
        assertThat(actualResult, equalTo(3.0));
    }

}