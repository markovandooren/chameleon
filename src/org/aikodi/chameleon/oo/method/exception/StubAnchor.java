package org.aikodi.chameleon.oo.method.exception;
public class StubAnchor{}
///*
// * @author Marko van Dooren
// * @author Nele Smeets
// * @author Kristof Mertens
// * @author Jan Dockx
// */
//package be.kuleuven.cs.distrinet.chameleon.core.method.exception;
//
//import java.util.List;
//import java.util.Set;
//
//import org.rejuse.association.Reference;
//
//import be.kuleuven.cs.distrinet.chameleon.core.MetamodelException;
//import be.kuleuven.cs.distrinet.chameleon.core.context.Context;
//import be.kuleuven.cs.distrinet.chameleon.core.type.StubTypeElement;
//import be.kuleuven.cs.distrinet.chameleon.util.Util;
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
