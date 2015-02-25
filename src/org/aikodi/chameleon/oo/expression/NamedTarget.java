package org.aikodi.chameleon.oo.expression;

import java.lang.ref.SoftReference;
import java.util.HashSet;
import java.util.Set;

import org.aikodi.chameleon.core.Config;
import org.aikodi.chameleon.core.declaration.Declaration;
import org.aikodi.chameleon.core.declaration.TargetDeclaration;
import org.aikodi.chameleon.core.lookup.DeclarationCollector;
import org.aikodi.chameleon.core.lookup.DeclarationSelector;
import org.aikodi.chameleon.core.lookup.LookupContext;
import org.aikodi.chameleon.core.lookup.LookupException;
import org.aikodi.chameleon.core.lookup.NameSelector;
import org.aikodi.chameleon.core.reference.CrossReferenceImpl;
import org.aikodi.chameleon.core.reference.CrossReferenceTarget;
import org.aikodi.chameleon.core.reference.CrossReferenceWithName;
import org.aikodi.chameleon.core.reference.CrossReferenceWithTarget;
import org.aikodi.chameleon.oo.language.ObjectOrientedLanguage;
import org.aikodi.chameleon.oo.statement.CheckedExceptionList;
import org.aikodi.chameleon.util.Util;
import org.aikodi.chameleon.util.association.Single;
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
  public NamedTarget(String fqn, ExpressionFactory factory) {
	  setName(Util.getLastPart(fqn));
    fqn = Util.getAllButLastPart(fqn);
    if(fqn != null) {
      setTarget(factory.createNamedTarget(fqn)); 
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
  	setName(identifier);
  	setTarget(target);
  }
  
	/**
	 * TARGET
	 */
	private Single<CrossReferenceTarget> _target = new Single<CrossReferenceTarget>(this);

  @Override
public CrossReferenceTarget getTarget() {
    return _target.getOtherEnd();
  }

  @Override
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
   
  @Override
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
		synchronized(this) {
			if(result != null) {
				return result;
			}

		DeclarationCollector<X> collector = new DeclarationCollector<X>(selector);
	  CrossReferenceTarget target = getTarget();
    if(target != null) {
      target.targetContext().lookUp(collector);
    } else {
      lexicalContext().lookUp(collector);
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
//        result = lookupContext().lookUp(selector);//findElement(getName());
//      }
//    	throw new LookupException("Lookup of named target with name: "+name()+" returned null.");
//    }
		}
  }
  
  @Override
public DeclarationSelector<TargetDeclaration> selector() {
  	if(_selector == null) {
  		_selector = new NameSelector<TargetDeclaration>(TargetDeclaration.class) {
  			@Override
         public String name() {
  				return NamedTarget.this.name();
  			}
  		};
  	}
  	return _selector;
  }
  
  private DeclarationSelector<TargetDeclaration> _selector; 
  
  /********
   * NAME *
   ********/

  @Override
public String name() {
    return _name;
  }

  @Override
public void setName(String name) {
    _name = name;
  }

	private String _name;

  
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

	protected NamedTarget() {
		
	}
	
  @Override
protected NamedTarget cloneSelf() {
    return new NamedTarget(name(),(CrossReferenceTarget)null);
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

  @Override
public LookupContext targetContext() throws LookupException {
    return getElement().targetContext();
  }

//	public void setSignature(Signature signature) {
//		if(signature instanceof SimpleNameSignature) {
//			_signature = (SimpleNameSignature) signature;
//		} else {
//			throw new ChameleonProgrammerException();
//		}
//	}
	
	@Override
   public String toString() {
		return name();
	}
}
