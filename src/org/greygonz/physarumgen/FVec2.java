package org.greygonz.physarumgen;

public class FVec2 {

    private float x;
    private float y;

    public FVec2() {

    }

    public FVec2(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    @Override
    public String toString() {
        return "FVec2{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }

}
