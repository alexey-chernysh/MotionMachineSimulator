/*
 * Copyright (c) 2017. Alexey Chernysh, Russia, Krasnoyarsk
 */

package motionmachinesimulator.Processor;

public class CNCPoint {

    private long x;

    private long y;

    public CNCPoint(){
        x = 0;
        y = 0;
    }

    public CNCPoint(long _x, long _y){
        x = _x;
        y = _y;
    }

    public CNCPoint(double _x, double _y){
        x = CNCScaler.double2long(_x);
        y = CNCScaler.double2long(_y);
    }

    public long getX() {
        return x;
    }

    public void setX(long newX) {
        x = newX;
    }

    public long getY() {
        return y;
    }

    public void setY(long newY) {
        y = newY;
    }

    public double getXinMeters(){
        return CNCScaler.long2double(x);
    }

    public double getYinMeters(){
        return CNCScaler.long2double(y);
    }

    public CNCPoint add(CNCPoint p){
        return new CNCPoint(this.x + p.x, this.y + p.y);
    }

    public CNCPoint sub(CNCPoint p){
        return new CNCPoint(this.x - p.x, this.y - p.y);
    }

    public long getDistance(){
        double dx = x;
        double dy = y;
        return (long)Math.sqrt(dx*dx+dy*dy);
    }

    public CNCPoint mul(double scale) {
        return new CNCPoint(scale*this.getXinMeters(), scale*this.getYinMeters());
    }
}
