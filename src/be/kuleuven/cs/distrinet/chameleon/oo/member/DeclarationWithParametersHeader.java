package be.kuleuven.cs.distrinet.chameleon.oo.member;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import com.google.common.collect.ImmutableList;

import be.kuleuven.cs.distrinet.chameleon.core.declaration.Declaration;
import be.kuleuven.cs.distrinet.chameleon.core.declaration.Signature;
import be.kuleuven.cs.distrinet.chameleon.core.element.Element;
import be.kuleuven.cs.distrinet.chameleon.core.element.ElementImpl;
import be.kuleuven.cs.distrinet.chameleon.core.lookup.Collector;
import be.kuleuven.cs.distrinet.chameleon.core.lookup.DeclarationSelector;
import be.kuleuven.cs.distrinet.chameleon.core.lookup.LookupContext;
import be.kuleuven.cs.distrinet.chameleon.core.lookup.LookupException;
import be.kuleuven.cs.distrinet.chameleon.core.lookup.SelectionResult;
import be.kuleuven.cs.distrinet.chameleon.exception.ModelException;
import be.kuleuven.cs.distrinet.chameleon.oo.type.Type;
import be.kuleuven.cs.distrinet.chameleon.oo.type.generics.TypeParameter;
import be.kuleuven.cs.distrinet.chameleon.oo.type.generics.TypeParameterBlock;
import be.kuleuven.cs.distrinet.chameleon.oo.variable.FormalParameter;
import be.kuleuven.cs.distrinet.chameleon.oo.variable.VariableContainer;
import be.kuleuven.cs.distrinet.chameleon.util.Lists;
import be.kuleuven.cs.distrinet.chameleon.util.Util;
import be.kuleuven.cs.distrinet.chameleon.util.association.Multi;
import be.kuleuven.cs.distrinet.chameleon.util.association.Single;
/**
 * A class of objects representing method headers. A method header contains for example the name and parameters of a method.
 * 
 * @author Marko van Dooren
 *
 * @param <E>
 * @param <P>
 * @param <S>
 */
public abstract class DeclarationWithParametersHeader extends ElementImpl implements VariableContainer { //extends Signature<E, P> 
  
  /**
   * Return the signature of the method of this method header. The signature is generated based on
   * the information in the header.
   * @return
   */
  public abstract DeclarationWithParametersSignature signature();
  
  public abstract void setName(String name);
  
  public abstract DeclarationWithParametersHeader createFromSignature(Signature signature);
  
//  protected abstract DeclarationWithParametersHeader cloneThis();
  
  /**
   * The name of a method header is the name of its signature.
   */
 /*@
   @ public behavior
   @
   @ post \result == signature().name();
   @*/
  public String name() {
  	return signature().name();
  }
  
  /*********************
   * FORMAL PARAMETERS *
   *********************/

  public List<FormalParameter> formalParameters() {
    return _parameters.getOtherEnds();
  }

  public void addFormalParameter(FormalParameter arg) {
    add(_parameters,arg);
  }
  
  public void removeFormalParameter(FormalParameter arg) {
    remove(_parameters,arg);
  }
  
  public void addFormalParameters(List<FormalParameter> parameters) {
	if (parameters == null)
		return;
	
	for (FormalParameter f : parameters)
		addFormalParameter(f);
		
  }

  public int nbFormalParameters() {
    return _parameters.size();
  }
  
  /**
   * The index starts from 1.
   * @param baseOneIndex
   * @return
   */
  public FormalParameter formalParameter(int baseOneIndex) {
  	return _parameters.elementAt(baseOneIndex);
  }

  private Multi<FormalParameter> _parameters = new Multi<FormalParameter>(this, "formal parameters");
  {
  	_parameters.enableCache();
  }
  
  /**
   * Return the type of the formal parameters of this signature.
   * 
   * @return
   * @throws ModelException
   */
  public List<Type> formalParameterTypes() throws LookupException {
  	List<Type> result = Lists.create();
  	for (FormalParameter param : formalParameters()) {
  		result.add(param.getType());
  	}
  	return result;
  }

//  /**
//   * Check whether or not this method contains a formal parameter with the given name.
//   *
//   * @param name
//   *        The name that has to be checked.
//   */
//  /*@
//   @ public behavior
//   @
//   @ post \result == (\exists FormalParameter fp; getFormalParameters.contains(fp);
//   @                   fp.getName().equals(name);
//   @*/
//  public boolean containsParameterWithName(final String name) {
//    return new SafePredicate() {
//      public boolean eval(Object o) {
//        return ((FormalParameter)o).getName().equals(name);
//      }
//    }.exists(getParameters());
//  }

//  public Type getNearestType() {
//  	return parent().getNearestType();
//  }
  
	public List<? extends Declaration> locallyDeclaredDeclarations() throws LookupException {
		return declarations();
	}

  
  /**
   * The declarations of a method header are its formal parameters and its type parameters.
   */
 /*@
   @ public behavior
   @
   @ post \result != null;
   @ post \result.containsAll(formalParameters());
   @ post \result.containsAll(typeParameters());
   @*/
  public List<Declaration> declarations() {
    List<Declaration>  result = Lists.create();
    result.addAll(formalParameters());
    result.addAll(typeParameters());
    return result;
  }
  
	public <D extends Declaration> List<? extends SelectionResult> declarations(DeclarationSelector<D> selector) throws LookupException {
		return selector.selection(declarations());
	}

  @Override
  public LookupContext lookupContext(Element element) throws LookupException {
  	if(typeParameters().contains(element)) {
  		return parent().lookupContext(this);
  	}
  	else {
  		return lexicalStrategy();
  	}
  }

	protected LookupContext lexicalStrategy() {
		if(_lexical == null) {
			_lexical = language().lookupFactory().createLexicalLookupStrategy(localLookupStrategy(), this);
		}
		return _lexical;
	}
  
  public LookupContext localLookupStrategy() {
  	if(_local == null) {
  		_local = language().lookupFactory().createTargetLookupStrategy(this);
  	}
  	return _local;
  }

  // The old method should suffice. The target of a type reference in Java should not be a SpecificReference<TargetDeclaration> because
  // that accepts a variable as well. If the variable does not match, we avoid the loop and still allow type anchors
  //  because for a type anchor, you know that the path can start with a parameter, so there  SpecificReference<TargetDeclaration> will do.
  
//	public LookupContext localLookupStrategy() {
//	  if(parameterBlock() != null) {
//	  	return parameterBlock().localContext();
//	  } else {
//	  	return new LookupContext(){
//				@Override
//				public <D extends Declaration> void lookUp(Collector<D> selector) throws LookupException {
//				}
//			};
//	  }
//	}
  
	@Override
	public LookupContext localContext() throws LookupException {
		return localLookupStrategy();
	}


  
  private LookupContext _local;
  
  private LookupContext _lexical;

	public boolean sameParameterTypesAs(DeclarationWithParametersHeader other) throws ModelException {
  	boolean result = false;
  	if (other != null) {
			List<FormalParameter> mine = formalParameters();
			List<FormalParameter> others = other.formalParameters();
			result = mine.size() == others.size();
			Iterator<FormalParameter> iter1 = mine.iterator();
			Iterator<FormalParameter> iter2 = others.iterator();
			while (result && iter1.hasNext()) {
        result = result && iter1.next().getType().equals(iter2.next().getType());
			}
		}
  	return result;
  }

  public Element variableScopeElement() {
  	return nearestAncestor(Element.class);
  }
  
//  public LookupStrategy lookupContext(Element element) {
//  	return language().lookupFactory().createLexicalLookupStrategy(language().lookupFactory().createLocalLookupStrategy(this),this);
//  }
  
	private Single<TypeParameterBlock> _typeParameters = new Single<TypeParameterBlock>(this,"type parameters");
	
	public TypeParameterBlock parameterBlock() {
		return _typeParameters.getOtherEnd();
	}
	
	/**
	 * Return the type parameters of this method header. If 
	 * @return
	 */
	public List<TypeParameter> typeParameters() {
		TypeParameterBlock parameterBlock = parameterBlock();
		return (parameterBlock == null ? ImmutableList.<TypeParameter>of() :parameterBlock.parameters());
	}
	
	public int nbTypeParameters() {
		TypeParameterBlock parameterBlock = parameterBlock();
		return (parameterBlock == null ? 0 : parameterBlock.nbTypeParameters());
	}
	
	/**
	 * Return the index-th type parameter. Indices start at 1.
	 */
	public TypeParameter typeParameter(int index) {
		return parameterBlock().parameter(index);
	}
	
	public void addAllTypeParameters(Collection<? extends TypeParameter> parameters) {
		for(TypeParameter param:parameters) {
			addTypeParameter(param);
		}
	}

	public void addTypeParameter(TypeParameter parameter) {
		TypeParameterBlock parameterBlock = parameterBlock();
		if(parameterBlock == null) {
			// Lazy init.
			parameterBlock = new TypeParameterBlock();
			set(_typeParameters,parameterBlock);
		}
		parameterBlock.add(parameter);
	}

	public void removeTypeParameter(TypeParameter parameter) {
		parameterBlock().add(parameter);
	}
	
	public void replaceTypeParameter(TypeParameter oldParameter, TypeParameter newParameter) {
		parameterBlock().replace(oldParameter, newParameter);
	}


}
