package com.example.deni.globalUtility;

import android.content.Context;


public enum Constants {
    MAX_GATE_COUNT(10),
    SCHEME_OFFSET(17),
    DEF_SCHEME_X(500),
    DEF_SCHEME_Y(500),
    MAX_SCHEME_N(200),
    CIRCLE_IGNORE(-2000),
    VIBRATE(70);

    private final int mValue;
    Constants(int value){
        mValue = value;
    }

    public int getValue(){
        return mValue;
    }
    public int getDPValue(Context context){
        return (int)DeviceDimensionsHelper.convertDpToPixel(mValue, context);
    }
}
