package org.aikodi.chameleon.aspect.core.model.advice;

import org.aikodi.chameleon.aspect.core.model.aspect.Aspect;
import org.aikodi.chameleon.aspect.core.model.pointcut.expression.MatchResult;
import org.aikodi.chameleon.aspect.core.model.pointcut.expression.PointcutExpression;
import org.aikodi.chameleon.core.document.Document;
import org.aikodi.chameleon.core.element.Element;
import org.aikodi.chameleon.core.lookup.LookupException;
import org.aikodi.chameleon.core.modifier.ElementWithModifiersImpl;
import org.aikodi.chameleon.core.validation.Valid;
import org.aikodi.chameleon.core.validation.Verification;
import org.aikodi.chameleon.util.association.Single;

import java.util.List;

public class Advice<B extends Element> extends ElementWithModifiersImpl {

	public Advice() {
	}
	
	public Advice(B body) {
		setBody(body);
	}

	private Single<B> _body = new Single<B>(this, "body");
	private Single<PointcutExpression<?>> _pointcutExpression = new Single<PointcutExpression<?>>(this, "pointcut expression");

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
		return (Aspect) lexical().parent();
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
   protected Advice cloneSelf() {
		return new Advice();
	}

	@Override
	public Verification verifySelf() {
		Verification result = Valid.create();

		return result;
	}

	public List<MatchResult<? extends Element>> getJoinPoints(Document compilationUnit) throws LookupException {
		//		return getExpandedPointcutExpression().joinpoints(compilationUnit);
		return pointcutExpression().joinpoints(compilationUnit);
	}
}
