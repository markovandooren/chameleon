package chameleon.oo.expression;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.rejuse.association.SingleAssociation;

import chameleon.core.declaration.Declaration;
import chameleon.core.declaration.Signature;
import chameleon.core.declaration.SimpleNameSignature;
import chameleon.core.element.Element;
import chameleon.core.lookup.DeclarationCollector;
import chameleon.core.lookup.DeclarationSelector;
import chameleon.core.lookup.DeclaratorSelector;
import chameleon.core.lookup.LookupException;
import chameleon.core.lookup.SelectorWithoutOrder;
import chameleon.core.reference.CrossReference;
import chameleon.core.reference.CrossReferenceTarget;
import chameleon.core.reference.UnresolvableCrossReference;
import chameleon.core.validation.Valid;
import chameleon.core.validation.VerificationResult;
import chameleon.exception.ChameleonProgrammerException;
import chameleon.oo.language.ObjectOrientedLanguage;
import chameleon.oo.type.Type;
import chameleon.oo.variable.Variable;
import chameleon.util.Util;

/**
 * @author Marko van Dooren
 */
public class VariableReference extends Expression<VariableReference> implements Assignable<VariableReference>, CrossReference<VariableReference,Variable> {

  /**
   * Create a new variable reference with the given identifier as name, and
   * the given invocation target as target.
   */
 /*@
   @ public behavior
   @
   @ post getName() == identifier;
   @ post getTarget() == target;
   @*/
  public VariableReference(String identifier, CrossReferenceTarget target) {
  	_signature = new SimpleNameSignature(identifier);
  	setName(identifier);
	  setTarget(target);
  }

  /********
   * NAME *
   ********/

  public String name() {
    return signature().name();
  }
  
  public SimpleNameSignature signature() {
  	return _signature;
  }

  public void setName(String name) {
    _signature.setName(name);
  }

	public void setSignature(Signature signature) {
		if(signature instanceof SimpleNameSignature) {
			_signature = (SimpleNameSignature) signature;
		} else {
			throw new ChameleonProgrammerException();
		}
	}

	private SimpleNameSignature _signature;

	/**
	 * TARGET
	 */
	private SingleAssociation<VariableReference,CrossReferenceTarget> _target = new SingleAssociation<VariableReference,CrossReferenceTarget>(this);

  public CrossReferenceTarget getTarget() {
    return _target.getOtherEnd();
  }

  public void setTarget(CrossReferenceTarget target) {
  	if(target != null) {
      _target.connectTo(target.parentLink());
  	} else {
  		_target.connectTo(null);
  	}
  }

  public Variable getVariable() throws LookupException {
  	return getElement();
  }

  protected Type actualType() throws LookupException {
    return getVariable().getType();
  }

  public VariableReference clone() {
    CrossReferenceTarget target = null;
    if(getTarget() != null) {
      target = getTarget().clone();
    }
    return new VariableReference(name(), target);
  }

//  public void prefix(InvocationTarget target) throws LookupException {
//    getTarget().prefixRecursive(target);
//  }
  
//  public void substituteParameter(String name, Expression expr) throws MetamodelException {
//    getTarget().substituteParameter(name, expr);
//  }

  public Set<Type> getDirectExceptions() throws LookupException {
    Set<Type> result = new HashSet<Type>();
    if(getTarget() != null) {
      Util.addNonNull(language(ObjectOrientedLanguage.class).getNullInvocationException(), result);
    }
    return result;
  }
  
  public List<? extends Element> children() {
    return Util.createNonNullList(getTarget());
  }

  public Variable getElement() throws LookupException {
  	return getElement(selector());
  }
  
	public Declaration getDeclarator() throws LookupException {
		return getElement(new DeclaratorSelector(selector()));
	}
  
  @SuppressWarnings("unchecked")
  public <X extends Declaration> X getElement(DeclarationSelector<X> selector) throws LookupException {
		DeclarationCollector<X> collector = new DeclarationCollector<X>(selector);
    CrossReferenceTarget<?> target = getTarget();
    X result;
    if(target != null) {
      target.targetContext().lookUp(collector);//findElement(getName());
    } else {
      lexicalLookupStrategy().lookUp(collector);//findElement(getName());
    }
//    if(result != null) {
      return collector.result();
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

	public DeclarationSelector<Variable> selector() {
		return new SelectorWithoutOrder<Variable>(Variable.class) {
			public Signature signature() {
				return _signature;
			}
		};
	}

	@Override
	public VerificationResult verifySelf() {
		try {
			Variable referencedVariable = getElement();
			if(referencedVariable != null) {
				return Valid.create();
			} else {
				return new UnresolvableCrossReference(this);
			}
		} catch (LookupException e) {
			return new UnresolvableCrossReference(this);
		}
	}

}
