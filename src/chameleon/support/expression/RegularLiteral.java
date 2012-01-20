package chameleon.support.expression;

import chameleon.core.lookup.LookupException;
import chameleon.core.reference.CrossReferenceTarget;
import chameleon.oo.type.TypeReference;

/**
 * @author Marko van Dooren
 */
public class RegularLiteral extends LiteralWithTypeReference<RegularLiteral> {

  public RegularLiteral(TypeReference type, String value) {
    super(value);
    setTypeReference(type);
  }

  public RegularLiteral clone() {
    return new RegularLiteral((TypeReference)getTypeReference().clone(), getValue());
  }


}
