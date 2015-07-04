package com.example.deni.logic;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.text.InputFilter;
import android.text.Spanned;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.deni.globalUtility.Constants;

import java.util.ArrayList;

import hr.fer.zemris.bool.fimpl.util.FunctionContext;

/**
 *
 */
public class GenerateDialog extends DialogFragment{

    private EditText mEditText;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final View v = getActivity().getLayoutInflater().inflate(R.layout.dialog_menu_generate, null);
        mEditText = ((EditText) v.findViewById(R.id.boolean_input));
        mEditText.setFilters(new InputFilter[]{
                new InputFilter() {
                    @Override
                    public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                        if (source.equals("")) {
                            return source;
                        }
                        if (source.toString().matches("[A-Z~+]+")) {
                            return source;
                        }
                        return "";
                    }
                }
        });

        Button notButton = (Button)v.findViewById(R.id.generate_button_not_literal);
        notButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditText.setText(mEditText.getText().toString() + "~");
            }
        });

        Button orButton = (Button)v.findViewById(R.id.generate_button_or);
        orButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mEditText.setText(mEditText.getText() + "+");
            }
        });

//        AnotherButton
        Button anotherButton = (Button)v.findViewById(R.id.generate_button_another);
        anotherButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = mEditText.getText().toString();
                if (text.equals("")){ return; }
                FunctionContext.getInstance().createFromString(text);
                mEditText.setText("");
            }
        });

        return new AlertDialog.Builder(getActivity())
                .setView(v)
                .setTitle(R.string.dialog_generate_title)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
//                        Get values from something and make list of enums.
                        ArrayList<String> netList = ((ArrayList<String>) FunctionContext.getInstance().generateMinimizedNetList());

                        Intent i = new Intent(getActivity(), SchemeActivity.class);
                        float dimension = Constants.MAX_SCHEME_N.getValue() * Constants.SCHEME_OFFSET.getDPValue(getActivity());
                        i.putExtra(SchemeFragment.EXTRA_SCHEME_DIM_X, dimension);
                        i.putExtra(SchemeFragment.EXTRA_SCHEME_DIM_Y, dimension);
                        i.putExtra(SchemeFragment.NET_LIST, netList);
                        startActivity(i);
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
