package be.kuleuven.cs.distrinet.chameleon.aspect.oo.model.pointcut;

import be.kuleuven.cs.distrinet.chameleon.aspect.core.model.pointcut.expression.MatchResult;
import be.kuleuven.cs.distrinet.chameleon.aspect.core.model.pointcut.expression.staticexpression.within.WithinPointcutExpressionDeprecated;
import be.kuleuven.cs.distrinet.chameleon.core.element.Element;
import be.kuleuven.cs.distrinet.chameleon.core.lookup.LookupException;
import be.kuleuven.cs.distrinet.chameleon.oo.type.Type;
import be.kuleuven.cs.distrinet.chameleon.oo.type.TypeReference;
import be.kuleuven.cs.distrinet.chameleon.util.association.Single;
import be.kuleuven.cs.distrinet.rejuse.action.Nothing;
import be.kuleuven.cs.distrinet.rejuse.predicate.UniversalPredicate;

public class WithinTypePointcutExpression<E extends WithinTypePointcutExpression<E>> extends WithinPointcutExpressionDeprecated {
	
	private Single<TypeReference> _typeReference = new Single<TypeReference>(this);
	private Single<SubtypeMarker> _subtypeMarker = new Single<SubtypeMarker>(this);

	public void setSubtypeMarker(SubtypeMarker marker) {
		set(_subtypeMarker, marker);
	}
	
	public boolean hasSubtypeMarker() {
		return subtypeMarker() != null;
	}
	
	public SubtypeMarker subtypeMarker() {
		return _subtypeMarker.getOtherEnd();
	}
	
	public TypeReference typeReference() {
		return _typeReference.getOtherEnd();
	}
	
	public void setTypeReference(TypeReference typeReference) {
		set(_typeReference, typeReference);
	}

	@Override
	public MatchResult match(Element joinpoint) throws LookupException {
		@SuppressWarnings("unchecked")
		boolean noMatch = joinpoint.ancestors(new UniversalPredicate<Type,Nothing>(Type.class) {

			@Override
			public boolean uncheckedEval(Type object) {
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
	public E cloneSelf() {
		return (E) new WithinTypePointcutExpression<E>();
	}
}
