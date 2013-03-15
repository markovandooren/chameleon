package chameleon.core.scope;

import java.util.Collection;

import be.kuleuven.cs.distrinet.rejuse.predicate.AbstractPredicate;
import chameleon.core.element.Element;
import chameleon.core.lookup.LookupException;

/**
 * @author Marko van Dooren
 */
public class Union extends CompositeScope {
	
	public boolean contains(final Element element) throws LookupException {
		try {
			return new AbstractPredicate<Scope>() {

				public boolean eval(Scope object) throws LookupException {
					return object.contains(element);
				}
			}.exists(scopes());
		} catch (LookupException e) {
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			throw new Error();
		}
	}

 /*@
   @ public behavior
   @
   @ getDomains().isEmpty();
   @*/
  public Union() {
  }
  
 /*@
   @ public behavior
   @
   @ pre domains != null;
   @
   @ getDomains().containsAll(domains);
   @*/
  public Union(Collection<Scope> domains) throws LookupException {
    super(domains);
  }
  
 /*@
   @ public behavior
   @
   @ pre first != null;
   @ pre second != null;
   @
   @ getDomains().contains(first);
   @ getDomains().contains(second);
   @*/
  public Union(Scope first, Scope second) throws LookupException {
  	add(first);
  	add(second);
  }

  public boolean geRecursive(final Scope other) throws LookupException {
    try {
      return new AbstractPredicate() {
        public boolean eval(Object o) throws LookupException {
          return ((Scope)o).greaterThanOrEqualTo(other);
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

  public boolean leRecursive(final Scope other) throws LookupException {
    try {
      return new AbstractPredicate() {
        public boolean eval(Object o) throws LookupException {
          return other.greaterThanOrEqualTo(((Scope)o));
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

  public Scope union(Scope other) throws LookupException {
    Union result = new Union(scope());
    result.add(other);
    return result;
  }
  
  protected void filter() throws LookupException {
    try {
      new AbstractPredicate() {
        public boolean eval(Object o) throws Exception {
          final Scope acc = (Scope)o;
          return ! new AbstractPredicate() {
            public boolean eval(Object o2) throws LookupException {
              Scope other = (Scope)o2;
              return (acc != other) && (other.greaterThanOrEqualTo(acc));
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
