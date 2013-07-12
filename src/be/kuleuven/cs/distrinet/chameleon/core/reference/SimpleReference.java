package be.kuleuven.cs.distrinet.chameleon.core.reference;

import java.util.List;

import be.kuleuven.cs.distrinet.chameleon.core.declaration.Declaration;
import be.kuleuven.cs.distrinet.chameleon.core.declaration.QualifiedName;
import be.kuleuven.cs.distrinet.chameleon.core.declaration.Signature;
import be.kuleuven.cs.distrinet.chameleon.core.declaration.SimpleNameSignature;
import be.kuleuven.cs.distrinet.chameleon.core.declaration.TargetDeclaration;
import be.kuleuven.cs.distrinet.chameleon.core.lookup.DeclarationSelector;
import be.kuleuven.cs.distrinet.chameleon.core.lookup.SimpleSelector;
import be.kuleuven.cs.distrinet.chameleon.util.CreationStackTrace;
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

  public SimpleReference(String fqn, Class<D> specificClass, boolean recursiveLimit) {
    this(createTarget(fqn, specificClass, recursiveLimit), 
                   new SimpleNameSignature(Util.getLastPart(fqn)), 
                   specificClass);
  }

	protected static SimpleReference createTarget(String fqn, Class specificClass, boolean recursiveLimit) {
		String allButLastPart = Util.getAllButLastPart(fqn);
		if(allButLastPart == null) {
			return null;
		} else {
			return new SimpleReference(allButLastPart, recursiveLimit ? specificClass : TargetDeclaration.class, recursiveLimit);
		}
	}

  
	public SimpleReference(CrossReferenceTarget target, String name, Class<D> specificClass) {
		this(target, new SimpleNameSignature(name), specificClass);
	}

	public SimpleReference(CrossReferenceTarget  target, Signature signature, Class<D> specificClass) {
		super(signature);
		setTarget(target); 
		_specificClass = specificClass;
	}

	//Only used in JLo compiler. Need to test if the limit should be recursive or not.
	public SimpleReference(QualifiedName name, Class<D> specificClass) {
		this(targetOf(name, specificClass), Util.clone(name.lastSignature()), specificClass);
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


	public static <DD extends Declaration> CrossReference targetOf(QualifiedName name, Class<DD> specificClass) {
		SimpleReference current = null;
		List<Signature> signatures = name.signatures();
		int size = signatures.size();
		for(int i = 0; i < size-1; i++) {
			current = new SimpleReference<DD>(current, Util.clone(signatures.get(i)), specificClass);
		}
		return current;
	}
	
	protected static CrossReferenceTarget getTarget(String qn) {
		if(qn == null) {
			return null;
		}
		SimpleReference<TargetDeclaration> target = new SimpleReference<TargetDeclaration>(Util.getFirstPart(qn),TargetDeclaration.class);
		qn = Util.getAllButFirstPart(qn);
		while(qn != null) {
			SimpleReference<TargetDeclaration> newTarget = new SimpleReference<TargetDeclaration>(Util.getFirstPart(qn),TargetDeclaration.class);
			newTarget.setTarget(target);
			target = newTarget;
			qn = Util.getAllButFirstPart(qn);
		}
		return target;
	}


}
