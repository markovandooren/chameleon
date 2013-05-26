package be.kuleuven.cs.distrinet.chameleon.support.statement;

import be.kuleuven.cs.distrinet.chameleon.core.validation.Valid;
import be.kuleuven.cs.distrinet.chameleon.core.validation.Verification;

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
