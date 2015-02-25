package org.aikodi.chameleon.support.statement;

import org.aikodi.chameleon.core.element.Element;
import org.aikodi.chameleon.core.lookup.LookupException;
import org.aikodi.chameleon.core.validation.BasicProblem;
import org.aikodi.chameleon.core.validation.Valid;
import org.aikodi.chameleon.core.validation.Verification;
import org.aikodi.chameleon.oo.expression.Expression;
import org.aikodi.chameleon.oo.language.ObjectOrientedLanguage;
import org.aikodi.chameleon.oo.method.exception.TypeExceptionDeclaration;
import org.aikodi.chameleon.oo.statement.CheckedExceptionList;
import org.aikodi.chameleon.oo.statement.ExceptionTuple;
import org.aikodi.chameleon.oo.type.Type;
import org.aikodi.chameleon.oo.type.TypeReference;

/**
 * @author Marko van Dooren
 */
public class ThrowStatement extends ExpressionContainingStatement {

 /*@
   @ public behavior
   @
   @ post getExpression() == expression;
   @*/
  public ThrowStatement(Expression expression) {
    super(expression);
  }

  @Override
protected ThrowStatement cloneSelf() {
    return new ThrowStatement(null);
  }

  @Override
public CheckedExceptionList getDirectCEL() throws LookupException {
	    CheckedExceptionList cel = new CheckedExceptionList();
	    Type type = getExpression().getType();
	    TypeReference tr = language(ObjectOrientedLanguage.class).createTypeReference(type.getFullyQualifiedName());
	    TypeExceptionDeclaration ted = new TypeExceptionDeclaration(tr);
	    cel.add(new ExceptionTuple(type, ted, this));
	    return cel;
	  }

	@Override
	public Verification verifySelf() {
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
