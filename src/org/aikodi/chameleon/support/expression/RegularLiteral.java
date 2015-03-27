package org.aikodi.chameleon.support.expression;

import org.aikodi.chameleon.oo.type.TypeReference;

/**
 * @author Marko van Dooren
 */
public class RegularLiteral extends LiteralWithTypeReference {

  public RegularLiteral(TypeReference type, String value) {
    super(value);
    setTypeReference(type);
  }

  @Override
protected RegularLiteral cloneSelf() {
    return new RegularLiteral(null, text());
  }

  @Override
  public String toString() {
  	return text();
  }
}
