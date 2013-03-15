package chameleon.core.scope;

import java.util.Collection;

import be.kuleuven.cs.distrinet.rejuse.predicate.AbstractPredicate;
import chameleon.core.element.Element;
import chameleon.core.lookup.LookupException;

/**
 * @author marko
 */
public class Intersection extends CompositeScope {
  
 /*@
   @ public behavior
   @
   @ getDomains().isEmpty();
   @*/
  public Intersection() {
  }
  
 /*@
   @ public behavior
   @
   @ pre domains != null;
   @
   @ getDomains().containsAll(domains);
   @*/
  public Intersection(Collection<Scope> domains) throws LookupException {
    super(domains);
  }

	public boolean contains(final Element element) throws LookupException {
		try {
			return new AbstractPredicate<Scope>() {

				public boolean eval(Scope object) throws LookupException {
					return object.contains(element);
				}
			}.forAll(scopes());
		} catch (LookupException e) {
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			throw new Error();
		}
	}

  
  public boolean geRecursive(final Scope other) throws LookupException {
    try {
      return new AbstractPredicate<Scope>() {
        public boolean eval(Scope o) throws LookupException {
          return (o).greaterThanOrEqualTo(other);
        }
      }.forAll(scope());
    }
    catch (LookupException e) {
      throw e;
    }
    catch (Exception e) {
      e.printStackTrace();
      throw new Error();
    }
  }

  protected boolean leRecursive(final Scope other) throws LookupException {
    try {
      return new AbstractPredicate<Scope>() {
        public boolean eval(Scope o) throws LookupException {
          return other.greaterThanOrEqualTo((o));
        }
      }.exists(scope());
    }
    catch (LookupException e) {
      throw e;
    }
    catch (Exception e) {
      e.printStackTrace();
      throw new Error();
    }
  }

  public Scope intersect(final Scope other) throws LookupException {
    Intersection result = new Intersection(scope());
    result.add(other);
    return result;
  }
  
  protected void filter() throws LookupException {
    try {
      new AbstractPredicate<Scope>() {
        public boolean eval(final Scope acc) throws Exception {
          return ! new AbstractPredicate<Scope>() {
            public boolean eval(Scope other) throws LookupException {
              return (acc != other) && (acc.greaterThanOrEqualTo(other));
            }
          }.exists(_scopes);
        }
      }.filter(_scopes);
    }
    catch (LookupException e) {
      throw e;
    }
    catch (Exception e) {
      e.printStackTrace();
      throw new Error();
    }
  }
  
}
