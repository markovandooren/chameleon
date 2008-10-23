package chameleon.core.compilationunit;

import chameleon.core.MetamodelException;
import chameleon.core.accessibility.AccessibilityDomain;
import chameleon.core.type.Type;

/**
 * @author marko
 */
public class CompilationUnitDomain extends AccessibilityDomain {

 /*@
   @ public behavior
   @
   @ pre outerType != null;
   @
   @ post getOuterType() == outerType;
   @*/
  public CompilationUnitDomain(Type outerType) {
    _outerType = outerType;
  }
  
 /*@
   @ also public behavior
   @
   @ post \result == (other instanceof CompilationUnitDomain) && 
   @                 ((CompilationUnitDomain)other).getOuterType().equals(getOuterType())
   @*/
  public boolean geRecursive(AccessibilityDomain other) throws MetamodelException {
    return (other instanceof CompilationUnitDomain) && ((CompilationUnitDomain)other).getOuterType().equals(getOuterType());
  }
  
 /*@
   @ public behavior
   @
   @ post \result != null;
   @*/
  public Type getOuterType() {
    return _outerType;
  }

	/**
	 * 
	 * @uml.property name="_outerType"
	 * @uml.associationEnd 
	 * @uml.property name="_outerType" multiplicity="(1 1)"
	 */
	private Type _outerType;

}
