package be.kuleuven.cs.distrinet.chameleon.oo.method.exception;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import be.kuleuven.cs.distrinet.chameleon.core.element.ElementImpl;
import be.kuleuven.cs.distrinet.chameleon.core.lookup.LookupException;
import be.kuleuven.cs.distrinet.chameleon.core.validation.Valid;
import be.kuleuven.cs.distrinet.chameleon.core.validation.Verification;
import be.kuleuven.cs.distrinet.chameleon.oo.expression.MethodInvocation;
import be.kuleuven.cs.distrinet.chameleon.util.association.Multi;
import be.kuleuven.cs.distrinet.rejuse.java.collections.RobustVisitor;
import be.kuleuven.cs.distrinet.rejuse.predicate.AbstractPredicate;

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
      return new AbstractPredicate() {
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

  public Set getExceptionTypes(final MethodInvocation invocation) throws LookupException {
    final Set result = new HashSet();
    try {
      new RobustVisitor() {
        public Object visit(Object element) throws LookupException {
          result.addAll(((ExceptionDeclaration)element).getExceptionTypes(invocation));
          return null;
        }

        public void unvisit(Object element, Object undo) {
          //NOP
        }
      }.applyTo(exceptionDeclarations());
    }
    catch (LookupException e) {
      throw e;
    }
    catch (Exception e) {
      e.printStackTrace();
      throw new Error();
    }

    return result;
  }

  public Set getWorstCaseExceptions() throws LookupException {
    final Set result = new HashSet();
    try {
      new RobustVisitor() {
        public Object visit(Object element) throws LookupException {
          result.addAll(((ExceptionDeclaration)element).getWorstCaseExceptionTypes());
          return null;
        }

        public void unvisit(Object element, Object undo) {
          //NOP
        }
      }.applyTo(exceptionDeclarations());
    }
    catch (LookupException e) {
      throw e;
    }
    catch (Exception e) {
      e.printStackTrace();
      throw new Error();
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
  protected ExceptionClause cloneSelf() {
    return new ExceptionClause();
  }

  public boolean hasValidAccessibility() throws LookupException {
    try {
      return new AbstractPredicate() {
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
