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

import java.util.List;

import org.rejuse.association.Reference;

import chameleon.core.element.Element;
import chameleon.core.type.Type;
import chameleon.util.Util;

/**
 * @author Marko van Dooren
 */
public abstract class InvocationTargetWithTarget<E extends InvocationTargetWithTarget> extends InvocationTarget<E,InvocationTargetContainer> {
  
  public InvocationTargetWithTarget() {
  }
  
  public Type getNearestType() {
    return getParent().getNearestType();
  }

  

	/**
	 * TARGET
	 */
	private Reference<InvocationTarget,InvocationTarget> _target = new Reference<InvocationTarget,InvocationTarget>(this);

  public InvocationTarget<? extends InvocationTarget, ? extends InvocationTargetContainer> getTarget() {
    return _target.getOtherEnd();
  }

  public void setTarget(InvocationTarget target) {
    if (target != null) {
      _target.connectTo(target.getParentLink());
    }
    else {
      _target.connectTo(null);
    }
  }

  public List<Element> getChildren() {
  	return Util.createNonNullList(getTarget());
  }
}