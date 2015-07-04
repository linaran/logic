package com.example.deni.logic;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.View;
import android.widget.NumberPicker;
import android.widget.Toast;

import com.example.deni.connect.Connect;
import com.example.deni.logicViewScheme.SchemeContext;

/**
 *
 */
public class GateOutputDialog extends DialogFragment{

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View v = getActivity().getLayoutInflater().inflate(R.layout.dialog_output_gate, null);
        final NumberPicker numberPicker = (NumberPicker) v.findViewById(R.id.gate_output_pick);

        numberPicker.setMinValue(1);
        numberPicker.setMaxValue(SchemeContext.componentView.getComponent().getOutputGateCount());

        return new AlertDialog.Builder(getActivity())
                .setView(v)
                .setTitle("Connect output gate:")
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Connect.getInstance().setSelectedOutputInOut(
                                SchemeContext.componentView.getComponent().getOutputGate(numberPicker.getValue() - 1)
                        );
                        Log.d("GATE", Connect.getInstance().getSelectedOutputInOut().toString());
                        Toast toast = Toast.makeText(
                                getActivity().getBaseContext(),
                                "Output gate for connection updated",
                                Toast.LENGTH_SHORT
                        );
                        toast.show();
                        if (Connect.getInstance().getSelectedInputInOut() != null) {
                            Connect.getInstance().connectComponents(
                                    Connect.getInstance().getSelectedOutputInOut(),
                                    Connect.getInstance().getSelectedInputInOut()
                            );
//                            SchemeContext.componentView.postInvalidate();
                        }
                        dismiss();
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dismiss();
                    }
                })
                .create();
    }
}
