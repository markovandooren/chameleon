package org.aikodi.chameleon.core.reference;

import java.lang.ref.SoftReference;
import java.util.concurrent.atomic.AtomicReference;

import org.aikodi.chameleon.core.Config;
import org.aikodi.chameleon.core.declaration.Declaration;
import org.aikodi.chameleon.core.lookup.DeclarationCollector;
import org.aikodi.chameleon.core.lookup.DeclarationSelector;
import org.aikodi.chameleon.core.lookup.LookupException;
import org.aikodi.chameleon.exception.ChameleonProgrammerException;
import org.aikodi.chameleon.util.association.Single;

/**
 * 
 * 
 * @author Marko van Dooren
 */
public abstract class ElementReference<D extends Declaration> extends CrossReferenceImpl<D> implements CrossReferenceWithName<D>, CrossReferenceWithTarget<D> {

	
//	protected ElementReference() {
//	}
	
	/*@
   @ public behavior
   @
   @ pre name != null;
   @
   @ post getName().equals(name);
   @*/
	public ElementReference(String name) {
		setName(name);
	}

  private String _name;

//  /**
//   * Return the signature of this element reference.
//   */
//  public Signature signature() {
//  	return _signature.getOtherEnd();
//  }
  
  @Override
public String name() {
  	return _name;
  }
  
// /*@
//   @ public behavior
//   @
//   @ pre name != null;
//   @
//   @ post getName() == name;
//   @*/
//  public void setSignature(Signature signature) {
//  	set(_signature, signature);
//  }
  
	@Override
	public final void setName(String name) {
		if(name == null) {
			throw new ChameleonProgrammerException("The name of an element reference cannot be null");
		} else if(name.equals("")) {
			throw new ChameleonProgrammerException("The name of an element reference cannot be the empty string");
		}
		_name = name;
	}
  
	/**
	 * The type of an expression is cached to increase performance. Call {@link #flushCache()} to
	 * flush the cache of the model has changed.
	 * 
	 * The reference is stored in a soft reference to allow garbage collection of cached types.
	 * 
	 * The soft reference is stored in an atomic reference to deal with concurrent lookups of the
	 * type of this expression without needing a lock.
	 */
	private final AtomicReference<SoftReference<D>> _cache = new AtomicReference<>();
//  private SoftReference<D> _cache;
  
  @Override
  public void flushLocalCache() {
  	super.flushLocalCache();
//  	_cache = null;
  	
		boolean success = false;
		do {
			success = _cache.compareAndSet(_cache.get(), null);
		} while(! success);


  }
  
  protected D getCache() {
  	D result = null;
  	if(Config.cacheElementReferences() == true) {
  		SoftReference<D> cache = _cache.get();
  	  result = (cache == null ? null: cache.get());
  	}
    return result;
  }
  
  protected void setCache(D value) {
    	if(Config.cacheElementReferences() == true) {
    		// We only try to set once. Concurrent lookups of this name
    		// should result in the same element.
				_cache.compareAndSet(null, new SoftReference<D>(value));
//    		_cache = new SoftReference<D>(value);
    	}
  }
  
	/**
	 * TARGET
	 */
	private Single<CrossReferenceTarget> _target = new Single<CrossReferenceTarget>(this);

	protected Single<CrossReferenceTarget> targetLink() {
		return _target;
	}

	@Override
   public CrossReferenceTarget getTarget() {
		return _target.getOtherEnd();
	}

	@Override
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
	@Override
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

//synchronized(this) {
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
//		}
	}

	@Override
   public String toString() {
		return (getTarget() == null ? "" : getTarget().toString()+".")+name();
	}

}
