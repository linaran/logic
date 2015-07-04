package com.example.deni.logicComponentView.specific;

import com.example.deni.logicComponentDrawable.specific.DrawableNOR;
import com.example.deni.logicComponentModel.single.BasicComponentModel;
import com.example.deni.logicComponentView.BasicComponentView;
import com.example.deni.logicComponentView.logicViewComponentsFactory.BasicComponent;

/**
 *
 */
public class NORView extends BasicComponentView {
    public NORView(BasicComponentModel component) {
        super(component, new DrawableNOR(
                component.getContext(),
                component.getInputGateCount(),
                component.getOutputGateCount()
        ));
        this.setTag(BasicComponent.NOR);
    }
}
