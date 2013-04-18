package be.kuleuven.cs.distrinet.chameleon.support.variable;

import java.util.ArrayList;
import java.util.List;

import be.kuleuven.cs.distrinet.chameleon.core.declaration.Declaration;
import be.kuleuven.cs.distrinet.chameleon.core.declaration.SimpleNameSignature;
import be.kuleuven.cs.distrinet.chameleon.core.element.Element;
import be.kuleuven.cs.distrinet.chameleon.core.lookup.DeclarationSelector;
import be.kuleuven.cs.distrinet.chameleon.core.lookup.LookupContext;
import be.kuleuven.cs.distrinet.chameleon.core.lookup.LookupException;
import be.kuleuven.cs.distrinet.chameleon.core.modifier.Modifier;
import be.kuleuven.cs.distrinet.chameleon.core.validation.Valid;
import be.kuleuven.cs.distrinet.chameleon.core.validation.VerificationResult;
import be.kuleuven.cs.distrinet.chameleon.exception.ChameleonProgrammerException;
import be.kuleuven.cs.distrinet.chameleon.oo.expression.Expression;
import be.kuleuven.cs.distrinet.chameleon.oo.statement.StatementImpl;
import be.kuleuven.cs.distrinet.chameleon.oo.type.Type;
import be.kuleuven.cs.distrinet.chameleon.oo.type.TypeReference;
import be.kuleuven.cs.distrinet.chameleon.oo.variable.Variable;
import be.kuleuven.cs.distrinet.chameleon.oo.variable.VariableDeclaration;
import be.kuleuven.cs.distrinet.chameleon.oo.variable.VariableDeclarator;
import be.kuleuven.cs.distrinet.chameleon.support.statement.ForInit;
import be.kuleuven.cs.distrinet.chameleon.util.association.Multi;
import be.kuleuven.cs.distrinet.chameleon.util.association.Single;

public class LocalVariableDeclarator extends StatementImpl implements VariableDeclarator, ForInit {

	
	public LocalVariableDeclarator() {
		
	}
	
	public List<LocalVariable> variables() {
		List<LocalVariable> result = new ArrayList<LocalVariable>();
		for(VariableDeclaration declaration: variableDeclarations()) {
			result.add((LocalVariable) declaration.variable());
		}
		return result;
	}
	
	public LocalVariableDeclarator(TypeReference tref) {
		setTypeReference(tref);
	}

	@Override
	public LocalVariableDeclarator clone() {
		LocalVariableDeclarator result = new LocalVariableDeclarator(typeReference().clone());
		for(VariableDeclaration declaration: variableDeclarations()) {
			result.add(declaration.clone());
		}
		for(Modifier mod: modifiers()) {
			result.addModifier(mod.clone());
		}
		return result;
	}

	public LocalVariable createVariable(SimpleNameSignature signature, Expression expression) {
		LocalVariable result = new LocalVariable(signature, typeReference().clone(),expression);
		for(Modifier mod: modifiers()) {
			result.addModifier(mod.clone());
		}
    return result;
	}

	/**
	 * TYPE
	 */
	private Single<TypeReference> _typeReference = new Single<TypeReference>(this);

  public Type type() throws LookupException {
  	return typeReference().getType();
  }
	
  public TypeReference typeReference() {
    return _typeReference.getOtherEnd();
  }

  public void setTypeReference(TypeReference type) {
    set(_typeReference,type);
  }

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

	
	
	// COPIED FROM TypeElementImpl
	
	
  /*************
   * MODIFIERS *
   *************/
  private Multi<Modifier> _modifiers = new Multi<Modifier>(this);


  /**
   * Return the list of modifiers of this member.
   */
 /*@
   @ behavior
   @
   @ post \result != null;
   @*/
  public List<Modifier> modifiers() {
    return _modifiers.getOtherEnds();
  }

  public void addModifier(Modifier modifier) {
    add(_modifiers,modifier);	
  }
  
  public void addModifiers(List<Modifier> modifiers) {
  	if(modifiers == null) {
  		throw new ChameleonProgrammerException("List passed to addModifiers is null");
  	} else {
  		for(Modifier modifier: modifiers) {
  			addModifier(modifier);
  		}
  	}
  }

  public void removeModifier(Modifier modifier) {
  	remove(_modifiers,modifier);
  }

  public boolean hasModifier(Modifier modifier) {
    return _modifiers.getOtherEnds().contains(modifier);
  }

  /**
   * The declarations of a local variable declaration are the declared variables.
   */
 /*@
   @ public behavior
   @ 
   @ post \result == variables();
   @*/
	public List<? extends Variable> declarations() throws LookupException {
		return variables();
	}
	
	public List<? extends Declaration> locallyDeclaredDeclarations() throws LookupException {
		return declarations();
	}

	public <D extends Declaration> List<D> declarations(DeclarationSelector<D> selector) throws LookupException {
		return selector.selection(declarations());
	}

	/**
	 * The context of a local variable declarator takes the order of the declarations
	 * into account. For example in 'int a=..., b=..., c=...', the initialization of each variable can reference the 
	 * variables to its left.Thus, 'int a=3,b=a,c=b' is valid, while in 'int a=b, b=c,c=3' the intializations of 
	 * both a and b are invalid.
	 * 
	 * @throws LookupException 
	 */
 /*@
   @ public behavior
   @
   @ post declarations().indexOf(element) == 0) ==> \result == parent().lexicalContext(this);
   @ post declarations().indexOf(Element) > 0) ==> 
   @      \result == declarations().elementAt(declarations().indexOf(element) - 1).lexicalContext();
   @*/
	public LookupContext lookupContext(Element element) throws LookupException {
		List<VariableDeclaration> declarations = variableDeclarations();
		int index = declarations.indexOf(element);
		if(index <= 0) {
			return parent().lookupContext(this);
		} else {
			return declarations.get(index-1).linearContext();
		}
	}
	
	public int numberOfVariableDeclarations() {
		return _declarations.size();
	}
	public LookupContext linearLookupStrategy() throws LookupException {
		return variableDeclarations().get(numberOfVariableDeclarations()-1).linearContext();
	}

	@Override
	public VerificationResult verifySelf() {
		VerificationResult result = Valid.create();
		return result;
	}

	@Override
	public LookupContext localContext() throws LookupException {
		throw new ChameleonProgrammerException();
	}

}
