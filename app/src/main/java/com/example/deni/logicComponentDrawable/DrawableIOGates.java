package com.example.deni.logicComponentDrawable;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;

import com.example.deni.globalUtility.Constants;
import com.example.deni.logicViewScheme.SchemeContext;

import java.io.Serializable;

/**
 *
 */
public class DrawableIOGates extends Drawable implements Serializable {

    private transient Context mContext;

    private int mInputNumber;
    private int mOutputNumber;

    private Paint mLinePaint;

    public DrawableIOGates(Context context, int inputNumber, int outputNumber){
        mInputNumber = inputNumber;
        mOutputNumber = outputNumber;
        mContext = context;
        mLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mLinePaint.setColor(Color.BLACK);
        mLinePaint.setStrokeWidth(2.5f);
    }

    @Override
    public void draw(Canvas canvas) {
        int inputX = canvas.getWidth()/10;
        int outputX = canvas.getWidth() - canvas.getWidth()/10;

        float offset = SchemeContext.scaleFactor * Constants.SCHEME_OFFSET.getDPValue(mContext);


        if (mInputNumber != 0) {
            for(int i = 1; i <= mInputNumber; i++){
                canvas.drawLine(0, i*offset, inputX, i*offset, mLinePaint);
            }
        }
        if (mOutputNumber != 0) {
            for(int i = 1; i <= mOutputNumber; i++){
                canvas.drawLine(outputX, i*offset, canvas.getWidth(), i*offset, mLinePaint);
            }
        }
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

    public void setGateNumbers(int inputNumber, int outputNumber){
        mInputNumber = inputNumber;
        mOutputNumber = outputNumber;
    }

    /**
     * This method should be called after deserialization.
     * @param context nuisance
     */
    public void rebuild (Context context) {
        mContext = context;
        mLinePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mLinePaint.setColor(Color.BLACK);
        mLinePaint.setStrokeWidth(2.5f);
    }
}
