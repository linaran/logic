package com.example.deni.logicViewScheme;

import com.example.deni.logicComponentView.BasicComponentView;

/**
 * Class was made to pass values to custom SchemeView without
 * much pain. Notice one SchemeContext is active as long as
 * one SchemeView is active. When creating another SchemeView
 * SchemeContext values will be changed.
 */
public class SchemeContext {

    public static float width;
    public static float height;
    public static BasicComponentView componentView;
    public static float scaleFactor = 1;

}
