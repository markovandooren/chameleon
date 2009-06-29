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

import chameleon.core.MetamodelException;
import chameleon.core.context.LookupException;
import chameleon.core.statement.Block;
import chameleon.core.type.Type;
import chameleon.core.type.TypeDescendantImpl;

/**
 * @author Marko van Dooren
 */

public abstract class Implementation<E extends Implementation> extends TypeDescendantImpl<E,Method<? extends Method, ? extends MethodHeader,? extends MethodSignature>> {

  public Implementation() {
    }

  public final Type getNearestType() {
    return parent().getNearestType();
  }


	//  public abstract Set getAllStatements();

  /**
   * @return
   */
  public abstract E clone();

  /**
   * Check if the implementation exception clause of the method body 
   * conforms to the exception clause of the parent method.
   * @return
   */
  public abstract boolean compatible() throws LookupException;

  /**
   * Check whether or not all catch blocks in the implementation are useful. If a catch block
   * catches a checked exception that can never be thrown in the try statement, that catch block
   * is useless and should be removed.
   */
  public boolean hasValidCatchClauses() throws LookupException {
    return true;
  }

  /**
   * Return the body of this implementation.
   */
	public abstract Block getBody();


}
