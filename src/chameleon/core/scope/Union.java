package chameleon.core.scope;

import java.util.Collection;

import org.rejuse.predicate.PrimitivePredicate;

import chameleon.core.MetamodelException;
import chameleon.core.element.Element;

/**
 * @author Marko van Dooren
 */
public class Union extends CompositeScope {
	
	public boolean contains(final Element element) throws MetamodelException {
		try {
			return new PrimitivePredicate<Scope>() {

				public boolean eval(Scope object) throws Exception {
					return object.contains(element);
				}
			}.exists(scopes());
		} catch (MetamodelException e) {
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
  public Union(Collection<Scope> domains) throws MetamodelException {
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
  public Union(Scope first, Scope second) throws MetamodelException {
  	add(first);
  	add(second);
  }

  public boolean geRecursive(final Scope other) throws MetamodelException {
    try {
      return new PrimitivePredicate() {
        public boolean eval(Object o) throws MetamodelException {
          return ((Scope)o).greaterThanOrEqualTo(other);
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

  public boolean leRecursive(final Scope other) throws MetamodelException {
    try {
      return new PrimitivePredicate() {
        public boolean eval(Object o) throws MetamodelException {
          return other.greaterThanOrEqualTo(((Scope)o));
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

  public Scope union(Scope other) throws MetamodelException {
    Union result = new Union(scope());
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
              return (acc != other) && (other.greaterThanOrEqualTo(acc));
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
