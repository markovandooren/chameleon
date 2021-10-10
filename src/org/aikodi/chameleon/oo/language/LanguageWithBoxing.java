package org.aikodi.chameleon.oo.language;

import org.aikodi.chameleon.core.language.Language;
import org.aikodi.chameleon.core.lookup.LookupException;
import org.aikodi.chameleon.core.property.ChameleonProperty;
import org.aikodi.chameleon.oo.type.Type;

/**
 * A language that support boxing of primitive types.
 */
public interface LanguageWithBoxing extends Language {
    //SLOW move to JavaView? Or will that be reverted anyway with multiview project
    //     which should allow the base library to be loaded only once?
    default Type box(Type type) throws LookupException {
        if (isBoxable(type)) {
            return boxedType(type);
        } else {
            return type;
        }
    }

    /**
     * Return the boxed type of the given boxable type.
     * @param boxableType The type to be boxed.
     * @return The result is not null, and different from the given type.
     * @throws LookupException A lookup exception occurred when trying to
     * compute the boxed type.
     */
    Type boxedType(Type boxableType) throws LookupException;

    /**
     * Check if the type can actually be boxed.
     *
     * @param type The type to be verified.
     * @return True if the type can be boxed to another type. False otherwise.
     * @throws LookupException A lookup exception occurred while trying
     * to determine if the type can be boxed.
     */
    boolean isBoxable(Type type) throws LookupException;

    /**
     * Return the unboxed type corresponding to the given type.
     * @param unboxableType The type to unbox. Must be unboxable.
     * @return The unboxed version of the given type.
     * @throws LookupException
     */
    Type unbox(Type unboxableType) throws LookupException;

    boolean isUnboxable(Type type);

    /**
     * A property that marks a type as a reference type.
     * @return The inverse of reference type.
     */
    ChameleonProperty REFERENCE_TYPE();

    /**
     * A property that marks a type as primitive.
     * @return Not null.
     */
    ChameleonProperty PRIMITIVE_TYPE();
}
