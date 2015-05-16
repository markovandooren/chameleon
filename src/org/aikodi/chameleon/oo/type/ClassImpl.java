package org.aikodi.chameleon.oo.type;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

import org.aikodi.chameleon.core.Config;
import org.aikodi.chameleon.core.declaration.Declaration;
import org.aikodi.chameleon.core.element.Element;
import org.aikodi.chameleon.core.language.Language;
import org.aikodi.chameleon.core.lookup.DeclarationSelector;
import org.aikodi.chameleon.core.lookup.LocalLookupContext;
import org.aikodi.chameleon.core.lookup.LookupContext;
import org.aikodi.chameleon.core.lookup.LookupContextSelector;
import org.aikodi.chameleon.core.lookup.LookupException;
import org.aikodi.chameleon.core.lookup.SelectionResult;
import org.aikodi.chameleon.core.modifier.Modifier;
import org.aikodi.chameleon.core.namespace.Namespace;
import org.aikodi.chameleon.core.property.ChameleonProperty;
import org.aikodi.chameleon.core.validation.BasicProblem;
import org.aikodi.chameleon.core.validation.Valid;
import org.aikodi.chameleon.core.validation.Verification;
import org.aikodi.chameleon.exception.ChameleonProgrammerException;
import org.aikodi.chameleon.oo.language.ObjectOrientedLanguage;
import org.aikodi.chameleon.oo.member.HidesRelation;
import org.aikodi.chameleon.oo.member.Member;
import org.aikodi.chameleon.oo.member.MemberRelationSelector;
import org.aikodi.chameleon.oo.member.SimpleNameMember;
import org.aikodi.chameleon.oo.type.generics.TypeParameter;
import org.aikodi.chameleon.oo.type.inheritance.InheritanceRelation;
import org.aikodi.chameleon.util.Lists;
import org.aikodi.chameleon.util.Pair;

import be.kuleuven.cs.distrinet.rejuse.collection.CollectionOperations;
import be.kuleuven.cs.distrinet.rejuse.java.collections.TypeFilter;
import be.kuleuven.cs.distrinet.rejuse.predicate.TypePredicate;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;

/**
 * <p>A class representing types in object-oriented programs.</p>
 *
 * <p>A class contains <a href="Member.html">members</a> as its content.</p>
 *
 * @author Marko van Dooren
 */
public abstract class ClassImpl extends SimpleNameMember implements Type {
 

  /**
   * Initialize a new class with the given name.
   */
 /*@
   @ public behavior
   @
   @ post name() == name;
   @ post parent() == null;
   @*/
  public ClassImpl(String name) {
      setName(name);
  }
  
	@Override
   public Type declarationType() {
		return this;
	}
	
	@Override
	public <P extends Parameter> void addAllParameters(Class<P> kind, Collection<P> parameters) {
		for(P p: parameters) {
			addParameter(kind, p);
		}
	}
	
//	public SimpleNameSignature signature() {
//		return (SimpleNameSignature) super.signature();
//	}
	private List<? extends Declaration> _declarationCache = null;
	
	private synchronized List<? extends Declaration> declarationCache() {
		if(_declarationCache != null && Config.cacheDeclarations()) {
		  return new ArrayList<Declaration>(_declarationCache);
		} else {
			return null;
		}
	}
	
  @Override
public List<? extends Declaration> locallyDeclaredDeclarations() throws LookupException {
    return directlyDeclaredMembers();
 }

  @Override
public String infoName() {
  	try {
  		try {
  			return getFullyQualifiedName();
  		} catch(Exception exc) {
  			return name();
  		}
  	} catch(NullPointerException exc) {
  		return "";
  	}
  }
  
  @Override
public Verification verifySubtypeOf(Type otherType, String meaningThisType, String meaningOtherType, Element cause) {
  	Verification result = Valid.create();
  	String messageOther = meaningOtherType+" ("+otherType.infoName()+").";
  	String messageThis = meaningThisType + " (" + infoName() + ")";
		try {
			boolean subtype = subTypeOf(otherType);
			if(! subtype) {
					result = result.and(new BasicProblem(cause, messageThis+" is not a subtype of " + messageOther));
			}
		} catch (Exception e) {
				result = result.and(new BasicProblem(cause, "Cannot determine if "+messageThis+" is a subtype of "+messageOther));
		}
  	return result;
  }
	
  @Override
  public synchronized void flushLocalCache() {
  	super.flushLocalCache();
		if(_lexicalMembersLookupStrategy != null) {
			_lexicalMembersLookupStrategy.flushCache();
		}
  	_declarationCache = null;
  	_membersCache = null;
  	_superTypeCache = null;
  	_judge = null;
  	_superTypeAndSelfCache = null;
  }

	private synchronized void setDeclarationCache(List<? extends Declaration> cache) {
		if(Config.cacheDeclarations()) {
		  _declarationCache = new ArrayList<Declaration>(cache);
		}
	}
	
	
	protected ClassImpl() {
		
	}
	
    /* (non-Javadoc)
		 * @see chameleon.oo.type.Tajp#getFullyQualifiedName()
		 */
   /*@
     @ public behavior
     @
     @ getPackage().getFullyQualifiedName().equals("") ==> \result == getName();
     @ ! getPackage().getFullyQualifiedName().equals("") == > \result.equals(getPackage().getFullyQualifiedName() + getName());
     @*/
    @Override
   public String getFullyQualifiedName() {
        String prefix;
        Type nearest = nearestAncestor(Type.class);
        if(nearest != null) {
        	prefix = nearest.getFullyQualifiedName();
        } else {
          Namespace namespace = namespace();
          if(namespace != null) {
					  prefix = namespace.fullyQualifiedName();
          } else {
          	prefix = null;
          }
        }
        return prefix == null ? null : (prefix.equals("") ? "" : prefix+".")+name();
    }

    @Override
   public String toString() {
			try {
				try {
					return getFullyQualifiedName();
				} catch(Exception exc) {
					return name();
				}
			}catch(NullPointerException exc) {
				return "";
			}
    }
    
    /* (non-Javadoc)
		 * @see chameleon.oo.type.Tajp#targetContext()
		 */
    
    
    @Override
   public LocalLookupContext<?> targetContext() throws LookupException {
    	return localContext();
    }
    
    protected LocalLookupContext  _target;
    
    /* (non-Javadoc)
		 * @see chameleon.oo.type.Tajp#localStrategy()
		 */
    @Override
   public LocalLookupContext localContext() throws LookupException {
    	if(_target == null) {
    		Language language = language();
    		if(language != null) {
    			_target = language.lookupFactory().createTargetLookupStrategy(this);
    		}	else {
    			throw new LookupException("Element of type "+getClass().getName()+" is not connected to a language. Cannot retrieve target context.");
    		}
    	} 
    	return _target;
    }
    
    /* (non-Javadoc)
		 * @see chameleon.oo.type.Tajp#lookupContext(chameleon.core.element.Element)
		 */
    @Override
   public LookupContext lookupContext(Element element) throws LookupException {
    	if(element instanceof InheritanceRelation && hasInheritanceRelation((InheritanceRelation) element)) {
    		Element parent = parent();
    		if(parent != null) {
    			return lexicalParametersLookupStrategy();
    		} else {
    			throw new LookupException("Parent of type is null when looking for the parent context of a type.");
    		}
    	} else {
    	  return lexicalMembersLookupStrategy();
    	  
    	  //language().lookupFactory().createLexicalContext(this,targetContext());
    	}
    }
    
    /**
     * Check whether the given element is an inheritance relation of this type.
     * The default implementation checks whether the given element is in the 
     * collection returned by inheritanceRelations().
     * 
     * This method can be overridden for example to deal with generated inheritance
     * relations, which are not lexically part of the type.
     * @throws LookupException 
     */
    public boolean hasInheritanceRelation(InheritanceRelation relation) throws LookupException {
    	return inheritanceRelations().contains(relation);
    }
    
    protected LookupContext lexicalMembersLookupStrategy() throws LookupException {
    	LookupContext result = _lexicalMembersLookupStrategy;
    	// Lazy initialization
    	if(result == null) {
    		Language language = language();
    		if(language == null) {
    			throw new LookupException("Parent of type "+name()+" is null.");
    		}
				_lexicalMembersLookupStrategy = language.lookupFactory().createLexicalLookupStrategy(targetContext(), this, 
    			new LookupContextSelector(){
					
						@Override
                  public LookupContext strategy() throws LookupException {
	    	  		return lexicalParametersLookupStrategy();
						}
					}); 
				_lexicalMembersLookupStrategy.enableCache();
    		result = _lexicalMembersLookupStrategy;
    	}
    	return result;
    }
    
    protected LookupContext _lexicalMembersLookupStrategy;
    
    protected LookupContext lexicalParametersLookupStrategy() {
    	LookupContext result = _lexicalParametersLookupStrategy;
    	// lazy initialization
    	if(result == null) {
    		_lexicalParametersLookupStrategy = language().lookupFactory().createLexicalLookupStrategy(_localInheritanceLookupStrategy, this);
    		result = _lexicalParametersLookupStrategy;
    	}
    	return result;
    }
    
    protected LookupContext _lexicalParametersLookupStrategy;
    
    protected LocalParameterBlockLookupStrategy _localInheritanceLookupStrategy = new LocalParameterBlockLookupStrategy(this);
    
  	protected class LocalParameterBlockLookupStrategy extends LocalLookupContext<Type> {
  	  public LocalParameterBlockLookupStrategy(Type element) {
  			super(element);
  		}

  	  @Override
  	  @SuppressWarnings("unchecked")
  	  public <D extends Declaration> List<? extends SelectionResult> declarations(DeclarationSelector<D> selector) throws LookupException {
//  	    return selector.selection(parameters());
  			List<SelectionResult> result = Lists.create();
  			List<ParameterBlock> parameterBlocks = parameterBlocks();
  			Iterator<ParameterBlock> iter = parameterBlocks.iterator();
  			// If the selector found a match, we stop.
  			// We must iterate in reverse.
  			while(result.isEmpty() && iter.hasNext()) {
  				ParameterBlock imporT = iter.next();
  				result.addAll(selector.selection(imporT.parameters()));
  			}
  			return result;
  	  }
  	}
  	
  	@Override
   public <P extends Parameter> int nbTypeParameters(Class<P> kind) {
  		return parameterBlock(kind).nbTypeParameters();
  	}

  	@Override
   public <P extends Parameter> List<P> parameters(Class<P> kind) {
  		List<P> result;
  		ParameterBlock<P> parameterBlock = parameterBlock(kind);
  		if(parameterBlock != null) {
			  result = parameterBlock.parameters();
  		} else {
  			result = ImmutableList.of();
  		}
  		return result;
  	}
  	
  	/**
  	 * Indices start at 1.
  	 */
  	@Override
   public <P extends Parameter> P parameter(Class<P> kind, int index) {
  		return parameterBlock(kind).parameter(index);
  	}

    @Override
   public List<Member> getIntroducedMembers() {
      return ImmutableList.<Member>of(this);
    }
    
    /* (non-Javadoc)
		 * @see chameleon.oo.type.Tajp#complete()
		 */
    @Override
   public boolean complete() throws LookupException {
	    	List<Member> members = localMembers(Member.class);
	    	// Only check for actual definitions
	    	new TypePredicate<Declaration>(Declaration.class).filter(members);
	    	Iterator<Member> iter = members.iterator();
	    	boolean result = true;
	    	while(iter.hasNext()) {
	    		Member member = iter.next();
	    		result = result && (mustBeOverridden(member));
	    	}
	      return result;
    }
    

  	/***********
  	 * MEMBERS *
  	 ***********/

    /* (non-Javadoc)
		 * @see chameleon.oo.type.Tajp#add(chameleon.oo.type.TypeElement)
		 */
   /*@
     @ public behavior
     @
     @ pre element != null;
     @
     @ post directlyDeclaredElements().contains(element);
     @*/
  	@Override
   public abstract void add(TypeElement element) throws ChameleonProgrammerException;
  	
    /* (non-Javadoc)
		 * @see chameleon.oo.type.Tajp#remove(chameleon.oo.type.TypeElement)
		 */
   /*@
     @ public behavior
     @
     @ pre element != null;
     @
     @ post ! directlyDeclaredElements().contains(element);
     @*/
  	@Override
   public abstract void remove(TypeElement element) throws ChameleonProgrammerException;
  	
    /* (non-Javadoc)
		 * @see chameleon.oo.type.Tajp#addAll(java.util.Collection)
		 */
   /*@
     @ public behavior
     @
     @ pre elements != null;
     @ pre !elements.contains(null);
     @
     @ post directlyDeclaredElements().containsAll(elements);
     @*/
  	@Override
   public void addAll(Collection<? extends TypeElement> elements) throws ChameleonProgrammerException {
  		for(TypeElement element: elements) {
  			add(element);
  		}
  	}

    /* (non-Javadoc)
		 * @see chameleon.oo.type.Tajp#getDirectSuperTypes()
		 */

  	@Override
   public List<Type> getDirectSuperTypes() throws LookupException {
  		List<Type> result = Lists.create();
  		for(InheritanceRelation element:inheritanceRelations()) {
  			Type type = element.superType();
  			if (type!=null) {
  				result.add(type);
  			}
  		}
  		return result;
  	}

    @Override
   public List<Type> getDirectSuperClasses() throws LookupException {
      List<Type> result = Lists.create();
      for(InheritanceRelation element:inheritanceRelations()) {
        result.add((Type)element.superElement());
      }
      return result;
}

    @Override
   public void accumulateAllSuperTypes(Set<Type> acc) throws LookupException {
    	List<Type> temp =getDirectSuperTypes();
    	for(Type type:temp) {
    		boolean add=true;
    		for(Type acced: acc) {
    			if(acced.baseType().sameAs(type.baseType())) {
    				add=false;
    				break;
    			}
    		}
    		if(add) {
    			acc.add(type);
    		  type.accumulateAllSuperTypes(acc);
    		}
    	}
    }
    
    @Override
   public Set<Type> getAllSuperTypes() throws LookupException {
    	if(_superTypeCache == null) {
    		synchronized(this) {
        	if(_superTypeCache == null) {
        		Set<Type> elements = new HashSet<Type>();
        		accumulateAllSuperTypes(elements);
        		_superTypeCache = ImmutableSet.<Type>builder().addAll(elements).build();
        	}
    		}
    	}
    	return _superTypeCache;
    }

    
  	@Override
   public Set<Type> getSelfAndAllSuperTypesView() throws LookupException {
  		try {
  			if(_superTypeAndSelfCache == null) {
  				synchronized(this) {
  					if(_superTypeAndSelfCache == null) {
          		Set<Type> elements = new HashSet<Type>();
  						newAccumulateSelfAndAllSuperTypes(elements);
  						_superTypeAndSelfCache = ImmutableSet.<Type>builder().addAll(elements).build();
  	  			}
  				}
  			}
  			return _superTypeAndSelfCache;
  			
  		} catch(ChameleonProgrammerException exc) {
  			if(exc.getCause() instanceof LookupException) {
  				throw (LookupException) exc.getCause();
  			} else {
  				throw exc;
  			}
  		}
  	}

  	protected SuperTypeJudge _judge;
  	protected AtomicBoolean _judgeLock = new AtomicBoolean();
  	
  	public SuperTypeJudge superTypeJudge() throws LookupException {
  		SuperTypeJudge result = _judge;
  		if(result == null) {
  			if(_judgeLock.compareAndSet(false, true)) {
  				result = new SuperTypeJudge();
  				accumulateSuperTypeJudge(result);
  				_judge = result;
  			} else {
  				//spin lock
  				while((result = _judge) == null) {}
  			}
  		}
  		return result;
//  		if(_judge == null) {
//  			synchronized(this) {
//  				if(_judge == null) {
//  					_judge = new SuperTypeJudge();
//
//  					accumulateSuperTypeJudge(_judge);
//
//  					//        SuperTypeJudge faster = new SuperTypeJudge();
//  					//        accumulateSuperTypeJudge(faster);
//  					//        
//  					//        _judge.add(this);
//  					//        List<Type> temp = getDirectSuperTypes();
//  					//        for(Type type:temp) {
//  					//          SuperTypeJudge superJudge = type.superTypeJudge();
//  					//          _judge.merge(superJudge);
//  					//        }
//  					//        Set<Type> fasterTypes = faster.types();
//  					//        Set<Type> judgeTypes = _judge.types();
//  					//        Set<Type> view = getSelfAndAllSuperTypesView();
//  					//        if(fasterTypes.size() != (judgeTypes.size())) {
//  					//          //_judge = null;
//  					//          System.out.println("debug");
//  					//        }
//  				}
//  			}
//  		}
//  		return _judge;
  	}

    @Override
   public void newAccumulateAllSuperTypes(Set<Type> acc) throws LookupException {
    	List<Type> temp = getDirectSuperTypes();
    	for(Type type:temp) {
    		boolean add=true;
    		for(Type acced: acc) {
    			if(acced.baseType().sameAs(type.baseType())) {
    				add=false;
    				break;
    			}
    		}
    		if(add) {
    		  type.newAccumulateSelfAndAllSuperTypes(acc);
    		}
    	}
    }
    
    @Override
   public void newAccumulateSelfAndAllSuperTypes(Set<Type> acc) throws LookupException {
    	acc.add(this);
    	newAccumulateAllSuperTypes(acc);
    }
    
    private Set<Type> _superTypeCache;
    
    private Set<Type> _superTypeAndSelfCache;
    
    //TODO: rename to properSubTypeOf

   /*@
     @ public behavior
     @
     @ post \result == equals(other) || subTypeOf(other);
     @*/
    @Override
   public boolean assignableTo(Type other) throws LookupException {
    	return subTypeOf(other);
    }

    
  	/* (non-Javadoc)
		 * @see chameleon.oo.type.Tajp#inheritanceRelations()
		 */
   /*@
     @ public behavior
     @
     @ post \result != null;
     @*/
  	@Override
   public abstract List<InheritanceRelation> inheritanceRelations() throws LookupException;
  	
  	/**
  	 * The default behavior is to return inheritanceRelations(). If there are
  	 * member inheritance relations, the method must be overridden to exclude them.
  	 * @return
  	 */
  	@Override
   public abstract List<InheritanceRelation> nonMemberInheritanceRelations();
  	
  	@Override
   public abstract List<InheritanceRelation> explicitNonMemberInheritanceRelations();
  	
    @Override
   public <I extends InheritanceRelation> List<I> explicitNonMemberInheritanceRelations(Class<I> kind) {
      List result = explicitNonMemberInheritanceRelations();
      CollectionOperations.filter(result, d -> kind.isInstance(d));
      return result;
    }

  	@Override
   public <I extends InheritanceRelation> List<I> nonMemberInheritanceRelations(Class<I> kind) {
      List result = nonMemberInheritanceRelations();
      CollectionOperations.filter(result, d -> kind.isInstance(d));
      return result;
  	}

   /*@
     @ public behavior
     @
     @ pre relation != null;
     @ post inheritanceRelations().contains(relation);
     @*/
  	// FIXME rename to addNonMemberInheritanceRelation.
  	@Override
   public abstract void addInheritanceRelation(InheritanceRelation relation) throws ChameleonProgrammerException;
    
  	/* (non-Javadoc)
		 * @see chameleon.oo.type.Tajp#removeInheritanceRelation(chameleon.oo.type.inheritance.InheritanceRelation)
		 */
   /*@
     @ public behavior
     @
     @ pre relation != null;
     @ post ! inheritanceRelations().contains(relation);
     @*/
  	@Override
   public abstract void removeNonMemberInheritanceRelation(InheritanceRelation relation) throws ChameleonProgrammerException;
  	
  	@Override
   public void removeAllNonMemberInheritanceRelations() {
  		for(InheritanceRelation relation: nonMemberInheritanceRelations()) {
  			removeNonMemberInheritanceRelation(relation);
  		}
  	}
  	
    /* (non-Javadoc)
		 * @see chameleon.oo.type.Tajp#localMembers(java.lang.Class)
		 */
    @Override
   public <T extends Member> List<T> localMembers(final Class<T> kind) throws LookupException {
      return (List<T>) new TypeFilter(kind).retain(localMembers());
    }
    
    /* (non-Javadoc)
		 * @see chameleon.oo.type.Tajp#localMembers()
		 */
    @Override
   public abstract List<Member> localMembers() throws LookupException;
    
    @Override
   public List<Member> implicitMembers() {
    	return Collections.EMPTY_LIST;
    }
    
    @Override
   public <M extends Member> List<M> implicitMembers(Class<M> kind) {
    	// implicitMembers returns an immutable list.
    	List result = new ArrayList(implicitMembers());
    	Iterator iter = result.iterator();
    	while(iter.hasNext()) {
    		Object o = iter.next();
    		if(! kind.isInstance(o)) {
    			iter.remove();
    		}
    	}
    	return result;
    }

    public <D extends Member> List<? extends SelectionResult> implicitMembers(DeclarationSelector<D> selector) throws LookupException {
    	return selector.selection(implicitMembers());
    }

    @Override
   public <T extends Member> List<T> directlyDeclaredMembers(Class<T> kind) {
      return (List<T>) new TypeFilter(kind).retain(directlyDeclaredMembers());
    }
    
    @Override
   public <T extends Member> List<T> directlyDeclaredMembers(Class<T> kind, ChameleonProperty property) {
      List<T> result = directlyDeclaredMembers(kind);
      Iterator<T> iter = result.iterator();
      while(iter.hasNext()) {
      	T t = iter.next();
      	if(! t.isTrue(property)) {
      		iter.remove();
      	}
      }
      return result;
    }
    
    @Override
   public  List<Member> directlyDeclaredMembers() {
  		List<Member> result = Lists.create();
      for(TypeElement m: directlyDeclaredElements()) {
        result.addAll(m.declaredMembers());
      }
      return result;
    }
    @Override
   public <D extends Member> List<? extends SelectionResult> members(DeclarationSelector<D> selector) throws LookupException {
    	// 1) perform local search
    	boolean nonGreedy = ! selector.isGreedy();
    	List<SelectionResult> result = (List)localMembers(selector);
    	if(nonGreedy || result.isEmpty()) {
    	  List<SelectionResult> implicitMembers = (List)implicitMembers(selector);
    	  if(result == Collections.EMPTY_LIST) {
    	  	result = implicitMembers;
    	  } else {
    	  	result.addAll(implicitMembers);
    	  }
    	}
    	// 2) process inheritance relations
    	//    only if the selector isn't greedy or
    	//    there are not results.
    	if(nonGreedy || result.isEmpty()) {
    		for (InheritanceRelation rel : inheritanceRelations()) {
    			result = rel.accumulateInheritedMembers(selector, result);
    		}
    		// We cannot take a shortcut and test for > 1 because if
    		// the inheritance relation transforms the member (as is done with subobjects)
    		// the transformed member may have to be removed, even if there is only 1.
    		selector.filter(result);
    		return result;
    	} else {
    	  return result;
    	}
    }
    
    @Override
    public void addAllInheritanceRelations(Collection<InheritanceRelation> relations) {
    	for(InheritanceRelation rel: relations) {
    		addInheritanceRelation(rel);
    	}
    }
    
    /* (non-Javadoc)
		 * @see chameleon.oo.type.Tajp#localMembers(chameleon.core.lookup.DeclarationSelector)
		 */
    @Override
   @SuppressWarnings("unchecked")
    public abstract <D extends Member> List<? extends SelectionResult> localMembers(DeclarationSelector<D> selector) throws LookupException;

    @Override
   public List<Member> members() throws LookupException {
      return members(Member.class);
    }
    
    @Override
   @SuppressWarnings("unchecked")
		public <M extends Member> List<M> members(final Class<M> kind) throws LookupException {

		// 1) All defined members of the requested kind are added.
    boolean foundInCache = false;
    List<M> result = null;
    if(_membersCache != null) {
    	result = _membersCache.get(kind);
    	if(result != null) {
    		foundInCache = true;
    		result = new ArrayList<M>(result);
    	}
    } 
    if(! foundInCache){
    	result = localMembers(kind);
    	result.addAll(implicitMembers(kind));
    	// 2) Fetch all potentially inherited members from all inheritance relations
    	for (InheritanceRelation rel : inheritanceRelations()) {
    		result = rel.accumulateInheritedMembers(kind, result);
    	}
    	if(Config.cacheDeclarations()) {
    		if(_membersCache == null) {
    			_membersCache = new HashMap<Class,List>();
    		}
    		_membersCache.put(kind, new ArrayList(result));
    	}
    }
		return result;
	}

    private Map<Class,List> _membersCache;
    
    @Override
   public abstract List<? extends TypeElement> directlyDeclaredElements();

  	@Override
   public <T extends TypeElement> List<T> directlyDeclaredElements(Class<T> kind) {
    	List<TypeElement> tmp = (List<TypeElement>) directlyDeclaredElements();
    	new TypePredicate<>(kind).filter(tmp);
      return (List<T>)tmp;
  	}

    /* (non-Javadoc)
		 * @see chameleon.oo.type.Tajp#declarations()
		 */
    @Override
   public List<? extends Declaration> declarations() throws LookupException {
    	List<? extends Declaration> result = declarationCache();
    	if(result == null) {
    		result = members();
    		setDeclarationCache(result);
    	}
    	return result;
    }
    /* (non-Javadoc)
		 * @see chameleon.oo.type.Tajp#declarations(chameleon.core.lookup.DeclarationSelector)
		 */
    @Override
    public <D extends Declaration> List<? extends SelectionResult> declarations(DeclarationSelector<D> selector) throws LookupException {
    	return members((DeclarationSelector<? extends Member>)selector);
    }
    
    protected void copyContents(Type from) {
  		copyContents(from, false);
  	}

  	protected void copyContents(Type from, boolean link) {
  		copyInheritanceRelations(from, link);
      copyEverythingExceptInheritanceRelations(from, link);
  	}

		protected void copyInheritanceRelations(Type from, boolean link) {
			List<InheritanceRelation> relations = from.explicitNonMemberInheritanceRelations();
			for(InheritanceRelation relation : relations) {
        InheritanceRelation clone = clone(relation);
        if(link) {
        	clone.setOrigin(relation);
        }
				addInheritanceRelation(clone);
  		}
		}

		protected void copyEverythingExceptInheritanceRelations(Type from, boolean link) {
			copyParameterBlocks(from, link);
			copyModifiers(from, link);
      copyTypeElements(from, link);
		}

		private void copyTypeElements(Type from, boolean link) {
			for(TypeElement el : from.directlyDeclaredElements()) {
        TypeElement clone = clone(el);
        if(link) {
        	clone.setOrigin(el);
        }
				add(clone);
      }
		}

		protected void copyParameterBlocks(Type from, boolean link) {
      for(ParameterBlock par : parameterBlocks()) {
      	removeParameterBlock(par);
      }
      for(ParameterBlock par : from.parameterBlocks()) {
      	ParameterBlock clone = clone(par);
        if(link) {
        	clone.setOrigin(par);
        }
				addParameterBlock(clone);
      }
		}

		protected void copyModifiers(Type from, boolean link) {
			for(Modifier mod : from.modifiers()) {
      	Modifier clone = clone(mod);
        if(link) {
        	clone.setOrigin(mod);
        }
				addModifier(clone);
      }
		}
  
  	/* (non-Javadoc)
		 * @see chameleon.oo.type.Tajp#alias(chameleon.core.declaration.SimpleNameSignature)
		 */
  	@Override
   public Type alias(String name) {
      return new TypeAlias(name,this);
  	}

  	/* (non-Javadoc)
		 * @see chameleon.oo.type.Tajp#intersection(chameleon.oo.type.Type)
		 */
  	@Override
   public Type intersection(Type type) throws LookupException {
  		return type.intersectionDoubleDispatch(this);
  	}
  	
  	@Override
   public Type intersectionDoubleDispatch(Type type) throws LookupException {
  		Type result = new IntersectionType(this,type);
  		result.setUniParent(parent());
  		return result;
  	}

  	@Override
   public Type intersectionDoubleDispatch(IntersectionType type) throws LookupException {
  		IntersectionType result = clone(type);
  		result.addType(type);
  		return result;
  	}

  	@Override
   public Type union(Type type) throws LookupException {
  		return type.unionDoubleDispatch(this);
  	}
  	
  	@Override
   public Type unionDoubleDispatch(Type type) throws LookupException {
  		Type result = new UnionType(this,type);
  		result.setUniParent(parent());
  		return result;
  	}
  	
  	@Override
   public Type unionDoubleDispatch(UnionType type) throws LookupException {
  		UnionType result = clone(type);
  		result.addType(type);
  		return result;
  	}

		/* (non-Javadoc)
		 * @see chameleon.oo.type.Tajp#replace(chameleon.oo.type.TypeElement, chameleon.oo.type.TypeElement)
		 */
		@Override
      public abstract void replace(TypeElement oldElement, TypeElement newElement);

		/* (non-Javadoc)
		 * @see chameleon.oo.type.Tajp#baseType()
		 */
		@Override
      public abstract Type baseType();

		protected boolean mustBeOverridden(Member member) {
			ObjectOrientedLanguage lang = language(ObjectOrientedLanguage.class);
			// ! CLASS ==> ! ABSTRACT
			return member.isTrue(lang.OVERRIDABLE) && member.isTrue(lang.INSTANCE) && member.isFalse(lang.DEFINED);
		}
		
		/* (non-Javadoc)
		 * @see chameleon.oo.type.Tajp#verifySelf()
		 */
		@Override
		public Verification verifySelf() {
			Verification result = Valid.create(); 
			ObjectOrientedLanguage lang = language(ObjectOrientedLanguage.class);
			if(! isTrue(lang.ABSTRACT)) {
				List<Member> members = null;
				try {
					members = members();
				} catch (LookupException e) {
					result = result.and(new BasicProblem(this, "Cannot compute the members of this class"));
				}
				if(members != null) {
					Iterator<Member> iter = members.iterator();
					while(iter.hasNext()) {
						Member m = iter.next();
						if(!mustBeOverridden(m)) {
							iter.remove();
						} else {
							//DEBUG
							mustBeOverridden(m);
						}
					}
					if(! members.isEmpty()) {
						StringBuffer msg = new StringBuffer("This class must implement the following abstract members: ");
						int size = members.size();
						for(int i=0; i< size; i++) {
							try {
								msg.append(members.get(i).name());
								if(i < size -1) {
									msg.append(',');
								}
							} catch(NullPointerException exc) {
							}
						}
						result = result.and(new BasicProblem(this, msg.toString()));
					}
				}
			}
			return result;
		}

		@Override
		public boolean lowerBoundAtLeatAsHighAs(Type other, TypeFixer trace) throws LookupException {
		  return false;
		}

		@Override
      public boolean sameAs(Type other, List<Pair<TypeParameter, TypeParameter>> trace) throws LookupException {
//			List<Pair<TypeParameter, TypeParameter>> newTrace = new ArrayList<Pair<TypeParameter, TypeParameter>>(trace);
			return uniSameAs(other,trace) || other.uniSameAs(this,trace);
		}
		
		@Override
      public Type lowerBound() throws LookupException {
			return this;
		}
		
		@Override
      public Type upperBound() throws LookupException {
			return this;
		}

		@Override
		public <D extends Member> List<D> membersDirectlyOverriddenBy(MemberRelationSelector<D> selector) throws LookupException {
			List<D> result = Lists.create();
			if(!selector.declaration().ancestors().contains(this)) {
				result.addAll((List)members(selector));
			} else {
				for(InheritanceRelation relation:inheritanceRelations()) {
					result.addAll(relation.membersDirectlyOverriddenBy(selector));
				}
			}
			return result;
		}
		
		@Override
      public <D extends Member> List<D> membersDirectlyAliasedBy(MemberRelationSelector<D> selector) throws LookupException {
			List<D> result = Lists.create();
			for(InheritanceRelation relation:inheritanceRelations()) {
				result.addAll(relation.membersDirectlyAliasedBy(selector));
			}
			return result;
		}
		
		@Override
      public <D extends Member> List<D> membersDirectlyAliasing(MemberRelationSelector<D> selector) throws LookupException {
			return ImmutableList.of();
		}
		
	  @Override
   public HidesRelation<? extends Member> hidesRelation() {
			return _hidesSelector;
	  }
	  
	  private static HidesRelation<Type> _hidesSelector = new HidesRelation<Type>(Type.class);
}


