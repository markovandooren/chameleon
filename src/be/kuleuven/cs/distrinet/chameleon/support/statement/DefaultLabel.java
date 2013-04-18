package be.kuleuven.cs.distrinet.chameleon.support.statement;

import be.kuleuven.cs.distrinet.chameleon.core.validation.Valid;
import be.kuleuven.cs.distrinet.chameleon.core.validation.Verification;

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
	public Verification verifySelf() {
		return Valid.create();
	}
}
