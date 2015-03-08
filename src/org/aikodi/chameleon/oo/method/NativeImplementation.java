package org.aikodi.chameleon.oo.method;

import org.aikodi.chameleon.core.validation.Valid;
import org.aikodi.chameleon.core.validation.Verification;
import org.aikodi.chameleon.oo.statement.Block;

/**
 * @author Marko van Dooren
 */
public class NativeImplementation extends Implementation {


	@Override
   protected NativeImplementation cloneSelf() {
    return new NativeImplementation();
  }

  @Override
public boolean compatible() {
    return true;
  }
  
  @Override
public Block getBody() {
    return null;
  }

	@Override
	public Verification verifySelf() {
		return Valid.create();
	}

	@Override
	public boolean complete() {
		return true;
	}

}
