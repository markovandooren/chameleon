package chameleon.support.statement;

import java.util.List;

import chameleon.core.element.Element;
import chameleon.core.lookup.LookupException;
import chameleon.core.validation.BasicProblem;
import chameleon.core.validation.Valid;
import chameleon.core.validation.VerificationResult;
import chameleon.oo.expression.Expression;
import chameleon.oo.language.ObjectOrientedLanguage;
import chameleon.oo.method.exception.TypeExceptionDeclaration;
import chameleon.oo.statement.CheckedExceptionList;
import chameleon.oo.statement.ExceptionTuple;
import chameleon.oo.type.Type;
import chameleon.oo.type.TypeReference;
import chameleon.util.Util;

/**
 * @author Marko van Dooren
 */
public class ThrowStatement extends ExpressionContainingStatement<ThrowStatement> {

 /*@
   @ public behavior
   @
   @ post getExpression() == expression;
   @*/
  public ThrowStatement(Expression expression) {
    super(expression);
  }

  public ThrowStatement clone() {
    return new ThrowStatement(getExpression().clone());
  }

 /*@
   @ also public behavior
   @
   @ post \result.contains(getExpression());
   @ post \result.size() == 1;
   @*/
  public List children() {
    return Util.createNonNullList(getExpression());
  }

  public CheckedExceptionList getDirectCEL() throws LookupException {
	    CheckedExceptionList cel = new CheckedExceptionList();
	    Type type = getExpression().getType();
	    TypeReference tr = language(ObjectOrientedLanguage.class).createTypeReference(type.getFullyQualifiedName());
	    TypeExceptionDeclaration ted = new TypeExceptionDeclaration(tr);
	    cel.add(new ExceptionTuple(type, ted, this));
	    return cel;
	  }

	@Override
	public VerificationResult verifySelf() {
		try {
		  Expression expr = getExpression();
			if(expr != null && language(ObjectOrientedLanguage.class).isException(expr.getType())) {
				return Valid.create();
			} else {
				return new ExpressionIsNoException(this);
			}
		} catch (LookupException e) {
			return new ExpressionIsNoException(this);
		}
	}
	
	public static class ExpressionIsNoException extends BasicProblem {

		public ExpressionIsNoException(Element element) {
			super(element,"The expression is not an exception.");
		}
		
	}
    
}
