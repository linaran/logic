package com.example.deni.connect;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.util.Log;
import android.widget.Toast;

import com.example.deni.connect.aStar.AStar;
import com.example.deni.globalUtility.Constants;
import com.example.deni.logicComponentModel.single.InOut;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;
import java.util.Set;

/**
 *
 */
public class Line implements Observer, Serializable{
    private InOut mOutputInOut;
    private InOut mInputInOut;
    private ArrayList<PointF> mLinePath;
//    One day...
//    private ArrayList<ArrayList<PointF>> mOtherPaths;
    private PointF mCircle;
    private Paint mPaint;
    private AStar mAStar;

    /**For looping lines*/
    private boolean mIsLooping;
    private boolean mIsUsed;

    public enum Task{ Remove, AStar , UpdateValue }

    public Line(InOut outputInOut, InOut inputInOut, Context context){
        mCircle = new PointF(Constants.CIRCLE_IGNORE.getValue(), 0);
        mAStar = new AStar(context, mCircle);
        mLinePath = mAStar.aStarSearch(
                outputInOut.getPosition(),
                inputInOut.getPosition()
        );

        mOutputInOut = outputInOut;
        mInputInOut = inputInOut;
        mOutputInOut.addObserver(this);
        mInputInOut.addObserver(this);

        if (mOutputInOut.getComponentModel().equals(mInputInOut.getComponentModel())) {
            mIsLooping = true;
            mIsUsed = false;
        }

        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(Color.BLACK);

//        Newborn line takes value from output gate and sends it to input gate.
        mInputInOut.setValue(mOutputInOut.getValue());
    }

    /**
     * Method should use SchemeView canvas only!
     * @param canvas
     */
    public void drawLine(Canvas canvas){
        for(int i = mLinePath.size() - 1; i > 0; i--){
            canvas.drawLine(
                    mLinePath.get(i).x,
                    mLinePath.get(i).y,
                    mLinePath.get(i-1).x,
                    mLinePath.get(i-1).y,
                    mPaint
            );
        }

//        -2000 is code that circle should not be drawn. This is terrible lol.
        if (mCircle.x != Constants.CIRCLE_IGNORE.getValue()) {
            canvas.drawCircle(
                    mCircle.x,
                    mCircle.y,
                    3,
                    mPaint
            );
        }
    }

    @Override
    public void update(Observable observable, Object data) {
        Set<Line> lines = Connect.getInstance().getLines();
        switch ((Task)data){
            case Remove:
                mInputInOut.setValue(null);
//                mOutputGate.setValue(null);

                mInputInOut.getLines().remove(this);
                mInputInOut.deleteObserver(this);
                mOutputInOut.getLines().remove(this);
                mOutputInOut.deleteObserver(this);

                lines.remove(this);
                Log.d("LINES", lines.toString() + "\n" + lines.size());
                break;
            case AStar:
                try{
                    mLinePath = mAStar.aStarSearch(
                            mOutputInOut.getPosition(),
                            mInputInOut.getPosition()
                    );
                    Connect.getInstance().getLines().add(this);
                    Log.d("LINES", lines.toString() + "\n" + lines.size());
                } catch (UnsupportedOperationException e){
                    this.update(null, Task.Remove);
                    Toast toast = Toast.makeText(
                            Connect.getInstance().getComponentViews().get(0).getContext(),
                            "Some connections discarded.",
                            Toast.LENGTH_SHORT
                    );
                    toast.show();
                }
                break;
            case UpdateValue:
                if (mIsLooping && mIsUsed) {
                    mIsUsed = false;
                    break;
                } else {
                    mIsUsed = true;
                }
                mInputInOut.setValue(mOutputInOut.getValue());
                break;
            default:
                throw new EnumConstantNotPresentException(Task.class, "Task enum not supported.");
        }
    }

    public ArrayList<PointF> getLinePath() {
        return mLinePath;
    }

    public void setLinePath(ArrayList<PointF> linePath) {
        mLinePath = linePath;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Line line = (Line) o;

        return mOutputInOut.equals(line.mOutputInOut) && mInputInOut.equals(line.mInputInOut);
    }

    @Override
    public int hashCode() {
        int result = mOutputInOut.hashCode();
        result = 31 * result + mInputInOut.hashCode();
        return result;
    }
}
