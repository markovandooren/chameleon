package org.aikodi.chameleon.core.scope;

import static org.aikodi.rejuse.collection.CollectionOperations.exists;
import static org.aikodi.rejuse.collection.CollectionOperations.forAll;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.aikodi.chameleon.core.element.Element;
import org.aikodi.chameleon.core.lookup.LookupException;
import org.aikodi.rejuse.collection.CollectionOperations;
import org.aikodi.rejuse.predicate.Predicate;

/**
 * A scope that is the union of a number of other scopes.
 * 
 * @author Marko van Dooren
 */
public class Union extends CompositeScope {

  @Override
  public boolean contains(final Element element) throws LookupException {
    return exists(scopes(), object -> object.contains(element));
  }

  /*
   * @
   * @ public behavior
   * @
   * @ getDomains().isEmpty();
   * @
   */
  public Union() {
  }

  /*
   * @
   * @ public behavior
   * @
   * @ pre domains != null;
   * @
   * @ getDomains().containsAll(domains);
   * @
   */
  public Union(Collection<Scope> domains) throws LookupException {
    super(domains);
  }

  /*
   * @
   * @ public behavior
   * @
   * @ pre first != null;
   * @ pre second != null;
   * @
   * @ getDomains().contains(first);
   * @ getDomains().contains(second);
   * @
   */
  public Union(Scope first, Scope second) throws LookupException {
    add(first);
    add(second);
  }

  @Override
  public boolean geRecursive(final Scope other) throws LookupException {
    return exists(scope(), o -> o.greaterThanOrEqualTo(other));
  }

  @Override
  public boolean leRecursive(final Scope other) throws LookupException {
    return forAll(scope(), o -> other.greaterThanOrEqualTo((o)));
  }

  @Override
  public Scope union(Scope other) throws LookupException {
    Union result = new Union(scope());
    result.add(other);
    return result;
  }

  @Override
  protected void filter() throws LookupException {
  	//TODO inefficient. Do the filtering in the super class and provides the filter
  	// in the subclass.
  	List<Scope> scopes = new ArrayList<>(scopesView());
    Predicate<Scope, LookupException> predicate = acc -> !exists(scopesView(), o2 -> (acc != o2) && (o2.greaterThanOrEqualTo(acc)));
    CollectionOperations.filter(scopes, predicate);
    setScopes(scopes);
  }

}
