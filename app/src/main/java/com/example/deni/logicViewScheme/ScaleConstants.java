package com.example.deni.logicViewScheme;

/**
 * All units should be converted using
 * DeviceDimensionHelper.convertDptoPx.
 */
public enum ScaleConstants {
    MINSCALE (0.93f),
    MAXSCALE (5.0f);

    private final float mValue;
    ScaleConstants(float value){
        mValue = value;
    }

    public float getValue(){
        return mValue;
    }
}
