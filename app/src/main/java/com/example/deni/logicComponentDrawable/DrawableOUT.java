package com.example.deni.logicComponentDrawable;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.util.Log;

import com.example.deni.logicComponentModel.single.OUTModel;

/**
 *
 */
public class DrawableOUT extends DrawableComponent{

    private OUTModel mComponent;

    /**
     * Since DrawableOUT needs to display final value of the scheme
     * it requires a reference to its model.
      * @param context
     * @param inputNumber
     * @param outputNumber
     * @param component
     */
    public DrawableOUT(Context context, int inputNumber, int outputNumber, OUTModel component){
        super(context, inputNumber, outputNumber, null);
        mComponent = component;
        Boolean value = mComponent.getInputGate(0).getValue();
        if (value == null){
            mComponentName = "N";
        } else if (value){
            mComponentName = "true";
        } else {
            mComponentName = "false";
        }
    }

    private String valueGetter(){
        Boolean value = mComponent.getInputGate(0).getValue();
        if (value == null){
            return "N";
        } else if (value){
            return "1";
        } else {
            return "0";
        }
    }

    @Override
    public void draw(Canvas canvas) {
        Log.d("EXE", "LOOL");
        super.draw(canvas);
        mDrawingBounds.set(
                canvas.getWidth()/10,
                0,
                canvas.getWidth() - canvas.getWidth()/10,
                canvas.getHeight()
        );

        mPaint.setColor(Color.WHITE);
        mPaint.setStrokeWidth(3);
        mTextPaint.setColor(Color.BLACK);
        mTextPaint.setTextSize(canvas.getWidth() * 0.2f);
        mTextPaint.setTextAlign(Paint.Align.CENTER);

        canvas.drawRect(mDrawingBounds, mPaint);
//        canvas.drawLine(0, 0, canvas.getWidth(), canvas.getHeight(), mTextPaint);
        canvas.drawText(
                valueGetter(),
                canvas.getWidth() / 2f,
                canvas.getHeight() / 2f - (mTextPaint.descent() + mTextPaint.ascent()) / 2f,
                mTextPaint
        );
    }

    @Override
    public void setAlpha(int alpha) {

    }

    @Override
    public void setColorFilter(ColorFilter cf) {

    }

    @Override
    public int getOpacity() {
        return 0;
    }
}
