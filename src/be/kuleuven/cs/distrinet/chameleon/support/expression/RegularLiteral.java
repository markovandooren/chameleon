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

  protected RegularLiteral cloneSelf() {
    return new RegularLiteral(null, getValue());
  }

  @Override
  public String toString() {
  	return getValue();
  }
}
