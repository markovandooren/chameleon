package org.aikodi.chameleon.oo.language;

import java.util.List;

import org.aikodi.chameleon.core.declaration.Declaration;
import org.aikodi.chameleon.core.element.Element;
import org.aikodi.chameleon.core.language.LanguageImpl;
import org.aikodi.chameleon.core.lookup.LookupContextFactory;
import org.aikodi.chameleon.core.lookup.LookupException;
import org.aikodi.chameleon.core.namespace.Namespace;
import org.aikodi.chameleon.core.property.ChameleonProperty;
import org.aikodi.chameleon.core.property.Defined;
import org.aikodi.chameleon.core.property.DynamicChameleonProperty;
import org.aikodi.chameleon.core.property.StaticChameleonProperty;
import org.aikodi.chameleon.core.reference.CrossReference;
import org.aikodi.chameleon.core.relation.EquivalenceRelation;
import org.aikodi.chameleon.core.relation.StrictPartialOrder;
import org.aikodi.chameleon.core.variable.Variable;
import org.aikodi.chameleon.oo.member.Member;
import org.aikodi.chameleon.oo.method.Method;
import org.aikodi.chameleon.oo.type.ConstrainedTypeReference;
import org.aikodi.chameleon.oo.type.IntersectionTypeReference;
import org.aikodi.chameleon.oo.type.Parameter;
import org.aikodi.chameleon.oo.type.Type;
import org.aikodi.chameleon.oo.type.TypeInstantiation;
import org.aikodi.chameleon.oo.type.TypeReference;
import org.aikodi.chameleon.oo.type.generics.EqualityTypeArgument;
import org.aikodi.chameleon.oo.type.generics.ExtendsWildcard;
import org.aikodi.chameleon.oo.type.generics.SuperWildcard;
import org.aikodi.chameleon.oo.type.generics.TypeArgument;
import org.aikodi.chameleon.oo.variable.VariableDeclarator;
import org.aikodi.chameleon.util.Util;

import be.kuleuven.cs.distrinet.rejuse.association.SingleAssociation;
import be.kuleuven.cs.distrinet.rejuse.junit.Revision;
import be.kuleuven.cs.distrinet.rejuse.predicate.Predicate;

/**
 * A convenience class for object-oriented programming languages.
 * It defined a number of properties that are applicable to object-oriented
 * programming languages.
 * 
 * @author Marko van Dooren
 */
public abstract class ObjectOrientedLanguage extends LanguageImpl {
	
	//TODO document the properties. This is becoming complicated without an explanation.
	public final ChameleonProperty EXTENSIBLE;
	public final DynamicChameleonProperty DEFINED;
	public final ChameleonProperty ABSTRACT;
	public final StaticChameleonProperty INSTANCE;
	public final ChameleonProperty CLASS;
	public final ChameleonProperty CONSTRUCTOR;
	public final ChameleonProperty DESTRUCTOR;
	public final ChameleonProperty REFERENCE_TYPE;
	public final ChameleonProperty VALUE_TYPE;
	public final ChameleonProperty NATIVE;
	public final ChameleonProperty INTERFACE;	
	public final ChameleonProperty FINAL;

	public ObjectOrientedLanguage(String name, Revision version) {
		this(name,null,version);
	}

	public ObjectOrientedLanguage(String name, LookupContextFactory factory, Revision version) {
		super(name, factory,version);
		// 1) Create the properties.
  	EXTENSIBLE = add(new StaticChameleonProperty("extensible", Declaration.class));
  	DEFINED = add(new Defined("defined"));
  	DEFINED.addValidElementType(Variable.class);
//  	ABSTRACT = DEFINED.inverse();
  	ABSTRACT = add(new StaticChameleonProperty("abstract", Declaration.class));
  	INSTANCE = add(new StaticChameleonProperty("instance",Declaration.class));
  	INSTANCE.addValidElementType(VariableDeclarator.class);
  	CLASS = INSTANCE.inverse();
    CLASS.setName("class");
    CONSTRUCTOR = add(new StaticChameleonProperty("constructor",Method.class));
    DESTRUCTOR = add(new StaticChameleonProperty("destructor",Method.class));
  	REFERENCE_TYPE = add(new StaticChameleonProperty("reference type", Type.class));
  	VALUE_TYPE = REFERENCE_TYPE.inverse();
  	NATIVE = add(new StaticChameleonProperty("native", Type.class));
		INTERFACE = add(new StaticChameleonProperty("interface", Type.class));
    FINAL = add(new StaticChameleonProperty("final", Declaration.class));
  	//2) Add relations between the properties.
    FINAL.addImplication(REFINABLE.inverse());
    FINAL.addImplication(DEFINED);
    EXTENSIBLE.addImplication(REFINABLE);
    NATIVE.addImplication(DEFINED);
    INTERFACE.addImplication(ABSTRACT);
    ABSTRACT.addImplication(DEFINED.inverse());
	}

  public abstract TypeReference createTypeReference(String fqn);
  
  public abstract TypeReference createTypeReference(Type type);
  
  public abstract TypeReference createTypeReference(CrossReference<? extends Declaration> target, String name);
  
  public abstract IntersectionTypeReference createIntersectionReference(TypeReference first, TypeReference second);
  
  // NEEDS_NS
  public TypeReference createTypeReferenceInNamespace(String fqn, Namespace namespace) {
	  TypeReference typeRef = createTypeReference(fqn);
	  typeRef.setUniParent(namespace);
	  return typeRef;
  }
  
  public abstract <P extends Parameter> TypeInstantiation createDerivedType(Class<P> kind, List<P> parameters, Type baseType);
  
  public abstract TypeInstantiation createDerivedType(Type baseType, List<TypeArgument> typeArguments) throws LookupException;
  
	public Type getDefaultSuperClass(Namespace root) throws LookupException {
//		Type result = _defaultSuperClass;
//		if(result == null) {
			TypeReference typeRef = createTypeReferenceInNamespace(getDefaultSuperClassFQN(),root);
			Type result = typeRef.getElement();
//			_defaultSuperClass = result;
//			if (result==null) {
//				throw new LookupException("Default super class "+getDefaultSuperClassFQN()+" not found.");
//			}
//		}
		return result;
	}
	
	public abstract EqualityTypeArgument createEqualityTypeArgument(TypeReference tref);
	
	public abstract ExtendsWildcard createExtendsWildcard(TypeReference tref);
	
	public abstract SuperWildcard createSuperWildcard(TypeReference tref);
	
//	private Type _defaultSuperClass;

	/**
	 * Return the fully qualified name of the class that acts as the default
	 * super class.
	 */
	public abstract String getDefaultSuperClassFQN();

	/**
	 * Return the exception thrown by the language when an invocation is done on a 'null' or 'void' target.
	 *
	 * @param defaultPackage The root package in which the exception type should be found.
	 */
	public abstract Type getNullInvocationException(Namespace ns) throws LookupException;

	/**
	 * Return the exception representing the top class of non-fatal runtime exceptions. This doesn't include
	 * errors.
	 *
	 * @param defaultPackage The root package in which the exception type should be found.
	 */
	public abstract Type getUncheckedException(Namespace ns) throws LookupException;

	/**
	 * Check whether the given type is an exception.
	 */
	public abstract boolean isException(Type type) throws LookupException;

	/**
	 * Check whether the given type is a checked exception.
	 */
	public abstract boolean isCheckedException(Type type) throws LookupException;

//	public abstract boolean upperBoundNotHigherThan(Type first, Type second, List<Pair<Type, TypeParameter>> trace) throws LookupException;

	public abstract Type getNullType(Namespace root);

	/**
	 * Return the relation that determines when a member overrides another
	 */
	public abstract SubtypeRelation subtypeRelation();
	
//	/**
//	 * Return the relation that determines when a member overrides another
//	 */
//	public abstract StrictPartialOrder<Member> overridesRelation();

//	/**
//	 * Return the relation that determines when a member hides another
//	 */
//	public abstract StrictPartialOrder<Member> hidesRelation();

	/**
	 * Return the relation that determines when a member implements another.
	 */
	public abstract StrictPartialOrder<Member> implementsRelation();

	public abstract Type voidType(Namespace ns) throws LookupException;

	/**
	 * Return the type that represents the boolean type in this language.
	 */
	public abstract Type booleanType(Namespace ns) throws LookupException;

	/**
	 * Return the type that represents class cast exceptions in this language.
	 */
	public abstract Type classCastException(Namespace ns) throws LookupException;

	/**
	 * Return the relation that determines when a member is equivalent to another.
	 */
	public abstract EquivalenceRelation<Member> equivalenceRelation();

	/**
	 * Find the type with the given fully qualified name in the given namespace.
	 * 
	 * @param fqn
	 * @param ns
	 * @return
	 * @throws LookupException
	 * @deprecated See {@link Namespace#find(String, Class)}
	 */
	public Type findType(String fqn, Namespace ns) throws LookupException {
		TypeReference ref = createTypeReferenceInNamespace(fqn,ns);
		return ref.getElement();
	}

	  /**
   * Replace all references in the given 'in' type reference that reference a
   * declaration whose declarator is the same as the given declarator by a clone
   * of the replacement type reference.
   * 
   * If the declarator of the 'in' type reference is the given declarator,
   * then 'in' is replaced by a clone of the given replacement.
   * 
   * @param replacement The type reference that will replace the matching
   *                    type references.
   * @param declarator The declaration for which the type references must be
   * replaced.
   * @param in The type reference within which the replacement is done.
   * @throws LookupException An exception was thrown during lookup.
   */
	public void replace(TypeReference replacement, final Declaration declarator, TypeReference in) throws LookupException {
		Predicate<TypeReference, LookupException> predicate = type -> type.getDeclarator().sameAs(declarator);
    List<TypeReference> crefs = in.descendants(TypeReference.class,predicate);
		if(predicate.eval(in)) {
			crefs.add(in);
		}
		for(TypeReference cref: crefs) {
			TypeReference clonedReplacement = Util.clone(replacement);
			TypeReference substitute = createNonLocalTypeReference(clonedReplacement, replacement.parent());
			SingleAssociation crefParentLink = cref.parentLink();
			crefParentLink.getOtherRelation().replace(crefParentLink, substitute.parentLink());
		}
	}
	
	public TypeReference createNonLocalTypeReference(TypeReference tref) {
		return createNonLocalTypeReference(tref, tref.parent());
	}

	public abstract TypeReference createNonLocalTypeReference(TypeReference tref, Element lookupParent);
	
	public abstract <E extends Element> E replace(TypeReference replacement, Declaration declarator, E in, Class<E> kind) throws LookupException;
	
	/**
	 * Return a type reference to the given type.
	 * 
	 * @param type The type for which a type reference is requested.
	 * @return A type reference that will resolve to the given type.
	 * @throws LookupException An exception was thrown during lookup.
	 */
	public abstract TypeReference reference(Type type) throws LookupException;

	public ConstrainedTypeReference createConstrainedTypeReference() {
		return new ConstrainedTypeReference();
	}

}
