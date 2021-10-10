package org.aikodi.chameleon.oo.type.inheritance;

import org.aikodi.chameleon.core.declaration.Declaration;
import org.aikodi.chameleon.core.declaration.DeclarationContainerRelation;
import org.aikodi.chameleon.core.element.Element;
import org.aikodi.chameleon.core.lookup.DeclarationSelector;
import org.aikodi.chameleon.core.lookup.LookupException;
import org.aikodi.chameleon.core.lookup.SelectionResult;
import org.aikodi.chameleon.oo.type.Type;
import org.aikodi.chameleon.oo.type.TypeReference;

import java.util.List;

/**
 * A general interface for inheritance relations. We use the dictionary definition of 'inheritance'. If
 * A inherits from B, then A may receive 'something' from B. That 'something' can be a type, a definition, or
 * any other property. Examples are extension, inclusion, specialization, ...
 * 
 * @author Marko van Dooren
 */
public interface InheritanceRelation extends Element, DeclarationContainerRelation {
	
	public Declaration target() throws LookupException;
	
	public TypeReference superClassReference();
	
	public <X extends Declaration> List<X> accumulateInheritedMembers(final Class<X> kind, List<X> current) throws LookupException;
	
	public <X extends Declaration> List<SelectionResult<X>> accumulateInheritedMembers(DeclarationSelector<X> selector, List<SelectionResult<X>> current) throws LookupException;
	
//	public <D extends Declaration> List<D> membersDirectlyOverriddenBy(MemberRelationSelector<D> selector) throws LookupException;
//	
//	public <D extends Declaration> List<D> membersDirectlyAliasedBy(MemberRelationSelector<D> selector) throws LookupException;
	
	/**
	 * Return the inherited type, if this relation also introduces a subtype relation.
	 */
 /*@
   @ public behavior
   @
   @ post \result == null || \result == superClass();
   @*/
	public Type superType() throws LookupException;

}
