package org.aikodi.chameleon.aspect.core.model.pointcut;

import java.util.List;

import org.aikodi.chameleon.core.declaration.Declaration;
import org.aikodi.chameleon.core.lookup.DeclarationSelector;
import org.aikodi.chameleon.core.lookup.LookupContext;
import org.aikodi.chameleon.core.lookup.LookupException;
import org.aikodi.chameleon.core.reference.CrossReference;
import org.aikodi.chameleon.core.reference.CrossReferenceWithArguments;
import org.aikodi.chameleon.core.validation.Verification;
import org.aikodi.chameleon.oo.expression.Expression;
import org.aikodi.chameleon.oo.expression.NameExpression;
import org.aikodi.chameleon.oo.lookup.SimpleNameCrossReferenceWithArgumentsSelector;
import org.aikodi.chameleon.oo.type.Type;
import org.aikodi.chameleon.oo.variable.FormalParameter;

public class PointcutReference extends CrossReferenceWithArguments implements CrossReference<Pointcut> {

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
	public Verification verifySelf() {
		return super.verifySelf();
	}
	

	public boolean hasParameter(FormalParameter fp) {
		return indexOfParameter(fp) != -1;
	}

	public int indexOfParameter(FormalParameter fp) {
		int index = 0;
		
		for (Expression param : getActualParameters()) {
			if (!(param instanceof NameExpression))
				continue;
			
			if (((NameExpression) param).name().equals(fp.name()))
				return index;
			
			index++;
		}
			
		
		return -1;
	}

	@Override
	protected PointcutReference cloneSelf() {
		return new PointcutReference(name());
	}

	@Override
	public LookupContext targetContext() throws LookupException {
		return getElement().targetContext();
	}
}
