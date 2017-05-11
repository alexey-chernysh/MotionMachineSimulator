/*
 * Copyright (c) 2017. Alexey Chernysh, Russia, Krasnoyarsk
 */

package motionmachinesimulator.Views;

import java.util.ArrayList;

public class VelocityHistory {

    ArrayList<VelocityRecord> history;

    private static VelocityHistory ourInstance = new VelocityHistory();

    public static VelocityHistory getInstance() {
        return ourInstance;
    }

    private VelocityHistory() {
        history = new ArrayList<VelocityRecord>();
    }

    public void addVelocity(double currentVelocity, double currentWayLength) {
        if(currentWayLength>0.0){
            VelocityRecord record = new VelocityRecord(currentVelocity, currentWayLength);
            history.add(record);
        }
    }

    class VelocityRecord{
        double velocityValue;
        double wayLengthPosition;
        VelocityRecord(double v, double l){
            velocityValue = v;
            wayLengthPosition = l;
        }

        double getWayLength() {
            return wayLengthPosition;
        }

        double getVelocity() {
            return velocityValue;
        }
    }
}
