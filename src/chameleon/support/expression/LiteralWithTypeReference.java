package chameleon.support.expression;

import java.util.ArrayList;
import java.util.List;

import org.rejuse.association.SingleAssociation;

import chameleon.core.element.Element;
import chameleon.core.lookup.LookupException;
import chameleon.core.validation.BasicProblem;
import chameleon.core.validation.Valid;
import chameleon.core.validation.VerificationResult;
import chameleon.oo.expression.Literal;
import chameleon.oo.type.Type;
import chameleon.oo.type.TypeReference;
import chameleon.util.Util;

/**
 * @author Marko van Dooren
 */
public abstract class LiteralWithTypeReference extends Literal {
  
  public LiteralWithTypeReference(String value) {
    super(value);
  }

  public LiteralWithTypeReference(String value, TypeReference ref) {
    super(value);
    setTypeReference(ref);
  }
  
	/**
	 * TARGET
	 */
	private SingleAssociation<LiteralWithTypeReference,TypeReference> _typeReference = new SingleAssociation<LiteralWithTypeReference,TypeReference>(this);

  
  public TypeReference getTypeReference() {
    return _typeReference.getOtherEnd();
  }
  
  public void setTypeReference(TypeReference type) {
    setAsParent(_typeReference,type);
  }
  
  protected Type actualType() throws LookupException {
  	return getTypeReference().getType();
  }
  
}
