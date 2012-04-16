package chameleon.support.expression;

import chameleon.core.lookup.LookupException;
import chameleon.oo.expression.Literal;
import chameleon.oo.type.Type;
import chameleon.oo.type.TypeReference;
import chameleon.util.association.Single;

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
	private Single<TypeReference> _typeReference = new Single<TypeReference>(this);

  
  public TypeReference getTypeReference() {
    return _typeReference.getOtherEnd();
  }
  
  public void setTypeReference(TypeReference type) {
    set(_typeReference,type);
  }
  
  protected Type actualType() throws LookupException {
  	return getTypeReference().getType();
  }
  
}
