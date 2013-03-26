package be.kuleuven.cs.distrinet.chameleon.aspect.core.model.pointcut.expression;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import be.kuleuven.cs.distrinet.rejuse.predicate.SafePredicate;
import be.kuleuven.cs.distrinet.chameleon.core.document.Document;
import be.kuleuven.cs.distrinet.chameleon.core.element.Element;
import be.kuleuven.cs.distrinet.chameleon.core.element.ElementImpl;
import be.kuleuven.cs.distrinet.chameleon.core.lookup.LookupException;
import be.kuleuven.cs.distrinet.chameleon.core.validation.Valid;
import be.kuleuven.cs.distrinet.chameleon.core.validation.VerificationResult;

/**
 * 	An abstract class with a default implementation for most methods of a pointcut expression.
 * 
 * 	@author Jens De Temmerman
 *  @author Marko van Dooren
 */
public abstract class AbstractPointcutExpression<J extends Element> extends ElementImpl implements PointcutExpression<J> {
	/**
	 *  {@inheritDoc}
	 */
	@Override
	public VerificationResult verifySelf() {
		return Valid.create();
	}
	
	/**
	 *  {@inheritDoc}
	 */
	@Override
	public abstract AbstractPointcutExpression clone();
		
	/**
	 * 	{@inheritDoc}
	 */
	@Override
	public final PointcutExpression<?> retainOnly(final SafePredicate<PointcutExpression<?>> filter) {
		return without(new SafePredicate<PointcutExpression<?>>() {

			@Override
			public boolean eval(PointcutExpression<?> object) {
				return ! filter.eval(object);
			}
		});
	}

	/**
	 *	{@inheritDoc}
	 */
	@Override
	public PointcutExpression<?> without(final Class<? extends PointcutExpression> type) {	
		SafePredicate<PointcutExpression<?>> filter = new SafePredicate<PointcutExpression<?>>() {
			@Override
			public boolean eval(PointcutExpression<?> object) {
				return type.isInstance(object);
			}
		};
		return without(filter);
	}
	
	/**
	 * 	{@inheritDoc}
	 */
	@Override
	public PointcutExpression<?> without(SafePredicate<PointcutExpression<?>> filter) {
		PointcutExpression<?> result = null;
		if (!filter.eval(this)) {
			result = clone();
			result.setOrigin(origin());
		}
		return result;
	}
	
	/**
	 * 	{@inheritDoc}
	 */
	@Override
	public List<PointcutExpression<?>> toPostorderList() {
		return Collections.<PointcutExpression<?>>singletonList(this);
	}
	
//	/**
//	 *	{@inheritDoc}
//	 */
//	@Override
//	public boolean hasParameter(FormalParameter fp) {
//		return false;
//	}
	
	/**
	 *	{@inheritDoc}
	 */
	@Override
	public final List<MatchResult> joinpoints(Document compilationUnit) throws LookupException {
		List<MatchResult> result = new ArrayList<MatchResult>();
		List<? extends Element> joinPoints = compilationUnit.descendants(joinPointType());
		for (Element joinPoint : joinPoints) {
			MatchResult match = matches(joinPoint);
			if (match.isMatch()) {
				result.add(match);
			}
		}
		return result; 
	}
	
	@Override
	public final MatchResult matches(Element joinpoint) throws LookupException {
		if(joinPointType().isInstance(joinpoint)) {
			return match((J)joinpoint);
		} else {
			return MatchResult.noMatch();
		}
	}
	
	protected abstract MatchResult match(J joinpoint) throws LookupException;
}
