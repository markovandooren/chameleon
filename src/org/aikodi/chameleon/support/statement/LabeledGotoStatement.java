package org.aikodi.chameleon.support.statement;

import org.aikodi.chameleon.core.validation.Valid;
import org.aikodi.chameleon.core.validation.Verification;

public class LabeledGotoStatement extends GotoStatement{

	public LabeledGotoStatement(String label) {
		setLabel(label);
	}
	
	private String _label;
	
	public String getLabel() {
		return _label;
	}
	
	/*@
	  @ public behavior
	  @
	  @ post getLabel() == label; 
	  @*/
	public void setLabel(String label) {
		_label = label;
	}

	@Override
	protected LabeledGotoStatement cloneSelf() {
		return new LabeledGotoStatement(getLabel());
	}

	@Override
	public Verification verifySelf() {
		return checkNull(getLabel(), "Missing label", Valid.create());
	}
}
