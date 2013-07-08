package be.kuleuven.cs.distrinet.chameleon.core.reference;

import java.util.List;

import be.kuleuven.cs.distrinet.chameleon.core.declaration.Declaration;
import be.kuleuven.cs.distrinet.chameleon.core.declaration.QualifiedName;
import be.kuleuven.cs.distrinet.chameleon.core.declaration.Signature;
import be.kuleuven.cs.distrinet.chameleon.core.declaration.SimpleNameSignature;
import be.kuleuven.cs.distrinet.chameleon.core.lookup.DeclarationSelector;
import be.kuleuven.cs.distrinet.chameleon.core.lookup.SelectorWithoutOrder;
import be.kuleuven.cs.distrinet.chameleon.util.Util;

public class SimpleReference<D extends Declaration> extends ElementReferenceWithTarget<D> {

	private Class<D> _specificClass;
	
	public SimpleReference(CrossReferenceTarget target, String name, Class<D> specificClass) {
		this(target, new SimpleNameSignature(name), specificClass);
	}

	public SimpleReference(CrossReferenceTarget  target, Signature signature, Class<D> specificClass) {
		super(target, signature);
		_specificClass = specificClass;
	}

	public SimpleReference(QualifiedName name, Class<D> specificClass) {
		this(targetOf(name, specificClass), Util.clone(name.lastSignature()), specificClass);
	}

	/**
	 * Initialize a new simple reference given a fully qualified name. The name is split at every dot, and
	 * multiple objects are created to form a chain of references.
	 * @param fqn
	 * @param specificClass
	 */
	public SimpleReference(String fqn, Class<D> specificClass) {
		super(fqn);
		_specificClass = specificClass;
	}

	/**
	 * YOU MUST OVERRIDE THIS METHOD IF YOU SUBCLASS THIS CLASS!
	 */
	@Override
	protected SimpleReference<D> cloneSelf() {
	   return new SimpleReference<D>(null, (SimpleNameSignature)null, specificType());
	}

	public static <DD extends Declaration> CrossReference targetOf(QualifiedName name, Class<DD> specificClass) {
		SimpleReference current = null;
		List<Signature> signatures = name.signatures();
		int size = signatures.size();
		for(int i = 0; i < size-1; i++) {
			current = new SimpleReference<DD>(current, Util.clone(signatures.get(i)), specificClass);
		}
		return current;
	}
	
	private DeclarationSelector<D> _selector;
	
	@Override
	public DeclarationSelector<D> selector() {
		if(_selector == null) {
			_selector = new SelectorWithoutOrder<D>(_specificClass) {
				public Signature signature() {
					return SimpleReference.this.signature();
				}
			};
		}
		return _selector;
	}
	
	public Class<D> specificType() {
		return _specificClass;
	}

}
