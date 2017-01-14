package com.fr.design.fun.impl;

import com.fr.design.formula.FunctionDefNAD;
import com.fr.design.formula.FunctionGroup;
import com.fr.design.formula.NameAndDescription;
import com.fr.script.FunctionDefContainer;
import com.fr.stable.fun.mark.API;
import com.fr.stable.fun.mark.Mutable;
import com.fr.stable.script.FunctionDef;


@API(level = FunctionDefContainer.CURRENT_LEVEL)
public abstract class AbstractFunctionGroup implements Mutable, FunctionGroup,FunctionDefContainer {
	
	@Override
	public int currentAPILevel() {
		return FunctionDefContainer.CURRENT_LEVEL;
	}
	
	@Override
	public String mark4Provider() {
		return getClass().getName();
	}
	
	@Override
	public NameAndDescription[] getDescriptions() {
		FunctionDef[] funcs = getFunctionDefs();
		int count = funcs.length;
        FunctionDefNAD[] nads = new FunctionDefNAD[count];
        for (int i = 0; i < count; i ++) {
            nads[i] = new FunctionDefNAD(funcs[i]);
        }
        return nads;
	}
	
	@Override
	public FunctionDef[]  getFunctionDefs(){
		return new FunctionDef[0];
	}
}
