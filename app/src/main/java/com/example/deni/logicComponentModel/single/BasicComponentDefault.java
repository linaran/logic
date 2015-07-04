package com.example.deni.logicComponentModel.single;

public enum BasicComponentDefault {
    N_WIDTH(4),
    N_HEIGHT(4),
    INPUT_COUNT(2),
    OUTPUT_COUNT(1);

    private final int mValue;
    BasicComponentDefault(int value){
        mValue = value;
    }

    public int getValue(){
        return mValue;
    }
}
