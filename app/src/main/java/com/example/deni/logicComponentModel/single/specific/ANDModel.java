package com.example.deni.logicComponentModel.single.specific;

import android.content.Context;
import android.graphics.Point;
import android.graphics.PointF;
import android.util.Log;

import com.example.deni.globalUtility.Constants;
import com.example.deni.logicComponentModel.single.BasicComponentModel;
import com.example.deni.logicComponentModel.single.InOut;

/**
 * Model for basic component AND.
 * AND outputs AND operations on all input gate values.
 */
public class ANDModel extends BasicComponentModel {

    public ANDModel(Context context){
        super(
                context,
                new Point(2, Constants.MAX_GATE_COUNT.getValue()),
                new Point(1, 1)
        );
    }

    public ANDModel(Context context, PointF position){
        super(
                context,
                position,
                new Point(2, Constants.MAX_GATE_COUNT.getValue()),
                new Point(1, 1)
        );
    }

    /**
     * AND component implementation. Assumed component has
     * only one output gate.
     */
    @Override
    public void execute() {
        boolean notNull = false;
        for (InOut inOut : mInputInOuts){
            if(inOut.getValue() == null){ continue; }
            if(inOut.getValue().equals(false)){
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
