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


import chameleon.core.MetamodelException;
import chameleon.core.lookup.LookupException;
import chameleon.core.method.Method;
import chameleon.core.type.Type;

/**
 * This class represents invocations that do not invoke a constructor, and thus have a name.
 * 
 * @author Marko van Dooren
 */
public abstract class NonConstructorInvocation<E extends NonConstructorInvocation, D extends Method> extends Invocation<E,D> {

  /**
   * @param target
   */
  public NonConstructorInvocation(InvocationTarget target) {
    super(target);
  }


  public Type actualType() throws LookupException {
	    try {
				Method method = getMethod();
				if (method != null) {
				  return method.returnType();
				}
				else {
				  getMethod();
				  throw new LookupException("Could not find method of constructor invocation", this);
				}
			} catch (LookupException e) {
				// TODO Auto-generated catch block
//				e.printStackTrace();
//				getMethod();
				throw e;
			}
	  }


  /**
   * @param inv
   * @param map
   * @return
   */
  public boolean superOf(InvocationTarget inv) throws LookupException {
  	throw new Error("Implement exception anchors again");
//    if(! (inv instanceof NonConstructorInvocation)) {
//      return false;
//    }
//    NonConstructorInvocation nc = (NonConstructorInvocation)inv;
//    Method method = nc.getMethod();
//    boolean result = method.canImplement(getMethod()) &&
//                    (
//                      ((getTarget() == null) && (nc.getTarget() == null)) ||
//                      (((getTarget() != null) && (nc.getTarget() != null)) && (getTarget().compatibleWith(nc.getTarget())))
//                    );
//    List params = getActualParameters();
//    List otherParams = nc.getActualParameters();
//    for(int i=0; i< params.size(); i++) {
//      result = result && ((InvocationTarget)params.get(i)).compatibleWith((InvocationTarget)otherParams.get(i));
//    }
//    return result;
  }
  
  public void prefix(InvocationTarget target) throws LookupException {
    if(getTarget() == null) {
      setTarget(target);
    }
    else {
      getTarget().prefixRecursive(target);
    }
  }
  
}
