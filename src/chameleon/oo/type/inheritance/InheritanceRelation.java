package chameleon.oo.type.inheritance;

import java.util.List;

import chameleon.core.declaration.Declaration;
import chameleon.core.declaration.Signature;
import chameleon.core.element.Element;
import chameleon.core.lookup.DeclarationSelector;
import chameleon.core.lookup.LookupException;
import chameleon.core.lookup.TwoPhaseDeclarationSelector;
import chameleon.core.member.Member;

/**
 * A general interface for inheritance relations. We use the dictionary definition of 'inheritance'. If
 * A inherits from B, then A may receive 'something' from B. That 'something' can be a type, a definition, or
 * any other property. Examples are extension, inclusion, specialization, ...
 * @author Marko van Dooren
 *
 * @param <E>
 * @param <M>
 * @param <S> The type of the super element from which things are inherited. 
 */
public interface InheritanceRelation<E extends InheritanceRelation<E,S>, S extends Declaration> extends Element<E,Element> {
	
	public E clone();

	public S superElement() throws LookupException;
	
	public <X extends Member> void accumulateInheritedMembers(final Class<X> kind, List<X> current) throws LookupException;
	
	public <X extends Member> void accumulateInheritedMembers(DeclarationSelector<X> selector, List<X> current) throws LookupException;
	
	public <D extends Member> List<D> membersOverriddenBy(TwoPhaseDeclarationSelector<D> selector) throws LookupException;
	
//	public <X>
	
//	public <X extends Element> List<X> overriddenDeclarations() throws LookupException;
}
