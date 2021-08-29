package org.aikodi.chameleon.oo.type;

/**
 * A class of parameter substitutions that can be applied to a type.
 *
 * @param <T> The kind of the parameters.
 */
public interface ParameterSubstitution<T extends Parameter> {

    /**
     * Apply the substitution to the given type.
     *
     * @param type The type to which to apply the substitution.
     *             Cannot be null. Must have parameters whose names match
     *             the names of the parameters in the substitution
     */
    void apply(Type type);
}

