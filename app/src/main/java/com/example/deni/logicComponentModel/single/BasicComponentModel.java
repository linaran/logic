package com.example.deni.logicComponentModel.single;

import android.content.Context;
import android.graphics.Point;
import android.graphics.PointF;
import android.util.Log;

import com.example.deni.connect.Connect;
import com.example.deni.connect.Line;
import com.example.deni.globalUtility.Constants;

import java.util.ArrayList;

/**
 * Abstract class for BasicComponentModel. Class is regarded <br>
 * as model if it calculates data for views and controllers for the app.<br>
 * Warning: every class implementing this abstract model MUST offer a constructor with
 * and without component position.
 */
public abstract class BasicComponentModel{
    protected Context mContext;

    protected int mWidth;
    protected int mHeight;

    protected PointF mPosition;

    protected int mInputGateCount;
    protected int mOutputGateCount;

    protected int mMinInputGateCount;
    protected int mMaxInputGateCount;
    protected int mMinOutputGateCount;
    protected int mMaxOutputGateCount;

    protected ArrayList<InOut> mInputInOuts;
    protected ArrayList<InOut> mOutputInOuts;

    /**
     * Constructor without position for manual schematic creation.
     * @param context
     */
    public BasicComponentModel(Context context, Point inputLimiters, Point outputLimiters){
        init(context, inputLimiters, outputLimiters);
        mPosition = new PointF(0, 0);

        initGates();
    }

    /**
     * Constructor with position for automatic schematic creation.
     * @param context
     * @param position
     */
    public BasicComponentModel(Context context, PointF position, Point inputLimiters, Point outputLimiters){
        init(context, inputLimiters, outputLimiters);
        mPosition = new PointF(position.x, position.y);

        initGates();
    }

    private void init(Context context, Point inputLimiters, Point outputLimiters){
        mContext = context;
        mMinInputGateCount = inputLimiters.x;
        mMaxInputGateCount = inputLimiters.y;
        mMinOutputGateCount = outputLimiters.x;
        mMaxOutputGateCount = outputLimiters.y;

        mInputGateCount = mMinInputGateCount;
        mOutputGateCount = mMinOutputGateCount;

        mWidth = BasicComponentDefault.N_WIDTH.getValue() * Constants.SCHEME_OFFSET.getDPValue(mContext);
        int maxGateCount = Math.max(mInputGateCount, mOutputGateCount);
        if(maxGateCount <= 3){
            mHeight = BasicComponentDefault.N_HEIGHT.getValue() * Constants.SCHEME_OFFSET.getDPValue(mContext);
        } else {
            mHeight = (maxGateCount + 1) * Constants.SCHEME_OFFSET.getDPValue(mContext);
        }

        mInputInOuts = new ArrayList<>();
        mOutputInOuts = new ArrayList<>();
    }

    private void initGates(){
        for(int i = 0; i < mInputGateCount; i++){
            mInputInOuts.add(
                    new InOut(InOut.GateType.Input, calculateGatePosition(i, InOut.GateType.Input), this)
            );
        }
        for(int i = 0; i < mOutputGateCount; i++){
            mOutputInOuts.add(
                    new InOut(InOut.GateType.Output, calculateGatePosition(i, InOut.GateType.Output), this)
            );
        }
    }

    private void updateGatesPosition(){
        if(mInputInOuts.size() != 0){
            for(int i = 0; i < mInputGateCount; i++){
                mInputInOuts.get(i).setPosition(calculateGatePosition(i, InOut.GateType.Input));
            }
        }
        if (mOutputInOuts.size() != 0) {
            for(int i = 0; i < mOutputGateCount; i++){
                mOutputInOuts.get(i).setPosition(calculateGatePosition(i, InOut.GateType.Output));
            }
        }
    }

    public void clearAllInOutLines(){
        for(InOut inOut : mInputInOuts){
            inOut.killConnection();
        }
        for(InOut inOut : mOutputInOuts){
            inOut.killConnection();
        }
    }

    /**
     * Method is to be called twice. Once with input and once with output
     * for a component that is being moved by user. Lines need to be hidden
     * from database so AStar algorithm doesn't try to avoid lines that haven't
     * been moved yet. THIS METHOD MUST BE USED  WITH updateAllLinesFromGates!!
     * NOTE: Lines will re-add themselves after AStar is done with them.
     * @param inOuts Input or Output for component.
     */
    private void hideLines(ArrayList<InOut> inOuts){
        for (InOut inOut : inOuts){
            for (Line line : inOut.getLines()){
                Connect.getInstance().getLines().remove(line);
            }
        }
    }

    /**
     * This method should be called when in case user moved the component.
     * When the component was moved line paths must be recalculated.
     */
    public void updateAllLinesFromGates(){
        hideLines(mInputInOuts);
        hideLines(mOutputInOuts);

        Log.d("LINES", Connect.getInstance().getLines().toString() + "\n" + Connect.getInstance().getLines().size());

        for(InOut inOut : mInputInOuts){
            if(inOut.countObservers() != 0){
                inOut.updateConnection();
            }
        }
        for (InOut inOut : mOutputInOuts){
            if(inOut.countObservers() != 0){
                inOut.updateConnection();
            }
        }
    }

    /**
     * Abstract method for executing BasicComponent. Method
     * should take some values from inputs, process them and
     * set some values on output.
     */
    public abstract void execute();

    public PointF getPosition() {
        return mPosition;
    }

    public void setPosition(PointF position) {
        mPosition = position;
        updateGatesPosition();
    }

    public int getWidth() {
        return mWidth;
    }

    public void setWidth(int width) {
        mWidth = width;
    }

    public int getHeight() {
        return mHeight;
    }

    public void setHeight(int height) {
        mHeight = height;
    }

    public int getInputGateCount() {
        return mInputGateCount;
    }

    /**
     * Takes normal array index.
     * @param index
     * @param gateType
     * @return
     */
    private PointF calculateGatePosition(int index, InOut.GateType gateType){
        int offset = Constants.SCHEME_OFFSET.getDPValue(mContext);
        if(gateType == InOut.GateType.Input){
            return new PointF(
                    mPosition.x,
                    mPosition.y + ((index + 1) * offset)
            );
        } else {
            return new PointF(
                    mPosition.x + getWidth(),
                    mPosition.y + ((index + 1) * offset)
            );
        }
    }

    private void recountGates(int gateCount, ArrayList<InOut> inOuts, InOut.GateType gateType){
        int oldSize = inOuts.size();
        if(gateCount < oldSize){
            for(int i = 0; i < oldSize - gateCount; i++){
                inOuts.get(inOuts.size() - 1).killConnection();
                inOuts.remove(inOuts.size() - 1);
            }
        } else if (gateCount > oldSize){
            for(int i = 0; i < gateCount - oldSize; i++){
                inOuts.add(new InOut(gateType, calculateGatePosition(i + oldSize, gateType), this));
            }
        }
    }

    public void setInputGateCount(int inputGateCount) {
        if(inputGateCount < mMinInputGateCount){
            inputGateCount = mMinInputGateCount;
        }
        if(inputGateCount > mMaxInputGateCount){
            inputGateCount = mMaxInputGateCount;
        }
        mInputGateCount = inputGateCount;
        recountGates(mInputGateCount, mInputInOuts, InOut.GateType.Input);
    }

    public int getOutputGateCount() {
        return mOutputGateCount;
    }

    public void setOutputGateCount(int outputGateCount) {
        if(outputGateCount < mMinOutputGateCount){
            outputGateCount = mMinOutputGateCount;
        }
        if(outputGateCount > mMaxOutputGateCount){
            outputGateCount = mMaxOutputGateCount;
        }
        mOutputGateCount = outputGateCount;
        recountGates(mOutputGateCount, mOutputInOuts, InOut.GateType.Output);
    }

    public int getMinInputGateCount() {
        return mMinInputGateCount;
    }

    public int getMaxInputGateCount() {
        return mMaxInputGateCount;
    }

    public int getMinOutputGateCount() {
        return mMinOutputGateCount;
    }

    public int getMaxOutputGateCount() {
        return mMaxOutputGateCount;
    }

    public InOut getInputGate(int index){
        return mInputInOuts.get(index);
    }

    public InOut getOutputGate(int index){
        return mOutputInOuts.get(index);
    }

    public Context getContext() {
        return mContext;
    }
}
