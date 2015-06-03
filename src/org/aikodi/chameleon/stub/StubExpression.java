/**
 * 
 */
package org.aikodi.chameleon.stub;

import org.aikodi.chameleon.core.element.Element;
import org.aikodi.chameleon.oo.expression.Expression;
import org.aikodi.chameleon.oo.type.Type;

import be.kuleuven.cs.distrinet.rejuse.contract.Contracts;

/**
 * A class of expression of which the type can be set directly.
 * This is handy for writing test code.
 * 
 * @author Marko van Dooren
 */
public class StubExpression extends Expression {

	/**
	 * Create a new stub expression with the given type.
	 * 
	 * @param type The type of the expression. The type
	 *             cannot be null.
	 */
	public StubExpression(Type type) {
		Contracts.notNull(type);
		_type = type;
	}
	
	private Type _type;
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	protected Type actualType() {
		return _type;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	protected Element cloneSelf() {
	  return new StubExpression(actualType());
	}

}
