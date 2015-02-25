package org.aikodi.chameleon.support.statement;

import org.aikodi.chameleon.core.validation.Valid;
import org.aikodi.chameleon.core.validation.Verification;

/**
 * @author Marko van Dooren
 */
public class DefaultLabel extends SwitchLabel {

  public DefaultLabel() {
	}

  @Override
protected DefaultLabel cloneSelf() {
    return new DefaultLabel();
  }
  
	@Override
	public Verification verifySelf() {
		return Valid.create();
	}
}
