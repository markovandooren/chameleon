/*
 * Copyright 2000-2004 the Jnome development team.
 *
 * @author Marko van Dooren
 * @author Nele Smeets
 * @author Kristof Mertens
 * @author Jan Dockx
 *
 * This file is part of Jnome.
 *
 * Jnome is free software; you can redistribute it and/or modify it under the
 * terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
 *
 * Jnome is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * Jnome; if not, write to the Free Software Foundation, Inc., 59 Temple Place,
 * Suite 330, Boston, MA 02111-1307 USA
 */
package chameleon.core.member;

import java.util.Set;

import chameleon.core.MetamodelException;
import chameleon.core.declaration.Declaration;
import chameleon.core.declaration.DeclarationContainer;
import chameleon.core.declaration.Signature;
import chameleon.core.type.TypeElement;

/**
 * A class of type elements (@see {@link #TypeElement}) that have a signature and can thus be referenced. Examples
 * are methods, variable, types, properties,...
 * 
 * @author Marko van Dooren
 * 
 * <E> The type of the element
 * <P> The type of the parent
 */
public interface Member<E extends Member<E,P,S,F>, P extends DeclarationContainer, S extends Signature, F extends Member> extends TypeElement<E,P>, Declaration<E,P,S> {
  
  
  /**
   * Set the signature of this member.
   * @param signature
   */
  public void setSignature(S signature);

  
  /**
   * Check whether this member overrides the given member.
   */
 /*@
   @ public behavior
   @
   @ post other == null ==> \result == null;
   @*/
  public boolean overrides(Member other) throws MetamodelException;
  
  /**
   * Check whether this member overrides the given member.
   */
 /*@
   @ public behavior
   @
   @ post other == null ==> \result == null;
   @*/
  public boolean hides(Member other) throws MetamodelException;
  
  /**
   * Check whether this is equivalent to given member.
   */
 /*@
   @ public behavior
   @
   @ post other == null ==> \result == null;
   @*/
  public boolean equivalentTo(Member other) throws MetamodelException;
  
  
  /**
   * Return the set of members that are overridden by this member.
   * @return
   */
 /*@
   @ public behavior
   @
   @ post \result != null;
   @ (\forall Member m; \result.contains(m); overrides(m));
   @*/
  public Set<Member> directlyOverriddenMembers() throws MetamodelException;

  // Return type Member for now, may have to introduce F(amily) type which is cut off at the level of e.g. Type,Method,MemberVariable,Property,....
  public abstract F alias(S signature);
}
