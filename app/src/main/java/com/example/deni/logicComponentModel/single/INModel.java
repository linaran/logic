package com.example.deni.logicComponentModel.single;

import android.content.Context;
import android.graphics.Point;
import android.graphics.PointF;

/**
 *
 */
public class INModel extends BasicComponentModel{

    public INModel(Context context){
        super(
                context,
                new Point(0, 0),
                new Point(1, 1)
        );
        init(null);
        getOutputGate(0).setValue(false);
    }

    public INModel(Context context, PointF position){
        super(
                context,
                position,
                new Point(0, 0),
                new Point(1, 1)
        );
        init(position);
        getOutputGate(0).setValue(false);
    }

    private void init(PointF position){
        mPosition = position;
    }

    @Override
    public void execute() {}
}
