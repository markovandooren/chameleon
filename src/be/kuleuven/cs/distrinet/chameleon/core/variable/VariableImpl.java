package be.kuleuven.cs.distrinet.chameleon.core.variable;

import java.util.List;

import be.kuleuven.cs.distrinet.chameleon.core.declaration.BasicDeclaration;
import be.kuleuven.cs.distrinet.chameleon.core.declaration.Declaration;
import be.kuleuven.cs.distrinet.chameleon.core.declaration.MissingSignature;
import be.kuleuven.cs.distrinet.chameleon.core.lookup.LookupException;
import be.kuleuven.cs.distrinet.chameleon.core.lookup.SelectionResult;
import be.kuleuven.cs.distrinet.chameleon.core.modifier.Modifier;
import be.kuleuven.cs.distrinet.chameleon.core.validation.Valid;
import be.kuleuven.cs.distrinet.chameleon.core.validation.Verification;
import be.kuleuven.cs.distrinet.chameleon.exception.ModelException;
import be.kuleuven.cs.distrinet.chameleon.oo.type.Type;
import be.kuleuven.cs.distrinet.chameleon.util.Lists;
import be.kuleuven.cs.distrinet.rejuse.property.Property;
import be.kuleuven.cs.distrinet.rejuse.property.PropertyMutex;

public abstract class VariableImpl extends BasicDeclaration implements Variable {
	
	public VariableImpl(String name) {
		super(name);
	}
	
	@Override
   public Type declarationType() throws LookupException {
		return getType();
	}
	
	@Override
   public boolean complete() {
		return true;
	}
	

	@Override
	public Verification verifySelf() {
		if(name() != null) {
		  return Valid.create();
		} else {
			return new MissingSignature(this); 
		}
	}

	/**
	 * COPIED FROM TypeElementImpl
	 */
  @Override
public List<Modifier> modifiers(PropertyMutex mutex) throws ModelException {
  	Property property = property(mutex);
  	List<Modifier> result = Lists.create();
  	for(Modifier mod: modifiers()) {
  		if(mod.impliesTrue(property)) {
  			result.add(mod);
  		}
  	}
  	return result;
  }

	@Override
   public List<Modifier> modifiers(Property property) throws ModelException {
		List<Modifier> result = Lists.create();
		for(Modifier modifier: modifiers()) {
			if(modifier.impliesTrue(property)) {
				result.add(modifier);
			}
		}
		return result;
	}
	
	@Override
   public String toString() {
		return getTypeReference().toString() +" "+name();
	}
	
	@Override
	public Declaration finalDeclaration() {
		return this;
	}

	@Override
	public Declaration template() {
		return finalDeclaration();
	}

	@Override
	public SelectionResult updatedTo(Declaration declaration) {
		return declaration;
	}

}
