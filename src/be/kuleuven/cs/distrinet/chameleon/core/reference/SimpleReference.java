package be.kuleuven.cs.distrinet.chameleon.core.reference;

import java.util.List;

import be.kuleuven.cs.distrinet.chameleon.core.declaration.Declaration;
import be.kuleuven.cs.distrinet.chameleon.core.declaration.QualifiedName;
import be.kuleuven.cs.distrinet.chameleon.core.declaration.Signature;
import be.kuleuven.cs.distrinet.chameleon.core.declaration.SimpleNameSignature;
import be.kuleuven.cs.distrinet.chameleon.core.declaration.TargetDeclaration;
import be.kuleuven.cs.distrinet.chameleon.core.lookup.DeclarationSelector;
import be.kuleuven.cs.distrinet.chameleon.core.lookup.SimpleSelector;
import be.kuleuven.cs.distrinet.chameleon.util.Util;

public class SimpleReference<D extends Declaration> extends ElementReference<D> {

	private Class<D> _specificClass;
	
	/**
	 * Initialize a new simple reference given a fully qualified name. The name is split at every dot, and
	 * multiple objects are created to form a chain of references.
	 * @param fqn
	 * @param specificClass
	 */
	public SimpleReference(String fqn, Class<D> specificClass) {
		this(fqn,specificClass,false);
	}

	public SimpleReference(Signature signature, Class<D> specificClass) {
		this(null, signature, specificClass);
	}
	
  public SimpleReference(String fqn, Class<D> specificClass, boolean recursiveLimit) {
    this(new SimpleNameSignature(Util.getLastPart(fqn)), specificClass);
    setTarget(createTarget(fqn, specificClass, recursiveLimit));
  }

	public SimpleReference(CrossReferenceTarget target, String name, Class<D> specificClass) {
		this(target, new SimpleNameSignature(name), specificClass);
	}

	public SimpleReference(CrossReferenceTarget  target, Signature signature, Class<D> specificClass) {
		super(signature);
		setTarget(target); 
		_specificClass = specificClass;
	}

	/**
	 * YOU MUST OVERRIDE THIS METHOD IF YOU SUBCLASS THIS CLASS!
	 */
	@Override
	protected SimpleReference<D> cloneSelf() {
	   return new SimpleReference<D>(null, (SimpleNameSignature)null, specificType());
	}


	private DeclarationSelector<D> _selector;
	
	@Override
	public DeclarationSelector<D> selector() {
		if(_selector == null) {
			_selector = new SimpleSelector<D>(_specificClass) {
				public Signature signature() {
					return SimpleReference.this.signature();
				}
			};
		}
		return _selector;
	}
	
	/**
	 * Return the {@link Class} object of the kind of elements that this reference can point at.
	 * @return
	 */
	public Class<D> specificType() {
		return _specificClass;
	}

	protected SimpleReference createTarget(String fqn, Class specificClass, boolean recursiveLimit) {
		String allButLastPart = Util.getAllButLastPart(fqn);
		if(allButLastPart == null) {
			return null;
		} else {
			return createSimpleReference(allButLastPart, recursiveLimit ? specificClass : TargetDeclaration.class, recursiveLimit);
		}
	}

	/**
	 * Subclasses must override this method and return an object of the type of the subclass.
	 * 
	 * @param fqn
	 * @param kind
	 * @param recursiveLimit
	 * @return
	 */
  protected <D extends Declaration> SimpleReference<D> createSimpleReference(String fqn, Class<D> kind, boolean recursiveLimit) {
  	return new SimpleReference(fqn, recursiveLimit ? kind : TargetDeclaration.class, recursiveLimit);
  }

}
