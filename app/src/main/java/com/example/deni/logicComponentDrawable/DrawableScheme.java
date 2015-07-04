package com.example.deni.logicComponentDrawable;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.drawable.Drawable;

import com.example.deni.globalUtility.Constants;
import com.example.deni.globalUtility.DeviceDimensionsHelper;
import com.example.deni.logicViewScheme.SchemeContext;

/**
 * Created by Deni on 3.3.2015..
 */
public class DrawableScheme extends Drawable {
    Context mContext;

    /**Line color*/
    Paint mBlack;

    /**Scheme dimensions*/
    PointF mSchemeDimensions;
    PointF mStartPoint;
    float mLineOffset;

    public DrawableScheme(Context context){
        mContext = context;
        mBlack = new Paint(Paint.ANTI_ALIAS_FLAG);
        mBlack.setColor(Color.parseColor("#D3D3D3"));

        mSchemeDimensions = new PointF();
        mStartPoint = new PointF();

//        Unsafe code : SchemeContext.width is not null because of fragment lifecycle!!
        mSchemeDimensions.x = DeviceDimensionsHelper.convertDpToPixel(SchemeContext.width, mContext);
        mSchemeDimensions.y = DeviceDimensionsHelper.convertDpToPixel(SchemeContext.height, mContext);
//        Unsafe code

        mStartPoint.x = 0;
        mStartPoint.y = 0;
        mLineOffset = Constants.SCHEME_OFFSET.getDPValue(mContext);
    }

    /**
     * Use DeviceDimensionHelper.convertDpToPixel to achieve screen density independence.
     * @param canvas
     */
    @Override
    public void draw(Canvas canvas) {
        gridMaker(canvas);
    }

    private void gridMaker(Canvas canvas){
        for(int i = 0; i <= mSchemeDimensions.x; i += mLineOffset){
            canvas.drawLine(mStartPoint.x + i, mStartPoint.y, mStartPoint.x + i, mStartPoint.y + mSchemeDimensions.y, mBlack);
        }
        for(int i = 0; i <= mSchemeDimensions.y; i += mLineOffset){
            canvas.drawLine(mStartPoint.x, mStartPoint.y + i, mStartPoint.x + mSchemeDimensions.x, mStartPoint.y + i, mBlack);
        }
    }

    @Override
    public void setAlpha(int alpha) {
        alpha = 255;
    }

    @Override
    public void setColorFilter(ColorFilter cf) {
        //no Idea
    }

    @Override
    public int getOpacity() {
        return 0;
    }
}
