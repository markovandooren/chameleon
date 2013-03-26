package be.kuleuven.cs.distrinet.chameleon.support.expression;

import be.kuleuven.cs.distrinet.chameleon.core.lookup.LookupException;
import be.kuleuven.cs.distrinet.chameleon.oo.type.Type;
import be.kuleuven.cs.distrinet.chameleon.oo.type.TypeReference;

/**
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

  protected Type actualType() throws LookupException {
    TypeReference tref = getTypeReference();
		if (tref == null) {
      return nearestAncestor(Type.class);
    }
    else {
      return tref.getType();
    }
  }

  public ThisLiteral clone() {
    ThisLiteral result = new ThisLiteral();
    TypeReference tref = getTypeReference();
    if(tref != null) {
      result.setTypeReference(tref.clone());
    }
    return result;
  }

}
