package com.example.deni.logicComponentView;

import com.example.deni.logicComponentDrawable.DrawableIN;
import com.example.deni.logicComponentModel.single.BasicComponentModel;
import com.example.deni.logicComponentView.logicViewComponentsFactory.BasicComponent;

/**
 *
 */
public class INView extends BasicComponentView {
    public INView(BasicComponentModel component){
        super(component, new DrawableIN(
                component.getContext(),
                component.getInputGateCount(),
                component.getOutputGateCount()
        ));
        this.setTag(BasicComponent.IN);
    }
}
