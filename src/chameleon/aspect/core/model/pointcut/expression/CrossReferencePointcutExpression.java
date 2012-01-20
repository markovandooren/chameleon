package chameleon.aspect.core.model.pointcut.expression;

import java.util.ArrayList;
import java.util.List;

import chameleon.aspect.core.model.pointcut.pattern.DeclarationPattern;
import chameleon.core.declaration.Declaration;
import chameleon.core.element.Element;
import chameleon.core.lookup.LookupException;
import chameleon.core.reference.CrossReference;

public class CrossReferencePointcutExpression<E extends CrossReferencePointcutExpression<E>> extends DeclarationPointcutExpression<E,CrossReference> {

	public CrossReferencePointcutExpression(DeclarationPattern pattern) {
		super(pattern);
	}

	@Override
	public Class<? extends CrossReference> joinPointType() throws LookupException {
		return CrossReference.class; 
	}

	@Override
	public List<? extends Element> children() {
		return new ArrayList<Element>();
	}

	@Override
	public E clone() {
		return (E) new CrossReferencePointcutExpression(pattern().clone());
	}

	protected Declaration declaration(CrossReference joinpoint) throws LookupException {
		return joinpoint.getElement();
	}

}
