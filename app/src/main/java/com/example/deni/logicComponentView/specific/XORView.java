package com.example.deni.logicComponentView.specific;

import com.example.deni.logicComponentDrawable.specific.DrawableXOR;
import com.example.deni.logicComponentModel.single.BasicComponentModel;
import com.example.deni.logicComponentView.BasicComponentView;
import com.example.deni.logicComponentView.logicViewComponentsFactory.BasicComponent;

/**
 * Created by Deni on 18.6.2015..
 */
public class XORView extends BasicComponentView{

    public XORView(BasicComponentModel component){
        super(component, new DrawableXOR(
                component.getContext(),
                component.getInputGateCount(),
                component.getOutputGateCount()
        ));
        this.setTag(BasicComponent.XOR);
    }
}
