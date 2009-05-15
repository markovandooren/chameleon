package chameleon.core.scope;

import chameleon.core.MetamodelException;

/**
 * A class of scopes. They represent the region of code where a declaration can be referenced provided that it
 * is not shadowed.
 * 
 * @author Marko van Dooren
 */
public abstract class Scope {

 /*@
   @ // The atLeastAsAccessibleAs relation is transitive.
   @ public invariant (\forall Scope first; first != null;
   @                    (\forall Scope second; second != null;
   @                      (\forall Scope third; third != null;
   @                        first.atLeastAsAccessibleAs(second) && second.atLeastAsAccessibleAs(third) ==>
   @                        first.atLeastAsAccessibleAs(third))));
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
  public final boolean greaterThanOrEqualTo(Scope other) throws MetamodelException {
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
  public final boolean ge(Scope other) throws MetamodelException {
    return greaterThanOrEqualTo(other);
  }
  
  protected abstract boolean geRecursive(Scope other) throws MetamodelException;
  
  protected boolean leRecursive(Scope other) throws MetamodelException {
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
   @         \result.atLeastAsAccessibileAs(ad) ==>
   @            atLeastAsAccessibileAs(ad) && other.atLeastAsAccessibileAs(ad));
   @*/
  public Scope intersect(Scope other) throws MetamodelException {
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
   @         atLeastAsAccessibileAs(ad) || other.atLeastAsAccessibileAs(ad) ==>
   @           \result.atLeastAsAccessibileAs(ad));
   @*/
  public Scope union(Scope other)  throws MetamodelException {
    Union result = new Union();
    result.add(this);
    result.add(other);
    return result;
  }
  
//  public boolean containsRecursive(Scope domain) {
//    return false;
//  }
  
  //public abstract boolean equals(Object o);

}
