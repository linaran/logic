package com.example.deni.logicComponentDrawable;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;

/**
 *  Abstract method for visual representation of basic components for SchemeView.
 *  Any method extending this class should this draw method before continuing with
 *  its own. Super draw method draws IOGates so subclasses are free to draw whatever they
 *  want regarding the component itself.
 */
public abstract class DrawableComponent extends Drawable{

    protected Context mContext;

    protected String mComponentName;

    protected Paint mTextPaint;
    protected Paint mPaint;

    protected Rect mDrawingBounds;

    protected DrawableIOGates mGateDrawer;

    public DrawableComponent(Context context, int inputNumber, int outputNumber, String componentName){
        mContext = context;
        mComponentName = componentName;
        mTextPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mDrawingBounds = new Rect();
        mGateDrawer = new DrawableIOGates(context, inputNumber, outputNumber);
    }

    @Override
    public void draw(Canvas canvas) {
        mGateDrawer.draw(canvas);
    }

    public String getComponentName() {
        return mComponentName;
    }

    public void setComponentName(String componentName) {
        mComponentName = componentName;
    }

    public void setInputOutputGateNumbers(int inputNumber, int outputNumber){
        mGateDrawer.setGateNumbers(inputNumber, outputNumber);
    }
}
