package com.example.deni.logicComponentView.specific;

import com.example.deni.logicComponentDrawable.specific.DrawableOR;
import com.example.deni.logicComponentModel.single.BasicComponentModel;
import com.example.deni.logicComponentView.BasicComponentView;
import com.example.deni.logicComponentView.logicViewComponentsFactory.BasicComponent;

/**
 *
 */
public class ORView extends BasicComponentView {
    public ORView(BasicComponentModel component) {
        super(component, new DrawableOR(
                component.getContext(),
                component.getInputGateCount(),
                component.getOutputGateCount()
        ));
        this.setTag(BasicComponent.OR);
    }
}
