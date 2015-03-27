package org.aikodi.chameleon.support.expression;

import org.aikodi.chameleon.core.lookup.LookupException;
import org.aikodi.chameleon.oo.type.Type;
import org.aikodi.chameleon.oo.type.TypeReference;

/**
 * FIXME This shouln't be a literal.
 * 
 * @author Marko van Dooren
 * @author Tim Laeremans
 */
public class ThisLiteral extends LiteralWithTypeReference {

  public ThisLiteral() {
    super("this");
  }
  
  public ThisLiteral(TypeReference ref) {
    super("this", ref);
  }

  @Override
protected Type actualType() throws LookupException {
    TypeReference tref = getTypeReference();
		if (tref == null) {
      return nearestAncestor(Type.class);
    }
    else {
      return tref.getType();
    }
  }

  @Override
protected ThisLiteral cloneSelf() {
    return new ThisLiteral();
  }

}
