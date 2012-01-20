package chameleon.aspect.core.model.pointcut;

import java.util.List;

import chameleon.core.declaration.Declaration;
import chameleon.core.lookup.DeclarationSelector;
import chameleon.core.lookup.LookupException;
import chameleon.core.lookup.LookupStrategy;
import chameleon.core.reference.CrossReference;
import chameleon.core.reference.CrossReferenceWithArguments;
import chameleon.core.reference.CrossReferenceTarget;
import chameleon.core.validation.VerificationResult;
import chameleon.oo.expression.Expression;
import chameleon.oo.expression.NamedTargetExpression;
import chameleon.oo.lookup.SimpleNameCrossReferenceWithArgumentsSelector;
import chameleon.oo.type.Type;
import chameleon.oo.type.generics.ActualTypeArgument;
import chameleon.oo.variable.FormalParameter;

public class PointcutReference<E extends PointcutReference<E>> extends 
		CrossReferenceWithArguments<E> implements CrossReference<E, Pointcut> {

	private String _pointcutName;

	public String name() {
		return _pointcutName;
	}

	public void setName(String method) {
		_pointcutName = method;
	}

	public PointcutReference(String name) {
		setName(name);
	}

	@Override
	public Pointcut getElement() throws LookupException {
		return (Pointcut) super.getElement();
	}
	
	@Override
	public DeclarationSelector<Declaration> selector() throws LookupException {
		return new SimpleNamePointcutSelector();
	}

	public class SimpleNamePointcutSelector<D extends Pointcut> extends SimpleNameCrossReferenceWithArgumentsSelector<D> {

		@Override
		public String name() {
			return PointcutReference.this.name();
		}

		@Override
		public int nbActualParameters() {
			return PointcutReference.this.nbActualParameters();
		}

		@Override
		public List<Type> getActualParameterTypes() throws LookupException {
			return PointcutReference.this.getActualParameterTypes();
		}

		@Override
		public Class<D> selectedClass() {
			return (Class<D>) Pointcut.class;
		}
		
		
	}


	@Override
	public VerificationResult verifySelf() {
		return super.verifySelf();
	}
	

	public boolean hasParameter(FormalParameter fp) {
		return indexOfParameter(fp) != -1;
	}

	public int indexOfParameter(FormalParameter fp) {
		int index = 0;
		
		for (Expression param : getActualParameters()) {
			if (!(param instanceof NamedTargetExpression))
				continue;
			
			if (((NamedTargetExpression) param).name().equals(fp.getName()))
				return index;
			
			index++;
		}
			
		
		return -1;
	}

	@Override
	public E clone() {
		CrossReferenceTarget target = null;
		if (getTarget() != null) {
			target = getTarget().clone();
		}
		final E result = (E) new PointcutReference<E>(name());
		result.setTarget(target);
		for (Expression element : getActualParameters()) {
			result.addArgument(element.clone());
		}
		for (ActualTypeArgument arg : typeArguments()) {
			result.addArgument(arg.clone());
		}
		return result;
	}

	@Override
	public LookupStrategy targetContext() throws LookupException {
		return getElement().targetContext();
	}
}