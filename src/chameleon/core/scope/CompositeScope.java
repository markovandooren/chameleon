package chameleon.core.scope;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.rejuse.predicate.PrimitiveTotalPredicate;

import chameleon.core.MetamodelException;

/**
 * @author marko
 */
public abstract class CompositeScope extends Scope {

 /*@
   @ public behavior
   @
   @ getDomains().isEmpty();
   @*/
  public CompositeScope() {
  }
  
 /*@
   @ public behavior
   @
   @ pre scopes != null;
   @
   @ getDomains().containsAll(scopes);
   @*/
  public CompositeScope(Collection<Scope> scopes) {
    _scopes.addAll(scopes);
  }

 /*@
   @ public behavior
   @
   @ post \result != null;
   @*/
  public Collection<Scope> scope() {
    return new ArrayList<Scope>(_scopes);
  }

 /*@
   @ public behavior
   @
   @ pre scope != null;
   @ pre scope != this;
   @ pre ! containsRecursive(scope);
   @
   @ post scope().contains(scope);
   @*/
  public void add(Scope scope) throws MetamodelException {
    _scopes.add(scope);
    filter();
  }
  
  protected abstract void filter() throws MetamodelException;

	protected List<Scope> _scopes = new ArrayList<Scope>();

// /*@
//   @ public behavior
//   @
//   @ post \result == ! _domains.contains(scope) && 
//   @                 new PrimitiveTotalPredicate<Scope>() {
//   @                   public boolean eval(Scope o) {
//   @                     return ! o.containsRecursive(scope);
//   @                   }
//   @                 }.forAll(_domains);
//   @*/
//  public boolean containsRecursive(final Scope scope) {
//    return ! _scopes.contains(scope) && 
//      new PrimitiveTotalPredicate() {
//        public boolean eval(Object o) {
//          return ! ((Scope)o).containsRecursive(scope);
//        }
//      }.forAll(_scopes);
//  }

}
