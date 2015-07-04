package com.example.deni.logicComponentView.logicViewComponentsFactory;

import android.content.Context;

import com.example.deni.logicComponentModel.single.OUTModel;
import com.example.deni.logicComponentModel.single.specific.ANDModel;
import com.example.deni.logicComponentModel.single.INModel;
import com.example.deni.logicComponentModel.single.specific.NANDModel;
import com.example.deni.logicComponentModel.single.specific.NORModel;
import com.example.deni.logicComponentModel.single.specific.NOTModel;
import com.example.deni.logicComponentModel.single.specific.ORModel;
import com.example.deni.logicComponentModel.single.specific.XORModel;
import com.example.deni.logicComponentView.OUTView;
import com.example.deni.logicComponentView.specific.ANDView;
import com.example.deni.logicComponentView.BasicComponentView;
import com.example.deni.logicComponentView.INView;
import com.example.deni.logicComponentView.specific.NANDView;
import com.example.deni.logicComponentView.specific.NORView;
import com.example.deni.logicComponentView.specific.NOTView;
import com.example.deni.logicComponentView.specific.ORView;
import com.example.deni.logicComponentView.specific.XORView;


public class BasicComponentFactory {

    public BasicComponentView create(BasicComponent component, Context context){
        switch (component){
            case AND:
                return new ANDView(new ANDModel(context));
            case OR:
                return new ORView(new ORModel(context));
            case IN:
                return new INView(new INModel(context));
            case OUT:
                return new OUTView(new OUTModel(context));
            case NOT:
                return new NOTView(new NOTModel(context));
            case XOR:
                return new XORView(new XORModel(context));
            case NOR:
                return new NORView(new NORModel(context));
            case NAND:
                return new NANDView(new NANDModel(context));
            default:
                throw new EnumConstantNotPresentException(BasicComponent.class, "Enum not supported!");
        }
    }
}
