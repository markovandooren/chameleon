package org.aikodi.chameleon.core.scope;

import org.aikodi.chameleon.core.element.Element;
import org.aikodi.chameleon.core.lookup.LookupException;
import org.aikodi.rejuse.predicate.Predicate;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author marko
 */
public class Intersection extends CompositeScope {

  /*
   * @
   * @ public behavior
   * @
   * @ getDomains().isEmpty();
   * @
   */
  public Intersection() {
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
  public Intersection(Collection<Scope> domains) throws LookupException {
    super(domains);
  }

  @Override
  public boolean contains(final Element element) throws LookupException {
    return new Predicate<Scope, LookupException>() {

      @Override
      public boolean eval(Scope object) throws LookupException {
        return object.contains(element);
      }
    }.forAll(scopes());
  }

  @Override
  public boolean geRecursive(final Scope other) throws LookupException {
    return new Predicate<Scope, LookupException>() {
      @Override
      public boolean eval(Scope o) throws LookupException {
        return (o).greaterThanOrEqualTo(other);
      }
    }.forAll(scope());
  }

  @Override
  protected boolean leRecursive(final Scope other) throws LookupException {
    return new Predicate<Scope, LookupException>() {
      @Override
      public boolean eval(Scope o) throws LookupException {
        return other.greaterThanOrEqualTo((o));
      }
    }.exists(scope());
  }

  @Override
  public Scope intersect(final Scope other) throws LookupException {
    Intersection result = new Intersection(scope());
    result.add(other);
    return result;
  }

  @Override
  protected void filter() throws LookupException {
  	List<Scope> scopes = new ArrayList<>(scopesView());
    new Predicate<Scope, LookupException>() {
      @Override
      public boolean eval(final Scope acc) throws LookupException {
        return !new Predicate<Scope, LookupException>() {
          @Override
          public boolean eval(Scope other) throws LookupException {
            return (acc != other) && (acc.greaterThanOrEqualTo(other));
          }
        }.exists(scopesView());
      }
    }.filter(scopes);
    setScopes(scopes);
  }

}
