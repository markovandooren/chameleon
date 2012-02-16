package chameleon.support.statement;

import java.util.ArrayList;
import java.util.List;

import chameleon.core.element.Element;
import chameleon.core.validation.Valid;
import chameleon.core.validation.VerificationResult;

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
	public LabeledGotoStatement clone() {
		return new LabeledGotoStatement(getLabel());
	}

	public List<Element> children() {
		return new ArrayList<Element>();
	}

	@Override
	public VerificationResult verifySelf() {
		return checkNull(getLabel(), "Missing label", Valid.create());
	}
}
