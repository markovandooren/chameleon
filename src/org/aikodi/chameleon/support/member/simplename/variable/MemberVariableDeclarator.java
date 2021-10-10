package org.aikodi.chameleon.support.member.simplename.variable;

import org.aikodi.chameleon.core.declaration.Declaration;
import org.aikodi.chameleon.core.declaration.Declarator;
import org.aikodi.chameleon.core.lookup.DeclarationSelector;
import org.aikodi.chameleon.core.lookup.LookupContext;
import org.aikodi.chameleon.core.lookup.LookupException;
import org.aikodi.chameleon.core.lookup.SelectionResult;
import org.aikodi.chameleon.core.modifier.ElementWithModifiersImpl;
import org.aikodi.chameleon.core.modifier.Modifier;
import org.aikodi.chameleon.core.validation.BasicProblem;
import org.aikodi.chameleon.core.validation.Valid;
import org.aikodi.chameleon.core.validation.Verification;
import org.aikodi.chameleon.core.variable.Variable;
import org.aikodi.chameleon.oo.expression.Expression;
import org.aikodi.chameleon.oo.type.TypeReference;
import org.aikodi.chameleon.oo.variable.RegularMemberVariable;
import org.aikodi.chameleon.oo.variable.VariableDeclaration;
import org.aikodi.chameleon.oo.variable.VariableDeclarator;
import org.aikodi.chameleon.util.association.Multi;
import org.aikodi.chameleon.util.association.Single;

import java.util.ArrayList;
import java.util.List;

public class MemberVariableDeclarator extends ElementWithModifiersImpl implements Declarator, VariableDeclarator {

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
   public Variable createVariable(String name, Expression expression) {
		Variable result = new RegularMemberVariable(name, clone(typeReference()),expression);
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
   public List<Declaration> declaredDeclarations() {
		List<Declaration> result = new ArrayList<Declaration>();
		for(VariableDeclaration declaration: variableDeclarations()) {
			result.add(declaration.variable());
		}
		return result;
	}
	
	/**
	 * TYPE
	 */
	private Single<TypeReference> _typeReference = new Single<TypeReference>(this);

  @Override
  public TypeReference typeReference() {
    return _typeReference.getOtherEnd();
  }

  public void setTypeReference(TypeReference type) {
    set(_typeReference,type);
  }

	@Override
   public List<? extends Declaration> declarations() throws LookupException {
		return declaredDeclarations();
	}

	@Override
   public List<? extends Declaration> locallyDeclaredDeclarations() throws LookupException {
		return declarations();
	}

	@Override
   public <D extends Declaration> List<? extends SelectionResult<D>> declarations(DeclarationSelector<D> selector) throws LookupException {
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
