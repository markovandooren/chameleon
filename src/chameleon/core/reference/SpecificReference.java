package chameleon.core.reference;

import chameleon.core.declaration.Declaration;
import chameleon.core.declaration.SimpleNameSignature;
import chameleon.core.declaration.TargetDeclaration;
import chameleon.core.element.Element;
import chameleon.core.lookup.DeclarationSelector;
import chameleon.core.lookup.SelectorWithoutOrder;

public class SpecificReference<E extends SpecificReference, P extends Element, R extends Declaration> extends ElementReferenceWithTarget<E,P,R> {

	private Class<R> _specificClass;
	
	public SpecificReference(String fqn, Class<R> specificClass){
		super(fqn);
		_specificClass = specificClass;
	}
	
	public SpecificReference(ElementReference<?, ?, ? extends TargetDeclaration> target, String name, Class<R> specificClass) {
		super(target, name);
		_specificClass = specificClass;
	}
	
	/**
	 * YOU MUST OVERRIDE THIS METHOD IF YOU SUBCLASS THIS CLASS!
	 */
	@Override
	public E clone() {
	   return (E) new SpecificReference((getTarget() == null ? null : getTarget().clone()), getName(), _specificClass);
	}

	@Override
	public DeclarationSelector<R> selector() {
		return new SelectorWithoutOrder<R>(new SimpleNameSignature(getName()),_specificClass);
	}

}

