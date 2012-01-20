package chameleon.support.member.simplename.variable;

import java.util.ArrayList;
import java.util.List;

import org.rejuse.association.OrderedMultiAssociation;
import org.rejuse.association.SingleAssociation;

import chameleon.core.declaration.Declaration;
import chameleon.core.declaration.SimpleNameSignature;
import chameleon.core.element.Element;
import chameleon.core.lookup.DeclarationSelector;
import chameleon.core.lookup.LookupException;
import chameleon.core.modifier.Modifier;
import chameleon.core.validation.BasicProblem;
import chameleon.core.validation.Valid;
import chameleon.core.validation.VerificationResult;
import chameleon.oo.expression.Expression;
import chameleon.oo.member.Member;
import chameleon.oo.type.Type;
import chameleon.oo.type.TypeElement;
import chameleon.oo.type.TypeElementImpl;
import chameleon.oo.type.TypeReference;
import chameleon.oo.variable.MemberVariable;
import chameleon.oo.variable.RegularMemberVariable;
import chameleon.oo.variable.VariableDeclaration;
import chameleon.oo.variable.VariableDeclarator;
import chameleon.util.Util;

public class MemberVariableDeclarator extends TypeElementImpl<MemberVariableDeclarator> implements TypeElement<MemberVariableDeclarator>, VariableDeclarator<MemberVariableDeclarator,MemberVariable> {

	public MemberVariableDeclarator() {
		
	}
	
	public MemberVariableDeclarator(TypeReference tref) {
		setTypeReference(tref);
	}
	
	public List<Element> children() {
		List<Element> result = super.children();
		result.addAll(variableDeclarations());
		Util.addNonNull(typeReference(),result);
		return result;
	}
	
	public List<VariableDeclaration<MemberVariable>> variableDeclarations() {
		return _declarations.getOtherEnds();
	}
	
	public void add(VariableDeclaration<MemberVariable> declaration) {
		if(declaration != null) {
			_declarations.add(declaration.parentLink());
		}
	}
	
	public void remove(VariableDeclaration declaration) {
		if(declaration != null) {
			_declarations.remove(declaration.parentLink());
		}
	}
	
	private OrderedMultiAssociation<MemberVariableDeclarator, VariableDeclaration<MemberVariable>> _declarations = new OrderedMultiAssociation<MemberVariableDeclarator, VariableDeclaration<MemberVariable>>(this);

	public MemberVariable createVariable(SimpleNameSignature signature, Expression expression) {
		MemberVariable result = new RegularMemberVariable(signature, typeReference().clone(),expression);
		for(Modifier mod: modifiers()) {
			result.addModifier(mod.clone());
		}
		return result;
	}

	@Override
	public MemberVariableDeclarator clone() {
		MemberVariableDeclarator result = new MemberVariableDeclarator(typeReference().clone());
		for(VariableDeclaration<MemberVariable> declaration: variableDeclarations()) {
			result.add(declaration.clone());
		}
		for(Modifier mod: modifiers()) {
			result.addModifier(mod.clone());
		}
		return result;
	}

	public List<MemberVariable> getIntroducedMembers() {
		List<MemberVariable> result = new ArrayList<MemberVariable>();
		for(VariableDeclaration<MemberVariable> declaration: variableDeclarations()) {
			result.add(declaration.variable());
		}
		return result;
	}
	
	/**
	 * TYPE
	 */
	private SingleAssociation<MemberVariableDeclarator,TypeReference> _typeReference = new SingleAssociation<MemberVariableDeclarator,TypeReference>(this);

  public Type type() throws LookupException {
  	return typeReference().getType();
  }
	
  public TypeReference typeReference() {
    return _typeReference.getOtherEnd();
  }

  public void setTypeReference(TypeReference type) {
    _typeReference.connectTo(type.parentLink());
  }

	public List<? extends Declaration> declarations() throws LookupException {
		return getIntroducedMembers();
	}

	public List<? extends Declaration> locallyDeclaredDeclarations() throws LookupException {
		return declarations();
	}

	public <D extends Declaration> List<D> declarations(DeclarationSelector<D> selector) throws LookupException {
		return selector.selection(declarations());
	}

	@Override
	public VerificationResult verifySelf() {
		VerificationResult result = Valid.create();
		if(typeReference() == null) {
			result = result.and(new BasicProblem(this, "The variable declaration has no type"));
		}
		return result;
	}

}
