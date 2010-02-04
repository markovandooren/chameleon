package chameleon.core.expression;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.rejuse.association.SingleAssociation;

import chameleon.core.declaration.Declaration;
import chameleon.core.declaration.Signature;
import chameleon.core.declaration.SimpleNameSignature;
import chameleon.core.element.Element;
import chameleon.core.lookup.DeclarationSelector;
import chameleon.core.lookup.DeclaratorSelector;
import chameleon.core.lookup.LookupException;
import chameleon.core.lookup.SelectorWithoutOrder;
import chameleon.core.reference.CrossReference;
import chameleon.core.type.DeclarationWithType;
import chameleon.core.type.Type;
import chameleon.core.validation.BasicProblem;
import chameleon.core.validation.Valid;
import chameleon.core.validation.VerificationResult;
import chameleon.oo.language.ObjectOrientedLanguage;
import chameleon.util.Util;

public class NamedTargetExpression extends Expression<NamedTargetExpression> implements CrossReference<NamedTargetExpression,Element,DeclarationWithType>{

  public NamedTargetExpression(String identifier, InvocationTarget target) {
  	_signature = new SimpleNameSignature(identifier);
  	setName(identifier);
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

  public <X extends Declaration> X getElement(DeclarationSelector<X> selector) throws LookupException {
    InvocationTarget target = getTarget();
    X result;
    if(target != null) {
      result = target.targetContext().lookUp(selector);//findElement(getName());
    } else {
      result = lexicalLookupStrategy().lookUp(selector);//findElement(getName());
    }
    if(result != null) {
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
		return new SelectorWithoutOrder<DeclarationWithType>(new SelectorWithoutOrder.SignatureSelector() {
			public Signature signature() {
				return _signature;
			}
		}, DeclarationWithType.class);
	}

}
