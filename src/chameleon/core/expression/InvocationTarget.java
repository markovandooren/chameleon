package chameleon.core.expression;

import java.util.List;
import java.util.Set;

import chameleon.core.element.Element;
import chameleon.core.language.Language;
import chameleon.core.lookup.LookupException;
import chameleon.core.lookup.LookupStrategy;
import chameleon.core.namespacepart.NamespaceElementImpl;
import chameleon.core.statement.ExceptionSource;

/**
 * @author Marko van Dooren
 */

public abstract class InvocationTarget<E extends InvocationTarget,P extends Element> 
                extends NamespaceElementImpl<E,P> 
                implements 
//                Target<E,P>, 
                Cloneable, ExceptionSource<E,P> {

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
public abstract LookupStrategy targetContext() throws LookupException;

	
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
  
//  /**
//   * @param prec
//   */
//  public abstract void setTarget(InvocationTarget prec);

  /**
   * @param target
   * @return
   */
  public abstract boolean superOf(InvocationTarget target) throws LookupException;

  public abstract boolean subOf(InvocationTarget target) throws LookupException;

  public abstract boolean compatibleWith(InvocationTarget target) throws LookupException;

  /**
   * @param expr
   */
  public abstract void prefix(InvocationTarget target) throws LookupException;

//  public abstract void substituteParameter(String name, Expression expr) throws MetamodelException;

  public abstract void prefixRecursive(InvocationTarget target) throws LookupException;

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
	public abstract Set getDirectExceptions() throws LookupException;

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
	public abstract Set getExceptions() throws LookupException;


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
