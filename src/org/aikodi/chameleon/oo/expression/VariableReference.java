package org.aikodi.chameleon.oo.expression;

import org.aikodi.chameleon.core.declaration.Declaration;
import org.aikodi.chameleon.core.event.name.NameChanged;
import org.aikodi.chameleon.core.lookup.DeclarationCollector;
import org.aikodi.chameleon.core.lookup.DeclarationSelector;
import org.aikodi.chameleon.core.lookup.LookupException;
import org.aikodi.chameleon.core.lookup.NameSelector;
import org.aikodi.chameleon.core.reference.CrossReference;
import org.aikodi.chameleon.core.reference.CrossReferenceTarget;
import org.aikodi.chameleon.core.reference.UnresolvableCrossReference;
import org.aikodi.chameleon.core.validation.Valid;
import org.aikodi.chameleon.core.validation.Verification;
import org.aikodi.chameleon.core.variable.Variable;
import org.aikodi.chameleon.oo.type.Type;
import org.aikodi.chameleon.util.association.Single;

/**
 * @author Marko van Dooren
 */
public class VariableReference extends Expression implements Assignable, CrossReference<Variable> {

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
  	setName(identifier);
	  setTarget(target);
  }

  /********
   * NAME *
   ********/

  public String name() {
    return _name;
  }
  
  public void setName(String name) {
    String old = _name;
    _name = name;
    if(changeNotificationEnabled()) {
      notify(new NameChanged(old, name));
    }
  }

  /**
   * @{inheritDoc}
   */
  @Override
  public Class<Variable> referencedType() {
    return Variable.class;
  }
  
	private String _name;

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

  public Variable getVariable() throws LookupException {
  	return getElement();
  }

  @Override
protected Type actualType() throws LookupException {
    return getVariable().getType();
  }

  @Override
public VariableReference cloneSelf() {
    return new VariableReference(name(), null);
  }

  @Override
public Variable getElement() throws LookupException {
  	return getElement(selector());
  }
  
  @SuppressWarnings("unchecked")
  public <X extends Declaration> X getElement(DeclarationSelector<X> selector) throws LookupException {
		DeclarationCollector<X> collector = new DeclarationCollector<X>(selector);
    CrossReferenceTarget target = getTarget();
    X result;
    if(target != null) {
      target.targetContext().lookUp(collector);//findElement(getName());
    } else {
      lexicalContext().lookUp(collector);//findElement(getName());
    }
      return collector.result();
  }

	public DeclarationSelector<Variable> selector() {
		return new NameSelector<Variable>(Variable.class) {
			@Override
         public String name() {
				return _name;
			}
		};
	}

	@Override
	public Verification verifySelf() {
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
