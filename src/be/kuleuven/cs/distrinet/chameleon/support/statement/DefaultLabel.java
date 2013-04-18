package be.kuleuven.cs.distrinet.chameleon.support.statement;

import be.kuleuven.cs.distrinet.chameleon.core.validation.Valid;
import be.kuleuven.cs.distrinet.chameleon.core.validation.VerificationResult;

/**
 * @author Marko van Dooren
 */
public class DefaultLabel extends SwitchLabel {

  public DefaultLabel() {
	}

  public DefaultLabel clone() {
    return new DefaultLabel();
  }
  
	@Override
	public VerificationResult verifySelf() {
		return Valid.create();
	}
}
