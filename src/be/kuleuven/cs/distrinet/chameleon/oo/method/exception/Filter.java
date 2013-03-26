package be.kuleuven.cs.distrinet.chameleon.oo.method.exception;
public class Filter{}
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
//import java.util.Set;
//
//import org.rejuse.predicate.PrimitivePredicate;
//
//import be.kuleuven.cs.distrinet.chameleon.core.MetamodelException;
//import be.kuleuven.cs.distrinet.chameleon.core.context.Context;
//import be.kuleuven.cs.distrinet.chameleon.core.element.Element;
//import be.kuleuven.cs.distrinet.chameleon.core.namespace.Namespace;
//import be.kuleuven.cs.distrinet.chameleon.core.namespacepart.NamespacePartElementImpl;
//import be.kuleuven.cs.distrinet.chameleon.core.reference.ReferenceContainer;
//import be.kuleuven.cs.distrinet.chameleon.core.type.Type;
//import be.kuleuven.cs.distrinet.chameleon.core.type.TypeReference;
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
