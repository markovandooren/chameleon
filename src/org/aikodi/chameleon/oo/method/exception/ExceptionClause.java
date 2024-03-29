package org.aikodi.chameleon.oo.method.exception;

import org.aikodi.chameleon.core.element.ElementImpl;
import org.aikodi.chameleon.core.lookup.LookupException;
import org.aikodi.chameleon.core.validation.Valid;
import org.aikodi.chameleon.core.validation.Verification;
import org.aikodi.chameleon.util.association.Multi;
import org.aikodi.rejuse.predicate.Predicate;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author Marko van Dooren
 */
public class ExceptionClause extends ElementImpl {

  public ExceptionClause() {
	}

  
public boolean compatibleWith(final ExceptionClause other) throws LookupException {
    if (other == null) {
      return false;
    }
    try {
      return new Predicate() {
        @Override
      public boolean eval(Object o) throws LookupException {
          return ((ExceptionDeclaration)o).compatibleWith(other);
        }
      }.forAll(exceptionDeclarations());
    }
    catch (LookupException e) {
      throw e;
    }
    catch (Exception e) {
      e.printStackTrace();
      throw new Error();
    }
  }

  public Set getWorstCaseExceptions() throws LookupException {
    final Set result = new HashSet();
    for(ExceptionDeclaration element : exceptionDeclarations()) {
      result.addAll(element.getWorstCaseExceptionTypes());
    }
    return result;
  }

	/**
	 * EXCEPTION DECLARATIONS
	 */

  private Multi<ExceptionDeclaration> _exceptionDeclarations = new Multi<ExceptionDeclaration>(this);

  public void add(ExceptionDeclaration decl) {
    add(_exceptionDeclarations,decl);
  }

  public void remove(ExceptionDeclaration decl) {
    remove(_exceptionDeclarations,decl);
  }

  public List<ExceptionDeclaration> exceptionDeclarations() {
    return _exceptionDeclarations.getOtherEnds();
  }


  /**
   * @return
   */
  @Override
protected ExceptionClause cloneSelf() {
    return new ExceptionClause();
  }

  public boolean hasValidAccessibility() throws LookupException {
    try {
      return new Predicate() {
        @Override
      public boolean eval(Object o) throws LookupException {
          return ((ExceptionDeclaration)o).hasValidAccessibility();
        }
      }.forAll(exceptionDeclarations());
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
	public Verification verifySelf() {
		return Valid.create();
	}

//  /**
//   * @param done
//   * @return
//   */
//  public boolean isAcyclic(final Set done) throws LookupException {
//    try {
//      return new AbstractPredicate() {
//        public boolean eval(Object o) throws LookupException {
//          ExceptionDeclaration decl = (ExceptionDeclaration)o;
//          return decl.isAcyclic(done);
//        }
//      }.forAll(exceptionDeclarations());
//    }
//    catch (LookupException e) {
//      throw e;
//    }
//    catch (Exception e) {
//      e.printStackTrace();
//      throw new Error();
//    }
//  }
}
