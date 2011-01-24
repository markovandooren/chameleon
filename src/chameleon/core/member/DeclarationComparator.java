package chameleon.core.member;

import chameleon.core.declaration.Declaration;
import chameleon.core.lookup.LookupException;

/**
 * A class for comparing declarations according to form certain relation. Examples
 * include overrides and hides relations. Note that a DeclarationComparator does not have the
 * responsibility to encode the entire relation. A DeclarationComparator only checks if a
 * pair of potential candidates is in the relation. It does not determine the set of candidates.
 * The latter is the responsibility of the involved DeclarationContainers (such as types) and the relations between
 * them (such as inheritance relations).
 * 
 * Typically a declaration class will have a private static member field to store the object, and
 * a public (non-static) getter to obtain it. This still allows dynamic binding while avoiding the
 * creation of too many comparator objects. This is not the ideal OO solution, but in this case
 * the disadvantage is not too big, whereas the gain is speed is.
 * 
 * @author Marko van Dooren
 *
 * @param <D>
 */
public abstract class DeclarationComparator<D extends Declaration<?, ?, ?, ?>> {

	private final Class<D> _declarationClass;

	public DeclarationComparator(Class<D> kind) {
		_declarationClass = kind;
	}

	public Class<D> selectedClass() {
		return _declarationClass;
	}

	public boolean contains(D first, D second) throws LookupException {
		return selectedClass().isInstance(second) && containsBasedOnName(first, second) &&
		       containsBasedOnRest(first, second);
	}

	public abstract boolean containsBasedOnRest(D first, D second) throws LookupException;

	public abstract boolean containsBasedOnName(D first, D second) throws LookupException;

}