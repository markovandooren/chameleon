package chameleon.support.expression;

import java.util.Set;

import org.rejuse.association.AssociationListener;

import chameleon.core.element.Element;
import chameleon.core.element.ElementImpl;
import chameleon.core.lookup.LookupException;
import chameleon.oo.expression.Expression;
import chameleon.oo.language.ObjectOrientedLanguage;
import chameleon.oo.type.Type;
import chameleon.oo.type.TypeReference;
import chameleon.util.CreationStackTrace;
import chameleon.util.Util;

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
    return Util.createNonNullSet(language(ObjectOrientedLanguage.class).classCastException());
  }

}
