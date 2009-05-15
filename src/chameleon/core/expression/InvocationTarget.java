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
import java.util.Set;

import chameleon.core.MetamodelException;
import chameleon.core.context.Target;
import chameleon.core.language.Language;
import chameleon.core.scope.Scope;
import chameleon.core.statement.ExceptionSource;
import chameleon.core.type.TypeDescendantImpl;

/**
 * @author Marko van Dooren
 */

public abstract class InvocationTarget<E extends InvocationTarget,P extends InvocationTargetContainer> 
                extends TypeDescendantImpl<E,P> 
                implements Target<E,P>, Cloneable, ExceptionSource<E,P> {

  public abstract E clone();
  
 /*@
   @ public behavior
   @
   @ post getSpecificContext() == null;
   @*/
  public InvocationTarget() {
  }

// /*@
//   @ public behavior
//   @
//   @ post getSpecificContext() == ctx;
//   @*/
//  public InvocationTarget(StaticContext ctx) {
//    setSpecificContext(ctx);
//  }
//
//  /**
//   * Return the context used to lookup types, methods, variables,...
//   */
// /*@
//   @ public behavior
//   @
//   @ post (getSpecificContext() == null) ==> \result == getParent().getContext();
//   @ post (getSpecificContext() != null) ==> \result == getSpecificContext();
//   @*/
//  public Context getContext(Element el) throws MetamodelException {
//		Context ctx = getSpecificContext();
//		if (ctx == null) {
//			ctx = getParent().getContext(el);
//		}
//		return ctx;
//	}
//
//	private Context _specificContext;
//
//
//  /**
//   * Return the specific context (usually null). This context can be set
//   * on cloned nodes in order to adapt the lookup mechanism for code analysis. This
//   * functionality will disappear when lookup is moved completely into context objects.
//   */
//  public Context getSpecificContext() {
//    return _specificContext;
//  }
//
//  /**
//   * Set the specific context. <b>Only do this on cloned elements</b>.
//   * @param context
//   *        The new specific context of this invocation target.
//   */
//  public void setSpecificContext(Context context) {
//    _specificContext = context;
//  }
  
  /**
   * @param prec
   */
  public abstract void setTarget(InvocationTarget prec);

  /**
   * @param target
   * @return
   */
  public abstract boolean superOf(InvocationTarget target) throws MetamodelException;

  public abstract boolean subOf(InvocationTarget target) throws MetamodelException;

  public abstract boolean compatibleWith(InvocationTarget target) throws MetamodelException;

  /**
   * @param expr
   */
  public abstract void prefix(InvocationTarget target) throws MetamodelException;

  public abstract void substituteParameter(String name, Expression expr) throws MetamodelException;

  public abstract void prefixRecursive(InvocationTarget target) throws MetamodelException;

	/**
	 * Return the set of exception thrown directly this invocation target under the assumption that
	 * the evaluation of their children does not throw any exceptions.
	 *
	 * @uml.property name="directExceptions"
	 */
	/*@
	 @ public behavior
	 @
	 @ post \result != null;
	 @ post ! \result.contains(null);
	 @ post (\forall Object o; \result.contains(o); o instanceof Type);
	 @*/
	public abstract Set getDirectExceptions() throws MetamodelException;

	/**
	 * Return the set of all exceptions thrown by the evaluation of this invocation target and all of its
	 * children.
	 */
	/*@
	 @ public behavior
	 @
	 @ post \result != null;
	 @ post ! \result.contains(null);
	 @ post (\forall Object o; \result.contains(o); o instanceof Type);
	 @ post (\forall InvocationTarget it; getChildren().contains(it);
	 @         \result.containsAll(it.getExceptions()));
	 @*/
	public abstract Set getExceptions() throws MetamodelException;


  /**
   * Return the programming language in which this invocation target is written.
   */
 /*@
   @ public behavior
   @
   @ post \result == getPackage().getLanguage();
   @*/
  public Language language() {
    return getNamespace().language();
  }

	/**
	 * Return the direct children of this invocation target.
	 */
	/*@
	 @ post \result != null;
	 @ post ! \result.contains(null);
	 @ post (\forall Object o; \result.contains(o); o instanceof InvocationTarget);
	 @*/
	public abstract List children();

//	/**
//	 * Return the accessibility domain for all elements of this expression. In short, it means
//	 * that at all places in the resulting accessibility domain, an expression with the same meaning as this one
//	 * can be written.
//	 */
//	public abstract AccessibilityDomain getAccessibilityDomain()
//		throws MetamodelException;

}
