package org.aikodi.chameleon.oo.type;

import java.util.ArrayList;
import java.util.List;

import static org.aikodi.contract.Contract.requireNotNull;
import static org.aikodi.contract.Contract.requireSaneCollection;

/**
 * A class of objects that represent a parameter substitution.
 * @param <T>
 */
public class ParameterSubstitution<T extends Parameter> {

	/**
	 * Return the type of the paramaters that are substituted.
	 *
	 * @return The non-null Class object of the type of parameters that are substituted.
	 */
	public Class<T> parameterKind() {
		return _type;
	}

	/**
	 * Create a new parameter substitution.
	 *
	 * @param kind The class object of the type of the parameters. Cannot be null.
	 * @param parameters The new parameters. Cannot be null or contains null or duplicates.
	 */
	public ParameterSubstitution(Class<T> kind, List<T> parameters) {
		requireNotNull(kind);
		requireSaneCollection(parameters);

		_type = kind;
		_parameters = new ArrayList<T>(parameters);
	}

	/**
	 * The type of the parameters that are substituted.
	 */
	private Class<T> _type;

	/**
	 * The parameters that will replace the original ones.
	 * Not null. Does not contain null or duplicates.
	 */
	private final List<T> _parameters;

	/**
	 * Return the list of parameters that will replace the original ones.
	 *
	 * @return The result is not null, and does not contain null or duplicates.
	 */
	public List<T> parameters() {
		return new ArrayList<T>(_parameters);
	}
}
