package chameleon.support.expression;

import java.util.Set;

import chameleon.core.lookup.LookupException;
import chameleon.core.reference.CrossReferenceTarget;
import chameleon.core.validation.BasicProblem;
import chameleon.core.validation.Valid;
import chameleon.core.validation.VerificationResult;
import chameleon.oo.expression.Expression;
import chameleon.oo.language.ObjectOrientedLanguage;
import chameleon.oo.type.Type;
import chameleon.oo.type.TypeReference;
import chameleon.util.Util;

/**
 * @author Marko van Dooren
 * @author Tim Laeremans
 */
public class ClassCastExpression extends ExprTypeRefContainingExpression<ClassCastExpression> {

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
    return new ClassCastExpression((TypeReference)getTypeReference().clone(), getExpression().clone());
  }

  public Set<Type> getDirectExceptions() throws LookupException {
    return Util.createNonNullSet(language(ObjectOrientedLanguage.class).classCastException());
  }

}
