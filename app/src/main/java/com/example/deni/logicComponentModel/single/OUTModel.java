package com.example.deni.logicComponentModel.single;

import android.content.Context;
import android.graphics.Point;
import android.graphics.PointF;
import android.util.Log;

import com.example.deni.connect.Connect;
import com.example.deni.logicComponentView.BasicComponentView;

/**
 *
 */
public class OUTModel extends BasicComponentModel{

    private BasicComponentView mComponentView;

    /**
     * Call only once and only once and I called it already.
     * @param componentView
     */
    public void setUpdateView(BasicComponentView componentView){
        mComponentView = componentView;
    }

    public OUTModel(Context context){
        super(
                context,
                new Point(1, 1),
                new Point(0, 0)
        );
    }

    public OUTModel(Context context, PointF position){
        super(
                context,
                position,
                new Point(1, 1),
                new Point(0, 0)
        );
    }

    @Override
    public void execute() {
//        Hackaround to avoid massive refactoring.
        mComponentView.invalidate();
    }
}
