package chameleon.core.expression;

import java.lang.ref.SoftReference;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.rejuse.association.SingleAssociation;

import chameleon.core.Config;
import chameleon.core.declaration.Declaration;
import chameleon.core.declaration.SimpleNameSignature;
import chameleon.core.element.Element;
import chameleon.core.lookup.DeclarationSelector;
import chameleon.core.lookup.DeclaratorSelector;
import chameleon.core.lookup.LookupException;
import chameleon.core.lookup.SelectorWithoutOrder;
import chameleon.core.reference.CrossReference;
import chameleon.core.validation.BasicProblem;
import chameleon.core.validation.Valid;
import chameleon.core.validation.VerificationResult;
import chameleon.oo.language.ObjectOrientedLanguage;
import chameleon.oo.type.DeclarationWithType;
import chameleon.oo.type.Type;
import chameleon.util.Util;

public class NamedTargetExpression extends TargetedExpression<NamedTargetExpression> implements CrossReference<NamedTargetExpression,Element,DeclarationWithType> {

  public NamedTargetExpression(String identifier) {
  	_signature = new SimpleNameSignature(identifier);
  	setName(identifier);
	}

  public NamedTargetExpression(String identifier, InvocationTarget target) {
  	this(identifier);
	  setTarget(target);
	}

  /********
   * NAME *
   ********/

  public String getName() {
    return _name;
  }

  public void setName(String name) {
    _name = name;
    _signature.setName(name);
  }

  private String _name;

	private SimpleNameSignature _signature;

	/**
	 * TARGET
	 */
	private SingleAssociation<NamedTargetExpression,InvocationTarget> _target = new SingleAssociation<NamedTargetExpression,InvocationTarget>(this);

  public InvocationTarget getTarget() {
    return _target.getOtherEnd();
  }

  public void setTarget(InvocationTarget target) {
  	if(target != null) {
      _target.connectTo(target.parentLink());
  	} else {
  		_target.connectTo(null);
  	}
  }
  
  protected Type actualType() throws LookupException {
    return getElement().declarationType();
  }


	@Override
	public NamedTargetExpression clone() {
    InvocationTarget target = null;
    if(getTarget() != null) {
      target = getTarget().clone();
    }
    return new NamedTargetExpression(getName(), target);
	}

	@Override
	public VerificationResult verifySelf() {
		VerificationResult result = Valid.create();
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
      Util.addNonNull(language(ObjectOrientedLanguage.class).getNullInvocationException(), result);
    }
    return result;
	}

  public List<? extends Element> children() {
    return Util.createNonNullList(getTarget());
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
	   
    InvocationTarget target = getTarget();
    if(target != null) {
      result = target.targetContext().lookUp(selector);//findElement(getName());
    } else {
      result = lexicalLookupStrategy().lookUp(selector);//findElement(getName());
    }
    if(result != null) {
	  	//OPTIMISATION
	  	if(cache) {
	  		setCache((DeclarationWithType) result);
	  	}
      return result;
    } else {
    	// repeat for debugging purposes
      if(target != null) {
        result = target.targetContext().lookUp(selector);//findElement(getName());
      } else {
        result = lexicalLookupStrategy().lookUp(selector);//findElement(getName());
      }
    	throw new LookupException("Lookup of named target with name: "+getName()+" returned null.");
    }
  }

	public DeclarationSelector<DeclarationWithType> selector() {
		return _selector;
	}
	
	private DeclarationSelector<DeclarationWithType> _selector = new SelectorWithoutOrder<DeclarationWithType>(new SelectorWithoutOrder.SignatureSelector() {
		public SimpleNameSignature signature() {
			return NamedTargetExpression.this._signature;
		}
	}, DeclarationWithType.class);

}
