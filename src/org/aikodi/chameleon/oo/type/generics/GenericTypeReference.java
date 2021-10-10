package org.aikodi.chameleon.oo.type.generics;

import com.google.common.collect.ImmutableSet;
import org.aikodi.chameleon.core.declaration.Declaration;
import org.aikodi.chameleon.core.lookup.LookupException;
import org.aikodi.chameleon.core.namespace.Namespace;
import org.aikodi.chameleon.core.reference.CrossReferenceTarget;
import org.aikodi.chameleon.core.reference.MultiTypeReference;
import org.aikodi.chameleon.oo.expression.NamedTarget;
import org.aikodi.chameleon.oo.language.ObjectOrientedLanguage;
import org.aikodi.chameleon.oo.type.BasicTypeReference;
import org.aikodi.chameleon.oo.type.RegularType;
import org.aikodi.chameleon.oo.type.Type;
import org.aikodi.chameleon.oo.type.TypeReference;

import java.lang.ref.SoftReference;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * A type reference that supports type arguments
 */
public abstract class GenericTypeReference extends BasicTypeReference implements TypeReference {

    public GenericTypeReference(String fqn) {
        super(fqn);
    }

    public GenericTypeReference(CrossReferenceTarget target, String name) {
        super(target, name);
    }

    protected static Set<? extends Class<? extends Declaration>> set(Set<? extends Class<? extends Declaration>> classes) {
        Set<Class<? extends Declaration>> result = new HashSet<>(classes);
        result.addAll(_typeReferenceTargetTypes);
        return result;
    }

    public static CrossReferenceTarget typeReferenceTarget(String fqn) {
        return typeReferenceTarget(fqn, _typeReferenceTargetTypes);
    }
    public static CrossReferenceTarget typeReferenceTarget(String fqn, Set<? extends Class<? extends Declaration>> classes) {
        return fqn == null ? null : new MultiTypeReference<Declaration>(fqn, classes,Declaration.class,Declaration.class);
    }

    protected static CrossReferenceTarget typeReferenceTarget(NamedTarget target, Set<? extends Class<? extends Declaration>> classes) {
        if(target == null) {
            return null;
        } else {
            CrossReferenceTarget t = target.getTarget();
            if(t == null) {
                return new MultiTypeReference<Declaration>(target.name(), classes,Declaration.class,Declaration.class);
            } else {
                return new MultiTypeReference<Declaration>(typeReferenceTarget((NamedTarget) t,classes),target.name(), classes ,Declaration.class);
            }
        }
    }

    protected static Set<Class<? extends Declaration>> typeReferenceTargetTypes() {
        return _typeReferenceTargetTypes;
    }

    private final static Set<Class<? extends Declaration>> _typeReferenceTargetTypes = ImmutableSet.<Class<? extends Declaration>>builder().add(Type.class).add(Namespace.class).build();

    @Override
    public Type getElement() throws LookupException {
        Type result = getGenericCache();
        if(result != null) {
            return result;
        }
        synchronized(this) {
            if(result != null) {
                return result;
            }

            result = typeConstructor();

            //First cast result to Type, then back to X.
            //Because the selector is the connected selector of this Java type reference,
            //we know that result is a Type.
            // FILL IN GENERIC PARAMETERS
            result = convertGenerics((Type)result);

//      if(result != null) {
            setGenericCache((Type)result);
            return result;
//      } else {
//        throw new LookupException("Result of type reference lookup is null: "+name(),this);
//      }
        }
    }

    /**
     * Return the type constructor.
     *
     * @return The result of looking up the type constructor. That is the element
     * referenced by this type reference, but without considering the type arguments.
     *
     * @throws LookupException The type constructor could not be found.
     */
    protected Type typeConstructor() throws LookupException {
        return super.getElement();
    }

    protected Type convertGenerics(Type type) throws LookupException {
        Type result = type;
//		if (type != null) {
        if(canBeGenericType(type)) {
            ObjectOrientedLanguage language = language(ObjectOrientedLanguage.class);

            //Does not work because there is no distinction yet between a diamond empty list and a non-diamond empty list.
//				if(type.nbTypeParameters(TypeParameter.class) > 0) {

            if (hasTypeArguments()) {
                result = language.createDerivedType(type, typeArguments());

                // This is going to give trouble if there is a special lexical context
                // selection for 'type' in its parent.
                // set to the type itself? seems dangerous as well.
                result.setUniParent(type.lexical().parent());
            } else if(type instanceof RegularType){
                // create raw type if necessary. The erasure method will check that.
                result = language.erasure(type);
            }
        }
//		}
        return result;
    }

    protected abstract boolean canBeGenericType(Type type);

    /**
     * Return the type argument of this type reference.
     *
     * @return A non-null list that contains all type arguments
     * of this type reference in order, and no other elements.
     */
    public abstract List<TypeArgument> typeArguments() throws LookupException;

    /**
     * Check if this reference actually has type argument.
     * @return True if this type reference has type arguments. False otherwise.
     */
    public abstract boolean hasTypeArguments() throws LookupException;

    /**
     * A cache reference to the referenced type.
     */
    private SoftReference<Type> _genericCache;

    @Override
    public synchronized void flushLocalCache() {
        super.flushLocalCache();
        _genericCache = null;
    }

    /**
     * Return the cached type, if any.
     *
     * @return If a type was cached, then that type is returned.
     * Otherwise, the result is null.
     */
    protected synchronized Type getGenericCache() {
        return (_genericCache == null ? null : _genericCache.get());
    }

    /**
     * Set the generic cache to the given type.
     *
     * @param value The type to be cached.
     */
    protected synchronized void setGenericCache(Type value) {
        _genericCache = new SoftReference<Type>(value);
    }

}
