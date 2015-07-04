package com.example.deni.logicComponentDrawable.specific;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;

import com.example.deni.logicComponentDrawable.DrawableComponent;

/**
 *
 */
public class DrawableNOT extends DrawableComponent {

    public DrawableNOT(Context context, int inputNumber, int outputNumber){
        super(context, inputNumber, outputNumber, "NOT");
    }

    /**
     *
     */
    @Override
    public void draw(Canvas canvas) {
        mDrawingBounds.set(
                canvas.getWidth() / 10,
                0,
                canvas.getWidth() - canvas.getWidth() / 10,
                canvas.getHeight()
        );

        mPaint.setColor(Color.WHITE);
        mPaint.setStrokeWidth(3);
        mTextPaint.setColor(Color.BLACK);
        mTextPaint.setTextSize(canvas.getWidth() * 0.2f);
        mTextPaint.setTextAlign(Paint.Align.CENTER);

//        canvas.drawPaint(mPaint);
        canvas.drawRect(mDrawingBounds, mPaint);
//        canvas.drawLine(0, 0, canvas.getWidth(), canvas.getHeight(), mTextPaint);
        canvas.drawText(
                mComponentName,
                canvas.getWidth() / 2f,
                canvas.getHeight() / 2f - (mTextPaint.descent() + mTextPaint.ascent()) / 2f,
                mTextPaint
        );
        super.draw(canvas);
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
