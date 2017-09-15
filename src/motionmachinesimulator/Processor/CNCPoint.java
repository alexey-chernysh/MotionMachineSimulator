/*
 * Copyright (c) 2017. Alexey Chernysh, Russia, Krasnoyarsk
 */

package motionmachinesimulator.Processor;

public class CNCPoint {

    private long x;
    private long y;

    public CNCPoint(){
        this.x = 0;
        this.y = 0;
    }

    CNCPoint(long _x, long _y){
        this.x = _x;
        this.y = _y;
    }

    CNCPoint(double newX, double newY){
        this.x = CNCScaler.double2long(newX);
        this.y = CNCScaler.double2long(newY);
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

    void setY(long newY) {
        y = newY;
    }

    public double getXinMeters(){
        return CNCScaler.long2double(x);
    }

    public double getYinMeters(){
        return CNCScaler.long2double(y);
    }

    CNCPoint add(CNCPoint p){
        return new CNCPoint(this.x + p.x, this.y + p.y);
    }

    CNCPoint sub(CNCPoint p){
        return new CNCPoint(this.x - p.x, this.y - p.y);
    }

    long getDistance(){
        double dx = x;
        double dy = y;
        return (long)Math.sqrt(dx*dx+dy*dy);
    }

    CNCPoint mul(double scale) {
        return new CNCPoint(scale*this.getXinMeters(), scale*this.getYinMeters());
    }
}
