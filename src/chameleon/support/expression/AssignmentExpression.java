package chameleon.support.expression;

import java.util.HashSet;
import java.util.Set;

import chameleon.core.element.Element;
import chameleon.core.lookup.LookupException;
import chameleon.core.validation.BasicProblem;
import chameleon.core.validation.Valid;
import chameleon.core.validation.VerificationResult;
import chameleon.oo.expression.Expression;
import chameleon.oo.type.Type;
import chameleon.util.association.Single;

/**
 * @author Marko van Dooren
 */
public class AssignmentExpression extends Expression {

  /**
   * @param first
   * @param second
   */
  public AssignmentExpression(Expression var, Expression value) {
    
	  setVariable(var);
    setValue(value);
  }

	/**
	 * VARIABLE
	 */
	private Single<Expression> _variable = new Single<Expression>(this);


  public Expression getVariable() {
    return _variable.getOtherEnd();
  }

  public void setVariable(Expression var) {
  	set(_variable,var);
  }

	/**
	 * VALUE
	 */
	private Single<Expression> _value = new Single<Expression>(this);

  public Expression getValue() {
    return (Expression)_value.getOtherEnd();
  }

  public void setValue(Expression expression) {
  	set(_value,expression);
  }

  protected Type actualType() throws LookupException {
    return getVariable().getType();
  }

  public AssignmentExpression clone() {
    return new AssignmentExpression(getVariable().clone(), getValue().clone());
  }

  public Set<Type> getDirectExceptions() throws LookupException {
    return new HashSet<Type>();
  }

	@Override
	public VerificationResult verifySelf() {
		VerificationResult result = Valid.create();
		try {
			Expression var = getVariable();
			if(var == null) {
				result = result.and(new BasicProblem(this, "The assignment has no variable at the left-hand side"));
			}
			Expression value = getValue();
			if(value == null) {
				result = result.and(new BasicProblem(this, "The assignment has no valid expression at the right-hand side"));
			}
			Type varType = var.getType();
			Type exprType = value.getType();
			if(! exprType.subTypeOf(varType)) {
				result = result.and(new InvalidType(this, varType, exprType));
			}
		}
	  catch (LookupException e) {
			result = result.and(new BasicProblem(this, "The type of the expression is not assignable to the type of the variable."));
	  }
	  return result;
	}

	public static class InvalidType extends BasicProblem {

		public InvalidType(Element element, Type varType, Type exprType) {
			super(element, "The type of the left-hand side ("+exprType.getFullyQualifiedName()+") is not assignable to a variable of type "+varType.getFullyQualifiedName());
		}
		
	}
//  public AccessibilityDomain getAccessibilityDomain() throws LookupException {
//    return getVariable().getAccessibilityDomain().intersect(getValue().getAccessibilityDomain());
//  }

  
}
