package be.kuleuven.cs.distrinet.chameleon.oo.member;

import be.kuleuven.cs.distrinet.chameleon.core.declaration.Declaration;
import be.kuleuven.cs.distrinet.chameleon.core.lookup.LookupException;

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
public class DeclarationComparator<D extends Declaration> {

	private final Class<D> _declarationClass;

	public DeclarationComparator(Class<D> kind) {
		_declarationClass = kind;
	}

	public Class<D> selectedClass() {
		return _declarationClass;
	}

	public boolean contains(Member first, Member second) throws LookupException {
		return selectedClass().isInstance(first) && 
				   selectedClass().isInstance(second) && 
		       containsBasedOnRest((D) first, (D) second);
	}

	public boolean containsBasedOnRest(D first, D second) throws LookupException {
		return first.name().equals(second.name());
	}

//	public final boolean containsBasedOnName(String first, String second) throws LookupException {
//		return first.equals(second);
//	}
	
}
