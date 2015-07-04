package com.example.deni.logic;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.deni.globalUtility.Constants;

/**
 * Dialog which opens when user is creating a new scheme.
 */
public class NewSchemeDialog extends DialogFragment{

    private float mWidth;
    private float mHeight;

    private SeekBar mWidthSeekBar;
    private SeekBar mHeightSeekBar;
    private TextView mWidthTextView;
    private TextView mHeightTextView;

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        View v = getActivity().getLayoutInflater().inflate(R.layout.dialog_menu_new, null);

        mWidthSeekBar = (SeekBar)v.findViewById(R.id.newWidthSeekBar);
        mHeightSeekBar = (SeekBar)v.findViewById(R.id.newHeightSeekBar);
        mWidthTextView = (TextView)v.findViewById(R.id.newWidthTextView);
        mHeightTextView = (TextView)v.findViewById(R.id.newHeightTextView);

        mWidthSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int mSetDimension;

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mSetDimension = progress * Constants.SCHEME_OFFSET.getValue();
                mWidthTextView.setText(mSetDimension + " dp");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mWidth = mSetDimension;
            }
        });

        mHeightSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int mSetDimension;
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                mSetDimension = progress* Constants.SCHEME_OFFSET.getValue();
                mHeightTextView.setText(mSetDimension + " dp");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mHeight = mSetDimension;
            }
        });

        return new AlertDialog.Builder(getActivity())
                .setView(v)
                .setTitle(R.string.dialog_new_title)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent i = new Intent(getActivity(), SchemeActivity.class);

                        if (mWidth == 0) {
                            mWidth = Constants.DEF_SCHEME_X.getValue();
                        }
                        if (mHeight == 0) {
                            mHeight = Constants.DEF_SCHEME_Y.getValue();
                        }

                        i.putExtra(SchemeFragment.EXTRA_SCHEME_DIM_X, mWidth);
                        i.putExtra(SchemeFragment.EXTRA_SCHEME_DIM_Y, mHeight);
                        startActivity(i);
                    }
                })
                .create();
    }
}
