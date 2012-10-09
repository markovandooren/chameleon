package chameleon.oo.expression;

import java.lang.ref.SoftReference;
import java.util.HashSet;
import java.util.Set;

import org.rejuse.association.AssociationListener;

import chameleon.core.Config;
import chameleon.core.declaration.Declaration;
import chameleon.core.declaration.Signature;
import chameleon.core.declaration.SimpleNameSignature;
import chameleon.core.declaration.TargetDeclaration;
import chameleon.core.element.Element;
import chameleon.core.element.ElementImpl;
import chameleon.core.lookup.DeclarationCollector;
import chameleon.core.lookup.DeclarationSelector;
import chameleon.core.lookup.LookupException;
import chameleon.core.lookup.LookupStrategy;
import chameleon.core.lookup.SelectorWithoutOrder;
import chameleon.core.reference.CrossReferenceImpl;
import chameleon.core.reference.CrossReferenceTarget;
import chameleon.core.reference.CrossReferenceWithName;
import chameleon.core.reference.CrossReferenceWithTarget;
import chameleon.exception.ChameleonProgrammerException;
import chameleon.oo.language.ObjectOrientedLanguage;
import chameleon.oo.statement.CheckedExceptionList;
import chameleon.util.CreationStackTrace;
import chameleon.util.Util;
import chameleon.util.association.Single;
public class NamedTarget extends CrossReferenceImpl<TargetDeclaration> implements CrossReferenceTarget, CrossReferenceWithTarget<TargetDeclaration>, CrossReferenceWithName<TargetDeclaration> {

	/**
	 * Initialize a new named target with the given fully qualified name. The
	 * constructor will split up the name at the dots if there are any.
	 * @param fqn
	 */
 /*@
   @ public behavior
   @
   @ pre fqn != null;
   @
   @*/
  public NamedTarget(String fqn) {
  	_signature=new SimpleNameSignature(""); // name will be set correctly in setName().
	  setName(Util.getLastPart(fqn));
    fqn = Util.getAllButLastPart(fqn);
    if(fqn != null) {
      setTarget(new NamedTarget(fqn)); 
    }

  }
  
  /**
   * Initialize a new named target with the given identifier as name,
   * and the given target as its target. The name should be an identifier
   * and thus not contain dots.
   * 
   * @param identifier
   * @param target
   */
  public NamedTarget(String identifier, CrossReferenceTarget target) {
  	_signature=new SimpleNameSignature(""); // name will be set correctly in setName().
  	setName(identifier);
  	setTarget(target);
  }
  
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

  private SoftReference<TargetDeclaration> _cache;
  
  @Override
  public void flushLocalCache() {
  	super.flushLocalCache();
  	_cache = null;
  }
  
  protected TargetDeclaration getCache() {
  	TargetDeclaration result = null;
  	if(Config.cacheElementReferences() == true) {
  	  result = (_cache == null ? null : _cache.get());
  	}
  	return result;
  }
  
  protected void setCache(TargetDeclaration value) {
//  	if(! value.isDerived()) {
    	if(Config.cacheElementReferences() == true) {
    		_cache = new SoftReference<TargetDeclaration>(value);
    	}
//  	} else {
//  		_cache = null;
//  	}
  }
  
  /***********
   * CONTEXT *
   ***********/
   
  @SuppressWarnings("unchecked")
  protected <X extends Declaration> X getElement(DeclarationSelector<X> selector) throws LookupException {
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
      target.targetContext().lookUp(collector);
    } else {
      lexicalLookupStrategy().lookUp(collector);
    }
    result = collector.result();
//    if(result != null) {
//	  	//OPTIMISATION
	  	if(cache) {
	  		setCache((TargetDeclaration) result);
	  	}
      return result;
//    } else {
//    	// repeat for debugging purposes
//      if(target != null) {
//        result = target.targetContext().lookUp(selector);//findElement(getName());
//      } else {
//        result = lexicalLookupStrategy().lookUp(selector);//findElement(getName());
//      }
//    	throw new LookupException("Lookup of named target with name: "+name()+" returned null.");
//    }
  }
  
  public DeclarationSelector<TargetDeclaration> selector() {
  	if(_selector == null) {
  		_selector = new SelectorWithoutOrder<TargetDeclaration>(TargetDeclaration.class) {
  			public SimpleNameSignature signature() {
  				return NamedTarget.this._signature;
  			}
  		};
  	}
  	return _selector;
  }
  
  private DeclarationSelector<TargetDeclaration> _selector; 
  
  /********
   * NAME *
   ********/

  public String name() {
    return signature().name();
  }

  public void setName(String name) {
    _signature.setName(name);
  }

  public SimpleNameSignature signature() {
  	return _signature;
  }
  
	private SimpleNameSignature _signature;

  
//  private boolean compatVar(Variable variable, Variable variable2) throws LookupException {
//    return ((variable == null) && (variable2 == null)) || 
//           (variable != null) && (
//               variable.equals(variable2) ||
//               (
//                   (variable instanceof FormalParameter) && (variable2 instanceof FormalParameter) &&
//                   ((FormalParameter)variable).compatibleWith((FormalParameter)variable2)
//               )
//           );
//  }
  
  

//  public boolean compatibleWith(InvocationTarget target) throws LookupException {
//    return superOf(target) || target.subOf(this);
//  }

//  public boolean subOf(InvocationTarget target) throws LookupException {
//    return false;
//  }

  public NamedTarget clone() {
    NamedTarget result = new NamedTarget(name());
    CrossReferenceTarget target = getTarget();
		if(target!= null) {
      result.setTarget(target.clone());
    }
    return result;
  }
  
//  public void prefix(InvocationTarget target) throws LookupException {
//    if (getTarget() == null) {
//      Target vt = getElement();
//      if (vt instanceof MemberVariable) {
//          setTarget(target);
//      }
//    }
//    else {
//      getTarget().prefixRecursive(target);
//    }
//  }

//  public void prefixRecursive(InvocationTarget target) throws LookupException {
//    prefix(target);
//  }

  @SuppressWarnings("unchecked")
  public Set getDirectExceptions() throws LookupException {
    Set result = new HashSet();
    if(getTarget() != null) {
      Util.addNonNull(language(ObjectOrientedLanguage.class).getNullInvocationException(view().namespace()), result);
    }
    return result;
  }

//  public Set getExceptions() throws LookupException {
//    Set result = getDirectExceptions();
//    if(getTarget() != null) {
//      result.addAll(getTarget().getExceptions());
//    }
//    return result;
//  }

  public CheckedExceptionList getCEL() throws LookupException {
//    if(getTarget() != null) {
//      return getTarget().getCEL();
//    }
//    else {
      return new CheckedExceptionList();
//    }
  }

  public CheckedExceptionList getAbsCEL() throws LookupException {
//    if(getTarget() != null) {
//      return getTarget().getAbsCEL();
//    }
//    else {
      return new CheckedExceptionList();
//    }
  }

  public LookupStrategy targetContext() throws LookupException {
    return getElement().targetContext();
  }

	public void setSignature(Signature signature) {
		if(signature instanceof SimpleNameSignature) {
			_signature = (SimpleNameSignature) signature;
		} else {
			throw new ChameleonProgrammerException();
		}
	}
	
	public String toString() {
		return signature().name();
	}
}
