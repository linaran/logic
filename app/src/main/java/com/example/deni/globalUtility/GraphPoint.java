package com.example.deni.globalUtility;

import android.graphics.Point;
import android.graphics.PointF;

/**
 * Created to handle Graphical math regarding coordinates.
 * Should replace GraphMath.
 */
public class GraphPoint extends PointF{
    public GraphPoint() {
    }

    public GraphPoint(float x, float y) {
        super(x, y);
    }

    public GraphPoint(Point p) {
        super(p);
    }

    public GraphPoint translate(float translationX, float translationY){
        this.x += translationX;
        this.y += translationY;
        return this;
    }

    public GraphPoint scale(float scaleFactorX, float scaleFactorY){
        this.x *= scaleFactorX;
        this.y *= scaleFactorY;
        return this;
    }

    public GraphPoint antiScale(float scaleFactorX, float scaleFactorY){
        this.x /= scaleFactorX;
        this.y /= scaleFactorY;
        return this;
    }
}
