package chameleon.core.accessibility;

import chameleon.core.MetamodelException;

/**
 * A class of accessibility domains. They represent the region of code where a certain
 * feature is accessible.
 * 
 * @author Marko van Dooren
 */
public abstract class AccessibilityDomain {

 /*@
   @ // The atLeastAsAccessibleAs relation is transitive.
   @ public invariant (\forall AccessibilityDomain first; first != null;
   @                    (\forall AccessibilityDomain second; second != null;
   @                      (\forall AccessibilityDomain third; third != null;
   @                        first.atLeastAsAccessibleAs(second) && second.atLeastAsAccessibleAs(third) ==>
   @                        first.atLeastAsAccessibleAs(third))));
   @*/
  
  /**
   * Check whether or not this accessibility domain is at least as accessible
   * as another one.
   * 
   * @param other
   *        The other accessibility domain
   */
 /*@
   @ public behavior
   @
   @ pre other != null;
   @*/
  public final boolean atLeastAsAccessibleAs(AccessibilityDomain other) throws MetamodelException {
    return geRecursive(other) || other.leRecursive(this);
  }
  
  protected abstract boolean geRecursive(AccessibilityDomain other) throws MetamodelException;
  
  protected boolean leRecursive(AccessibilityDomain other) throws MetamodelException {
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
   @ post (\forall AccessibilityDomain ad; ad != null;
   @         \result.atLeastAsAccessibileAs(ad) ==>
   @            atLeastAsAccessibileAs(ad) && other.atLeastAsAccessibileAs(ad));
   @*/
  public AccessibilityDomain intersect(AccessibilityDomain other) throws MetamodelException {
    Intersection result = new Intersection();
    result.add(this);
    result.add(other);
    return result;
  }
  
  /**
   * Return a new accessibility domain representing the union of this accessibility domain
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
   @ // The accessibility domain cannot decrease in size.
   @ post (\forall AccessibilityDomain ad; ad != null;
   @         atLeastAsAccessibileAs(ad) || other.atLeastAsAccessibileAs(ad) ==>
   @           \result.atLeastAsAccessibileAs(ad));
   @*/
  public AccessibilityDomain union(AccessibilityDomain other)  throws MetamodelException {
    Union result = new Union();
    result.add(this);
    result.add(other);
    return result;
  }
  
  public boolean containsRecursive(AccessibilityDomain domain) {
    return false;
  }
  
  //public abstract boolean equals(Object o);

}
