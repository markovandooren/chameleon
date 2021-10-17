package org.aikodi.chameleon.oo.language;

import org.aikodi.chameleon.core.declaration.Declaration;
import org.aikodi.chameleon.core.element.Element;
import org.aikodi.chameleon.core.language.Language;
import org.aikodi.chameleon.core.lookup.LookupException;
import org.aikodi.chameleon.core.namespace.Namespace;
import org.aikodi.chameleon.core.property.ChameleonProperty;
import org.aikodi.chameleon.core.property.DynamicChameleonProperty;
import org.aikodi.chameleon.core.property.StaticChameleonProperty;
import org.aikodi.chameleon.core.reference.CrossReference;
import org.aikodi.chameleon.oo.type.*;
import org.aikodi.chameleon.oo.type.generics.EqualityTypeArgument;
import org.aikodi.chameleon.oo.type.generics.ExtendsWildcard;
import org.aikodi.chameleon.oo.type.generics.SuperWildcard;
import org.aikodi.chameleon.oo.type.generics.TypeArgument;
import org.aikodi.chameleon.util.Util;
import org.aikodi.rejuse.association.SingleAssociation;
import org.aikodi.rejuse.predicate.Predicate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.aikodi.contract.Contract.requireNotNull;

public interface ObjectOrientedLanguage extends Language {

    TypeReference createTypeReference(String fqn);

    TypeReference createTypeReference(Type type);

    TypeReference createTypeReference(CrossReference<? extends Declaration> target, String name);

    /**
     * Create a type reference that references the intersection of the types references by a list of type references.
     * @param types The type references to intersect.
     * @return A non-null type reference that references a type that is the intersection of the types referenced
     * by the given type references.
     */
    default IntersectionTypeReference createIntersectionReference(TypeReference... types) {
        return new IntersectionTypeReference(Arrays.asList(types));
    }

    /**
     * Create a type reference that references the union of the types references by a list of type references.
     * @param types The type references of which to return the union.
     * @return A non-null type reference that references a type that is the union of the types referenced
     * by the given type references.
     */
    default UnionTypeReference createUnionReference(TypeReference... types) {
        return new UnionTypeReference(Arrays.asList(types));
    }

    <P extends Parameter> TypeInstantiation instantiatedType(Class<P> kind, List<P> parameters, Type baseType);

    TypeInstantiation createDerivedType(Type baseType, List<TypeArgument> typeArguments) throws LookupException;

    /**
     * Check whether the given type is an exception.
     */
    boolean isException(Type type) throws LookupException;

    /**
     * Check whether the given type is a checked exception.
     */
    boolean isCheckedException(Type type) throws LookupException;

    /**
     * Return the relation that determines when a member overrides another
     */
    SubtypeRelation subtypeRelation();

    Type getNullType(Namespace root);

    /**
     * Return the exception thrown by the language when an invocation is done on a 'null' or 'void' target.
     *
     * @param ns The root package in which the exception type should be found.
     */
    Type getNullInvocationException(Namespace ns) throws LookupException;

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
     * Return the fully qualified name of the class that acts as the default
     * super class.
     */
    String getDefaultSuperClassFQN();

    /**
     * Return the erasure of the given type.
     *
     * @param original The of which the erasure is requested.
     *
     * @return The result is not null and has not type parameters.
     */
    Type erasure(Type original);

    default Type getDefaultSuperClass(Namespace root) throws LookupException {
        TypeReference typeRef = createTypeReferenceInNamespace(getDefaultSuperClassFQN(),root);
        Type result = typeRef.getElement();
        return result;
    }

    /**
     * Find the type with the given fully qualified name in the given namespace.
     *
     * @param fqn
     * @param ns
     * @return
     * @throws LookupException
     * @deprecated See {@link Namespace#find(String, Class)}
     */
    default Type findType(String fqn, Namespace ns) throws LookupException {
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
    default void replace(TypeReference replacement, final Declaration declarator, TypeReference in) throws LookupException {
        Predicate<TypeReference, LookupException> predicate = type -> type.getDeclarator().sameAs(declarator);
        List<TypeReference> crefs = in.lexical().descendants(TypeReference.class,predicate);
        if(predicate.eval(in)) {
            crefs.add(in);
        }
        for(TypeReference cref: crefs) {
            TypeReference clonedReplacement = Util.clone(replacement);
            TypeReference substitute = createNonLocalTypeReference(clonedReplacement, replacement.lexical().parent());
            SingleAssociation crefParentLink = cref.parentLink();
            crefParentLink.getOtherRelation().replace(crefParentLink, substitute.parentLink());
        }
    }

    /**
     * Create a type reference that wraps the given type reference and redirects the
     * lookup to an element other than the lexical parent.
     * @param tref The type reference to wrap. Cannot be null.
     * @param lookupTarget The element to which the lookup must be directed.
     * @return A non-null type reference that has the given type reference as its direct child,
     * and that redirects the lookup to the given target.
     */
    default NonLocalTypeReference createNonLocalTypeReference(TypeReference tref, Element lookupTarget) {
        return new NonLocalTypeReference(tref, lookupTarget);
    }

    /**
     * Create a type reference that directly resolves to a type.
     *
     * @param type The type to which the type reference should resolve. Cannot be null.
     * @return A type reference that does not do any lookup but directly returns the given type.
     */
    TypeReference createDirectTypeReference(Type type);

    /**
     * Replace references to the a declarator in an expression.
     *
     * @param replacement The replacement type reference.
     * @param declarator The declarator to which the references must be replaced.
     * @param in The element in which to replace the references.
     * @param kind The type of the element.
     * @param <E> The type of the element.
     *
     * @return The given element.
     * @throws LookupException One of the references in the given element could not be resolved.
     */
    default <E extends Element> E replace(TypeReference replacement, Declaration declarator, E in, Class<E> kind) throws LookupException {
        return NonLocalTypeReference.replace(replacement, declarator, in,kind);
    }

    default ConstrainedTypeReference createConstrainedTypeReference() {
        return new ConstrainedTypeReference();
    }

    /**
     * Create a type argument that specifies that another instance of the parameterized
     * type can only be a subtype if its corresponding argument is a type that is equal to
     * the type reference by the given type reference.
     *
     * @param tref A reference to the type passed as an argument. Cannot be null.
     * @return
     */
    default EqualityTypeArgument createEqualityTypeArgument(TypeReference tref) {
        requireNotNull(tref);
        return new EqualityTypeArgument(tref);
    }

    default ExtendsWildcard createExtendsWildcard(TypeReference tref) {
        requireNotNull(tref);
        return new ExtendsWildcard(tref);
    }

    default SuperWildcard createSuperWildcard(TypeReference tref) {
        requireNotNull(tref);
        return new SuperWildcard(tref);
    }

    TypeArgument createPureWildcard();

    default TypeReference createTypeReferenceInNamespace(String fqn, Namespace namespace) {
        TypeReference typeRef = createTypeReference(fqn);
        typeRef.setUniParent(namespace);
        return typeRef;
    }

    /**
     * Return a type reference to the given type.
     *
     * @param type The type for which a type reference is requested.
     * @return A type reference that will resolve to the given type.
     */
    TypeReference reference(Type type);

    /**
     * A property that marks a method as a constructor.
     * @return The result is not null.
     */
    ChameleonProperty CONSTRUCTOR();

    ChameleonProperty EXTENSIBLE();

    DynamicChameleonProperty DEFINED();

    ChameleonProperty ABSTRACT();

    StaticChameleonProperty INSTANCE();

    ChameleonProperty CLASS();

    ChameleonProperty DESTRUCTOR();

    ChameleonProperty VALUE_TYPE();

    ChameleonProperty NATIVE();

    ChameleonProperty INTERFACE();

    ChameleonProperty FINAL();

    ChameleonProperty REFERENCE_TYPE();
}
