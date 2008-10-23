//package chameleon.core.method.exception;
//
//import java.util.Collection;
//import java.util.HashSet;
//import java.util.List;
//import java.util.Set;
//
//import org.rejuse.association.Reference;
//import org.rejuse.predicate.PrimitivePredicate;
//
//import chameleon.core.MetamodelException;
//import chameleon.core.element.Element;
//import chameleon.core.reference.ReferenceContainer;
//import chameleon.core.type.Type;
//import chameleon.core.type.TypeDescendantImpl;
//import chameleon.core.type.TypeReference;
//import chameleon.util.Util;
//
///**
// * @author marko
// *
// */
//public class FilterClause extends TypeDescendantImpl<FilterClause,AnchoredExceptionDeclaration> implements ReferenceContainer<FilterClause,AnchoredExceptionDeclaration>  {
//
//	public FilterClause() {
//	}
//	
//	public FilterClause(PropagatingClause propagating, BlockingClause blocking) {
//		setPropagatingClause(propagating);
//		setBlockingClause(blocking);
//	}
//	
//	public Type getNearestType() {
//		return getParent().getNearestType();
//	}
//
//	/**
//	 * @param result
//	 * @return
//	 * @throws MetamodelException
//	 */
//	public Set filteredTypes(Set set) throws MetamodelException {
//		Set result = new HashSet(set);
//		filterTypes(result);
//		return result;
//	}
//
//	public void filterTypes(Collection collection) throws MetamodelException {
//		 getPropagatingClause().filterTypes(collection);
//		 getBlockingClause().filterTypes(collection);
//	}
//
//	public void filterTypeReferences(Collection collection) throws MetamodelException {
//		 getPropagatingClause().filterTypeReferences(collection);
//		 getBlockingClause().filterTypeReferences(collection);
//	}
//
//	public void filterDeclarations(Collection toFilter) throws MetamodelException {
//		 getPropagatingClause().filterDeclarations(toFilter);
//		 getBlockingClause().filterDeclarations(toFilter);
//	}
//
//	public void setPropagatingClause(PropagatingClause clause) {
//		_propagatingClause.connectTo(clause.getParentLink());
//	}
//
//	public PropagatingClause getPropagatingClause() {
//		return _propagatingClause.getOtherEnd();
//	}
//
//	private Reference<FilterClause,PropagatingClause> _propagatingClause = new Reference<FilterClause,PropagatingClause>(this);
//
//	public void setBlockingClause(BlockingClause clause) {
//		_blockingClause.connectTo(clause.getParentLink());
//	}
//
//	public BlockingClause getBlockingClause() {
//		return _blockingClause.getOtherEnd();
//	}
//
//	private Reference<FilterClause,BlockingClause> _blockingClause = new Reference<FilterClause,BlockingClause>(this);
//
//  public FilterClause clone() {
//  	final FilterClause result = new FilterClause();
//  	result.setPropagatingClause((PropagatingClause) getPropagatingClause().clone());
//  	result.setBlockingClause((BlockingClause) getBlockingClause().clone());
//  	return result;
//  }
//  
//  public void setParent(AnchoredDeclaration parent) {
//  	getParentLink().connectTo(parent.getFilterClauseLink());
//  }
//
//  public List<Element> getChildren() {
//	  List result = Util.createNonNullList(getPropagatingClause());
//	  Util.addNonNull(getBlockingClause(),result);
//	  return result;
//  }
//
//  /**
//   * Return a new filter clause that represents the logical 'and' of this filter clause and the given one.
//   *
//   * @param filterClause
//   *        The other filter clause
//   * @return
//   */
// /*@
//   @ public behavior
//   @
//   @ pre filterClause != null;
//   @
//   @ post \fresh(\result);
//   @ post \result.getFilters().containsAll(getFilters());
//   @ post \result.getFilters().containsAll(filterClause.getFilters());
//   @*/
//  public FilterClause and(FilterClause filterClause) {
//	  final FilterClause result = new FilterClause();
//	  result.setPropagatingClause(getPropagatingClause().and(filterClause.getPropagatingClause()));
//	  result.setBlockingClause(getBlockingClause().and(filterClause.getBlockingClause()));
//	  return result;
//  }
//
//  /**
//   * @param filterClause
//   * @return
//   */
//  public boolean strongerThan(FilterClause filterClause) throws MetamodelException {
//	  List pa = getPropagatingClause().getPropagatedTypeReferences();
//	  final List ba = getBlockingClause().getBlockedTypeReferences();
//	  filter(pa,ba);
//	  final List pb = filterClause.getPropagatingClause().getPropagatedTypeReferences();
//	  final List bb = filterClause.getBlockingClause().getBlockedTypeReferences();
//	  filter(pb,bb);
//	  try {
//		  // forall Pa: exists Pb : Pa <: Pb  & ! exists Bb: Pa <: Bb
//		  boolean result = new PrimitivePredicate() {
//		  	public boolean eval(final Object o) throws MetamodelException {
//		  		try {
//					return new PrimitivePredicate() {
//						public boolean eval(Object o2) throws MetamodelException {
//							return ((TypeReference) o).getType().subTypeOf(
//									((TypeReference) o2).getType());
//						}
//					}.exists(pb)
//							&& (!new PrimitivePredicate() {
//								public boolean eval(Object o2) throws MetamodelException {
//									return ((TypeReference) o).getType().subTypeOf(
//											((TypeReference) o2).getType());
//								}
//							}.exists(bb));
//				} catch (MetamodelException e) {
//					throw e;
//				}
//				catch(Exception e) {
//					throw new Error();
//				}
//		  	}
//		  }.forAll(pa);
//
//		  // forall Bb: exists Ba : Bb <: Ba  & ! exists Pa: Pa <: Bb (last is redundant, already checked)
//		  result = result && new PrimitivePredicate() {
//		  	public boolean eval(final Object o) throws MetamodelException {
//		  		try {
//					return new PrimitivePredicate() {
//						public boolean eval(Object o2) throws MetamodelException {
//							return ((TypeReference) o).getType().subTypeOf(
//									((TypeReference) o2).getType());
//						}
//					}.exists(ba);
//				} catch (MetamodelException e) {
//					throw e;
//				}
//				catch(Exception e) {
//					throw new Error();
//				}
//		  	}
//		  }.forAll(bb);
//
//		  return result;
//	  } catch (MetamodelException e) {
//		  throw e;
//	  } catch (Exception e) {
//		  throw new Error();
//	  }
//  }
//
//  private void filter(final List pa, final List ba) throws MetamodelException {
//    filterRedundant(pa);
//    filterRedundant(ba);
//	  try {
//		  new PrimitivePredicate() {
//		  	public boolean eval(final Object o) throws MetamodelException {
//		  		try {
//					return ! new PrimitivePredicate() {
//						public boolean eval(Object o2) throws MetamodelException{
//							return ((TypeReference)o).getType().subTypeOf(((TypeReference) o2).getType());
//						}
//					}.exists(ba);
//				} catch (MetamodelException e) {
//					throw e;
//				} catch (Exception e) {
//					throw new Error();
//				}
//		  	}
//		  }.filter(pa);
//	  } catch (MetamodelException e) {
//		  throw e;
//	  } catch (Exception e) {
//		  throw new Error();
//	  }
//
//	  try {
//		  new PrimitivePredicate() {
//		  	public boolean eval(final Object o) throws MetamodelException {
//		  		try {
//					return new PrimitivePredicate() {
//						public boolean eval(Object o2) throws MetamodelException{
//							return ((TypeReference)o).getType().subTypeOf(((TypeReference) o2).getType());
//						}
//					}.exists(pa);
//				} catch (MetamodelException e) {
//					throw e;
//				} catch (Exception e) {
//					throw new Error();
//				}
//		  	}
//		  }.filter(ba);
//	  } catch (MetamodelException e) {
//		  throw e;
//	  } catch (Exception e) {
//		  throw new Error();
//	  }
//  }
//
//  private void filterRedundant(final List pa) throws MetamodelException {
//	  try {
//		  new PrimitivePredicate() {
//		  	public boolean eval(final Object o) throws MetamodelException {
//		  		try {
//					return ! new PrimitivePredicate() {
//						public boolean eval(Object o2) throws MetamodelException{
//							return (o == o2) || (!((TypeReference)o).getType().subTypeOf((Type) o2));
//						}
//					}.exists(pa);
//				} catch (MetamodelException e) {
//					throw e;
//				} catch (Exception e) {
//					throw new Error();
//				}
//		  	}
//		  }.filter(pa);
//	  } catch (MetamodelException e) {
//		  throw e;
//	  } catch (Exception e) {
//		  throw new Error();
//	  }
//
//  }
//
//
//
//
//
//
//}
