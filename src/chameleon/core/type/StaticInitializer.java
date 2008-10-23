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
package chameleon.core.type;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.rejuse.association.Reference;

import chameleon.core.MetamodelException;
import chameleon.core.accessibility.AccessibilityDomain;
import chameleon.core.context.LexicalContext;
import chameleon.core.element.Element;
import chameleon.core.member.Member;
import chameleon.core.statement.Block;
import chameleon.core.statement.CheckedExceptionList;
import chameleon.core.statement.ExceptionSource;
import chameleon.core.statement.StatementContainer;
import chameleon.support.property.accessibility.EmptyDomain;
import chameleon.util.Util;

/**
 * @author Marko van Dooren
 */
public class StaticInitializer extends TypeElementImpl<StaticInitializer,Type> implements StatementContainer<StaticInitializer,Type>, ExceptionSource<StaticInitializer,Type> {

  public StaticInitializer(Block block) {
      setBlock(block);
  }

  public Type getNearestType() {
    return getType();
  }

  public Type getType() {
  	return getParent();
  }

  /*********
   * BLOCK *
   *********/

  public Reference getBlockLink() {
    return _blockLink;
  }

  public Block getBlock() {
    return (Block)_blockLink.getOtherEnd();
  }


  private Reference<StaticInitializer,Block> _blockLink = new Reference<StaticInitializer,Block>(this);

  public void setBlock(Block block) {
    _blockLink.connectTo(block.getParentLink());
  }

  /**
   * @return
   */
  public StaticInitializer clone() {
    return new StaticInitializer(getBlock().clone());
  }

 /*@
   @ also public behavior
   @
   @ post getBlock() != null ==> \result.contains(getBlock());
   @ post \result.size() == 1;
   @*/
  public List getChildren() {
    return Util.createNonNullList(getBlock());
  }

 /*@
   @ also public behavior
   @
   @ post \result == getBlock().getCEL();
   @*/
  public CheckedExceptionList getCEL() throws MetamodelException {
    return getBlock().getCEL();
  }

 /*@
   @ also public behavior
   @
   @ post \result == getBlock().getAbsCEL();
   @*/
  public CheckedExceptionList getAbsCEL() throws MetamodelException {
    return getBlock().getAbsCEL();
  }

 /*@
   @ also public behavior
   @
   @ // A block does not introduce any methods.
   @ post \result.isEmpty();
   @*/
  public List getIntroducedMethods() {
	  return new ArrayList();
  }

 /*@
   @ also public behavior
   @
   @ // A block does not introduce any variables.
   @ post \result.isEmpty();
   @*/
  public Set getIntroducedVariables() {
	  return new HashSet();
  }

  public List getIntroducedIntroducingMembers(){
	return new ArrayList();
}

 /*@
   @ post \result instanceof EmptyDomain;
   @*/
  public AccessibilityDomain getAccessibilityDomain() throws MetamodelException {
	  return new EmptyDomain();
  }

  /**
   * A static initializer does not add members to a type.
   */
  public Set<Member> getIntroducedMembers() {
    return new HashSet<Member>();
  }





}
