package chameleon.core.expression;

import chameleon.core.lookup.LocalLookupStrategy;
import chameleon.core.lookup.LookupException;
import chameleon.core.namespace.NamespaceElement;
import chameleon.core.statement.ExceptionSource;

/**
 * @author Marko van Dooren
 */

public interface InvocationTarget<E extends InvocationTarget> 
                extends NamespaceElement<E>, Cloneable, ExceptionSource<E> {

  /**
   * Return the target context of this target.
   *
   * A target context is the context used to look up elements that are expressed
   * relative to a target. For example, when looking up <code>a.b</code>, 
   * first <code>a</code> is looked up in the current context. After that, 
   * <code>b</code> must be looked up in the context of the element returned by the 
   * lookup of <code>a</code>. But <code>b</code> must <b>not</b> be lookup up as 
   * if it were used in the lexical context of the class representing the type of 
   * <code>a</code>. Therefore, two contexts are provided: a lexical context and 
   * a target context.
   *
   * For example:
   *   1) in "expr.f", "f" must be looked up in the static type of "expr",
   *      and not in its lexical context, which is the current lexical context.
   *   2) in "typename.f", "f" must be looked up in the type represented by "typename"
   *   3) in "packagename.f", "f" must be looked up in the package represented by "package"
   */
public abstract LocalLookupStrategy<?> targetContext() throws LookupException;

	
  public abstract E clone();
  
//  /**
//   * @param expr
//   */
//  public abstract void prefix(InvocationTarget target) throws LookupException;
//
//  public abstract void prefixRecursive(InvocationTarget target) throws LookupException;

//	/**
//	 * Return the set of exception thrown directly this invocation target under the assumption that
//	 * the evaluation of their children does not throw any exceptions.
//	 */
//	/*@
//	 @ public behavior
//	 @
//	 @ post \result != null;
//	 @ post ! \result.contains(null);
//	 @ post (\forall Object o; \result.contains(o); o instanceof Type);
//	 @*/
//	public abstract Set getDirectExceptions() throws LookupException;

//	/**
//	 * Return the set of all exceptions thrown by the evaluation of this invocation target and all of its
//	 * children.
//	 */
//	/*@
//	 @ public behavior
//	 @
//	 @ post \result != null;
//	 @ post ! \result.contains(null);
//	 @ post (\forall Object o; \result.contains(o); o instanceof Type);
//	 @ post (\forall InvocationTarget it; getChildren().contains(it);
//	 @         \result.containsAll(it.getExceptions()));
//	 @*/
//	public abstract Set getExceptions() throws LookupException;


}
