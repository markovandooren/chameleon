package be.kuleuven.cs.distrinet.chameleon.core.reference;

import java.util.List;

import be.kuleuven.cs.distrinet.chameleon.core.declaration.Declaration;
import be.kuleuven.cs.distrinet.chameleon.core.declaration.QualifiedName;
import be.kuleuven.cs.distrinet.chameleon.core.declaration.Signature;
import be.kuleuven.cs.distrinet.chameleon.core.declaration.SimpleNameSignature;
import be.kuleuven.cs.distrinet.chameleon.core.declaration.TargetDeclaration;
import be.kuleuven.cs.distrinet.chameleon.core.lookup.DeclarationCollector;
import be.kuleuven.cs.distrinet.chameleon.core.lookup.DeclarationSelector;
import be.kuleuven.cs.distrinet.chameleon.core.lookup.LookupException;
import be.kuleuven.cs.distrinet.chameleon.core.lookup.SelectorWithoutOrder;
import be.kuleuven.cs.distrinet.chameleon.util.Util;
import be.kuleuven.cs.distrinet.chameleon.util.association.Single;

public class SimpleReference<D extends Declaration> extends ElementReference<D> {

	private Class<D> _specificClass;
	
	
	/*@
  @ public behavior
  @
  @ pre qn != null;
  @
  @ post getTarget() == getTarget(Util.getAllButLastPart(qn));
  @ post getName() == Util.getLastPart(qn);
  @*/
	private SimpleReference(String qn) {
		this(getTarget(Util.getAllButLastPart(qn)), new SimpleNameSignature(Util.getLastPart(qn)));
	}

	/*@
  @ public behavior
  @
  @ pre name != null;
  @
  @ post getTarget() == target;
  @ post getName() == name;
  @*/
	private SimpleReference(CrossReferenceTarget target, Signature signature) {
		super(signature);
		setTarget(target); 
	}



	public SimpleReference(CrossReferenceTarget target, String name, Class<D> specificClass) {
		this(target, new SimpleNameSignature(name), specificClass);
	}

	public SimpleReference(CrossReferenceTarget  target, Signature signature, Class<D> specificClass) {
		this(target, signature);
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
		this(fqn);
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
			_selector = new SelectorWithoutOrder<D>(_specificClass) {
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

	/**
	 * TARGET
	 */
	private Single<CrossReferenceTarget> _target = new Single<CrossReferenceTarget>(this);

	protected Single<CrossReferenceTarget> targetLink() {
		return _target;
	}

	public CrossReferenceTarget getTarget() {
		return _target.getOtherEnd();
	}

	public void setTarget(CrossReferenceTarget target) {
		set(_target,target);
	}

	/*@
	  @ also public behavior
	  @
	  @ post getTarget() == null ==> \result == getContext(this).findPackageOrType(getName());
	  @ post getTarget() != null ==> (
	  @     (getTarget().getPackageOrType() == null ==> \result == null) &&
	  @     (getTarget().getPackageOrType() == null ==> \result == 
	  @         getTarget().getPackageOrType().getTargetContext().findPackageOrType(getName()));
	  @*/
	public <X extends Declaration> X getElement(DeclarationSelector<X> selector) throws LookupException {
		X result = null;

		//OPTIMISATION
		boolean cache = selector.equals(selector());
		if(cache) {
			result = (X) getCache();
		}
		if(result != null) {
			return result;
		}

		DeclarationCollector<X> collector = new DeclarationCollector<X>(selector);
		CrossReferenceTarget targetReference = getTarget();
		if(targetReference != null) {
			targetReference.targetContext().lookUp(collector);
		}
		else {
			lexicalContext().lookUp(collector);
		}
		result = collector.result();
		if(cache) {
			setCache((D) result);
		}
		return result;
	}

	public String toString() {
		return (getTarget() == null ? "" : getTarget().toString()+".")+signature().toString();
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
