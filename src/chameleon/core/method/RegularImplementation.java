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
package chameleon.core.method;

import java.util.Collection;
import java.util.List;

import org.rejuse.association.Reference;
import org.rejuse.java.collections.Visitor;
import org.rejuse.predicate.PrimitivePredicate;

import chameleon.core.MetamodelException;
import chameleon.core.method.exception.ExceptionClause;
import chameleon.core.method.exception.ExceptionDeclaration;
import chameleon.core.method.exception.StubExceptionClauseContainer;
import chameleon.core.statement.Block;
import chameleon.core.statement.StatementContainer;
import chameleon.support.statement.TryStatement;
import chameleon.util.Util;

/**
 * @author Marko van Dooren
 */
public class RegularImplementation extends Implementation<RegularImplementation> implements StatementContainer<RegularImplementation,Method<? extends Method, ? extends MethodSignature>> {

  public RegularImplementation(Block body) {
	  setBody(body);
  }

	/**
	 * BODY
	 */
	private Reference<RegularImplementation,Block> _body = new Reference<RegularImplementation,Block>(this);

  public void setBody(Block block) {
    _body.connectTo(block.getParentLink());
  }

  public Block getBody() {
    return _body.getOtherEnd();
  }

  public RegularImplementation clone() {
    return new RegularImplementation(getBody().clone());
  }

  public boolean compatible() throws MetamodelException {
  	throw new Error("Implement exception anchors again.");
//    if(getBody() == null) {
//      return true;
//    }
//    Collection declarations = getBody().getCEL().getDeclarations();
//    final ExceptionClause exceptionClause = new ExceptionClause();
//    new Visitor() {
//      public void visit(Object element) {
//        exceptionClause.add((ExceptionDeclaration)element);
//      }
//    }.applyTo(declarations);
//    StubExceptionClauseContainer stub = new StubExceptionClauseContainer(getParent(),exceptionClause,getParent().lexicalContext());
//    return exceptionClause.compatibleWith(getParent().getExceptionClause());
  }
  
 /*@
   @ public behavior
   @
   @ post \result == (\forall TryStatement ts; getAllStatements().contains(ts);
   @                    ts.hasValidCatchClauses()); 
   @*/
  public boolean hasValidCatchClauses() throws MetamodelException {
    try {
      Collection<TryStatement> statements = getBody().getDescendants(TryStatement.class);
      return new PrimitivePredicate<TryStatement>() {
        public boolean eval(TryStatement tryStatement) throws MetamodelException {
          return tryStatement.hasValidCatchClauses();
        }
      }.forAll(statements);
    }
    catch (MetamodelException e) {
      throw e;
    }
    catch (Exception e) {
      e.printStackTrace();
      throw new Error();
    }
  }

 /*@
   @ also public behavior
   @
   @ post \result.contains(getBody());
   @ post \result.size() == 1;
   @*/
  public List getChildren() {
    return Util.createNonNullList(getBody());
  }
  
	
	
	


	
  

}
