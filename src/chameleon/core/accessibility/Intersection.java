package chameleon.core.accessibility;

import java.util.Collection;

import org.rejuse.predicate.PrimitivePredicate;

import chameleon.core.MetamodelException;

/**
 * @author marko
 */
public class Intersection extends ComposedAccessibilityDomain {
  
 /*@
   @ public behavior
   @
   @ getDomains().isEmpty();
   @*/
  public Intersection() {
  }
  
 /*@
   @ public behavior
   @
   @ pre domains != null;
   @
   @ getDomains().containsAll(domains);
   @*/
  public Intersection(Collection<AccessibilityDomain> domains) {
    super(domains);
  }

  public boolean geRecursive(final AccessibilityDomain other) throws MetamodelException {
    try {
      return new PrimitivePredicate() {
        public boolean eval(Object o) throws MetamodelException {
          return ((AccessibilityDomain)o).atLeastAsAccessibleAs(other);
        }
      }.forAll(getDomains());
    }
    catch (MetamodelException e) {
      throw e;
    }
    catch (Exception e) {
      e.printStackTrace();
      throw new Error();
    }
  }

  protected boolean leRecursive(final AccessibilityDomain other) throws MetamodelException {
    try {
      return new PrimitivePredicate() {
        public boolean eval(Object o) throws MetamodelException {
          return other.atLeastAsAccessibleAs(((AccessibilityDomain)o));
        }
      }.exists(getDomains());
    }
    catch (MetamodelException e) {
      throw e;
    }
    catch (Exception e) {
      e.printStackTrace();
      throw new Error();
    }
  }

  public AccessibilityDomain intersect(final AccessibilityDomain other) throws MetamodelException {
    Intersection result = new Intersection(getDomains());
    result.add(other);
    return result;
  }
  
  protected void filter() throws MetamodelException {
    try {
      new PrimitivePredicate() {
        public boolean eval(Object o) throws Exception {
          final AccessibilityDomain acc = (AccessibilityDomain)o;
          return ! new PrimitivePredicate() {
            public boolean eval(Object o2) throws MetamodelException {
              AccessibilityDomain other = (AccessibilityDomain)o2;
              return (acc != other) && (acc.atLeastAsAccessibleAs(other));
            }
          }.exists(_domains);
        }
      }.filter(_domains);
    }
    catch (MetamodelException e) {
      throw e;
    }
    catch (Exception e) {
      e.printStackTrace();
      throw new Error();
    }
  }
  
}
