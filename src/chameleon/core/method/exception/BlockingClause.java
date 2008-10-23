///*
// * Copyright 2000-2004 the Jnome development team.
// *
// * @author Marko van Dooren
// * @author Nele Smeets
// * @author Kristof Mertens
// * @author Jan Dockx
// *
// * This file is part of Jnome.
// *
// * Jnome is free software; you can redistribute it and/or modify it under the
// * terms of the GNU General Public License as published by the Free Software
// * Foundation; either version 2 of the License, or (at your option) any later
// * version.
// *
// * Jnome is distributed in the hope that it will be useful, but WITHOUT ANY
// * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
// * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
// * details.
// *
// * You should have received a copy of the GNU General Public License along with
// * Jnome; if not, write to the Free Software Foundation, Inc., 59 Temple Place,
// * Suite 330, Boston, MA 02111-1307 USA
// */
//package chameleon.core.method.exception;
//
//import java.util.List;
//
//import org.rejuse.association.ReferenceSet;
//import org.rejuse.java.collections.Visitor;
//import org.rejuse.predicate.PrimitivePredicate;
//
//import chameleon.core.MetamodelException;
//import chameleon.core.type.Type;
//import chameleon.core.type.TypeReference;
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
