/*
 * Copyright (c) 2017. Alexey Chernysh, Russia, Krasnoyarsk
 */

package motionmachinesimulator.Views;

import java.util.ArrayList;

/**
 * Created by Sales on 17.04.2017.
 */
public class VelocityHistory {

    public ArrayList<VelocityRecord> history;

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

        public double getWayLength() {
            return wayLengthPosition;
        }

        public double getVelocity() {
            return velocityValue;
        }
    }
}
