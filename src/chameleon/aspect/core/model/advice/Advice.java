package chameleon.aspect.core.model.advice;

import java.util.ArrayList;
import java.util.List;

import org.rejuse.association.OrderedMultiAssociation;
import org.rejuse.association.SingleAssociation;
import org.rejuse.property.Property;
import org.rejuse.property.PropertyMutex;
import org.rejuse.property.PropertySet;

import chameleon.aspect.core.model.aspect.Aspect;
import chameleon.aspect.core.model.pointcut.expression.MatchResult;
import chameleon.aspect.core.model.pointcut.expression.PointcutExpression;
import chameleon.core.compilationunit.CompilationUnit;
import chameleon.core.element.Element;
import chameleon.core.lookup.LookupException;
import chameleon.core.modifier.ElementWithModifiers;
import chameleon.core.modifier.Modifier;
import chameleon.core.namespace.NamespaceElementImpl;
import chameleon.core.property.ChameleonProperty;
import chameleon.core.validation.Valid;
import chameleon.core.validation.VerificationResult;
import chameleon.exception.ModelException;
import chameleon.oo.type.TypeReference;
import chameleon.util.Util;

public class Advice<E extends Advice<E,B>, B extends Element> extends NamespaceElementImpl<E> implements ElementWithModifiers<E> {

	public Advice() {
	}
	
	public Advice(B body) {
		setBody(body);
	}

	private SingleAssociation<Advice<E,B>, B> _body = new SingleAssociation<Advice<E,B>, B>(this);
	private SingleAssociation<Advice<E,B>, PointcutExpression> _pointcutExpression = new SingleAssociation<Advice<E,B>, PointcutExpression>(this);

	public B body() {
		return _body.getOtherEnd();
	}

	public void setBody(B element) {
		setAsParent(_body, element);
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

	public void setPointcutExpression(PointcutExpression<?,?> pointcutref) {
		setAsParent(_pointcutExpression, pointcutref);
	}

	@Override
	public List<Element> children() {
		List<Element> result = new ArrayList<Element>();

		Util.addNonNull(body(), result);
		Util.addNonNull(pointcutExpression(), result);
		result.addAll(modifiers());

		return result;
	}

	@Override
	public E clone() {
		TypeReference returnTypeClone = null;
		Advice clone = cloneThis();
		PointcutExpression pointcutExpression = pointcutExpression();
		clone.setPointcutExpression((pointcutExpression == null ? null : (PointcutExpression) pointcutExpression.clone()));
		B body = body();
		clone.setBody(body == null ?  null : body.clone());
		for (Modifier m : modifiers()) {
			clone.addModifier(m.clone());
		}
		return (E) clone;
	}

	protected Advice cloneThis() {
		return new Advice();
	}

	@Override
	public VerificationResult verifySelf() {
		VerificationResult result = Valid.create();

		return result;
	}

	private OrderedMultiAssociation<Advice<E,B>, Modifier> _modifiers = new OrderedMultiAssociation<Advice<E,B>, Modifier>(
			this);

	@Override
	public List<Modifier> modifiers() {
		return _modifiers.getOtherEnds();
	}

	@Override
	public void addModifier(Modifier modifier) {
		if ((modifier != null) && (!_modifiers.contains(modifier.parentLink()))) {
			_modifiers.add(modifier.parentLink());
		}
	}

	@Override
	public void removeModifier(Modifier modifier) {
		_modifiers.remove(modifier.parentLink());
	}

	@Override
	public void addModifiers(List<Modifier> modifiers) {
		if (modifiers == null)
			return;

		for (Modifier modifier : modifiers)
			addModifier(modifier);

	}

	@Override
	public List<Modifier> modifiers(PropertyMutex mutex) throws ModelException {
		Property property = property(mutex);
		List<Modifier> result = new ArrayList<Modifier>();
		for (Modifier mod : modifiers()) {
			if (mod.impliesTrue(property)) {
				result.add(mod);
			}
		}
		return result;
	}

	@Override
	public List<Modifier> modifiers(Property property) throws ModelException {
		List<Modifier> result = new ArrayList<Modifier>();
		for(Modifier mod: modifiers()) {
			if(mod.impliesTrue(property)) {
				result.add(mod);
			}
		}
		return result;
	}

	@Override
	public PropertySet<Element, ChameleonProperty> declaredProperties() {
		PropertySet<Element, ChameleonProperty> result = new PropertySet<Element, ChameleonProperty>();
		for (Modifier modifier : modifiers()) {
			result.addAll(modifier.impliedProperties());
		}
		return result;
	}

	public List<MatchResult<? extends Element>> getJoinPoints(CompilationUnit compilationUnit) throws LookupException {
		//		return getExpandedPointcutExpression().joinpoints(compilationUnit);
		return pointcutExpression().joinpoints(compilationUnit);
	}
}
