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
package chameleon.core.expression;

import org.rejuse.association.Reference;


/**
 * @author Marko van Dooren
 */
public abstract class BinaryExpression extends Expression<BinaryExpression> implements ExpressionContainer<BinaryExpression,ExpressionContainer> {
  
  public BinaryExpression(Expression first, Expression second) {
    setFirst(first);
    setSecond(second);
  }

	/**
	 * FIRST
	 */
  
	private Reference<BinaryExpression,Expression> _first = new Reference<BinaryExpression,Expression>(this);

  /**
   * Return the first expression
   */
  public Expression<? extends Expression> getFirst() {
    return _first.getOtherEnd();
  }

  /**
   * Set the first expression
   */
 /*@
   @ public behavior
   @
   @ post getFirst().equals(first); 
   @*/
  public void setFirst(Expression expression) {
    _first.connectTo(expression.getParentLink());
  }

	/**
	 * SECOND
	 */
	private Reference<BinaryExpression,Expression> _second = new Reference<BinaryExpression,Expression>(this);

  
  /**
   * Return the second expression
   */
  public Expression<? extends Expression> getSecond() {
    return _second.getOtherEnd();
  }
  
  /**
   * Set the second expression
   */
 /*@
   @ public behavior
   @
   @ post getSecond().equals(second); 
   @*/
  public void setSecond(Expression expression) {
    _second.connectTo(expression.getParentLink());
  }
  
}
