package com.example.deni.logic;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.widget.NumberPicker;

import com.example.deni.logicComponentModel.single.BasicComponentModel;
import com.example.deni.logicViewScheme.SchemeContext;


/**
 *
 */
public class InputOutputDialog extends DialogFragment {

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View v = getActivity().getLayoutInflater().inflate(R.layout.dialog_input_output, null);

        final NumberPicker inputNumberPicker = (NumberPicker) v.findViewById(R.id.input_number_picker);
        final NumberPicker outputNumberPicker = (NumberPicker) v.findViewById(R.id.output_number_picker);
        final BasicComponentModel component = SchemeContext.componentView.getComponent();

        inputNumberPicker.setMinValue(component.getMinInputGateCount());
        inputNumberPicker.setMaxValue(component.getMaxInputGateCount());
        outputNumberPicker.setMinValue(component.getMinOutputGateCount());
        outputNumberPicker.setMaxValue(component.getMaxOutputGateCount());

        return new AlertDialog.Builder(getActivity())
                .setView(v)
                .setTitle(R.string.input_output_dialog_title)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
//                        Unsafe code.
                        SchemeContext.componentView.setGateCount(
                                inputNumberPicker.getValue(),
                                outputNumberPicker.getValue()
                        );
//                        Unsafe code.
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
