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
package chameleon.core.method.exception;

import java.util.List;

import org.rejuse.association.Reference;

import chameleon.core.MetamodelException;
import chameleon.core.context.LexicalContext;
import chameleon.core.element.Element;
import chameleon.core.type.StubTypeElement;
import chameleon.core.type.TypeDescendant;
import chameleon.util.Util;

/**
 * @author marko
 */
public class StubExceptionClauseContainer extends StubTypeElement {
  
    public StubExceptionClauseContainer(TypeDescendant parent, ExceptionClause exc) {
      super(parent); //FIXME this entire class should be removed, and replaced by a more flexible lookup mechanism so we won't have to duplicate items.
      _exceptionClause.connectTo(exc.getParentLink());
    }

	/**
	 * 
	 * @uml.property name="_exceptionClause"
	 * @uml.associationEnd 
	 * @uml.property name="_exceptionClause" multiplicity="(0 -1)" elementType="chameleon.core.method.exception.ExceptionClause"
	 */
	private Reference _exceptionClause = new Reference(this);

    
    public ExceptionClause getExceptionClause() {
      return (ExceptionClause)_exceptionClause.getOtherEnd();
    }

   /*@
     @ also public behavior
     @
     @ post \result.contains(getExceptionClause());
     @ post \result.size() == 1;
     @*/
    public List getChildren() {
      return Util.createNonNullList(getExceptionClause());
    }

    @Override
    public StubExceptionClauseContainer clone() {
      StubExceptionClauseContainer result;
        result = new StubExceptionClauseContainer(null,getExceptionClause().clone());
      return result;
    }

}
