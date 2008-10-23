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
//import chameleon.core.MetamodelException;
//import chameleon.core.type.Type;
//import chameleon.core.type.TypeReference;
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
