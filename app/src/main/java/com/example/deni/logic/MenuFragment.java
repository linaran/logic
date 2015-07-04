package com.example.deni.logic;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

/**
 * Fragment that handles basic menu.
 */
public class MenuFragment extends Fragment {
//    public static MenuFragment newInstance(args){
//        Bundle args = new Bundle();
//        args.put...
//
//        Menu fragment = new LogicFragment();
//        fragment.setArguments(args);
//
//        return fragment;
//    }

    public static final String DIALOG_NEW = "DialogNew";
    private static final String DIALOG_GENERATE = "DialogGenerate";


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_menu, container, false);

        Button newButton = (Button)v.findViewById(R.id.menu_new);
        newButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getActivity().getSupportFragmentManager();
                NewSchemeDialog dialog = new NewSchemeDialog();
                dialog.show(fm, DIALOG_NEW);
            }
        });

        Button generateButton = (Button)v.findViewById(R.id.menu_generate);
        generateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getActivity().getSupportFragmentManager();
                DialogFragment dialog = new GenerateDialog();
                dialog.show(fm, DIALOG_GENERATE);
            }
        });

        return v;
    }
}
