package be.kuleuven.cs.distrinet.chameleon.oo.method;

import java.util.ArrayList;
import java.util.List;

import be.kuleuven.cs.distrinet.chameleon.core.element.Element;
import be.kuleuven.cs.distrinet.chameleon.core.validation.Valid;
import be.kuleuven.cs.distrinet.chameleon.core.validation.VerificationResult;
import be.kuleuven.cs.distrinet.chameleon.oo.statement.Block;

/**
 * @author Marko van Dooren
 */
public class NativeImplementation extends Implementation {


  public NativeImplementation clone() {
    return new NativeImplementation();
  }

  public boolean compatible() {
    return true;
  }
  
  public Block getBody() {
    return null;
  }

	@Override
	public VerificationResult verifySelf() {
		return Valid.create();
	}

	@Override
	public boolean complete() {
		return true;
	}

}
