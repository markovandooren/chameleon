package chameleon.core.expression;

import java.util.List;

import org.rejuse.association.OrderedMultiAssociation;
import org.rejuse.association.SingleAssociation;
import org.rejuse.java.collections.Visitor;

import chameleon.core.element.Element;
import chameleon.core.lookup.LookupException;
import chameleon.core.modifier.ElementWithModifiersImpl;
import chameleon.core.modifier.Modifier;
import chameleon.core.namespace.Namespace;
import chameleon.core.namespace.NamespaceElementImpl;
import chameleon.core.type.Type;
import chameleon.core.validation.Valid;
import chameleon.core.validation.VerificationResult;
import chameleon.util.Util;

/**
 * A class of elements representing actual parameters. An actual parameter has an expression, and can optionally
 * have modifiers.
 * 
 * Exmaple: ref parameters in C#. 
 * 
 * 
 * @author Marko van Dooren
 * @author Tim Laeremans
 */
public class ActualArgument extends ElementWithModifiersImpl<ActualArgument,ActualArgumentList> {


	/**
	 * @param parent
	 * @param target
	 */
	public ActualArgument(Expression expression ) {
        setExpression(expression);
	}

	/**
	 * EXPRESSION
	 */
	private SingleAssociation<ActualArgument,Expression> _expression = new SingleAssociation<ActualArgument,Expression>(this);

	public SingleAssociation getExpressionLink(){
		return _expression;
	}

	public Expression getExpression() {
		return _expression.getOtherEnd();
	}

	public void setExpression(Expression expression) {
		if(expression != null) {
			_expression.connectTo(expression.parentLink());
		} else {
			_expression.connectTo(null);
			throw new Error("Debugging Exception");
		}
	}


	public ActualArgument clone(){
		Expression expr = getExpression().clone();
		final ActualArgument result = new ActualArgument(expr);

		new Visitor<Modifier>() {
			public void visit(Modifier element) {
				result.addModifier(element.clone());
			}
		}.applyTo(modifiers());

		return result;
	}

	public List<Element> children() {
		List<Element> result = Util.createNonNullList(getExpression());
		result.addAll(modifiers());
		return result;
	}

	public Type getType() throws LookupException{
		return getExpression().getType();
	}

	@Override
	public VerificationResult verifyThis() {
		return Valid.create();
	}

}
