package com.example.deni.logicComponentView;

import com.example.deni.logicComponentDrawable.DrawableOUT;
import com.example.deni.logicComponentModel.single.BasicComponentModel;
import com.example.deni.logicComponentModel.single.OUTModel;
import com.example.deni.logicComponentView.logicViewComponentsFactory.BasicComponent;

/**
 *
 */
public class OUTView extends BasicComponentView {
    public OUTView(BasicComponentModel component){
        super(component, new DrawableOUT(
                component.getContext(),
                component.getInputGateCount(),
                component.getOutputGateCount(),
                (OUTModel)component
        ));
        ((OUTModel) component).setUpdateView(this);
        this.setTag(BasicComponent.OUT);
    }
}
