package be.kuleuven.cs.distrinet.chameleon.core.reference;

import java.lang.ref.SoftReference;

import be.kuleuven.cs.distrinet.chameleon.core.Config;
import be.kuleuven.cs.distrinet.chameleon.core.declaration.Declaration;
import be.kuleuven.cs.distrinet.chameleon.core.declaration.Signature;
import be.kuleuven.cs.distrinet.chameleon.core.lookup.DeclarationCollector;
import be.kuleuven.cs.distrinet.chameleon.core.lookup.DeclarationSelector;
import be.kuleuven.cs.distrinet.chameleon.core.lookup.LookupException;
import be.kuleuven.cs.distrinet.chameleon.util.association.Single;

/**
 * 
 * 
 * @author Marko van Dooren
 */
public abstract class ElementReference<D extends Declaration> extends CrossReferenceImpl<D> implements CrossReferenceWithName<D>, CrossReferenceWithTarget<D> {

//	private static Logger logger = Logger.getLogger("lookup.elementreference");
	
//	public Logger lookupLogger() {
//		return logger;
//	}
	
	public ElementReference() {
	}
	
//	public abstract ElementReference<D> clone();

	/*@
   @ public behavior
   @
   @ pre name != null;
   @
   @ post getName().equals(name);
   @*/
	public ElementReference(Signature signature) {
		setSignature(signature);
	}
	
  private Single<Signature> _signature = new Single<Signature>(this);

  /**
   * Return the signature of this element reference.
   */
  public Signature signature() {
  	return _signature.getOtherEnd();
  }
  
  public String name() {
  	return signature().name();
  }
  
 /*@
   @ public behavior
   @
   @ pre name != null;
   @
   @ post getName() == name;
   @*/
  public void setSignature(Signature signature) {
  	set(_signature, signature);
  }
  
	@Override
	public final void setName(String name) {
		signature().setName(name);
	}
  
  private SoftReference<D> _cache;
  
  @Override
  public synchronized void flushLocalCache() {
  	super.flushLocalCache();
  	_cache = null;
  }
  
  protected synchronized D getCache() {
  	D result = null;
  	if(Config.cacheElementReferences() == true) {
  	  result = (_cache == null ? null: _cache.get());
  	}
    return result;
  }
  
  protected synchronized void setCache(D value) {
    	if(Config.cacheElementReferences() == true) {
    		_cache = new SoftReference<D>(value);
    	}
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

}
