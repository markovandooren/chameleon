package be.kuleuven.cs.distrinet.chameleon.oo.method.exception;
public class PropagatingClause{}
///*
// * @author Marko van Dooren
// * @author Nele Smeets
// * @author Kristof Mertens
// * @author Jan Dockx
// *
// */
//package be.kuleuven.cs.distrinet.chameleon.core.method.exception;
//
//import java.util.Collection;
//import java.util.HashSet;
//import java.util.List;
//import java.util.Set;
//
//import org.rejuse.association.ReferenceSet;
//import org.rejuse.java.collections.RobustVisitor;
//import org.rejuse.java.collections.Visitor;
//import org.rejuse.predicate.PrimitivePredicate;
//
//import be.kuleuven.cs.distrinet.chameleon.core.MetamodelException;
//import be.kuleuven.cs.distrinet.chameleon.core.type.Type;
//import be.kuleuven.cs.distrinet.chameleon.core.type.TypeReference;
//
///**
// * @author marko
// */
//public class PropagatingClause extends Filter<PropagatingClause> {
//  
//  public PropagatingClause() {
//	}
//  
//  /*************************
//   * PROPAGATED EXCEPTIONS *
//   *************************/
//
//  public List<TypeReference> getPropagatedTypeReferences() {
//    return _propagated.getOtherEnds();
//  }
//  
//  public void addPropagatedType(TypeReference type) {
//    _propagated.add(type.getParentLink());
//  }
//  
//  public void removePropagatedType(TypeReference type) {
//    _propagated.remove(type.getParentLink());
//  }
//  
//  private ReferenceSet<PropagatingClause,TypeReference> _propagated = new ReferenceSet<PropagatingClause,TypeReference>(this);
//  
//  public Set getPropagatedTypes() throws MetamodelException {
//    Collection supers = getPropagatedTypeReferences();
//    try {
//      final Set result = new HashSet();
//      new RobustVisitor() {
//        public Object visit(Object element) throws MetamodelException {
//          Type type = ((TypeReference) element).getType();
//          if(type == null) {
//            throw new MetamodelException(); 
//          }
//          else {
//            result.add(type);
//          }
//          return null;
//        }
//        public void unvisit(Object element, Object undo) {
//          //NOP
//        }
//      }.applyTo(supers);
//      return result;
//    }
//    catch (MetamodelException e) {
//      throw e;
//    }
//    catch (Exception e) {
//      e.printStackTrace();
//      throw new Error();
//    }
//  }
//
//  public PropagatingClause clone() {
//	  //XXX bij alle cloneclauses: moeten daar geen kopies van elementparent gegeven worden?
//    final PropagatingClause result = new PropagatingClause();
//    new Visitor() {
//      public void visit(Object element) {
//        result.addPropagatedType((TypeReference)((TypeReference)element).clone());
//      }
//    }.applyTo(getPropagatedTypeReferences());
//    return result;
//  }
//  
///*@
//  @ public behavior
//  @
//  @ post \result.equals(getPropagatedTypeReferences());
//  @*/
// public List getChildren() {
//   return getPropagatedTypeReferences();
// }
//
//  /**
//   * @param propagatingClause
//   * @return
//   */
//  public PropagatingClause and(PropagatingClause propagatingClause) {
//	  final PropagatingClause result = (PropagatingClause) clone();
//	  new Visitor() {
//	  	public void visit(Object o) {
//	  		result.addPropagatedType((TypeReference) ((TypeReference)o).clone());		
//	  	}
//	  }.applyTo(propagatingClause.getPropagatedTypeReferences());
//	  return result;
//  }
//
//  public boolean isFiltered(final Type type) throws MetamodelException {
//	  try {
//	  	return getPropagatedTypeReferences().isEmpty() || new PrimitivePredicate() {
//	    	public boolean eval(Object o) throws MetamodelException {
//	  	    try {
//	  	      return type.assignableTo(((TypeReference)o).getType());
//	  	    }
//	  	    catch (ClassCastException e) {
//	  	      e.printStackTrace();
//	  	      throw e;
//	  	    }
//	  	  }
//      }.exists(getPropagatedTypeReferences());
//	  } catch (MetamodelException e) {
//		  throw e;
//  	} catch (Exception e) {
//  		e.printStackTrace();
//  		throw new Error();
//	  }
//  }
//
//
//
//
//
//
//}
