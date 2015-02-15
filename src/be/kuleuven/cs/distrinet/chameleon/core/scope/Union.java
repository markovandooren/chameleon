package be.kuleuven.cs.distrinet.chameleon.core.scope;

import java.util.Collection;

import be.kuleuven.cs.distrinet.chameleon.core.element.Element;
import be.kuleuven.cs.distrinet.chameleon.core.lookup.LookupException;
import be.kuleuven.cs.distrinet.rejuse.predicate.AbstractPredicate;

/**
 * @author Marko van Dooren
 */
public class Union extends CompositeScope {
	
	@Override
   public boolean contains(final Element element) throws LookupException {
		try {
			return new AbstractPredicate<Scope,LookupException>() {

				@Override
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

  @Override
public boolean geRecursive(final Scope other) throws LookupException {
    try {
      return new AbstractPredicate() {
        @Override
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

  @Override
public boolean leRecursive(final Scope other) throws LookupException {
    try {
      return new AbstractPredicate() {
        @Override
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

  @Override
public Scope union(Scope other) throws LookupException {
    Union result = new Union(scope());
    result.add(other);
    return result;
  }
  
  @Override
protected void filter() throws LookupException {
    try {
      new AbstractPredicate() {
        @Override
      public boolean eval(Object o) throws Exception {
          final Scope acc = (Scope)o;
          return ! new AbstractPredicate() {
            @Override
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
