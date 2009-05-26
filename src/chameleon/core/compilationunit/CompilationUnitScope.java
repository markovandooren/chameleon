//package chameleon.core.compilationunit;
//
//import chameleon.core.MetamodelException;
//import chameleon.core.scope.Scope;
//import chameleon.core.type.Type;
//
///**
// * @author Marko van Dooren
// */
//public class CompilationUnitScope extends Scope {
//
// /*@
//   @ public behavior
//   @
//   @ pre outerType != null;
//   @
//   @ post getOuterType() == outerType;
//   @*/
//  public CompilationUnitScope(Type outerType) {
//    _outerType = outerType;
//  }
//  
// /*@
//   @ also public behavior
//   @
//   @ post \result == (other instanceof CompilationUnitDomain) && 
//   @                 ((CompilationUnitDomain)other).getOuterType().equals(getOuterType())
//   @*/
//  protected boolean geRecursive(Scope other) throws MetamodelException {
//    return (other instanceof CompilationUnitScope) && ((CompilationUnitScope)other).getOuterType().equals(getOuterType());
//  }
//  
// /*@
//   @ public behavior
//   @
//   @ post \result != null;
//   @*/
//  public Type getOuterType() {
//    return _outerType;
//  }
//
//	/**
//	 * 
//	 * @uml.property name="_outerType"
//	 * @uml.associationEnd 
//	 * @uml.property name="_outerType" multiplicity="(1 1)"
//	 */
//	private Type _outerType;
//
//}
