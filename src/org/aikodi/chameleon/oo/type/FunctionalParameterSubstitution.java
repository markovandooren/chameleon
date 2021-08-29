package org.aikodi.chameleon.oo.type;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static org.aikodi.contract.Contract.requireNotNull;
import static org.aikodi.contract.Contract.requireSaneCollection;

/**
 * A class of objects that represent a parameter substitution.
 *
 * @param <T>
 */
public class FunctionalParameterSubstitution<T extends Parameter> implements ParameterSubstitution<T> {

    /**
     * Return the type of the paramaters that are substituted.
     *
     * @return The non-null Class object of the type of parameters that are substituted.
     */
    public Class<T> parameterKind() {
        return _parameterKind;
    }

    /**
     * Create a new parameter substitution.
     *
     * @param kind       The class object of the type of the parameters. Cannot be null.
     * @param parameters The new parameters. Cannot be null or contains null or duplicates.
     */
    public FunctionalParameterSubstitution(Class<T> kind, List<T> parameters) {
        requireNotNull(kind);
        requireSaneCollection(parameters);

        _parameterKind = kind;
        _parameters = new ArrayList<T>(parameters);
    }

    /**
     * Return the list of parameters that will replace the original ones.
     *
     * @return The result is not null, and does not contain null or duplicates.
     */
    public List<T> parameters() {
        return new ArrayList<T>(_parameters);
    }

    /**
     * {@inheritDoc}
     */
    public void apply(Type type) {
        Iterator<T> parametersIterator = type.parameters(_parameterKind).iterator();
        Iterator<T> argumentsIterator = _parameters.iterator();
        while (parametersIterator.hasNext()) {
            T parameter = parametersIterator.next();
            T argument = argumentsIterator.next();
            // The next call does not change the parent of 'argument'. It is stored in InstantiatedTypeParameter
            // using a regular reference.
            type.replaceParameter(_parameterKind, parameter, argument);
        }
    }


    /**
     * The type of the parameters that are substituted.
     */
    private final Class<T> _parameterKind;

    /**
     * The parameters that will replace the original ones.
     * Not null. Does not contain null or duplicates.
     */
    private final List<T> _parameters;
}
