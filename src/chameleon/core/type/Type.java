/*
 * Copyright 2000-2004 the Jnome development team.
 *
 * @author Marko van Dooren
 * @author Nele Smeets
 * @author Kristof Mertens
 * @author Jan Dockx
 *
 * This file is part of Jnome.
 *
 * Jnome is free software; you can redistribute it and/or modify it under the
 * terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
 *
 * Jnome is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * Jnome; if not, write to the Free Software Foundation, Inc., 59 Temple Place,
 * Suite 330, Boston, MA 02111-1307 USA
 */
package chameleon.core.type;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.rejuse.association.OrderedReferenceSet;
import org.rejuse.association.Relation;
import org.rejuse.java.collections.TypeFilter;
import org.rejuse.java.collections.Visitor;
import org.rejuse.logic.ternary.Ternary;
import org.rejuse.predicate.PrimitivePredicate;
import org.rejuse.predicate.TypePredicate;

import chameleon.core.MetamodelException;
import chameleon.core.context.Context;
import chameleon.core.context.TargetContext;
import chameleon.core.declaration.Declaration;
import chameleon.core.declaration.DeclarationContainer;
import chameleon.core.declaration.Definition;
import chameleon.core.declaration.TargetDeclaration;
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
public class Type extends MemberImpl<Type,TypeContainer,TypeSignature> 
                implements TargetDeclaration<Type,TypeContainer>, NamespaceOrType<Type,TypeContainer>, 
                           TypeContainer<Type,TypeContainer>, VariableContainer<Type,TypeContainer>, 
                           VariableOrType<Type,TypeContainer>, Definition<Type,TypeContainer>,
                           StatementContainer<Type,TypeContainer>, 
                           Cloneable, ExceptionSource<Type,TypeContainer>, ModifierContainer<Type,TypeContainer>, 
                           Member<Type,TypeContainer>, DeclarationContainer<Type,TypeContainer> {
 
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
    public Type(TypeSignature sig) {
        //  TODO getMemberContext() spec not complete
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

//    public void addTypeListener(TypeListener typeListener) {
//        typeListeners.add(typeListener);
//    }

//    private void publishNameChangedEvent() {
//        final Type type = this;
//
//        new Visitor() {
//            public void visit(Object object) {
//                ((TypeListener)object).onTypeNameChangedEvent(type);
//            }
//        }.applyTo(typeListeners);
//    }

//    ArrayList typeListeners = new ArrayList();

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

//    public AccessibilityDomain getTypeAccessibilityDomain() throws MetamodelException {
//        return getParent().getTypeAccessibilityDomain().intersect(getAccessModifier().getAccessibilityDomain(this));
//    }

    public Type getTopLevelType() {
        // FIXME: BAD design !!!
        if (getParent() instanceof Type) {
            return ((Type)getParent()).getTopLevelType();
        } else {
            return this;
        }
    }


  	/**
  	 * NESTED TYPES
  	 */

  	public Relation getTypesLink() {
  		return _elements;
  	}

//  	public void addType(Type type) {
//  		_members.add(type.getParentLink());
//  	}
//
//  	public void removeType(Type type) {
//  		_members.remove(type.getParentLink());
//  	}

//  	public List getNestedTypes() {
//  		List result = _members.getOtherEnds();
//  		new TypePredicate(Type.class).filter(result);
//  		return result;
//  	}
  	
//  	protected void addAllNestedTypes(Set<Type> acc) throws MetamodelException {
//  		List<Type> superTypes = getDirectSuperTypes();
//  		for(Type type: superTypes) {
//  			type.addAllNestedTypes(acc);
//  		}
//  		final List<Type> nested = getNestedTypes();
//      new PrimitiveTotalPredicate() {
//        public boolean eval(Object o) {
//            final String name = ((Type)o).getName();
//            return ! new PrimitiveTotalPredicate() {
//                public boolean eval(Object o2) {
//                    return ((Type)o2).getName().equals(name);
//                }
//            }.exists(nested);
//        }
//    }.filter(acc);
//    acc.addAll(nested);
//  		
//  	}

//    public Set<Type> getAllNestedTypes() throws MetamodelException {
//    	Set<Type> result = new HashSet<Type>();
//    	addAllNestedTypes(result);
//    	return result;
////        final List result = getNestedTypes();
////        //Add nested types from supertypes, ...
////        final List supers = new ArrayList();
////        try {
////            new RobustVisitor() {
////                public Object visit(Object element) throws MetamodelException {
////                    supers.addAll(((Type)element).getAllNestedTypes());
////                    return null;
////                }
////
////                public void unvisit(Object element, Object undo) {
////                }
////            }.applyTo(getDirectSuperTypes());
////        } catch (MetamodelException e) {
////            throw e;
////        } catch (Exception e) {
////            throw new ChameleonProgrammerException(e);
////        }
////        // ... but remove the ones that are overwritten.
////
//////    try {
//////      new PrimitivePredicate() {
//////        public boolean eval(final Object o1) throws Exception {
//////          return ! new PrimitivePredicate() {
//////            public boolean eval(Object o2) throws Exception {
//////              Type t1 = (Type)o1;
//////              Type t2 = (Type)o2;
//////              return (o1 != o2) && t1.getName().equals(t2.getName()) && ((Type)t2.getParent()).subTypeOf((Type)t1.getParent());
//////            }
//////          }.exists(result);
//////        }
//////      }.filter(result);
//////    }
//////    catch (MetamodelException e) {
//////      throw e;
//////    }
//////    catch (Exception e) {
//////      throw new Error();
//////    }
////        new PrimitiveTotalPredicate() {
////            public boolean eval(Object o) {
////                final String name = ((Type)o).getName();
////                return ! new PrimitiveTotalPredicate() {
////                    public boolean eval(Object o2) {
////                        return ((Type)o2).getName().equals(name);
////                    }
////                }.exists(result);
////            }
////        }.filter(supers);
////
////        result.addAll(supers);
////        return result;
//    }

    public Type getType() {
        return this;
    }

//    public Type getType(final String name) throws MetamodelException {
//        return getTypeLocalContext().getType(name);
//    }
//
//    public Type findTypeLocally(String name) throws MetamodelException {
//        if ((name==null) || name.equals("")) {
//            return this;
//        }
//        Type result = getType(Util.getFirstPart(name));
//        if ((result!=null) && (Util.getSecondPart(name)!=null)) {
//            result = result.findTypeLocally(Util.getSecondPart(name));
//        }
//        return result;
//    }

  	/***********
  	 * MEMBERS *
  	 ***********/

  	public OrderedReferenceSet<Type, TypeElement> getMembersLink() {
  		return _elements;
  	}

  	private OrderedReferenceSet<Type, TypeElement> _elements = new OrderedReferenceSet<Type, TypeElement>(this);

  	public void add(TypeElement element) {
  	  if(element != null) {
  	    _elements.add(element.getParentLink());
  	  }
  	}
//    /**
//     * Search for a member with the given name
//     * A member can be a Variable or an IntroducingMember
//     * Methods are not handled here.
//     */
//    public Member getMember(String name) throws MetamodelException {
//        return getTypeLocalContext().getMember(name);
//    }

//    /**
//     * ******************
//     * IntroducingMember *
//     * *******************
//     */
//
//    public List getAllIntroducingMembers() throws MetamodelException {
//        final List result = getIntroducedIntroducingMembers();
//
//        //Add nested types from supertypes, ...
//        final Set supers = new HashSet();
//        try {
//            new RobustVisitor() {
//                public Object visit(Object element) throws MetamodelException {
//
//                    supers.addAll(((Type)element).getAllIntroducingMembers());
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
//        // ... but remove the ones that are overwritten.
//        new PrimitiveTotalPredicate() {
//            public boolean eval(Object o) {
//                final String name = ((IntroducingMember)o).getName();
//                return ! new PrimitiveTotalPredicate() {
//                    public boolean eval(Object o2) {
//                        return ((IntroducingMember)o2).getName().equals(name);
//                    }
//                }.exists(result);
//            }
//        }.filter(supers);
//
//        result.addAll(supers);
//        return result;
//    }


//  	/**
//  	 * VARIABLES
//  	 */
//  	public void addVariable(MemberVariable v) {
//  		_members.add(v.getParentLink());
//  	}
//
//  	public OrderedReferenceSet getVariablesLink() {
//  		return _members;
//  	}
//
//    public List getAllVariables() throws MetamodelException {
//        final List result = getVariables();
//
//        //Add nested types from supertypes, ...
//        final Set supers = new HashSet();
//        try {
//            new RobustVisitor() {
//                public Object visit(Object element) throws MetamodelException {
//
//                    supers.addAll(((Type)element).getAllVariables());
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
//        // ... but remove the ones that are overwritten.
//        new PrimitiveTotalPredicate() {
//            public boolean eval(Object o) {
//                final String name = ((MemberVariable)o).getName();
//                return ! new PrimitiveTotalPredicate() {
//                    public boolean eval(Object o2) {
//                        return ((MemberVariable)o2).getName().equals(name);
//                    }
//                }.exists(result);
//            }
//        }.filter(supers);
//
//        result.addAll(supers);
//        return result;
//    }
//
//    public MemberVariable getVariable(final String name) throws MetamodelException {
//        return this.getTypeLocalContext().getVariable(name);
//    }

    /*********************
     * IntroducingMember *
     *********************/

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

    public boolean assignableTo(Type other) throws MetamodelException {
    	boolean equal = other.equals(this);
    	boolean subtype = subTypeOf(other);
    	return (equal || subtype);
    }

  	/**
  	 * SUPERTYPES
  	 */
  	public List<TypeReference> getSuperTypeReferences() {
  		return _superTypes.getOtherEnds();
  	}

  	public void addSuperType(TypeReference type) {
  		_superTypes.add(type.getParentLink());
  	}

  	public void removeSuperType(TypeReference type) {
  		_superTypes.remove(type.getParentLink());
  	}

  	private OrderedReferenceSet<Type, TypeReference> _superTypes = new OrderedReferenceSet<Type, TypeReference>(this);

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
    public Set<TypeElement> directlyDeclaredElements() {
       Set<TypeElement> result = new HashSet<TypeElement>();
       for(TypeElement m: _elements.getOtherEnds()) {
         result.addAll(m.getIntroducedMembers());
       }
       return result;
    }

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

    public Type clone() {
//  TODO: simply clone all the members. JDK15 (returntype of clone cannot be restricted for now).
        final Type result = cloneThis();
        // SUPERTYPES
        new Visitor() {
            public void visit(Object element) {
                result.addSuperType((TypeReference)((TypeReference)element).clone());
            }
        }.applyTo(getSuperTypeReferences());
        //MODIFIERS

        new Visitor<Modifier>() {
            public void visit(Modifier element) {
                result.addModifier(element.clone());
            }
        }.applyTo(modifiers());

        //MEMBERS

        new Visitor<TypeElement>() {
            public void visit(TypeElement element) {
                result.add(element.clone());
            }
        }.applyTo(directlyDeclaredElements()); // what the heck? does not compile without the cast

        return result;
    }

    protected Type cloneThis() {
      return this;
    }

//    public AccessibilityDomain getAccessibilityDomain() throws MetamodelException {
//        return getTypeAccessibilityDomain();
//    }

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

//   	/*****************
//  	 * LOCAL CONTEXT *
//  	 *****************/
//
//  	//FIXME: why create separate objects when they can be shared
//  	//       use a factory if necessary so that each language can decide
//  	//       whether to share them or not.
//  	private Reference _typeLocalContext = new Reference(this);
//
//  	public Reference getTypeLocalContextLink() {
//  		return _typeLocalContext;
//  	}
//
//  	public TypeLocalContext getTypeLocalContext() {
//  		return (TypeLocalContext) _typeLocalContext.getOtherEnd();
//  	}
//
//  	public void setTypeLocalContext(TypeLocalContext typeLocalContext) {
//  		if (typeLocalContext != null) {
//  			_typeLocalContext.connectTo(typeLocalContext.getTypeLink());
//  		}
//  	}
    
    public Set<Declaration> declarations() throws MetamodelException {
    	Set<Declaration> result = new HashSet<Declaration>();
    	result.addAll(members());
    	return result;
    }

}


