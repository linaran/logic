package com.example.deni.logicComponentModel.single.specific;

import android.content.Context;
import android.graphics.Point;
import android.graphics.PointF;

import com.example.deni.globalUtility.Constants;
import com.example.deni.logicComponentModel.single.BasicComponentModel;
import com.example.deni.logicComponentModel.single.InOut;

/**
 *
 */
public class NORModel extends BasicComponentModel{

    public NORModel(Context context){
        super(
                context,
                new Point(2, Constants.MAX_GATE_COUNT.getValue()),
                new Point(1, 1)
        );
    }

    public NORModel(Context context, PointF position){
        super(
                context,
                position,
                new Point(2, Constants.MAX_GATE_COUNT.getValue()),
                new Point(1, 1)
        );
    }

    @Override
    public void execute() {
        boolean notNull = false;
        for (InOut inOut : mInputInOuts){
            if(inOut.getValue() == null){ continue; }
            if(inOut.getValue().equals(true)){
                mOutputInOuts.get(0).setValue(false);
                return;
            } else {
                notNull = true;
            }
        }
        if (notNull){
            mOutputInOuts.get(0).setValue(true);
        } else {
            mOutputInOuts.get(0).setValue(null);
        }
    }
}
