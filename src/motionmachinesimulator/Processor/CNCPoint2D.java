/*
 * Copyright (c) 2017. Alexey Chernysh, Russia, Krasnoyarsk
 */

package motionmachinesimulator.Processor;

public class CNCPoint2D {

    public double x;
    public double y;

    public CNCPoint2D(double _x, double _y) {
        this.x = _x;
        this.y = _y;
    }

    public CNCPoint2D() {
        this.x = 0.0;
        this.y = 0.0;
    }

    public CNCPoint2D add(CNCPoint2D p){
        return new CNCPoint2D(this.x + p.x, this.y + p.y);
    }

    public CNCPoint2D sub(CNCPoint2D p){
        return new CNCPoint2D(this.x - p.x, this.y - p.y);
    }

    public CNCPoint2D mul(double scale) { return new CNCPoint2D(this.x * scale, this.y * scale);}

    public double distance(){
        return Math.sqrt(x*x+y*y);
    }

}
