package com.example.deni.logicComponentModel.single.specific;

import android.content.Context;
import android.graphics.Point;
import android.graphics.PointF;

import com.example.deni.logicComponentModel.single.BasicComponentModel;

/**
 *
 */
public class NOTModel extends BasicComponentModel {

    public NOTModel(Context context){
        super(
                context,
                new Point(1, 1),
                new Point(1, 1)
        );
    }

    public NOTModel(Context context, PointF position){
        super(
                context,
                position,
                new Point(1, 1),
                new Point(1, 1)
        );
    }

    @Override
    public void execute() {
    	if(mInputInOuts.get(0).getValue() != null){
    		mOutputInOuts.get(0).setValue(!mInputInOuts.get(0).getValue());
    	} else {
            mOutputInOuts.get(0).setValue(null);
        }
    }
}
