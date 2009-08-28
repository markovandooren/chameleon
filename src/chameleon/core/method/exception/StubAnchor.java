package chameleon.core.method.exception;
public class StubAnchor{}
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
//import java.util.Set;
//
//import org.rejuse.association.Reference;
//
//import chameleon.core.MetamodelException;
//import chameleon.core.context.Context;
//import chameleon.core.type.StubTypeElement;
//import chameleon.util.Util;
//
///**
// * @author marko
// */
//public class StubAnchor extends StubTypeElement implements AnchoredDeclaration {
//  
//  public StubAnchor(AnchoredDeclaration parent, FilterClause filter) {
//    super(parent); //FIXME: remove class, and replace with more flexible lookup mechanism such that we don't need to duplicate objects.
//    _filter.connectTo(filter.getParentLink());
//  }
//  
//  private Reference _filter = new Reference(this);
//  
//  public FilterClause getFilterClause() {
//    return (FilterClause)_filter.getOtherEnd();
//  }
//  
// /*@
//   @ also public behavior
//   @
//   @ post getExpression() != null ==> \result.contains(getExpression());
//   @ post \result.size() == 1;
//   @*/
//  public List getChildren() {
//    return Util.createNonNullList(getFilterClause());
//  }
//
//  /* (non-Javadoc)
//   * @see chameleon.core.method.exception.AnchoredDeclaration#getRawExceptionTypes()
//   */
//  public Set getRawExceptionTypes() throws MetamodelException {
//  	return ((AnchoredDeclaration)getParent()).getRawExceptionTypes();
//  }
//
//  /* (non-Javadoc)
//   * @see chameleon.core.method.exception.AnchoredDeclaration#getFilterClauseLink()
//   */
//  public Reference getFilterClauseLink() {
//	  return _filter;
//  }
//
//  public StubAnchor clone() {
//    StubAnchor result;
//      result = new StubAnchor(null, getFilterClause().clone());
//    return result;
//  }
//}
