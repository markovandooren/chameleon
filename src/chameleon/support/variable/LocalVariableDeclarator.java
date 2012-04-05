package chameleon.support.variable;

import java.util.ArrayList;
import java.util.List;

import org.rejuse.association.OrderedMultiAssociation;
import org.rejuse.association.SingleAssociation;

import chameleon.core.declaration.Declaration;
import chameleon.core.declaration.SimpleNameSignature;
import chameleon.core.element.Element;
import chameleon.core.lookup.DeclarationSelector;
import chameleon.core.lookup.LookupException;
import chameleon.core.lookup.LookupStrategy;
import chameleon.core.modifier.Modifier;
import chameleon.core.validation.Valid;
import chameleon.core.validation.VerificationResult;
import chameleon.exception.ChameleonProgrammerException;
import chameleon.oo.expression.Expression;
import chameleon.oo.statement.StatementImpl;
import chameleon.oo.type.Type;
import chameleon.oo.type.TypeReference;
import chameleon.oo.variable.Variable;
import chameleon.oo.variable.VariableDeclaration;
import chameleon.oo.variable.VariableDeclarator;
import chameleon.support.statement.ForInit;
import chameleon.util.Util;

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
	private SingleAssociation<LocalVariableDeclarator,TypeReference> _typeReference = new SingleAssociation<LocalVariableDeclarator,TypeReference>(this);

  public Type type() throws LookupException {
  	return typeReference().getType();
  }
	
  public TypeReference typeReference() {
    return _typeReference.getOtherEnd();
  }

  public void setTypeReference(TypeReference type) {
    setAsParent(_typeReference,type);
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
	
	private OrderedMultiAssociation<LocalVariableDeclarator, VariableDeclaration> _declarations = new OrderedMultiAssociation<LocalVariableDeclarator, VariableDeclaration>(this);

	
	
	// COPIED FROM TypeElementImpl
	
	
  /*************
   * MODIFIERS *
   *************/
  private OrderedMultiAssociation<LocalVariableDeclarator, Modifier> _modifiers = new OrderedMultiAssociation<LocalVariableDeclarator, Modifier>(this);


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
	public LookupStrategy lexicalLookupStrategy(Element element) throws LookupException {
		List<VariableDeclaration> declarations = variableDeclarations();
		int index = declarations.indexOf(element);
		if(index <= 0) {
			return parent().lexicalLookupStrategy(this);
		} else {
			return declarations.get(index-1).linearContext();
		}
	}
	
	public int numberOfVariableDeclarations() {
		return _declarations.size();
	}
	public LookupStrategy linearLookupStrategy() throws LookupException {
		return variableDeclarations().get(numberOfVariableDeclarations()-1).linearContext();
	}

	@Override
	public VerificationResult verifySelf() {
		VerificationResult result = Valid.create();
		return result;
	}

	@Override
	public LookupStrategy localStrategy() throws LookupException {
		throw new ChameleonProgrammerException();
	}

}
