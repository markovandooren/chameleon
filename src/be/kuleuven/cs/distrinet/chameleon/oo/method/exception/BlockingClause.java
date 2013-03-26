package be.kuleuven.cs.distrinet.chameleon.oo.method.exception;
public class BlockingClause{}
///*
// *
// * @author Marko van Dooren
// * @author Nele Smeets
// * @author Kristof Mertens
// * @author Jan Dockx
// *
// */
//package be.kuleuven.cs.distrinet.chameleon.core.method.exception;
//
//import java.util.List;
//
//import org.rejuse.association.ReferenceSet;
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
//public class BlockingClause extends Filter<BlockingClause> {
//  
//  public BlockingClause() {
//		
//	}
//
//  /**********************
//   * BLOCKED EXCEPTIONS *
//   **********************/
//
//  public List<TypeReference> getBlockedTypeReferences() {
//    return _blocked.getOtherEnds();
//  }
//  
//  public void addBlockedType(TypeReference type) {
//    _blocked.add(type.getParentLink());
//  }
//  
//  public void removeBlockedType(TypeReference type) {
//    _blocked.remove(type.getParentLink());
//  }
//  
//  private ReferenceSet<BlockingClause,TypeReference> _blocked = new ReferenceSet<BlockingClause,TypeReference>(this);
//  
//  public BlockingClause clone() {
//    final BlockingClause result = new BlockingClause();
//    new Visitor() {
//      public void visit(Object element) {
//        result.addBlockedType((TypeReference)((TypeReference)element).clone());
//      }
//    }.applyTo(getBlockedTypeReferences());
//    return result;
//  }
//  
// /*@
//   @ public behavior
//   @
//   @ post \result.equals(getFilteredTypeReferences());
//   @*/
//  public List getChildren() {
//    return getBlockedTypeReferences();
//  }
//  /**
//   * @param propagatingClause
//   * @return
//   */
//  public BlockingClause and(BlockingClause blockingClause) {
//	  final BlockingClause result = (BlockingClause) clone();
//	  new Visitor() {
//	  	public void visit(Object o) {
//	  		result.addBlockedType((TypeReference) ((TypeReference)o).clone());		
//	  	}
//	  }.applyTo(blockingClause.getBlockedTypeReferences());
//	  return result;
//  }
//
///* (non-Javadoc)
// * @see chameleon.core.method.exception.Filter#isFiltered(chameleon.core.type.Type)
// */
//public boolean isFiltered(final Type type) throws MetamodelException {
//	try {
//		return ! new PrimitivePredicate() {
//		public boolean eval(Object o) throws MetamodelException {
//		  try {
//		    return type.assignableTo(((TypeReference)o).getType());
//		  }
//		  catch (ClassCastException e) {
//		    e.printStackTrace();
//		    throw e;
//		  }
//		}
//  }.exists(getBlockedTypeReferences());
//	} catch (MetamodelException e) {
//		throw e;
//	} catch (Exception e) {
//		throw new Error();
//	}
//}
//}
