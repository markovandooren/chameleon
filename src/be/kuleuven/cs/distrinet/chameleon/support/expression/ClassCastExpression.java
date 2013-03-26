package be.kuleuven.cs.distrinet.chameleon.support.expression;

import java.util.Set;

import be.kuleuven.cs.distrinet.rejuse.association.AssociationListener;
import be.kuleuven.cs.distrinet.chameleon.core.element.Element;
import be.kuleuven.cs.distrinet.chameleon.core.element.ElementImpl;
import be.kuleuven.cs.distrinet.chameleon.core.lookup.LookupException;
import be.kuleuven.cs.distrinet.chameleon.oo.expression.Expression;
import be.kuleuven.cs.distrinet.chameleon.oo.language.ObjectOrientedLanguage;
import be.kuleuven.cs.distrinet.chameleon.oo.type.Type;
import be.kuleuven.cs.distrinet.chameleon.oo.type.TypeReference;
import be.kuleuven.cs.distrinet.chameleon.util.CreationStackTrace;
import be.kuleuven.cs.distrinet.chameleon.util.Util;

/**
 * @author Marko van Dooren
 * @author Tim Laeremans
 */
public class ClassCastExpression extends ExprTypeRefContainingExpression {
	
	@Override
	public void disconnect() {
		super.disconnect();
	}
	
  public ClassCastExpression(TypeReference type, Expression expression) {
	  setTypeReference(type);
    setExpression(expression);
  }


  protected Type actualType() throws LookupException {
    Type result = getTypeReference().getType();
    if(result == null) {
      getTypeReference().getType();
      throw new LookupException("Type reference of class cast expression returns null", getTypeReference());
    }
    return result;
  }

  public ClassCastExpression clone() {
    ClassCastExpression classCastExpression = new ClassCastExpression((TypeReference)getTypeReference().clone(), getExpression().clone());
		return classCastExpression;
  }

  public Set<Type> getDirectExceptions() throws LookupException {
    return Util.createNonNullSet(language(ObjectOrientedLanguage.class).classCastException(view().namespace()));
  }

}
