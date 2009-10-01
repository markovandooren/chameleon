package chameleon.core.method;

import java.util.ArrayList;
import java.util.List;

import chameleon.core.element.Element;
import chameleon.core.statement.Block;
import chameleon.core.validation.Valid;
import chameleon.core.validation.VerificationResult;

/**
 * @author Marko van Dooren
 */
public class NativeImplementation extends Implementation<NativeImplementation> {


  public NativeImplementation clone() {
    return new NativeImplementation();
  }

  public boolean compatible() {
    return true;
  }
  
  public List<Element> children() {
    return new ArrayList<Element>();
  }

  public Block getBody() {
    return null;
  }

	@Override
	public VerificationResult verifyThis() {
		return Valid.create();
	}

}
