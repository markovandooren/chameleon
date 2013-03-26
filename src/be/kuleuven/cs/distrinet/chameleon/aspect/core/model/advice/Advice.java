package be.kuleuven.cs.distrinet.chameleon.aspect.core.model.advice;

import java.util.List;

import be.kuleuven.cs.distrinet.chameleon.aspect.core.model.aspect.Aspect;
import be.kuleuven.cs.distrinet.chameleon.aspect.core.model.pointcut.expression.MatchResult;
import be.kuleuven.cs.distrinet.chameleon.aspect.core.model.pointcut.expression.PointcutExpression;
import be.kuleuven.cs.distrinet.chameleon.core.document.Document;
import be.kuleuven.cs.distrinet.chameleon.core.element.Element;
import be.kuleuven.cs.distrinet.chameleon.core.lookup.LookupException;
import be.kuleuven.cs.distrinet.chameleon.core.modifier.ElementWithModifiersImpl;
import be.kuleuven.cs.distrinet.chameleon.core.modifier.Modifier;
import be.kuleuven.cs.distrinet.chameleon.core.validation.Valid;
import be.kuleuven.cs.distrinet.chameleon.core.validation.VerificationResult;
import be.kuleuven.cs.distrinet.chameleon.oo.type.TypeReference;
import be.kuleuven.cs.distrinet.chameleon.util.association.Single;

public class Advice<B extends Element> extends ElementWithModifiersImpl {

	public Advice() {
	}
	
	public Advice(B body) {
		setBody(body);
	}

	private Single<B> _body = new Single<B>(this);
	private Single<PointcutExpression<?>> _pointcutExpression = new Single<PointcutExpression<?>>(this);

	public B body() {
		return _body.getOtherEnd();
	}

	public void setBody(B element) {
		set(_body, element);
	}

	/**
	 * Get the Aspect this Advice belongs to
	 */
	public Aspect aspect() {
		return (Aspect) parent();
	}

	//	public PointcutExpression getExpandedPointcutExpression() {
	//		PointcutExpression<?> expr = pointcutExpression();
	//		setPointcutExpression(expr.expand());
	//		
	//		return pointcutExpression();
	//	}
	//
	public PointcutExpression pointcutExpression() {
		return _pointcutExpression.getOtherEnd();
	}

	public void setPointcutExpression(PointcutExpression<?> pointcutref) {
		set(_pointcutExpression, pointcutref);
	}

	@Override
	public Advice clone() {
		TypeReference returnTypeClone = null;
		Advice clone = cloneThis();
		PointcutExpression pointcutExpression = pointcutExpression();
		clone.setPointcutExpression((pointcutExpression == null ? null : (PointcutExpression) pointcutExpression.clone()));
		B body = body();
		clone.setBody(body == null ?  null : body.clone());
		for (Modifier m : modifiers()) {
			clone.addModifier(m.clone());
		}
		return clone;
	}

	protected Advice cloneThis() {
		return new Advice();
	}

	@Override
	public VerificationResult verifySelf() {
		VerificationResult result = Valid.create();

		return result;
	}

	public List<MatchResult<? extends Element>> getJoinPoints(Document compilationUnit) throws LookupException {
		//		return getExpandedPointcutExpression().joinpoints(compilationUnit);
		return pointcutExpression().joinpoints(compilationUnit);
	}
}
