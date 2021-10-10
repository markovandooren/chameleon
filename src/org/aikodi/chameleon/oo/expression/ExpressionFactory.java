package org.aikodi.chameleon.oo.expression;

import org.aikodi.chameleon.core.reference.CrossReferenceTarget;
import org.aikodi.chameleon.plugin.LanguagePluginImpl;
import org.aikodi.chameleon.support.expression.AssignmentExpression;
import org.aikodi.chameleon.support.expression.ConditionalExpression;
import org.aikodi.chameleon.support.member.simplename.method.RegularMethodInvocation;
import org.aikodi.chameleon.support.member.simplename.operator.infix.InfixOperatorInvocation;
import org.aikodi.chameleon.support.member.simplename.operator.postfix.PostfixOperatorInvocation;
import org.aikodi.chameleon.support.member.simplename.operator.prefix.PrefixOperatorInvocation;

/**
 * A factory for expressions in object-oriented programming languages.
 * 
 * @author Marko van Dooren
 */
public class ExpressionFactory extends LanguagePluginImpl {

	/**
	 * Create a new conditional expression with the given condition, and first and second operands.
	 * 
	 * @param condition The condition of the conditional expression.
	 * @param firstOperand The first operand.
	 * @param secondOperand The second operand.
	 * 
	 * @return A new conditional expression with the given condition as its {@link ConditionalExpression#getCondition()}, the
	 * given first operand as its {@link ConditionalExpression#getFirst()} operand, and the
	 * given second operand as its {@link ConditionalExpression#getSecond()}.
	 */
	public ConditionalExpression createConditionalExpression(Expression condition, Expression firstOperand, Expression secondOperand) {
		return new ConditionalExpression(condition, firstOperand, secondOperand);
	}

	@Override
	public ExpressionFactory clone() {
		return new ExpressionFactory();
	}

	/**
	 * Create a new invocation based on the name of the invoked method and the target.
	 *  
	 * @param name
	 * @param target
	 * @return
	 */
	public MethodInvocation<?> createInvocation(String name, CrossReferenceTarget target) {
		return new RegularMethodInvocation(name, target);
	}
	
	public InfixOperatorInvocation createInfixOperatorInvocation(String name, CrossReferenceTarget target) {
		return new InfixOperatorInvocation(name, target);
	}

	public PrefixOperatorInvocation createPrefixOperatorInvocation(String name, CrossReferenceTarget target) {
		return new PrefixOperatorInvocation(name, target);
	}

	public PostfixOperatorInvocation createPostfixOperatorInvocation(String name, CrossReferenceTarget target) {
		return new PostfixOperatorInvocation(name, target);
	}

	public NamedTarget createNamedTarget(String fqn) {
		return new NamedTarget(fqn,this);
	}

	public NamedTarget createNamedTarget(String fqn, CrossReferenceTarget target) {
		return new NamedTarget(fqn,target);
	}

	/**
	 * Create a new name expression without a target.
	 * 
	 * @param name The name of the name expression to be created. The name cannot
	 *             be null.
	 * @return A name expression whose name is set to the given name. The result
	 *         has no target.
	 */
  public NameExpression createNameExpression(String name) {
  	return new NameExpression(name);
  }

  /**
   * Create a new name expression with the given name and prefix.
   * 
   * @param name The name of the name expression to be created. The name cannot
   *             be null.
   * @param prefix The prefix of the name expression.             
   * @return A name expression whose name is set to the given name. The prefix
   *         of the result is set to the given prefix.
   */
  public NameExpression createNameExpression(String name, CrossReferenceTarget prefix) {
  	return new NameExpression(name,prefix);
  }

  /**
   * Create a new assignment expression.
   * 
   * @param variableReference An expression that points to a variable.
   * @param value An expression that represents the value that is assigned to
   *              the variable.
   * @return An assignment expression whose variable expression is set to the
   * given variable reference, and whose value is set to the given value. 
   */
  public AssignmentExpression createAssignmentExpression(Expression variableReference, Expression value) {
    return new AssignmentExpression(variableReference, value);
  }
}
