package org.aikodi.chameleon.support.variable;

import org.aikodi.chameleon.core.declaration.Declaration;
import org.aikodi.chameleon.core.element.Element;
import org.aikodi.chameleon.core.lookup.DeclarationSelector;
import org.aikodi.chameleon.core.lookup.LookupContext;
import org.aikodi.chameleon.core.lookup.LookupException;
import org.aikodi.chameleon.core.lookup.SelectionResult;
import org.aikodi.chameleon.core.modifier.Modifier;
import org.aikodi.chameleon.core.validation.Valid;
import org.aikodi.chameleon.core.validation.Verification;
import org.aikodi.chameleon.core.variable.Variable;
import org.aikodi.chameleon.exception.ChameleonProgrammerException;
import org.aikodi.chameleon.oo.expression.Expression;
import org.aikodi.chameleon.oo.statement.StatementImpl;
import org.aikodi.chameleon.oo.type.Type;
import org.aikodi.chameleon.oo.type.TypeReference;
import org.aikodi.chameleon.oo.variable.VariableDeclaration;
import org.aikodi.chameleon.oo.variable.VariableDeclarator;
import org.aikodi.chameleon.support.statement.ForInit;
import org.aikodi.chameleon.util.association.Multi;
import org.aikodi.chameleon.util.association.Single;

import java.util.ArrayList;
import java.util.List;

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
	protected LocalVariableDeclarator cloneSelf() {
		return new LocalVariableDeclarator(null);
	}

	@Override
   public LocalVariable createVariable(String name, Expression expression) {
		LocalVariable result = new LocalVariable(name, clone(typeReference()),expression);
		for(Modifier mod: modifiers()) {
			result.addModifier(clone(mod));
		}
    return result;
	}

	/**
	 * TYPE
	 */
	private Single<TypeReference> _typeReference = new Single<TypeReference>(this, "type");

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
   public List<VariableDeclaration> variableDeclarations() {
		return _declarations.getOtherEnds();
	}
	
	public void add(VariableDeclaration declaration) {
		add(_declarations,declaration);
	}
	
	public void remove(VariableDeclaration declaration) {
		remove(_declarations,declaration);
	}
	
	private Multi<VariableDeclaration> _declarations = new Multi<VariableDeclaration>(this, "variable declarations");
	{
		_declarations.enableCache();
	}

	
	
	// COPIED FROM TypeElementImpl
	
	
  /*************
   * MODIFIERS *
   *************/
  private Multi<Modifier> _modifiers = new Multi<Modifier>(this, "modifiers");


  /**
   * Return the list of modifiers of this member.
   */
 /*@
   @ behavior
   @
   @ post \result != null;
   @*/
  @Override
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
	@Override
   public List<? extends Variable> declarations() throws LookupException {
		return variables();
	}
	
	@Override
   public List<? extends Declaration> locallyDeclaredDeclarations() throws LookupException {
		return declarations();
	}

	@Override
   public <D extends Declaration> List<? extends SelectionResult<D>> declarations(DeclarationSelector<D> selector) throws LookupException {
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
	@Override
   public LookupContext lookupContext(Element element) throws LookupException {
//		LINEAR.start();
		List<VariableDeclaration> declarations = variableDeclarations();
		int index = declarations.indexOf(element);
		LookupContext result;
		if(index <= 0) {
			result = parent().lookupContext(this);
		} else {
			result = declarations.get(index-1).linearContext();
		}
//		LINEAR.stop();
		return result;
	}
//	public final static Timer LINEAR = new Timer();

	public int numberOfVariableDeclarations() {
		return _declarations.size();
	}
	@Override
   public LookupContext linearLookupStrategy() throws LookupException {
		return variableDeclarations().get(numberOfVariableDeclarations()-1).linearContext();
	}

	@Override
	public Verification verifySelf() {
		Verification result = Valid.create();
		return result;
	}

	@Override
	public LookupContext localContext() throws LookupException {
		throw new ChameleonProgrammerException();
	}

}
