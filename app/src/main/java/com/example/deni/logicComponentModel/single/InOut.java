package com.example.deni.logicComponentModel.single;

import android.graphics.PointF;

import com.example.deni.connect.Line;

import java.util.ArrayList;
import java.util.Observable;
import java.util.UUID;

/**
 *
 */
public class InOut extends Observable{

    public enum GateType { Input, Output }

//    mValue is allowed to be null
    private Boolean mValue;
    private GateType mGateType;

    /**Convenience for hashing*/
    private UUID mId;

    /**Convenience for path algorithms*/
    private PointF mPosition;

    private BasicComponentModel mComponentModel;

    /**Weird design -- Needed to hide all lines that are being moved so AStar can work properly*/
    private ArrayList<Line> mLines;

    public InOut(GateType gateType, PointF position, BasicComponentModel componentModel){
        init(gateType, null, position, componentModel);
    }

    /**Likely not going to use this constructor*/
    public InOut(GateType gateType, Boolean value, PointF position, BasicComponentModel componentModel){
        init(gateType, value, position, componentModel);
    }

    public void init(GateType gateType, Boolean value, PointF position, BasicComponentModel componentModel){
        mGateType = gateType;
        mValue = value;
        mPosition = position;
        mComponentModel = componentModel;
        mId = UUID.randomUUID();
        mLines = new ArrayList<>();
    }

    public PointF getPosition() {
        return mPosition;
    }

    public void setPosition(PointF position) {
        mPosition = position;
    }

    public Boolean getValue() {
        return mValue;
    }

    /**
     * Method sets boolean value for gate.
     * If gate is input then gate will tell its component to begin execute
     * and set new values for output gates.
     * @param value
     */
    public void setValue(Boolean value) {
        mValue = value;
        if (mGateType.equals(GateType.Input)){
            mComponentModel.execute();
        }
        if(mGateType.equals(GateType.Output)){
            updateValueOnConnection();
        }
    }

    public GateType getGateType() {
        return mGateType;
    }

    public BasicComponentModel getComponentModel() {
        return mComponentModel;
    }

    public ArrayList<Line> getLines() {
        return mLines;
    }

//    public ArrayList<ArrayList<PointF>> getPaths(){
//        ArrayList<ArrayList<PointF>> retValue = new ArrayList<>();
//
//        for (Line line : mLines){
//            retValue.add(line.getLinePath());
//        }
//
//        return retValue;
//    }

    @Override
    public String toString(){
        return mPosition.toString();
    }

    /**
     * When removing BasicComponentView from SchemeView this method
     * should be called to notify all of Line classes that use it
     * in their connections to remove themselves as well.
     */
    public void killConnection(){
        setChanged();
        notifyObservers(Line.Task.Remove);
    }

    /**
     * This method should called to inform the line
     * that IN component changed its value.
     */
    public void updateValueOnConnection(){
        setChanged();
        notifyObservers(Line.Task.UpdateValue);
    }

    /**
     * Method should be called when component on SchemeView
     * changes position to recalculate line path for all
     * connections.
     */
    public void updateConnection(){
        setChanged();
        notifyObservers(Line.Task.AStar);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        InOut inOut = (InOut) o;

        return mId.equals(inOut.mId);
    }

    @Override
    public int hashCode() {
        return mId.hashCode();
    }
}
