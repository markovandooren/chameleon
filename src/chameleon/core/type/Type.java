package chameleon.core.type;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.rejuse.java.collections.TypeFilter;
import org.rejuse.logic.ternary.Ternary;
import org.rejuse.predicate.PrimitivePredicate;
import org.rejuse.predicate.TypePredicate;

import chameleon.core.MetamodelException;
import chameleon.core.context.Context;
import chameleon.core.context.TargetContext;
import chameleon.core.declaration.Declaration;
import chameleon.core.declaration.DeclarationContainer;
import chameleon.core.declaration.Definition;
import chameleon.core.declaration.SimpleNameSignature;
import chameleon.core.declaration.TargetDeclaration;
import chameleon.core.element.ChameleonProgrammerException;
import chameleon.core.element.Element;
import chameleon.core.member.Member;
import chameleon.core.member.MemberImpl;
import chameleon.core.modifier.Modifier;
import chameleon.core.modifier.ModifierContainer;
import chameleon.core.namespace.NamespaceOrType;
import chameleon.core.namespacepart.NamespacePart;
import chameleon.core.statement.CheckedExceptionList;
import chameleon.core.statement.ExceptionSource;
import chameleon.core.statement.StatementContainer;
import chameleon.core.variable.VariableContainer;

/**
 * <p>A class representing types in object-oriented programs.</p>
 *
 * <p>A class contains <a href="Member.html">members</a> as its content.</p>
 *
 * @author Marko van Dooren
 */
public abstract class Type<E extends Type<E>> extends MemberImpl<E,TypeContainer,SimpleNameSignature> 
                implements TargetDeclaration<E,TypeContainer,SimpleNameSignature>, NamespaceOrType<E,TypeContainer,SimpleNameSignature>, 
                           TypeContainer<E,TypeContainer>, VariableContainer<E,TypeContainer>, 
                           VariableOrType<E,TypeContainer>, Definition<E,TypeContainer,SimpleNameSignature>,
                           StatementContainer<E,TypeContainer>, 
                           Cloneable, ExceptionSource<E,TypeContainer>, ModifierContainer<E,TypeContainer>, 
                           DeclarationContainer<E,TypeContainer> {
 
    /**
     * Initialize a new Type.
     *
     * @param name    The short name of the new type.
     * @param access  The access modifier.
     * @param context The context of the new type. This will determine the lookup used by the new type.
     */
   /*@
     @ public behavior
     @
     @ pre name != null;
     @ pre access != null;
     @ pre context != null;
 	   @
     @ post getName() == name;
     @ post getAccessModifier() == access;
     @ post getMemberContext() == context;
     @ post getParent() == null;
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
  		return signature().getName();
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
        String pack = getNamespace().getFullyQualifiedName();
        return (pack.equals("") ? "" : pack+".")+getName();
    }

    /*******************
     * LEXICAL CONTEXT *
     *******************/
    
    
    public TargetContext targetContext() {
    	return language().contextFactory().createTargetContext(this);
    }
    
    public Context lexicalContext(Element element) {
    	return language().contextFactory().createLexicalContext(this,targetContext());
    }

    /************************
     * BEING A TYPE ELEMENT *
     ************************/
    
    public Set<Member> getIntroducedMembers() {
      Set<Member> result = new HashSet<Member>();
      result.add(this);
      return result;
    }
    
    public boolean complete() {
    	Set<Member> members = directlyDeclaredElements(Member.class);
    	// Only check for actual definitions
    	new TypePredicate<Element,Definition>(Definition.class).filter(members);
    	Iterator<Member> iter = members.iterator();
    	boolean result = true;
    	while(result && iter.hasNext()) {
    		result = result && iter.next().is(language().DEFINED) == Ternary.TRUE;
    	}
      return false;
    }
    
    /**********
     * ACCESS *
     **********/

    public Type getTopLevelType() {
        // FIXME: BAD design !!!
        if (getParent() instanceof Type) {
            return ((Type)getParent()).getTopLevelType();
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

//  	public OrderedReferenceSet<Type, TypeElement> getMembersLink() {
//  		return _elements;
//  	}


    /**
     * Add the given element to this type.
     * 
     * @throws ChameleonProgrammerException
     *         The given element could not be added. E.g when you try to add
     *         an element to a computed type.
     */
  	public abstract void add(TypeElement element) throws ChameleonProgrammerException;

    /**************
     * SUPERTYPES *
     **************/

    public List<Type> getDirectSuperTypes() throws MetamodelException {
            final ArrayList<Type> result = new ArrayList<Type>();
            for(TypeReference element:getSuperTypeReferences()) {
                    String nnn = ((TypeReference)element).getName();
                    Type type = ((TypeReference)element).getType();
                    if (type!=null) {
                        result.add(type);
                    } else {
                        ((TypeReference)element).getType();
                        throw new MetamodelException();
                    }
                    return null;
                }
            return result;
    }

    protected void accumulateAllSuperTypes(Set<Type> acc) throws MetamodelException {
    	List<Type> temp =getDirectSuperTypes();
    	acc.addAll(temp);
    	for(Type type:temp) {
    		type.accumulateAllSuperTypes(acc);
    	}
    }
    
    public Set<Type> getAllSuperTypes() throws MetamodelException {
    	Set result = new HashSet();
    	accumulateAllSuperTypes(result);
    	return result;
    }

    public boolean subTypeOf(Type other) throws MetamodelException {
    	  Collection superTypes = getAllSuperTypes(); 
        return superTypes.contains(other);
    }
    
    /**
     * Check if this type equals the given other type. This is
     * a unidirectional check to keep things extensible. It is fine
     * if equals(other) is false, but other.equals(this) is true.
     *  
     * @param other
     * @return
     */
    public boolean uniEqualTo(Type other) {
    	return other == this;
    }
    
   /*@
     @ public behavior
     @
     @ post ! other instanceof Type ==> \result == false
     @ post other instanceof Type ==> \result == equalTo(other) || other.equalTo(this); 
     @*/
    public boolean equals(Object other) {
    	boolean result = false;
    	if(other instanceof Type) {
    		result = uniEqualTo((Type)other) || ((Type)other).uniEqualTo(this);
    	}
    	return result;
    }

    /**
     * Check if this type is assignable to another type.
     * 
     * @param other
     * @return
     * @throws MetamodelException
     */
   /*@
     @ public behavior
     @
     @ post \result == equals(other) || subTypeOf(other);
     @*/
    public boolean assignableTo(Type other) throws MetamodelException {
    	boolean equal = equals(other);
    	boolean subtype = subTypeOf(other);
    	return (equal || subtype);
    }

  	/**
  	 * Return the super type reference of this type.
  	 */
   /*@
     @ public behavior
     @
     @ post \result != null;
     @*/
  	public abstract List<TypeReference> getSuperTypeReferences();

  	/**
  	 * Add the give type reference as a super type.
  	 * @param type
  	 * @throws ChameleonProgrammerException
  	 *         It is not possible to add the given type. E.g. you cannot
  	 *         add a super type reference to a computed type.
  	 */
   /*@
     @ public behavior
     @
     @ pre type != null;
     @ post getSuperTypeReferences().contains(type)
     @*/
  	public abstract void addSuperType(TypeReference type) throws ChameleonProgrammerException;

  	/**
  	 * Add the give type reference as a super type.
  	 * @param type
  	 * @throws ChameleonProgrammerException
  	 *         It is not possible to add the given type. E.g. you cannot
  	 *         add a super type reference to a computed type.
  	 */
   /*@
     @ public behavior
     @
     @ pre type != null;
     @ post ! getSuperTypeReferences().contains(type)
     @*/
  	public abstract void removeSuperType(TypeReference type);


//    /***********
//     * METHODS *
//     ***********/
//
//    /**
//     * Return the methods defined and declared directly in this type.
//     */
//    public List getMethods() {
//        return getMemberMethods();
//    }

//    /**
//     * Return the list of all methods introduced in this type by its members.
//     */
//    /*@
//        @ public behavior
//        @
//        @ post \result != null;
//        @ // All introduced methods are present in the result.
//        @ post (\forall Member member; getMembers().contains(member);
//        @         \result.containsAll(member.getIntroducedMethods()));
//        @ // All methods in the result are introduced by a member.
//        @ post (\forall Method method; \result.contains(method);
//        @        (\exists Member member; getMembers().contains(member);
//        @          member.getIntroducedMethods().contains(method)));
//        @*/
//    public List getMemberMethods() {
//        List members = getMembers();
//        final List result = new ArrayList();
//        new Visitor() {
//            public void visit(Object element) {
//                try {
//                    result.addAll(((Member)element).getIntroducedMethods());
//                } catch (ClassCastException exc) {
//                    exc.printStackTrace();
//                }
//            }
//        }.applyTo(members);
//        return result;
//    }

//     /**
//      * Return the list of all variables introduced in this type by its members.
//      */
//    /*@
//      @ public behavior
//      @
//      @ post \result != null;
//      @ // All introduced variables are present in the result.
//      @ post (\forall Member member; getMembers().contains(member);
//      @         \result.containsAll(member.getIntroducedVariables()));
//      @ // All variables in the result are introduced by a member.
//      @ post (\forall Variable variable; \result.contains(variable);
//      @        (\exists Member member; getMembers().contains(member);
//      @          member.getIntroducedVariables().contains(variable)));
//      @*/
//    public List getVariables() {
//        List members = getMembers();
//        final List result = new ArrayList();
//        new Visitor() {
//            public void visit(Object element) {
//              result.addAll(((Member)element).getIntroducedVariables());
//            }
//        }.applyTo(members);
//        return result;
//    }

//    public List getIntroducingMembers() {
//        List members = getMembers();
//        final List result = new ArrayList();
//        new Visitor() {
//            public void visit(Object element) {
//                    result.addAll(((Member)element).getIntroducedIntroducingMembers());
//            }
//        }.applyTo(members);
//        return result;
//    }

//  	/***********
//  	 * METHODS *
//  	 ***********/
//  	public void addMethod(Method m) {
//  		_members.add(m.getParentLink());
//  	}
//
//  	// remove method in model
//  	public void removeMethod(Method m) {
//  		_members.remove(m.getParentLink());
//  	}
//
//  	public void replaceMethod(Method oldMethod, Method newMethod) {
//  		_members.replace(oldMethod.getParentLink(), newMethod.getParentLink());
//  	}
//
//
//    public void addIntroducingMember(IntroducingMember im) {
//      getMembersLink().add(im.getParentLink());
//    }

//  // remove mehtod in model
//  public void removeMethod(NonConstructor m) {
//  	_members.remove(m.getTypeLink());
//  }
//
//  // replace method in model
//  public void replaceMethod(NonConstructor oldMethod, NonConstructor newMethod){
//  	_members.replace(oldMethod.getTypeLink(), newMethod.getTypeLink());
//  }
//
//    public OrderedReferenceSet getMethodsLink() {
//        return getMembersLink();
//    }
//
//    //private ReferenceSet _methods = new ReferenceSet(this);
//
//    protected List getMethods(Class type) {
//        List result = getMethods();
//        new TypePredicate(type).filter(result);
//        return result;
//    }
//
//    public Set getAllMethods() throws MetamodelException {
//        return getAllMethods(Method.class);
//    }

//    private List getAllMethodsSimple(final Class type) throws MetamodelException {
//        final List result = getMethods(type);
//
//        //Add methods from supertypes, ...
//        try {
//            new RobustVisitor() {
//                public Object visit(Object element) throws MetamodelException {
//                    result.addAll(((Type)element).getAllMethodsSimple(type));
//                    return null;
//                }
//
//                public void unvisit(Object element, Object undo) {
//                }
//            }.applyTo(getDirectSuperTypes());
//        } catch (MetamodelException e) {
//            throw e;
//        } catch (Exception e) {
//            e.printStackTrace();
//            throw new Error();
//        }
//        return result;
//    }
    
    /**
     * Return the members of the given kind directly declared by this type.
     * @return
     */
    public <T extends TypeElement> Set<T> directlyDeclaredElements(final Class<T> kind) {
      return (Set<T>) new TypeFilter(kind).retain(directlyDeclaredElements());
    }
    
    /**
     * Return the members directly declared by this type.
     * @return
     */
    public abstract Set<? extends TypeElement> directlyDeclaredElements();

    public Set<Member> members() throws MetamodelException {
      return members(Member.class);
    }

    /**
     * Return the members of this class.
     * @param <M>
     * @param kind
     * @return
     * @throws MetamodelException
     */
    public <M extends Member> Set<M> members(final Class<M> kind) throws MetamodelException {
      
        // 1) All defined members of the requested kind are added.
    final HashSet<M> result = new HashSet(directlyDeclaredElements(kind));

    // 2) Fetch all INHERITABLE methods from supertypes of the requested kind
    final List supers = new ArrayList();
    try {

      for (Type t : getDirectSuperTypes()) {
        Collection<M> allFromSuper = t.members(kind);
        new PrimitivePredicate<M>() {
          public boolean eval(M method) throws MetamodelException {
            Ternary temp = method.is(language().INHERITABLE);
            boolean result;
            if (temp == Ternary.TRUE) {
              result = true;
            } else if (temp == Ternary.FALSE) {
              result = false;
            } else {
              assert (temp == Ternary.UNKNOWN);
              throw new MetamodelException(
                  "For one of the members of a super type of "
                      + getFullyQualifiedName()
                      + " it is unknown whether it is inheritable or not.");
            }
            return result;
          }
        }.filter(allFromSuper);
        supers.addAll(allFromSuper);

      }
      // ... but remove the ones that are overridden or hidden.
      new PrimitivePredicate<M>() {
        public boolean eval(final M superMember) throws Exception {
          return !new PrimitivePredicate<M>() {
            public boolean eval(M nc) throws MetamodelException {
              return nc.overrides(superMember) || nc.hides(superMember);
            }
          }.exists(result);
        }
      }.filter(supers);
    } catch (MetamodelException e) {
      throw e;
    } catch (Exception e1) {
      throw new Error();
    }

    result.addAll(supers);
    return result;

    }

//    public RegularMethod getApplicableRegularMethod(final String name, final List paramTypes) throws MetamodelException {
//        return (RegularMethod)getApplicableMethod(name, paramTypes, RegularMethod.class);
//    }
//
//    public PrefixOperator getApplicablePrefixOperator(final String name, final List paramTypes) throws MetamodelException {
//        return (PrefixOperator)getApplicableMethod(name, paramTypes, PrefixOperator.class);
//    }
//
//    public PostfixOperator getApplicablePostfixOperator(final String name, final List paramTypes) throws MetamodelException {
//        return (PostfixOperator)getApplicableMethod(name, paramTypes, PostfixOperator.class);
//    }
//
//    public InfixOperator getApplicableInfixOperator(final String name, final List paramTypes) throws MetamodelException {
//        return (InfixOperator)getApplicableMethod(name, paramTypes, InfixOperator.class);
//    }

//    /**
//     * Return the method with the given name and type that is applicable to the given list of parameter types.
//     * The type is for example a regular method, or a prefix, infix, or postfix method.
//     *
//     * @param name       The name of the requested method
//     * @param paramTypes The list of types of the parameters supplied to the method.
//     * @param type       The type of the requested method.
//     */
//    /*@
//    @ public behavior
//    @
//    @ pre name != null;
//    @ pre paramTypes != null;
//    @ pre type != null;
//    @*/
//    public Method getApplicableMethod(final String name, final List paramTypes, Class type) throws MetamodelException {
//        final Collection<Method> methods = getAllMethodsSimple(type);
//        // filter on name
//        // filter on number of parameters.
//        final int nbParams = paramTypes.size();
////        new PrimitiveTotalPredicate() {
////            public boolean eval(Object o) {
////                return ((Method)o).getName().equals(name) && (((Method)o).getNbParameters()==nbParams);
////            }
////        }.filter(methods);
//
//        // filter out those to which the parameters do not apply
//        Iterator<Method> methodListIterator = methods.iterator();
//        while (methodListIterator.hasNext()) {
//        	Method method = methodListIterator.next();
//        	if((! method.getName().equals(name)) || (method.getNbParameters()!=nbParams)) {
//        		methodListIterator.remove();
//        		// go to the next method in the list
//        		continue;
//        	}
//        	
//        	List<FormalParameter> params = method.getParameters();
//        	for (int i = 0; i < nbParams ; i++) {
//        		Type param = (Type) paramTypes.get(i);
//        		Type methodParamType = params.get(i).getType();
//        		if (!param.assignableTo(methodParamType)) {
//        			methodListIterator.remove();
//        			// break from the loop when we encounter the first parameter that does not match.
//        			break;
//        		}
//        	}
//        }
//
//
//        Set result = filter(methods);
//
//        if (result.size()==0) {
//            return null;
//        } else if (result.size()==1) {
//            return (Method)result.iterator().next();
//        } else {
//            // It is possible that there isn't an implementation yet because this type is abstract, and
//            // there are multiple abstract definitions of the given method in supertypes. If all remaining
//            // methods have the same signature, we can pick any one of them.
//            final Method nc = (Method)result.iterator().next();
//            try {
//                new PrimitivePredicate() {
//                    public boolean eval(Object o) throws MetamodelException {
//                        return (o==nc) || (! nc.sameSignatureAs((Method)o));
//                    }
//                }.filter(result);
//            } catch (MetamodelException e1) {
//                throw e1;
//            } catch (Exception e1) {
//                e1.printStackTrace();
//                throw new Error();
//            }
//            if (result.size()==1) {
//                return (Method)result.iterator().next();
//            } else {
//                throw new MetamodelException("Multiple matches");
//            }
//        }
//    }

//    /**
//     * MODIFIES THE GIVEN COLLECTION !!!
//     */
//    protected Set filter(Collection methods) throws MetamodelException {
//        filterOverriddenMethods(methods);
//        return filterGeneralMethods(methods);
//    }
//
//    /**
//     * filter out methods that will not be selected
//     * because there is a second method for which there
//     * is at least 1 parameter that has a type which
//     * is a subtype of the corresponding parameter of
//     * the first method and the other arguments of the second
//     * method are assignable to the parameters of the first
//     * method.
//     */
//    protected Set filterGeneralMethods(Collection methods) throws MetamodelException {
//        Set result = new HashSet();
//        Iterator iter = methods.iterator();
//        while (iter.hasNext()) {
//            Method method = (Method)iter.next();
//            Iterator inner = methods.iterator();
//            boolean obsolete = false;
//            while (inner.hasNext()) {
//                Method other = (Method)inner.next();
//                if (other!=method) {
//                    boolean hasCompat = false;
//                    boolean hasIncompat = false;
//                    Iterator outerParams = method.getParameters().iterator();
//                    Iterator innerParams = other.getParameters().iterator();
//                    while (outerParams.hasNext()) {
//                        Type outerType = ((FormalParameter)outerParams.next()).getType();
//                        Type innerType = ((FormalParameter)innerParams.next()).getType();
//                        if (innerType.assignableTo(outerType)) {
//                            hasCompat = true;
//                        } else {
//                            hasIncompat = true;
//                        }
//                    }
//                    // If 'other' can implement 'method', then
//                    // 'method' is obsolete.
//                    if ((hasCompat && (! hasIncompat)) || other.canImplement(method)) {
//                        obsolete = true;
//                    }
//                }
//            }
//            if (! obsolete) {
//                result.add(method);
//            }
//        }
//        return result;
//    }
//
//    /**
//     *
//     */
//    protected void filterOverriddenMethods(final Collection methods) throws MetamodelException {
//        try {
////    	 ... but remove the ones that are overridden, implemented or hidden.
//            new PrimitivePredicate() {
//                public boolean eval(Object o) throws Exception {
//                    final Method superMethod = (Method)o;
//                    return ! new PrimitivePredicate() {
//                        public boolean eval(Object o2) throws MetamodelException {
//                            Method nc = (Method)o2;
//                            return (nc!=superMethod) && (nc.getName().equals(superMethod.getName())) && (nc.overrides(superMethod) || nc.canImplement(superMethod) || nc.hides(superMethod));
//                        }
//                    }.exists(methods);
//                }
//            }.filter(methods);
//        } catch (MetamodelException e1) {
//            throw e1;
//        } catch (Exception e1) {
//            throw new Error();
//        }
//    }

//    public RegularMethod getApplicableSuperRegularMethod(final String name, final List paramTypes) throws MetamodelException {
//        return (RegularMethod)getApplicableSuperMethod(name, paramTypes, RegularMethod.class);
//    }
//
//    public PrefixOperator getApplicableSuperPrefixOperator(final String name, final List paramTypes) throws MetamodelException {
//        return (PrefixOperator)getApplicableSuperMethod(name, paramTypes, PrefixOperator.class);
//    }
//
//    public InfixOperator getApplicableSuperInfixOperator(final String name, final List paramTypes) throws MetamodelException {
//        return (InfixOperator)getApplicableSuperMethod(name, paramTypes, InfixOperator.class);
//    }
//
//    public PostfixOperator getApplicableSuperPostfixOperator(final String name, final List paramTypes) throws MetamodelException {
//        return (PostfixOperator)getApplicableSuperMethod(name, paramTypes, PostfixOperator.class);
//    }
//
//    protected Method getApplicableSuperMethod(final String name, final List paramTypes, final Class type) throws MetamodelException {
//        List superTypes = getDirectSuperTypes();
//        final List methods = new ArrayList();
//        try {
//            new RobustVisitor() {
//                public Object visit(Object element) throws MetamodelException {
//                    Method method = ((Type)element).getApplicableMethod(name, paramTypes, type);
//                    if (method!=null) {
//                        methods.add(method);
//                    }
//                    return null;
//                }
//
//                public void unvisit(Object element, Object undo) {
//                }
//            }.applyTo(superTypes);
//        } catch (MetamodelException e) {
//            throw e;
//        } catch (Exception e) {
//            throw new Error();
//        }
//        //filter
//
//        Set filtered = filter(methods);
//
//        if (filtered.isEmpty()) {
//            return null;
//        } else if (filtered.size()==1) {
//            return (Method)filtered.iterator().next();
//        } else {
//            throw new MetamodelException("Cannot find super method "+name);
//        }
//    }

    /****************
     * CONSTRUCTORS *
     ****************/
//
//    /**
//     * Return the constructors of this type.
//     */
//    /*@
//    @ public behavior
//    @
//    @ post \result != null;
//    @ // The result only contains methods from this type.
//    @ post (\forall Method m; \result.contains(m); getMethods().contains(m));
//    @ // The result contains all methods of this type with the Constructor modifier.
//    @ post (\forall Method m; getMethods().contains(m);
//    @         m.is(Constructor.PROTOTYPE) ==> \result.contains(m));
//    @*/
//    public List getConstructors() {
//        List result = getMembersLink().getOtherEnds();
//        new PrimitiveTotalPredicate() {
//            public boolean eval(Object o) {
//                return (o instanceof RegularMethod) && (((RegularMethod)o).hasModifier(new Constructor()));
//            }
//        }.filter(result);
//        return result;
//    }

//    public RegularMethod getConstructor(final List paramTypes) throws MetamodelException {
//        List constructors = getConstructors();
//        // filter on name
//        // filter on number of parameters.
//        final int nbParams = paramTypes.size();
//        new PrimitiveTotalPredicate() {
//            public boolean eval(Object o) {
//                return (((RegularMethod)o).getNbParameters()==nbParams);
//            }
//        }.filter(constructors);
//        // eliminate methods to which the arguments are not applicable
//        try {
//            new PrimitivePredicate() {
//                public boolean eval(Object o) throws MetamodelException {
//                    RegularMethod constructor = (RegularMethod)o;
//                    List params = constructor.getParameters();
//                    boolean ok = true;
//                    for (int i = 0;i<nbParams;i++) {
//                        Type param = (Type)paramTypes.get(i);
//                        Type methodParamType = (Type)(((FormalParameter)params.get(i)).getType());
//                        if (methodParamType==null) {
//                            ((FormalParameter)params.get(i)).getType();
//                        }
//                        if (!param.assignableTo(methodParamType)) {
//                            ok = false;
//                        }
//                    }
//                    return ok;
//                }
//            }.filter(constructors);
//        } catch (MetamodelException e) {
//            throw e;
//        } catch (Exception e) {
//            e.printStackTrace();
//            throw new Error();
//        }
//
//        Set result = filterGeneralMethods(constructors);
//
//        if (result.size()==0) {
//            return null;
//        } else if (result.size()==1) {
//            return (RegularMethod)result.iterator().next();
//        } else {
//            throw new MetamodelException();
//        }
//    }

//    public RegularMethod getSuperConstructor(final List paramTypes) throws MetamodelException {
//        List superTypes = getDirectSuperTypes();
//        final List constructors = new ArrayList();
//        try {
//            new RobustVisitor() {
//                public Object visit(Object element) throws MetamodelException {
//                    RegularMethod constructor = ((Type)element).getConstructor(paramTypes);
//                    if (constructor!=null) {
//                        constructors.add(constructor);
//                    }
//                    return null;
//                }
//
//                public void unvisit(Object element, Object undo) {
//                }
//            }.applyTo(superTypes);
//        } catch (MetamodelException e) {
//            throw e;
//        } catch (Exception e) {
//            throw new Error();
//        }
//        if (constructors.isEmpty()) {
//            return null;
//        } else if (constructors.size()==1) {
//            return (RegularMethod)constructors.get(0);
//        } else {
//            throw new MetamodelException();
//        }
//    }


    /*@
    @ also public behavior
    @
    @ post \result == this;
    @*/
    public Type getNearestType() {
        return this;
    }


    public NamespacePart getNearestNamespacePart() {
        return getParent().getNearestNamespacePart();
    }

//    /**
//     * @param string
//     * @return
//     */
//    public VariableOrType getVariableOrType(String name) throws MetamodelException {
//        VariableOrType result;
//        result = getVariable(name);
//        if (result!=null) {
//            return result;
//        } else {
//            return getType(name);
//        }
//    }

    public abstract E clone();

//    protected abstract Type cloneThis();

   /*@
     @ also public behavior
     @
     @ post \result.containsAll(getSuperTypeReferences());
     @ post \result.containsAll(getMembers());
     @ post \result.containsAll(getModifiers());
     @*/
    public List<? extends Element> getChildren() {
        List<Element> result = new ArrayList<Element>();
        result.addAll(getSuperTypeReferences());
        result.addAll(modifiers());
        result.addAll(directlyDeclaredElements());
        return result;
    }


    /********************
     * EXCEPTION SOURCE *
     ********************/

    public CheckedExceptionList getCEL() throws MetamodelException {
        CheckedExceptionList cel = new CheckedExceptionList(getNamespace().language());
        Iterator<StaticInitializer> iter = directlyDeclaredElements(StaticInitializer.class).iterator();
        while (iter.hasNext()) {
            cel.absorb(iter.next().getCEL());
        }
        return cel;
    }

    public CheckedExceptionList getAbsCEL() throws MetamodelException {
        CheckedExceptionList cel = new CheckedExceptionList(getNamespace().language());
        Iterator<StaticInitializer> iter = directlyDeclaredElements(StaticInitializer.class).iterator();
        while (iter.hasNext()) {
            cel.absorb(iter.next().getAbsCEL());
        }
        return cel;
    }

    public Set<Declaration> declarations() throws MetamodelException {
    	Set<Declaration> result = new HashSet<Declaration>();
    	result.addAll(members());
    	return result;
    }

  	protected void copyContents(Type<? extends Type> from) {
  		for(TypeReference tref : from.getSuperTypeReferences()) {
        addSuperType(tref.clone());
  		}
      for(Modifier mod : from.modifiers()) {
      	addModifier(mod.clone());
      }
      for(TypeElement el : from.directlyDeclaredElements()) {
        add(el.clone());
      }
  	}
  
  	public Type alias(SimpleNameSignature sig) {
      return new TypeAlias(sig,this);
  	}

  	public Type union(Type type) {
  		return type.unionDoubleDispatch(type);
  	}
  	
  	protected Type unionDoubleDispatch(Type type) {
  		return new UnionType(this,type);
  	}

  	protected Type unionDoubleDispatch(UnionType type) {
  		UnionType result = type.clone();
  		result.addType(type);
  		return result;
  	}

}


