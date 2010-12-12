package chameleon.core.method.exception;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.rejuse.association.OrderedMultiAssociation;
import org.rejuse.java.collections.RobustVisitor;
import org.rejuse.java.collections.Visitor;
import org.rejuse.predicate.AbstractPredicate;

import chameleon.core.element.Element;
import chameleon.core.expression.MethodInvocation;
import chameleon.core.lookup.LookupException;
import chameleon.core.method.Method;
import chameleon.core.namespace.NamespaceElementImpl;
import chameleon.core.validation.Valid;
import chameleon.core.validation.VerificationResult;
import chameleon.oo.type.Type;

/**
 * @author Marko van Dooren
 */
public class ExceptionClause extends NamespaceElementImpl<ExceptionClause,Method<?,?,?,?>> {

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

  private OrderedMultiAssociation<ExceptionClause,ExceptionDeclaration> _exceptionDeclarations = new OrderedMultiAssociation<ExceptionClause,ExceptionDeclaration>(this);

  public void add(ExceptionDeclaration decl) {
    _exceptionDeclarations.add(decl.parentLink());
  }

  public void remove(ExceptionDeclaration decl) {
    _exceptionDeclarations.remove(decl.parentLink());
  }

  public List<ExceptionDeclaration> exceptionDeclarations() {
    return _exceptionDeclarations.getOtherEnds();
  }


  /**
   * @return
   */
  public ExceptionClause clone() {
    ExceptionClause result = new ExceptionClause();
    for(ExceptionDeclaration declaration: exceptionDeclarations()) {
    	result.add(declaration.clone());
    }
    return result;
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

 /*@
   @ public behavior
   @
   @ post \result.equals(getDeclarations());
   @*/
  public List<? extends Element> children() {
    return exceptionDeclarations();
  }


	@Override
	public VerificationResult verifySelf() {
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
