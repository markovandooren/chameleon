package chameleon.core.type;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.rejuse.java.collections.TypeFilter;
import org.rejuse.logic.ternary.Ternary;
import org.rejuse.predicate.TypePredicate;

import chameleon.core.Config;
import chameleon.core.declaration.Declaration;
import chameleon.core.declaration.DeclarationContainer;
import chameleon.core.declaration.Definition;
import chameleon.core.declaration.MissingSignature;
import chameleon.core.declaration.SimpleNameSignature;
import chameleon.core.declaration.TargetDeclaration;
import chameleon.core.element.Element;
import chameleon.core.language.Language;
import chameleon.core.lookup.DeclarationSelector;
import chameleon.core.lookup.LocalLookupStrategy;
import chameleon.core.lookup.LookupException;
import chameleon.core.lookup.LookupStrategy;
import chameleon.core.lookup.LookupStrategySelector;
import chameleon.core.member.FixedSignatureMember;
import chameleon.core.member.Member;
import chameleon.core.modifier.Modifier;
import chameleon.core.namespace.NamespaceOrType;
import chameleon.core.statement.CheckedExceptionList;
import chameleon.core.statement.ExceptionSource;
import chameleon.core.type.generics.TypeParameter;
import chameleon.core.type.inheritance.InheritanceRelation;
import chameleon.core.validation.Valid;
import chameleon.core.validation.VerificationResult;
import chameleon.exception.ChameleonProgrammerException;
import chameleon.oo.language.ObjectOrientedLanguage;
import chameleon.util.Util;

/**
 * <p>A class representing types in object-oriented programs.</p>
 *
 * <p>A class contains <a href="Member.html">members</a> as its content.</p>
 *
 * @author Marko van Dooren
 */
public abstract class Type extends FixedSignatureMember<Type,Element,SimpleNameSignature,Type> 
                implements NamespaceOrType<Type,Element,SimpleNameSignature,Type>, 
                           VariableOrType<Type,Element,SimpleNameSignature,Type>, 
                           Definition<Type,Element,SimpleNameSignature,Type>,
                           Cloneable, 
                           ExceptionSource<Type,Element>, 
                           DeclarationContainer<Type,Element>,
                           DeclarationWithType<Type,Element,SimpleNameSignature,Type>{
 
	
	public Class<SimpleNameSignature> signatureType() {
		return SimpleNameSignature.class;
	}
	
	public Type declarationType() {
		return this;
	}
	
	private List<? extends Declaration> _declarationCache = null;
	
	private List<? extends Declaration> declarationCache() {
		if(_declarationCache != null && Config.cacheDeclarations()) {
		  return new ArrayList<Declaration>(_declarationCache);
		} else {
			return null;
		}
	}
	
  @Override
  public void flushLocalCache() {
  	super.flushLocalCache();
  	_declarationCache = null;
  }

	private void setDeclarationCache(List<? extends Declaration> cache) {
		if(Config.cacheDeclarations()) {
		  _declarationCache = new ArrayList<Declaration>(cache);
		}
	}
	
	
    /**
     * Initialize a new Type.
     */
   /*@
     @ public behavior
     @
     @ pre sig != null;
 	   @
     @ post signature() == sig;
     @ post parent() == null;
     @*/
    public Type(SimpleNameSignature sig) {
        setSignature(sig);
    }
    

  	/********
  	 * NAME *
  	 ********/

  	/*@
  	 @ public behavior
  	 @
  	 @ post \result != null;
  	 @*/
  	public String getName() {
  		SimpleNameSignature signature = signature();
			return (signature != null ? signature.name() : null);
  	}

    /**
     * Return the fully qualified name.
     */
   /*@
     @ public behavior
     @
     @ getPackage().getFullyQualifiedName().equals("") ==> \result == getName();
     @ ! getPackage().getFullyQualifiedName().equals("") == > \result.equals(getPackage().getFullyQualifiedName() + getName());
     @*/
    public String getFullyQualifiedName() {
        String prefix;
        Type nearest = nearestAncestor(Type.class);
        if(nearest != null) {
        	prefix = nearest.getFullyQualifiedName();
        } else {
          prefix = getNamespace().getFullyQualifiedName();
        }
        return (prefix.equals("") ? "" : prefix+".")+getName();
    }

    /*******************
     * LEXICAL CONTEXT 
     *******************/
    
    
    public LookupStrategy targetContext() throws LookupException {
    	Language language = language();
    	if(language != null) {
			  return language.lookupFactory().createTargetLookupStrategy(this);
    	} else {
    		throw new LookupException("Element of type "+getClass().getName()+" is not connected to a language. Cannot retrieve target context.");
    	}
    }
    
    public LookupStrategy localStrategy() throws LookupException {
    	return targetContext();
    }
    
    /**
     * If the given element is an inheritance relation, the lookup must proceed to the parent. For other elements,
     * the context is a lexical context connected to the target context to perform a local search.
     * @throws LookupException 
     */
    public LookupStrategy lexicalLookupStrategy(Element element) throws LookupException {
    	if(inheritanceRelations().contains(element)) {
    		Element parent = parent();
    		if(parent != null) {
    			return lexicalParametersLookupStrategy();
//    		  return parent().lexicalContext(this);
    		} else {
    			throw new LookupException("Parent of type is null when looking for the parent context of a type.");
    		}
    	} else {
    	  return lexicalMembersLookupStrategy();
    	  
    	  //language().lookupFactory().createLexicalContext(this,targetContext());
    	}
    }
    
    protected LookupStrategy lexicalMembersLookupStrategy() throws LookupException {
    	LookupStrategy result = _lexicalMembersLookupStrategy;
    	// Lazy initialization
    	if(result == null) {
    		Language language = language();
    		if(language == null) {
    			throw new LookupException("Parent of type "+signature().name()+" is null.");
    		}
				_lexicalMembersLookupStrategy = language.lookupFactory().createLexicalLookupStrategy(targetContext(), this, 
    			new LookupStrategySelector(){
					
						public LookupStrategy strategy() throws LookupException {
	    	  		return lexicalParametersLookupStrategy();
						}
					}); 
    		result = _lexicalMembersLookupStrategy;
    	}
    	return result;
    }
    
    protected LookupStrategy _lexicalMembersLookupStrategy;
    
    protected LookupStrategy lexicalParametersLookupStrategy() {
    	LookupStrategy result = _lexicalParametersLookupStrategy;
    	// lazy initialization
    	if(result == null) {
    		_lexicalParametersLookupStrategy = language().lookupFactory().createLexicalLookupStrategy(_localInheritanceLookupStrategy, this);
    		result = _lexicalParametersLookupStrategy;
    	}
    	return result;
    }
    
    protected LookupStrategy _lexicalParametersLookupStrategy;
    
    protected LocalInheritanceLookupStrategy _localInheritanceLookupStrategy = new LocalInheritanceLookupStrategy(this);
    
  	protected class LocalInheritanceLookupStrategy extends LocalLookupStrategy<Type> {
  	  public LocalInheritanceLookupStrategy(Type element) {
  			super(element);
  		}

  	  @Override
  	  @SuppressWarnings("unchecked")
  	  public <D extends Declaration> List<D> declarations(DeclarationSelector<D> selector) throws LookupException {
  	    return selector.selection(parameters());
  	  }
  	}

  	public abstract List<TypeParameter> parameters();
  	
  	public abstract void addParameter(TypeParameter parameter);
  	
  	public abstract void replaceParameter(TypeParameter oldParameter, TypeParameter newParameter);

  	public abstract void replaceAllParameter(List<TypeParameter> newParameters);
  	
    /************************
     * BEING A TYPE ELEMENT *
     ************************/
    
    public List<Member> getIntroducedMembers() {
      List<Member> result = new ArrayList<Member>();
      result.add(this);
      return result;
    }
    
    public Ternary complete() {
			try {
	    	List<Member> members = localMembers(Member.class);
	    	// Only check for actual definitions
	    	new TypePredicate<Element,Definition>(Definition.class).filter(members);
	    	Iterator<Member> iter = members.iterator();
	    	Ternary result = Ternary.TRUE;
	    	while(iter.hasNext()) {
	    		result = result.and(iter.next().is(language(ObjectOrientedLanguage.class).DEFINED));
	    	}
	      return result;
			} catch (LookupException e) {
				return Ternary.UNKNOWN;
			}
    }
    
    /**********
     * ACCESS *
     **********/

    public Type getTopLevelType() {
        // FIXME: BAD design !!!
        if (parent() instanceof Type) {
            return ((Type)parent()).getTopLevelType();
        } else {
            return this;
        }
    }


    public Type getType() {
        return this;
    }

  	/***********
  	 * MEMBERS *
  	 ***********/

    /**
     * Add the given element to this type.
     * 
     * @throws ChameleonProgrammerException
     *         The given element could not be added. E.g when you try to add
     *         an element to a computed type.
     */
   /*@
     @ public behavior
     @
     @ pre element != null;
     @
     @ post directlyDeclaredElements().contains(element);
     @*/
  	public abstract void add(TypeElement element) throws ChameleonProgrammerException;
  	
    /**
     * Remove the given element to this type.
     * 
     * @throws ChameleonProgrammerException
     *         The given element could not be added. E.g when you try to add
     *         an element to a computed type.
     */
   /*@
     @ public behavior
     @
     @ pre element != null;
     @
     @ post ! directlyDeclaredElements().contains(element);
     @*/
  	public abstract void remove(TypeElement element) throws ChameleonProgrammerException;
  	
    /**
     * Add all type elements in the given collection to this type.
     * @param elements
     * @throws ChameleonProgrammerException
     */
   /*@
     @ public behavior
     @
     @ pre elements != null;
     @ pre !elements.contains(null);
     @
     @ post directlyDeclaredElements().containsAll(elements);
     @*/
  	public void addAll(Collection<? extends TypeElement> elements) throws ChameleonProgrammerException {
  		for(TypeElement element: elements) {
  			add(element);
  		}
  	}

    /**************
     * SUPERTYPES *
     **************/

    public List<Type> getDirectSuperTypes() throws LookupException {
            final ArrayList<Type> result = new ArrayList<Type>();
            for(InheritanceRelation element:inheritanceRelations()) {
              Type type = element.superType();
              if (type!=null) {
                result.add(type);
              }
            }
            return result;
    }

    public List<Type> getDirectSuperClasses() throws LookupException {
      final ArrayList<Type> result = new ArrayList<Type>();
      for(InheritanceRelation element:inheritanceRelations()) {
        result.add(element.superClass());
      }
      return result;
}

    protected void accumulateAllSuperTypes(Set<Type> acc) throws LookupException {
    	List<Type> temp =getDirectSuperTypes();
    	acc.addAll(temp);
    	for(Type type:temp) {
    		type.accumulateAllSuperTypes(acc);
    	}
    }
    
    public Set<Type> getAllSuperTypes() throws LookupException {
    	Set<Type> result = new HashSet<Type>();
    	accumulateAllSuperTypes(result);
    	return result;
    }

    
    //TODO: rename to properSubTypeOf
    
    public boolean subTypeOf(Type other) throws LookupException {
    	return language(ObjectOrientedLanguage.class).subtypeRelation().contains(this, other);
//    	  Collection superTypes = getAllSuperTypes(); 
//        return superTypes.contains(other);
    }
    
    /**
     * Check if this type equals the given other type. This is
     * a unidirectional check to keep things extensible. It is fine
     * if equals(other) is false, but other.equals(this) is true.
     *  
     * @param other
     * @return
     */
    @Override
    public boolean uniSameAs(Element other) throws LookupException {
    	return other == this;
    }
    
//   /*@
//     @ public behavior
//     @
//     @ post ! other instanceof Type ==> \result == false
//     @ post other instanceof Type ==> \result == equalTo(other) || other.equalTo(this); 
//     @*/
//    public boolean equals(Object other) {
//    	boolean result = false;
//    	if(other instanceof Type) {
//    		result = uniEqualTo((Type)other) || ((Type)other).uniEqualTo(this);
//    	}
//    	return result;
//    }

    /**
     * Check if this type is assignable to another type.
     * 
     * @param other
     * @return
     * @throws LookupException
     */
   /*@
     @ public behavior
     @
     @ post \result == equals(other) || subTypeOf(other);
     @*/
    public boolean assignableTo(Type other) throws LookupException {
    	boolean equal = equals(other);
    	boolean subtype = subTypeOf(other);
    	return (equal || subtype);
    }

    
  	/**
  	 * Return the inheritance relations of this type.
  	 */
   /*@
     @ public behavior
     @
     @ post \result != null;
     @*/
  	public abstract List<InheritanceRelation> inheritanceRelations();
  	
  	public List<Type> directSuperTypes() throws LookupException {
  	  List<Type> result = new ArrayList<Type>();
  		for(InheritanceRelation relation: inheritanceRelations()) {
  			Type superType = relation.superType();
  			if(superType != null) {
  				result.add(superType);
  			}
  		}
  		return result;
  	}

  	/**
  	 * Add the give given inheritance relation to this type.
  	 * @param type
  	 * @throws ChameleonProgrammerException
  	 *         It is not possible to add the given type. E.g. you cannot
  	 *         add an inheritance relation to a computed type.
  	 */
   /*@
     @ public behavior
     @
     @ pre relation != null;
     @ post inheritanceRelations().contains(relation);
     @*/
  	public abstract void addInheritanceRelation(InheritanceRelation relation) throws ChameleonProgrammerException;
    
  	/**
  	 * Remove the give given inheritance relation from this type.
  	 * @param type
  	 * @throws ChameleonProgrammerException
  	 *         It is not possible to remove the given type. E.g. you cannot
  	 *         remove an inheritance relation to a computed type.
  	 */
   /*@
     @ public behavior
     @
     @ pre relation != null;
     @ post ! inheritanceRelations().contains(relation);
     @*/
  	public abstract void removeInheritanceRelation(InheritanceRelation relation) throws ChameleonProgrammerException;
  	
  	public void removeAllInheritanceRelations() {
  		for(InheritanceRelation relation: inheritanceRelations()) {
  			removeInheritanceRelation(relation);
  		}
  	}
  	
    /**
     * Return the members of the given kind directly declared by this type.
     * @return
     * @throws LookupException 
     * @throws  
     */
    public <T extends Member> List<T> localMembers(final Class<T> kind) throws LookupException {
      return (List<T>) new TypeFilter(kind).retain(localMembers());
    }
    
    /**
     * Return the members directly declared by this type. The order of the elements in the list is the order in which they
     * are written in the type.
     * @return
     * @throws LookupException 
     */
    public abstract List<Member> localMembers() throws LookupException;
    
    public <T extends Member> List<T> directlyDeclaredMembers(Class<T> kind) {
      return (List<T>) new TypeFilter(kind).retain(directlyDeclaredMembers());
    }
    
    public  List<Member> directlyDeclaredMembers() {
  		List<Member> result = new ArrayList<Member>();
      for(TypeElement m: directlyDeclaredElements()) {
        result.addAll(m.declaredMembers());
      }
      return result;
    }
    
    public <D extends Member> List<D> members(DeclarationSelector<D> selector) throws LookupException {

  		// 1) All defined members of the requested kind are added.
  		List<D> result = localMembers(selector);

  		// 2) Fetch all potentially inherited members from all inheritance relations
  		for (InheritanceRelation rel : inheritanceRelations()) {
  				rel.accumulateInheritedMembers(selector, result);
  		}
  		// The selector must still apply its order to the candidates.
  		return selector.selection(result);
    }
    
/*    public <D extends Member> List<D> members(DeclarationSelector<D> selector) throws LookupException {
    	System.out.println("MEMBERS of: "+getFullyQualifiedName());
  		DeclarationContainerAlias alias = InheritanceRelation.membersInContext(this);
  		List<Declaration> declarations = alias.allDeclarations();
  		List<Member> result = new ArrayList<Member>();
  		for(Declaration declaration:declarations) {
  			if(
  					 (declaration.is(language(ObjectOrientedLanguage.class).INHERITABLE) == Ternary.TRUE || 
  							 declaration.nearestAncestor(Type.class) == this
  					 )  && 
  				(! (declaration instanceof DeclarationAlias))) {
  				result.add((Member) declaration);
  			}
  		}
  		return selector.selection(result);
    }*/

    
    @SuppressWarnings("unchecked")
    public abstract <D extends Member> List<D> localMembers(DeclarationSelector<D> selector) throws LookupException;

    public List<Member> members() throws LookupException {
      return members(Member.class);
    }
    
//    public <M extends Member> Set<M> potentiallyInheritedMembers(final Class<M> kind) throws MetamodelException {
//  		final Set<M> result = new HashSet<M>();
//			for (InheritanceRelation rel : inheritanceRelations()) {
//				result.addAll(rel.potentiallyInheritedMembers(kind));
//			}
//  		return result;
//    }
//
    /**
     * Return the members of this class.
     * @param <M>
     * @param kind
     * @return
     * @throws LookupException
     */
    public <M extends Member> List<M> members(final Class<M> kind) throws LookupException {

		// 1) All defined members of the requested kind are added.
		final List<M> result = new ArrayList(localMembers(kind));

		// 2) Fetch all potentially inherited members from all inheritance relations
		for (InheritanceRelation rel : inheritanceRelations()) {
				rel.accumulateInheritedMembers(kind, result);
		}
		return result;
	}

    /****************
     * CONSTRUCTORS *
     ****************/



    public abstract Type clone();

   /*@
     @ also public behavior
     @
     @ post \result.containsAll(getSuperTypeReferences());
     @ post \result.containsAll(getMembers());
     @ post \result.containsAll(getModifiers());
     @*/
    public List<Element> children() {
        List<Element> result = super.children();
        Util.addNonNull(signature(), result);
        result.addAll(inheritanceRelations());
//        result.addAll(directlyDeclaredElements());
        return result;
    }

    /**
     * DO NOT CONFUSE THIS METHOD WITH localMembers. This method does not
     * transform type elements into members.
     * 
     * @return
     */
    public abstract List<? extends TypeElement> directlyDeclaredElements();

    /********************
     * EXCEPTION SOURCE *
     ********************/

    public CheckedExceptionList getCEL() throws LookupException {
        CheckedExceptionList cel = new CheckedExceptionList();
        for(TypeElement el : localMembers()) {
        	cel.absorb(el.getCEL());
        }
        return cel;
    }

    public CheckedExceptionList getAbsCEL() throws LookupException {
      CheckedExceptionList cel = new CheckedExceptionList();
      for(TypeElement el : localMembers()) {
      	cel.absorb(el.getAbsCEL());
      }
      return cel;
    }

    public List<? extends Declaration> declarations() throws LookupException {
    	List<? extends Declaration> result = declarationCache();
    	if(result == null) {
    		result = members();
    		setDeclarationCache(result);
    	}
    	return result;
    }
    public <D extends Declaration> List<D> declarations(DeclarationSelector<D> selector) throws LookupException {
    	return (List<D>) members((DeclarationSelector<? extends Member>)selector);
    }

  	protected void copyContents(Type from) {
  		copyContents(from, false);
  	}

  	protected void copyContents(Type from, boolean link) {
  		for(InheritanceRelation relation : from.inheritanceRelations()) {
        InheritanceRelation clone = relation.clone();
        if(link) {
        	clone.setOrigin(relation);
        }
				addInheritanceRelation(clone);
  		}
      for(Modifier mod : from.modifiers()) {
      	Modifier clone = mod.clone();
        if(link) {
        	clone.setOrigin(mod);
        }
				addModifier(clone);
      }
      for(TypeElement el : from.directlyDeclaredElements()) {
        TypeElement clone = el.clone();
        if(link) {
        	clone.setOrigin(el);
        }
				add(clone);
      }
      for(TypeParameter par : from.parameters()) {
      	TypeParameter clone = par.clone();
        if(link) {
        	clone.setOrigin(par);
        }
				addParameter(clone);
      }
  	}
  
  	public Type alias(SimpleNameSignature sig) {
      return new TypeAlias(sig,this);
  	}

  	public Type intersection(Type type) throws LookupException {
  		return type.intersectionDoubleDispatch(type);
  	}
  	
  	protected Type intersectionDoubleDispatch(Type type) throws LookupException {
  		Type result;
  		if(type.subTypeOf(this)) {
  			result = type;
  		} else if (subTypeOf(type)) {
  			result = this;
  		} else {
  		  result = new IntersectionType(this,type);
  		  result.setUniParent(parent());
  		}
  		return result;
  	}

  	protected Type intersectionDoubleDispatch(IntersectionType type) {
  		IntersectionType result = type.clone();
  		result.addType(type);
  		return result;
  	}

		public abstract void replace(TypeElement oldElement, TypeElement newElement);

		public abstract Type baseType();

		@Override
		public VerificationResult verifySelf() {
			if(signature() != null) {
			  return Valid.create();
			} else {
				return new MissingSignature(this); 
			}
		}

//		public boolean sameRangeAs(Type other) throws LookupException {
//			return other != null && (uniSameRangeAs(other)) || other.uniSameRangeAs(this);
//		}
		
//		public abstract boolean uniSameRangeAs(Type other) throws LookupException;
		
//		public boolean sameBoundsAs(Type other) throws LookupException {
//			return upperBound().sameAs(other.upperBound()) && (lowerBound().sameAs(other.lowerBound()));
//		}
		
//		public abstract Type upperBound();
//		
//		public abstract Type lowerBound();
}


