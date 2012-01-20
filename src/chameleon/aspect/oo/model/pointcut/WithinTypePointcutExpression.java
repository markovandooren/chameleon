package chameleon.aspect.oo.model.pointcut;

import java.util.ArrayList;
import java.util.List;

import org.rejuse.association.SingleAssociation;
import org.rejuse.predicate.SafePredicate;

import chameleon.aspect.core.model.pointcut.expression.MatchResult;
import chameleon.aspect.core.model.pointcut.expression.staticexpression.within.WithinPointcutExpressionDeprecated;
import chameleon.core.element.Element;
import chameleon.core.lookup.LookupException;
import chameleon.oo.type.Type;
import chameleon.oo.type.TypeReference;
import chameleon.util.Util;

public class WithinTypePointcutExpression<E extends WithinTypePointcutExpression<E>> extends WithinPointcutExpressionDeprecated<E> {
	
	private SingleAssociation<WithinTypePointcutExpression<E>, TypeReference> _typeReference = new SingleAssociation<WithinTypePointcutExpression<E>, TypeReference>(this);
	private SingleAssociation<WithinTypePointcutExpression<E>, SubtypeMarker> _subtypeMarker = new SingleAssociation<WithinTypePointcutExpression<E>, SubtypeMarker>(this);

	public void setSubtypeMarker(SubtypeMarker marker) {
		setAsParent(_subtypeMarker, marker);
	}
	
	public boolean hasSubtypeMarker() {
		return subtypeMarker() != null;
	}
	
	public SubtypeMarker<?> subtypeMarker() {
		return _subtypeMarker.getOtherEnd();
	}
	
	public TypeReference<?> typeReference() {
		return _typeReference.getOtherEnd();
	}
	
	public void setTypeReference(TypeReference typeReference) {
		setAsParent(_typeReference, typeReference);
	}

	@Override
	public MatchResult match(Element joinpoint) throws LookupException {
		@SuppressWarnings("unchecked")
		boolean noMatch = joinpoint.ancestors(Type.class, new SafePredicate<Type>() {

			@Override
			public boolean eval(Type object) {
				try {
					if (hasSubtypeMarker())
						return object.assignableTo(typeReference().getType());
					else
						return object.sameAs(typeReference().getType());
				} catch (LookupException e) {
					// Shouldn't occur with normale usage, only due to a bug
					e.printStackTrace();
					return false;
				}
			}
		}).isEmpty();
		
		if (noMatch)
			return MatchResult.noMatch();
		else
			return new MatchResult<Element>(this, joinpoint);
	}

	@Override
	public List<? extends Element> children() {
		List<Element> result = new ArrayList<Element>();
		
		Util.addNonNull(typeReference(), result);
		Util.addNonNull(subtypeMarker(), result);
		
		return result;
	}

	@Override
	public E clone() {
		WithinTypePointcutExpression<E> clone = new WithinTypePointcutExpression<E>();
		clone.setSubtypeMarker(Util.cloneOrNull(subtypeMarker()));
		clone.setTypeReference(Util.cloneOrNull(typeReference()));

		return (E) clone;
	}
}