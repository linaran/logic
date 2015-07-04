package com.example.deni.logicComponentView.specific;

import com.example.deni.logicComponentDrawable.specific.DrawableNOT;
import com.example.deni.logicComponentModel.single.BasicComponentModel;
import com.example.deni.logicComponentView.BasicComponentView;
import com.example.deni.logicComponentView.logicViewComponentsFactory.BasicComponent;

/**
 *
 */
public class NOTView extends BasicComponentView {
    public NOTView(BasicComponentModel component){
        super(component, new DrawableNOT(
                component.getContext(),
                component.getInputGateCount(),
                component.getOutputGateCount()
        ));
        this.setTag(BasicComponent.NOT);
    }
}
