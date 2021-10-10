package org.aikodi.chameleon.support.expression;

import org.aikodi.chameleon.core.lookup.LookupException;
import org.aikodi.chameleon.oo.expression.Expression;
import org.aikodi.chameleon.oo.language.ObjectOrientedLanguage;
import org.aikodi.chameleon.oo.type.Type;
import org.aikodi.chameleon.oo.type.TypeReference;
import org.aikodi.chameleon.util.Util;

import java.util.Set;

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


  @Override
protected Type actualType() throws LookupException {
    Type result = getTypeReference().getElement();
//    if(result == null) {
//      getTypeReference().getElement();
//      throw new LookupException("Type reference of class cast expression returns null", getTypeReference());
//    }
    return result;
  }

  @Override
public ClassCastExpression cloneSelf() {
    return new ClassCastExpression(null, null);
  }

  public Set<Type> getDirectExceptions() throws LookupException {
    return Util.createNonNullSet(language(ObjectOrientedLanguage.class).classCastException(view().namespace()));
  }

}
