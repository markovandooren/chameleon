package org.aikodi.chameleon.core.scope;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.aikodi.chameleon.core.lookup.LookupException;
import org.aikodi.chameleon.util.Lists;

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
  public CompositeScope(Collection<Scope> scopes) throws LookupException {
    _scopes.addAll(scopes);
    filter();
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
   @ post scopes().contains(scope);
   @*/
  public void add(Scope scope) throws LookupException {
    _scopes.add(scope);
    filter();
  }
  
 /*@
   @ public behavior
   @
   @ post \result != null;
   @*/
  public List<Scope> scopes() {
  	return new ArrayList<Scope>(_scopes);
  }
  
  protected abstract void filter() throws LookupException;

  private List<Scope> _scopes = Lists.create();

  protected List<Scope> scopesView() {
  	return Collections.unmodifiableList(_scopes);
  }
  
  protected void setScopes(List<Scope> scopes) {
  	_scopes = new ArrayList<>(scopes);
  }
  
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
