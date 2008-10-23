package chameleon.core.accessibility;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.rejuse.predicate.PrimitiveTotalPredicate;

import chameleon.core.MetamodelException;

/**
 * @author marko
 */
public abstract class ComposedAccessibilityDomain extends AccessibilityDomain {

 /*@
   @ public behavior
   @
   @ getDomains().isEmpty();
   @*/
  public ComposedAccessibilityDomain() {
  }
  
 /*@
   @ public behavior
   @
   @ pre domains != null;
   @
   @ getDomains().containsAll(domains);
   @*/
  public ComposedAccessibilityDomain(Collection<AccessibilityDomain> domains) {
    _domains.addAll(domains);
  }

 /*@
   @ public behavior
   @
   @ post \result != null;
   @*/
  public Collection<AccessibilityDomain> getDomains() {
    return new ArrayList<AccessibilityDomain>(_domains);
  }

 /*@
   @ public behavior
   @
   @ pre domain != null;
   @ pre domain != this;
   @ pre ! containsRecursive(domain);
   @
   @ post getDomains().contains(domain);
   @*/
  public void add(AccessibilityDomain domain) throws MetamodelException {
    _domains.add(domain);
    filter();
  }
  
  protected abstract void filter() throws MetamodelException;

	/**
	 * 
	 * @uml.property name="_domains"
	 * @uml.associationEnd 
	 * @uml.property name="_domains" multiplicity="(0 -1)" elementType="chameleon.core.accessibility.AccessibilityDomain"
	 */
	protected List<AccessibilityDomain> _domains = new ArrayList<AccessibilityDomain>();

 /*@
   @ public behavior
   @
   @ post \result == ! _domains.contains(domain) && 
   @                 new PrimitiveTotalPredicate() {
   @                   public boolean eval(Object o) {
   @                     return ! ((AccessibilityDomain)o).containsRecursive(domain);
   @                   }
   @                 }.forAll(_domains);
   @*/
  public boolean containsRecursive(final AccessibilityDomain domain) {
    return ! _domains.contains(domain) && 
      new PrimitiveTotalPredicate() {
        public boolean eval(Object o) {
          return ! ((AccessibilityDomain)o).containsRecursive(domain);
        }
      }.forAll(_domains);
  }

}
