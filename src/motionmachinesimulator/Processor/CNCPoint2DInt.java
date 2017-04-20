/*
 * Copyright (c) 2017. Alexey Chernysh, Russia, Krasnoyarsk
 */

package motionmachinesimulator.Processor;

/**
 * Created by Sales on 20.04.2017.
 */
public class CNCPoint2DInt {
    public int x;
    public int y;

    public CNCPoint2DInt(){
        x = 0;
        y = 0;
    }

    public CNCPoint2DInt(int _x, int _y){
        x = _x;
        y = _y;
    }

    public CNCPoint2DInt add(CNCPoint2DInt p){
        return new CNCPoint2DInt(this.x + p.x, this.y + p.y);
    }

    public CNCPoint2DInt sub(CNCPoint2DInt p){
        return new CNCPoint2DInt(this.x - p.x, this.y - p.y);
    }

    public CNCPoint2D toCNCPoint2D() {
        return new CNCPoint2D(this.x, this.y);
    }
}
