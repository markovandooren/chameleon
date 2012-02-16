package chameleon.support.statement;

import java.util.ArrayList;
import java.util.List;

import chameleon.core.element.Element;
import chameleon.core.validation.Valid;
import chameleon.core.validation.VerificationResult;

/**
 * @author Marko van Dooren
 */
public class DefaultLabel extends SwitchLabel {

  public DefaultLabel() {
	}

  public DefaultLabel clone() {
    return new DefaultLabel();
  }
  
  public List<Element> children() {
    return new ArrayList<Element>();
  }

	@Override
	public VerificationResult verifySelf() {
		return Valid.create();
	}
}
