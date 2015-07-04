package com.example.deni.connect;

import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.deni.logicComponentModel.single.InOut;
import com.example.deni.logicComponentView.BasicComponentView;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * Singleton class responsible for managing line connections on SchemeView.
 * Bad design to avoid serialization parcelables and speed up development.
 */
public class Connect {

    private ArrayList<BasicComponentView> mComponents;

    private Set<Line> mLines;
    private InOut mSelectedInputInOut;
    private InOut mSelectedOutputInOut;

    private static Connect instance;
    public static Connect getInstance() {
        if (Connect.instance == null){
            Connect.instance = new Connect();
        }
        return Connect.instance;
    }
    private Connect(){
        mComponents = new ArrayList<>();
        mLines = new HashSet<>();
        mSelectedInputInOut = null;
        mSelectedOutputInOut = null;
    }

    public ArrayList<BasicComponentView> getComponentViews() {
        return mComponents;
    }

    public Set<Line> getLines() {
        return mLines;
    }

    public InOut getSelectedInputInOut() {
        return mSelectedInputInOut;
    }

    public void setSelectedInputInOut(InOut selectedInputInOut) {
        mSelectedInputInOut = selectedInputInOut;
    }

    public InOut getSelectedOutputInOut() {
        return mSelectedOutputInOut;
    }

    public void setSelectedOutputInOut(InOut selectedOutputInOut) {
        mSelectedOutputInOut = selectedOutputInOut;
    }

    /**
     * This method should be called only with existing gates.
     * Never create a new gate inside this method!
     * @param selectedInputInOut
     * @param selectedOutputInOut
     */
    public void connectComponents(InOut selectedOutputInOut, InOut selectedInputInOut){
        if (selectedInputInOut.countObservers() != 0){
            mSelectedInputInOut = null;
            Toast toast = Toast.makeText(
                    getComponentViews().get(0).getContext(),
                    "Input gate already taken",
                    Toast.LENGTH_SHORT
            );
            toast.show();
            return;
        }

        Line line;
        try{
            line = new Line(selectedOutputInOut, selectedInputInOut, mComponents.get(0).getContext());
        } catch (UnsupportedOperationException e){
            mSelectedInputInOut = null;
            Toast toast = Toast.makeText(
                    getComponentViews().get(0).getContext(),
                    "Unable to find a valid path for connection.",
                    Toast.LENGTH_SHORT
            );
            toast.show();
            return;
        }

        mLines.add(line);

        selectedOutputInOut.getLines().add(line);
        selectedInputInOut.getLines().add(line);

//        mSelectedInputGate.getOtherGates().add(mSelectedOutputGate);
//        mSelectedOutputGate.getOtherGates().add(mSelectedInputGate);

        clearSelection();
        Log.d("LINES", mLines.toString() + "\n" + mLines.size());

//        Hackaround for forcing layout to draw the new line.
        ((View)mComponents.get(0).getParent()).invalidate();
    }

    public void clearSelection(){
        mSelectedInputInOut = null;
        mSelectedOutputInOut = null;
    }

    /**
     * Method wipes singleton responsible for connecting
     * components in SchemeView. Should be called only
     * when SchemeView is being destroyed and I already
     * called it there so there are
     * no obvious reasons to use this method.
     */
    public void wipe(){
        instance = null;
    }
}
