package chameleon.core.reference;

import java.util.List;

import chameleon.core.declaration.Declaration;
import chameleon.core.declaration.QualifiedName;
import chameleon.core.declaration.Signature;
import chameleon.core.declaration.SimpleNameSignature;
import chameleon.core.declaration.TargetDeclaration;
import chameleon.core.element.Element;
import chameleon.core.lookup.DeclarationSelector;
import chameleon.core.lookup.SelectorWithoutOrder;

public class SpecificReference<E extends SpecificReference, P extends Element, D extends Declaration> extends ElementReferenceWithTarget<E,P,D> {

	private Class<D> _specificClass;
	
	public SpecificReference(String fqn, Class<D> specificClass){
		super(fqn);
		_specificClass = specificClass;
		_selector = new SelectorWithoutOrder<D>(new SelectorWithoutOrder.SignatureSelector() {
			public Signature signature() {
				return SpecificReference.this.signature();
			}
		},_specificClass);
	}

	public SpecificReference(CrossReference<?, ?, ? extends TargetDeclaration> target, Signature signature, Class<D> specificClass) {
		super(target, signature);
		_specificClass = specificClass;
		_selector = new SelectorWithoutOrder<D>(new SelectorWithoutOrder.SignatureSelector() {
			public Signature signature() {
				return SpecificReference.this.signature();
			}
		},_specificClass);
	}

	public SpecificReference(CrossReference<?, ?, ? extends TargetDeclaration> target, String name, Class<D> specificClass) {
		this(target, new SimpleNameSignature(name), specificClass);
//		super(target, name);
//		_specificClass = specificClass;
//		_selector = new SelectorWithoutOrder<D>(new SelectorWithoutOrder.SignatureSelector() {
//			public Signature signature() {
//				return SpecificReference.this.signature();
//			}
//		},_specificClass);
	}
	
	public SpecificReference(QualifiedName<?,?> name, Class<D> specificClass) {
		this(targetOf(name, specificClass), name.lastSignature().clone(), specificClass);
	}
	
	public static <DD extends Declaration> CrossReference targetOf(QualifiedName<?,?> name, Class<DD> specificClass) {
		SpecificReference current = null;
		List<Signature> signatures = name.signatures();
		int size = signatures.size();
		for(int i = 0; i < size-1; i++) {
			current = new SpecificReference<SpecificReference, Element, DD>(current, signatures.get(i).clone(), specificClass);
		}
		return current;
	}
	
	/**
	 * YOU MUST OVERRIDE THIS METHOD IF YOU SUBCLASS THIS CLASS!
	 */
	@Override
	public E clone() {
	   return (E) new SpecificReference((getTarget() == null ? null : getTarget().clone()), signature().clone(), _specificClass);
	}

	private final DeclarationSelector<D> _selector;
	
	@Override
	public DeclarationSelector<D> selector() {
		return _selector;
	}
	
	public Class<D> specificType() {
		return _specificClass;
	}

}

