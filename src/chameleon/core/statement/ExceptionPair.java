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

import chameleon.core.method.exception.ExceptionDeclaration;
import chameleon.core.type.Type;

/**
 * A class of pairs (checked exception type, exception declaration). These are the elements of a
 * checked exception list(CEL).
 * 
 * @author Marko van Dooren
 */
public class ExceptionPair {
  
	 /*@
	   @ public behavior
	   @
	   @ post getException() == exception;
	   @ post getDeclaration() == declaration;
	   @*/
	  public ExceptionPair(Type exception, ExceptionDeclaration declaration, ExceptionSource cause) {
	    _exception = exception;
	    _declaration = declaration;
	    _cause = cause;
	  }

	  private ExceptionSource _cause;
	  
	  public ExceptionSource getCause() {
	    return _cause;
	  }

	  /**
	   * Return the type of exception of this pair.
	   */
	 /*@
	   @ public behavior
	   @
	   @ post \result != null
	   @*/
	  public Type getException() {
	    return _exception;
	  }
	  
	  private Type _exception;
	  
	  private ExceptionDeclaration _declaration;
	  
	 /*@
	   @ public behavior
	   @
	   @ post \result != null
	   @*/
	  public ExceptionDeclaration getDeclaration() {
	    return _declaration;
	  }
}
