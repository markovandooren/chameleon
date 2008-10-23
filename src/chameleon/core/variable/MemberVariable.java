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
package chameleon.core.variable;


import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.rejuse.predicate.PrimitivePredicate;

import chameleon.core.MetamodelException;
import chameleon.core.expression.Expression;
import chameleon.core.member.Member;
import chameleon.core.relation.StrictPartialOrder;
import chameleon.core.type.Type;
import chameleon.core.type.TypeContainer;
import chameleon.core.type.TypeDescendant;
import chameleon.core.type.TypeReference;
import chameleon.util.Util;

/**
 * @author Marko van Dooren
 */
public class MemberVariable extends InitializableVariable<MemberVariable,Type> implements Member<MemberVariable,Type>{
  
  
  /**
   * @param name
   */
  public MemberVariable(VariableSignature sig, TypeReference type) {
    super(sig, type, null);
  }
  
  /**
   * @param name
   */
  public MemberVariable(VariableSignature sig, TypeReference type, Expression initCode) {
    super(sig, type, initCode);
  }

  
  
  /**********
   * ACCESS *
   **********/

//  public AccessModifier getAccessModifier() {
//    List mods = modifiers();
//    new TypePredicate(AccessModifier.class).filter(mods);
//    if(mods.size() != 1) {
//      throw new Error("Multiple access modifiers for a member variable");
//    }
//    return (AccessModifier)mods.iterator().next();
//  }

  protected boolean surroundingTypeAncestorOfOneOfTheSurroundingTypesOf(TypeDescendant other) throws MetamodelException {
    TypeContainer type = other.getNearestType();
    boolean found = true;
    while(type instanceof Type && (!found)) {
      if(((Type)type).assignableTo(getParent())) {
        found = true; 
      }
      type = ((Type)type).getParent(); 
    }
    return found;
  }

  protected MemberVariable cloneThis() {
    Expression expr = null;
    if(getInitialization() != null) {
      expr = getInitialization().clone();
    }
    return new MemberVariable(signature().clone(), (TypeReference)getTypeReference().clone(), expr);
  }

//  public AccessibilityDomain getAccessibilityDomain() throws MetamodelException {
//    return getAccessModifier().getAccessibilityDomain(getParent());
//  }
  
	
	  
  public Set<Member> getIntroducedMembers() {
    return Util.createSingletonSet(this);
  }

  public Set<Member> directlyOverriddenMembers() throws MetamodelException {
    List<Type> superTypes = getNearestType().getDirectSuperTypes();
    // Collect the overridden members in the following set.
    final Set<Member> result = new HashSet<Member>();
    // Iterate over all super types.
    for(Type type: superTypes) {
      // Fetch all members from the current super type.
      Collection superMembers = type.members(Member.class);
      // Retain only those members that are overridden by this member. 
      try {
        new PrimitivePredicate<Member>() {
          public boolean eval(Member o) throws MetamodelException {
            return overrides(o);
          }
        }.filter(superMembers);
      } catch(MetamodelException e) {
        throw e; 
      } catch (Exception e) {
        e.printStackTrace();
        throw new Error();
      }
      result.addAll(superMembers);
    }
    return result;
  }

  public boolean overrides(Member other) throws MetamodelException {
    StrictPartialOrder<Member> overridesRelation = language().overridesRelation();
    return overridesRelation.contains(this, other);
  }

  public boolean hides(Member other) throws MetamodelException {
    StrictPartialOrder<Member> hidesRelation = language().hidesRelation();
    return hidesRelation.contains(this, other);
  }

}
