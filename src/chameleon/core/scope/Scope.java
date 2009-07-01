package chameleon.core.scope;

import chameleon.core.MetamodelException;
import chameleon.core.element.Element;
import chameleon.core.lookup.LookupException;

/**
 * A class of scopes. They represent the region of code where a declaration can be referenced provided that it
 * is not shadowed.
 * 
 * @author Marko van Dooren
 */
public abstract class Scope {

	/**
	 * Check if the given element is in this scope.
	 * @throws LookupException 
	 */
 /*@
   @ public behavior
   @
   @ pre element != null;
   @*/
  public abstract boolean contains(Element element) throws LookupException;
	
 /*@
   @ // The greaterThanOrEqualTo relation is transitive.
   @ public invariant (\forall Scope first; first != null;
   @                    (\forall Scope second; second != null;
   @                      (\forall Scope third; third != null;
   @                        first.greaterThanOrEqualTo(second) && second.greaterThanOrEqualTo(third) ==>
   @                        first.greaterThanOrEqualTo(third))));
   @*/
  
  /**
   * Check whether or not this scope is greater than another scope.
   * 
   * @param other
   *        The other scope
   */
 /*@
   @ public behavior
   @
   @ pre other != null;
   @*/
  public final boolean greaterThanOrEqualTo(Scope other) throws LookupException {
    return geRecursive(other) || other.leRecursive(this);
  }
  
  /**
   * Shortcut method for greaterThanOrEqualTo.
   */
 /*@
   @ public behavior
   @
   @ pre other != null;
   @
   @ post \result == greaterThanOrEqualTo(other);
   @*/
  public final boolean ge(Scope other) throws LookupException {
    return greaterThanOrEqualTo(other);
  }
  
  protected abstract boolean geRecursive(Scope other) throws LookupException;
  
  protected boolean leRecursive(Scope other) throws LookupException {
    return other.geRecursive(this);
  }
  
  /**
   * Return a new accessibility domain representing the intersection of this accessibility domain
   * and the given other accessibility domain.
   * 
   * @param other
   *        The other accessibility domain.        
   */
 /*@
   @ public behavior
   @
   @ pre other != null;
   @ pre other != this;
   @
   @ post \result != null;
   @ post \fresh(\result);
   @ // The accessibility domain cannot increase in size.
   @ post (\forall Scope ad; ad != null;
   @         \result.greaterThanOrEqualTo(ad) ==>
   @            greaterThanOrEqualTo(ad) && other.greaterThanOrEqualTo(ad));
   @*/
  public Scope intersect(Scope other) throws LookupException {
    Intersection result = new Intersection();
    result.add(this);
    result.add(other);
    return result;
  }
  
  /**
   * Return a new scope representing the union of this scope
   * and the given other scope.
   * 
   * @param other
   *        The other scope.        
   */
 /*@
   @ public behavior
   @
   @ pre other != null;
   @ pre other != this;
   @
   @ post \result != null;
   @ post \fresh(\result);
   @ // The scope cannot decrease in size.
   @ post (\forall Scope ad; ad != null;
   @         greaterThanOrEqualTo(ad) || other.greaterThanOrEqualTo(ad) ==>
   @           \result.greaterThanOrEqualTo(ad));
   @*/
  public Scope union(Scope other) throws LookupException {
    return new Union(this,other);
  }
    
}
