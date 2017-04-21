/*
 * Copyright (c) 2017. Alexey Chernysh, Russia, Krasnoyarsk
 */

package motionmachinesimulator.Processor;

/**
 * Created by Sales on 20.04.2017.
 */
public class CNCPoint2DInt {
    public long x;
    public long y;

    public CNCPoint2DInt(){
        x = 0;
        y = 0;
    }

    public CNCPoint2DInt(long _x, long _y){
        x = _x;
        y = _y;
    }

    public CNCPoint2DInt(double _x, double _y){
        x = CNCScaleForLong.getLongFromDouble(_x);
        y = CNCScaleForLong.getLongFromDouble(_x);
    }

    public double getXinMeters(){
        return CNCScaleForLong.getDoubleFromLong(x);
    }

    public double getYinMeters(){
        return CNCScaleForLong.getDoubleFromLong(y);
    }

    public CNCPoint2DInt add(CNCPoint2DInt p){
        return new CNCPoint2DInt(this.x + p.x, this.y + p.y);
    }

    public CNCPoint2DInt sub(CNCPoint2DInt p){
        return new CNCPoint2DInt(this.x - p.x, this.y - p.y);
    }

    public double getDistanceInMeters(){
        double dx = this.getXinMeters();
        double dy = this.getYinMeters();
        return (long)Math.sqrt(dx*dx+dy*dy);
    }

}
