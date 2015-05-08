package org.aikodi.chameleon.support.member.simplename.variable;

import java.util.ArrayList;
import java.util.List;

import org.aikodi.chameleon.core.declaration.Declaration;
import org.aikodi.chameleon.core.lookup.DeclarationSelector;
import org.aikodi.chameleon.core.lookup.LookupContext;
import org.aikodi.chameleon.core.lookup.LookupException;
import org.aikodi.chameleon.core.lookup.SelectionResult;
import org.aikodi.chameleon.core.modifier.ElementWithModifiersImpl;
import org.aikodi.chameleon.core.modifier.Modifier;
import org.aikodi.chameleon.core.validation.BasicProblem;
import org.aikodi.chameleon.core.validation.Valid;
import org.aikodi.chameleon.core.validation.Verification;
import org.aikodi.chameleon.oo.expression.Expression;
import org.aikodi.chameleon.oo.type.Type;
import org.aikodi.chameleon.oo.type.TypeElement;
import org.aikodi.chameleon.oo.type.TypeReference;
import org.aikodi.chameleon.oo.variable.MemberVariable;
import org.aikodi.chameleon.oo.variable.RegularMemberVariable;
import org.aikodi.chameleon.oo.variable.VariableDeclaration;
import org.aikodi.chameleon.oo.variable.VariableDeclarator;
import org.aikodi.chameleon.util.association.Multi;
import org.aikodi.chameleon.util.association.Single;

public class MemberVariableDeclarator extends ElementWithModifiersImpl implements TypeElement, VariableDeclarator {
  //FIXME Why isn't this a member?
	public MemberVariableDeclarator() {
		
	}
	
	public MemberVariableDeclarator(TypeReference tref) {
		setTypeReference(tref);
	}
	
	@Override
   public List<VariableDeclaration> variableDeclarations() {
		return _declarations.getOtherEnds();
	}
	
	public void add(VariableDeclaration declaration) {
		add(_declarations,declaration);
	}
	
	public void remove(VariableDeclaration declaration) {
		remove(_declarations,declaration);
	}
	
	private Multi<VariableDeclaration> _declarations = new Multi<VariableDeclaration>(this);

	@Override
   public MemberVariable createVariable(String name, Expression expression) {
		MemberVariable result = new RegularMemberVariable(name, clone(typeReference()),expression);
		for(Modifier mod: modifiers()) {
			result.addModifier(clone(mod));
		}
		return result;
	}

	@Override
  protected MemberVariableDeclarator cloneSelf() {
		return new MemberVariableDeclarator(null);
	}

	@Override
   public List<MemberVariable> getIntroducedMembers() {
		List<MemberVariable> result = new ArrayList<MemberVariable>();
		for(VariableDeclaration declaration: variableDeclarations()) {
			result.add((MemberVariable) declaration.variable());
		}
		return result;
	}
	
	/**
	 * TYPE
	 */
	private Single<TypeReference> _typeReference = new Single<TypeReference>(this);

  public Type type() throws LookupException {
  	return typeReference().getElement();
  }
	
  @Override
public TypeReference typeReference() {
    return _typeReference.getOtherEnd();
  }

  public void setTypeReference(TypeReference type) {
    set(_typeReference,type);
  }

	@Override
   public List<? extends Declaration> declarations() throws LookupException {
		return getIntroducedMembers();
	}

	@Override
   public List<? extends Declaration> locallyDeclaredDeclarations() throws LookupException {
		return declarations();
	}

	@Override
   public <D extends Declaration> List<? extends SelectionResult> declarations(DeclarationSelector<D> selector) throws LookupException {
		return selector.selection(declarations());
	}

	@Override
	public Verification verifySelf() {
		Verification result = Valid.create();
		if(typeReference() == null) {
			result = result.and(new BasicProblem(this, "The variable declaration has no type"));
		}
		return result;
	}

	@Override
	public LookupContext localContext() throws LookupException {
		return language().lookupFactory().createLocalLookupStrategy(this);
	}

}
