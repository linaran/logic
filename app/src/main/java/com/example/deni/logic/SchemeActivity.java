package com.example.deni.logic;

import android.support.v4.app.Fragment;

import com.example.deni.globalUtility.Constants;

import java.util.ArrayList;


public class SchemeActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment() {
        float width = getIntent()
                .getFloatExtra(SchemeFragment.EXTRA_SCHEME_DIM_X, (float)Constants.DEF_SCHEME_X.getValue());
        float height = getIntent()
                .getFloatExtra(SchemeFragment.EXTRA_SCHEME_DIM_Y, (float)Constants.DEF_SCHEME_Y.getValue());
        ArrayList<String> netList = getIntent()
                .getStringArrayListExtra(SchemeFragment.NET_LIST);

        return SchemeFragment.newInstance(width, height, netList);
    }

}
