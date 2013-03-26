package be.kuleuven.cs.distrinet.chameleon.support.expression;

import be.kuleuven.cs.distrinet.chameleon.oo.type.TypeReference;

/**
 * @author Marko van Dooren
 */
public class RegularLiteral extends LiteralWithTypeReference {

  public RegularLiteral(TypeReference type, String value) {
    super(value);
    setTypeReference(type);
  }

  public RegularLiteral clone() {
    return new RegularLiteral((TypeReference)getTypeReference().clone(), getValue());
  }


}
