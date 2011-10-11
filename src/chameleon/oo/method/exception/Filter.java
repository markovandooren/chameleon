package chameleon.oo.method.exception;
public class Filter{}
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
//import java.util.Set;
//
//import org.rejuse.predicate.PrimitivePredicate;
//
//import chameleon.core.MetamodelException;
//import chameleon.core.context.Context;
//import chameleon.core.element.Element;
//import chameleon.core.namespace.Namespace;
//import chameleon.core.namespacepart.NamespacePartElementImpl;
//import chameleon.core.reference.ReferenceContainer;
//import chameleon.core.type.Type;
//import chameleon.core.type.TypeReference;
//
///**
// * @author marko
// */
//public abstract class Filter<E extends Filter> extends NamespacePartElementImpl<E,FilterClause> implements ReferenceContainer<E,FilterClause> {
//
//  public Filter() {
//	}
//  
//  public Type getNearestType() {
//  	return getParent().getNearestType();
//  }
//
//
//  public Namespace getNamespace() {
//    return getParent().getNamespace();
//  }
//
//  public Collection getPropagatedTypes(Set set) throws MetamodelException {
//  	Set result = new HashSet(set);
//  	filterTypeReferences(result);
//    return result;
//  }
//
//  public void filterTypes(Collection toFilter) throws MetamodelException {
//  	try {
//  		new PrimitivePredicate() {
//  			public boolean eval(Object o) throws MetamodelException {
//  				return isFiltered((Type)o);
//  			}
//  		}.filter(toFilter);
//  	} catch (MetamodelException e) {
//  		throw e;
//  	} catch (Exception e) {
//  		e.printStackTrace();
//  		throw new Error();
//  	}
//  }
//  public void filterTypeReferences(Collection toFilter) throws MetamodelException {
//  	try {
//  		new PrimitivePredicate() {
//  			public boolean eval(Object o) throws MetamodelException {
//  				return isFiltered((TypeReference)o);
//  			}
//  		}.filter(toFilter);
//  	} catch (MetamodelException e) {
//  		throw e;
//  	} catch (Exception e) {
//  		e.printStackTrace();
//  		throw new Error();
//  	}
//  }
//
//  public void filterDeclarations(Collection toFilter) throws MetamodelException {
//  	try {
//  		new PrimitivePredicate() {
//  			public boolean eval(Object o) throws MetamodelException {
//  				return isFiltered(((TypeExceptionDeclaration)o).getType());
//  			}
//  		}.filter(toFilter);
//  	} catch (MetamodelException e) {
//  		throw e;
//  	} catch (Exception e) {
//  		throw new Error();
//  	}
//  }
//
//  /**
//   * Check whether or not the given type is filtered by this filter.
//   *
//   * @param type
//   * @return
//   * @throws MetamodelException
//   */
//  public abstract boolean isFiltered(Type type) throws MetamodelException;
//
//  /**
//   * Check whether or not the type referenced by the given type reference is filtered by this filter.
//   *
//   * @param type
//   * @return
//   * @throws MetamodelException
//   */
//  public boolean isFiltered(TypeReference reference) throws MetamodelException {
//  	return isFiltered(reference.getType());
//  }
//
//  /**
//   * @return
//   */
//  public abstract E clone();
//
//  public boolean propagatedSuperTypeSetOf(Collection propagateTypes) throws MetamodelException {
//    final Collection otherRefs = getPropagatedTypes(getParent().getParent().getRawExceptionTypes());
//    if(otherRefs.isEmpty()) {
//      return true;
//    }
//    else {
//      try {
//        return new PrimitivePredicate() {
//          public boolean eval(Object o) throws Exception {
//            final Type propagated = (Type)o;
//            return new PrimitivePredicate() {
//              public boolean eval(Object o2) throws MetamodelException {
//                Type other = (Type)o2;
//                return propagated.assignableTo(other);
//              }
//            }.exists(otherRefs);
//          }
//        }.forAll(propagateTypes);
//      }
//      catch (MetamodelException e) {
//        throw e;
//      }
//      catch (Exception e) {
//        e.printStackTrace();
//        throw new Error();
//      }
//    }
//  }
//
//}
