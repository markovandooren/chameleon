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
package chameleon.core.statement;

import java.util.List;

import org.rejuse.association.OrderedReferenceSet;
import org.rejuse.java.collections.Visitor;

import chameleon.core.element.Element;

/**
 * @author Marko van Dooren
 */
public class Block extends Statement<Block> implements StatementContainer<Block,StatementContainer>, StatementListContainer<Block,StatementContainer> {
	//TODO: should this be a member, or should there be a separate ObjectInitializer that contains a block ?
	//TODO: can any statement be a member ? In this case the methods of Member have to move up.

	/**
	 * STATEMENTS
	 */
	private OrderedReferenceSet<Block,Statement> _statements = new OrderedReferenceSet<Block,Statement>(this);

  public OrderedReferenceSet<Block,Statement> getStatementsLink() {
    return _statements;
  }

  public void addStatement(Statement statement) {
    _statements.add(statement.getParentLink());
  }

  public void removeStatement(Statement statement) {
    _statements.add(statement.getParentLink());
  }

  public List<Statement> getStatements() {
    return _statements.getOtherEnds();
  }

  public Block clone() {
    final Block result = new Block();
    new Visitor<Statement>() {
      public void visit(Statement element) {
        result.addStatement(element.clone());
      }
    }.applyTo(getStatements());
    return result;
  }

  public List<? extends Element> getChildren() {
    return getStatements();
  }

  public int getIndexOf(Statement statement) {
    return getStatements().indexOf(statement) + 1;
  }
  
//	/*@
//	  @ post \result instanceof EmptyDomain;
//	  @*/
//	 public AccessibilityDomain getAccessibilityDomain() throws MetamodelException {
//		  return new EmptyDomain();
//	 }
}
