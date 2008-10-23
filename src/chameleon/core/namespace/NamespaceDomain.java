package chameleon.core.namespace;

import chameleon.core.MetamodelException;
import chameleon.core.accessibility.AccessibilityDomain;
import chameleon.core.compilationunit.CompilationUnitDomain;

/**
 * @author marko
 */
public class NamespaceDomain extends AccessibilityDomain {
  
 /*@
   @ public behavior
   @
   @ pre pack != null;
   @
   @ post getPackage() == pack;
   @*/
  public NamespaceDomain(Namespace ns) {
    _namespace = ns;
  }
  
 /*@
   @ also public behavior
   @
   @ post \result == (other instanceof NamespaceDomain) && 
   @                 ((NamespaceDomain)other).getPackage().equals(getPackage());
   @*/
  public boolean geRecursive(AccessibilityDomain other) throws MetamodelException {
    return (
             (other instanceof NamespaceDomain) && 
             ((NamespaceDomain)other).getNamespace().equals(getNamespace())
           )
           ||
           (
             (other instanceof CompilationUnitDomain) &&
             ((CompilationUnitDomain)other).getOuterType().getNamespace().equals(getNamespace())
           );
  }
  
 /*@
   @ public behavior
   @
   @ post \result != null;
   @*/
  public Namespace getNamespace() {
    return _namespace;
  }

	private Namespace _namespace;

}
