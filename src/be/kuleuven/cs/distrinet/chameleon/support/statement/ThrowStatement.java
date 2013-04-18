package be.kuleuven.cs.distrinet.chameleon.support.statement;

import be.kuleuven.cs.distrinet.chameleon.core.element.Element;
import be.kuleuven.cs.distrinet.chameleon.core.lookup.LookupException;
import be.kuleuven.cs.distrinet.chameleon.core.validation.BasicProblem;
import be.kuleuven.cs.distrinet.chameleon.core.validation.Valid;
import be.kuleuven.cs.distrinet.chameleon.core.validation.VerificationResult;
import be.kuleuven.cs.distrinet.chameleon.oo.expression.Expression;
import be.kuleuven.cs.distrinet.chameleon.oo.language.ObjectOrientedLanguage;
import be.kuleuven.cs.distrinet.chameleon.oo.method.exception.TypeExceptionDeclaration;
import be.kuleuven.cs.distrinet.chameleon.oo.statement.CheckedExceptionList;
import be.kuleuven.cs.distrinet.chameleon.oo.statement.ExceptionTuple;
import be.kuleuven.cs.distrinet.chameleon.oo.type.Type;
import be.kuleuven.cs.distrinet.chameleon.oo.type.TypeReference;

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

  public ThrowStatement clone() {
    return new ThrowStatement(getExpression().clone());
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
