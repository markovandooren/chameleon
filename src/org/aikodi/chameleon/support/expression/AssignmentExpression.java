package org.aikodi.chameleon.support.expression;

import java.util.HashSet;
import java.util.Set;

import org.aikodi.chameleon.core.declaration.Declaration;
import org.aikodi.chameleon.core.element.Element;
import org.aikodi.chameleon.core.lookup.LookupException;
import org.aikodi.chameleon.core.reference.CrossReference;
import org.aikodi.chameleon.core.validation.BasicProblem;
import org.aikodi.chameleon.core.validation.Valid;
import org.aikodi.chameleon.core.validation.Verification;
import org.aikodi.chameleon.core.variable.Variable;
import org.aikodi.chameleon.oo.expression.Expression;
import org.aikodi.chameleon.oo.type.Type;
import org.aikodi.chameleon.util.association.Single;

/**
 * @author Marko van Dooren
 */
public class AssignmentExpression extends Expression {

  /**
   * @param first
   * @param second
   */
  public AssignmentExpression(Expression var, Expression value) {
	  setVariableExpression(var);
    setValue(value);
  }

	/**
	 * VARIABLE
	 */
	private Single<Expression> _variable = new Single<Expression>(this);

	//FIXME This is wrong. It does not deal with arrays.
	public Variable variable() throws LookupException {
		Expression variableExpression = getVariableExpression();
		if(variableExpression instanceof CrossReference) {
			Declaration decl = ((CrossReference) variableExpression).getElement();
			if(decl instanceof Variable) {
				return (Variable) decl;
			}
			throw new LookupException("The left-hand side of the assignment resolves, but does not reference a variable.");
		}
		throw new LookupException("The left-hand side of the assignment is not a cross-reference.");
	}

  public Expression getVariableExpression() {
    return _variable.getOtherEnd();
  }

  public void setVariableExpression(Expression var) {
  	set(_variable,var);
  }

	/**
	 * VALUE
	 */
	private Single<Expression> _value = new Single<Expression>(this);

  public Expression getValue() {
    return _value.getOtherEnd();
  }

  public void setValue(Expression expression) {
  	set(_value,expression);
  }

  @Override
protected Type actualType() throws LookupException {
    return getVariableExpression().getType();
  }

  @Override
protected AssignmentExpression cloneSelf() {
    return new AssignmentExpression(null,null);
  }

  public Set<Type> getDirectExceptions() throws LookupException {
    return new HashSet<Type>();
  }

	@Override
	public Verification verifySelf() {
		Verification result = Valid.create();
		try {
			Expression var = getVariableExpression();
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
