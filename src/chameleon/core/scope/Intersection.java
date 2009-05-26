package chameleon.core.scope;

import java.util.Collection;

import org.rejuse.predicate.PrimitivePredicate;

import chameleon.core.MetamodelException;
import chameleon.core.element.Element;

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
  public Intersection(Collection<Scope> domains) throws MetamodelException {
    super(domains);
  }

	public boolean contains(final Element element) throws MetamodelException {
		try {
			return new PrimitivePredicate<Scope>() {

				public boolean eval(Scope object) throws Exception {
					return object.contains(element);
				}
			}.forAll(scopes());
		} catch (MetamodelException e) {
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			throw new Error();
		}
	}

  
  public boolean geRecursive(final Scope other) throws MetamodelException {
    try {
      return new PrimitivePredicate() {
        public boolean eval(Object o) throws MetamodelException {
          return ((Scope)o).greaterThanOrEqualTo(other);
        }
      }.forAll(scope());
    }
    catch (MetamodelException e) {
      throw e;
    }
    catch (Exception e) {
      e.printStackTrace();
      throw new Error();
    }
  }

  protected boolean leRecursive(final Scope other) throws MetamodelException {
    try {
      return new PrimitivePredicate() {
        public boolean eval(Object o) throws MetamodelException {
          return other.greaterThanOrEqualTo(((Scope)o));
        }
      }.exists(scope());
    }
    catch (MetamodelException e) {
      throw e;
    }
    catch (Exception e) {
      e.printStackTrace();
      throw new Error();
    }
  }

  public Scope intersect(final Scope other) throws MetamodelException {
    Intersection result = new Intersection(scope());
    result.add(other);
    return result;
  }
  
  protected void filter() throws MetamodelException {
    try {
      new PrimitivePredicate() {
        public boolean eval(Object o) throws Exception {
          final Scope acc = (Scope)o;
          return ! new PrimitivePredicate() {
            public boolean eval(Object o2) throws MetamodelException {
              Scope other = (Scope)o2;
              return (acc != other) && (acc.greaterThanOrEqualTo(other));
            }
          }.exists(_scopes);
        }
      }.filter(_scopes);
    }
    catch (MetamodelException e) {
      throw e;
    }
    catch (Exception e) {
      e.printStackTrace();
      throw new Error();
    }
  }
  
}
