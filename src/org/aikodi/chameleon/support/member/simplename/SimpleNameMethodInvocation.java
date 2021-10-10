package org.aikodi.chameleon.support.member.simplename;

import org.aikodi.chameleon.core.event.name.NameChanged;
import org.aikodi.chameleon.core.lookup.LookupException;
import org.aikodi.chameleon.core.reference.CrossReferenceTarget;
import org.aikodi.chameleon.core.reference.CrossReferenceWithName;
import org.aikodi.chameleon.oo.expression.MethodInvocation;
import org.aikodi.chameleon.oo.language.ObjectOrientedLanguage;
import org.aikodi.chameleon.oo.lookup.SimpleNameCrossReferenceWithArgumentsSelector;
import org.aikodi.chameleon.oo.method.Method;
import org.aikodi.chameleon.oo.type.Type;

import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public abstract class SimpleNameMethodInvocation<D extends Method> extends MethodInvocation<D> implements CrossReferenceWithName<D> {

	public SimpleNameMethodInvocation(CrossReferenceTarget target, String name) {
		super(target);
		setName(name);
	}

	@Override
	protected Type actualType() throws LookupException {
		Method method = getElement();
		return method.returnType();
	}




	/********
	 * NAME *
	 ********/

	private String _methodName;

	@Override
	public String name() {
		return _methodName;
	}

	@Override
	public void setName(String name) {
		String old = _methodName;
		_methodName = name;
		if(changeNotificationEnabled()) {
			notify(new NameChanged(old, name));
		}
	}

	public abstract class SimpleNameMethodSelector extends SimpleNameCrossReferenceWithArgumentsSelector<D> {

		//  	private int _nameHash = SimpleNameMethodInvocation.this._methodName.hashCode();

		@Override
		public boolean selectedRegardlessOfName(D declaration) throws LookupException {
			boolean result = !declaration.isTrue(language(ObjectOrientedLanguage.class).CONSTRUCTOR());
			if(result) {
				result = super.selectedRegardlessOfName(declaration);
			}

			return result;
		}

		@Override
		public int nbActualParameters() {
			return SimpleNameMethodInvocation.this.nbActualParameters();
		}

		@Override
		public List<Type> getActualParameterTypes() throws LookupException {
			return SimpleNameMethodInvocation.this.getActualParameterTypes();
		}

		@Override
		public String name() {
			return SimpleNameMethodInvocation.this.name();
		}
	}
	
	@Override
	public String toString() {
		Collector<CharSequence, ?, String> joiner = Collectors.joining(", ");
		return (getTarget() == null ? "" : getTarget().toString()+".")
				+name()
				+"("+getActualParameters().stream().map(e -> e.toString()).collect(joiner)+")";
	}
	
}
