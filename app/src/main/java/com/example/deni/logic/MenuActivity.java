package com.example.deni.logic;

import android.support.v4.app.Fragment;

/**
 * Activity that holds basic menu.
 */
public class MenuActivity extends SingleFragmentActivity{

    @Override
    protected Fragment createFragment() {
        return new MenuFragment();
    }
}
