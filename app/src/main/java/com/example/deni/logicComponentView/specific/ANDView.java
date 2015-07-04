package com.example.deni.logicComponentView.specific;

import com.example.deni.logicComponentDrawable.specific.DrawableAND;
import com.example.deni.logicComponentModel.single.BasicComponentModel;
import com.example.deni.logicComponentView.BasicComponentView;
import com.example.deni.logicComponentView.logicViewComponentsFactory.BasicComponent;

/**
 *
 */
public class ANDView extends BasicComponentView {
    public ANDView(BasicComponentModel component){
        super(component, new DrawableAND(
                component.getContext(),
                component.getInputGateCount(),
                component.getOutputGateCount()
        ));
        this.setTag(BasicComponent.AND);
    }
}
