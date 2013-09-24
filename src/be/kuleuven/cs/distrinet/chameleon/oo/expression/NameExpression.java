package be.kuleuven.cs.distrinet.chameleon.oo.expression;

import java.lang.ref.SoftReference;
import java.util.HashSet;
import java.util.Set;

import be.kuleuven.cs.distrinet.chameleon.core.Config;
import be.kuleuven.cs.distrinet.chameleon.core.declaration.Declaration;
import be.kuleuven.cs.distrinet.chameleon.core.declaration.Signature;
import be.kuleuven.cs.distrinet.chameleon.core.declaration.SimpleNameSignature;
import be.kuleuven.cs.distrinet.chameleon.core.element.Element;
import be.kuleuven.cs.distrinet.chameleon.core.lookup.DeclarationCollector;
import be.kuleuven.cs.distrinet.chameleon.core.lookup.DeclarationSelector;
import be.kuleuven.cs.distrinet.chameleon.core.lookup.DeclaratorSelector;
import be.kuleuven.cs.distrinet.chameleon.core.lookup.LookupException;
import be.kuleuven.cs.distrinet.chameleon.core.lookup.SimpleSelector;
import be.kuleuven.cs.distrinet.chameleon.core.reference.CrossReferenceTarget;
import be.kuleuven.cs.distrinet.chameleon.core.reference.CrossReferenceWithName;
import be.kuleuven.cs.distrinet.chameleon.core.reference.CrossReferenceWithTarget;
import be.kuleuven.cs.distrinet.chameleon.core.validation.BasicProblem;
import be.kuleuven.cs.distrinet.chameleon.core.validation.Valid;
import be.kuleuven.cs.distrinet.chameleon.core.validation.Verification;
import be.kuleuven.cs.distrinet.chameleon.exception.ChameleonProgrammerException;
import be.kuleuven.cs.distrinet.chameleon.oo.language.ObjectOrientedLanguage;
import be.kuleuven.cs.distrinet.chameleon.oo.type.DeclarationWithType;
import be.kuleuven.cs.distrinet.chameleon.oo.type.Type;
import be.kuleuven.cs.distrinet.chameleon.util.Util;
import be.kuleuven.cs.distrinet.chameleon.util.association.Single;

public class NameExpression extends TargetedExpression implements CrossReferenceWithName<DeclarationWithType>, CrossReferenceWithTarget<DeclarationWithType> {

  public NameExpression(String identifier) {
  	setSignature(new SimpleNameSignature(identifier));
	}
  
  public NameExpression(String identifier, CrossReferenceTarget target) {
  	this(identifier);
	  setTarget(target);
	}
  
  protected NameExpression() {
  }

  /********
   * NAME *
   ********/

  public String name() {
    return signature().name();
  }
  
  public String toString() {
  	return name();
  }

  public void setName(String name) {
    signature().setName(name);
  }

	public void setSignature(Signature signature) {
		set(_signature, signature);
	}

	public Signature signature() {
		return _signature.getOtherEnd();
	}

	private Single<Signature> _signature = new Single<Signature>(this);

	/**
	 * TARGET
	 */
	private Single<CrossReferenceTarget> _target = new Single<CrossReferenceTarget>(this);

  public CrossReferenceTarget getTarget() {
    return _target.getOtherEnd();
  }

  public void setTarget(CrossReferenceTarget target) {
  	set(_target,target);
  }
  
  protected Type actualType() throws LookupException {
    return getElement().declarationType();
  }


	@Override
	public NameExpression cloneSelf() {
    return new NameExpression();
	}

	@Override
	public Verification verifySelf() {
		Verification result = Valid.create();
		try {
			Element element = getElement();
		} catch (LookupException e) {
			result = result.and(new BasicProblem(this, "The referenced element cannot be found."));
		}
		return result;
	}

	public Set getDirectExceptions() throws LookupException {
    Set<Type> result = new HashSet<Type>();
    if(getTarget() != null) {
      Util.addNonNull(language(ObjectOrientedLanguage.class).getNullInvocationException(view().namespace()), result);
    }
    return result;
	}

	public Declaration getDeclarator() throws LookupException {
		return getElement(new DeclaratorSelector(selector()));
	}

	public DeclarationWithType getElement() throws LookupException {
  	return getElement(selector());
	}

  private SoftReference<DeclarationWithType> _cache;
  
  @Override
  public void flushLocalCache() {
  	super.flushLocalCache();
  	_cache = null;
  }
  
  protected DeclarationWithType getCache() {
  	DeclarationWithType result = null;
  	if(Config.cacheElementReferences() == true) {
  	  result = (_cache == null ? null : _cache.get());
  	}
  	return result;
  }
  
  protected void setCache(DeclarationWithType value) {
//  	if(! value.isDerived()) {
    	if(Config.cacheElementReferences() == true) {
    		_cache = new SoftReference<DeclarationWithType>(value);
    	}
//  	} else {
//  		_cache = null;
//  	}
  }

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
    CrossReferenceTarget target = getTarget();
    if(target != null) {
      target.targetContext().lookUp(collector);//findElement(getName());
    } else {
      lexicalContext().lookUp(collector);//findElement(getName());
    }
    result = collector.result(); 
//    if(result != null) {
//	  	//OPTIMISATION
	  	if(cache) {
	  		setCache((DeclarationWithType) result);
	  	}
      return result;
//    } else {
//    	// repeat for debugging purposes
//      if(target != null) {
//        result = target.targetContext().lookUp(selector);//findElement(getName());
//      } else {
//        result = lookupContext().lookUp(selector);//findElement(getName());
//      }
//    	throw new LookupException("Lookup of named target with name: "+name()+" returned null.");
//    }
  }

	public DeclarationSelector<DeclarationWithType> selector() {
		return _selector;
	}
	
	private DeclarationSelector<DeclarationWithType> _selector = new SimpleSelector<DeclarationWithType>(DeclarationWithType.class) {
		public Signature signature() {
			return NameExpression.this.signature();
		}
	};

}
