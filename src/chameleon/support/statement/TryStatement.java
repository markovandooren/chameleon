package chameleon.support.statement;

import java.util.Iterator;
import java.util.List;

import org.rejuse.association.OrderedMultiAssociation;
import org.rejuse.association.SingleAssociation;
import org.rejuse.java.collections.Visitor;
import org.rejuse.predicate.AbstractPredicate;

import chameleon.core.element.Element;
import chameleon.core.lookup.LookupException;
import chameleon.exception.ChameleonProgrammerException;
import chameleon.oo.statement.CheckedExceptionList;
import chameleon.oo.statement.Statement;
import chameleon.util.Util;

/**
 * @author Marko van Dooren
 */
public class TryStatement extends StatementContainingStatement {

  public TryStatement(Statement statement) {
    super(statement);
  }

	/**
	 * CATCH CLAUSES
	 */
	private OrderedMultiAssociation<TryStatement,CatchClause> _catchClausesLink = new OrderedMultiAssociation<TryStatement,CatchClause>(this);


  public OrderedMultiAssociation<TryStatement,CatchClause> getCatchClausesLink() {
    return _catchClausesLink;
  }

  public void addCatchClause(CatchClause catchClause) {
  	add(_catchClausesLink,catchClause);
  }
  
  public void addAllCatchClauses(List<CatchClause> catchClauses) {
  	for(CatchClause clause : catchClauses) {
  		addCatchClause(clause);
  	}
  }

  public void removeCatchClause(CatchClause catchClause) {
    remove(_catchClausesLink,catchClause);
  }

  public List<CatchClause> getCatchClauses() {
    return _catchClausesLink.getOtherEnds();
  }

	/**
	 * FINALLY
	 */
	private SingleAssociation _finally = new SingleAssociation(this);

  public FinallyClause getFinallyClause() {
    return (FinallyClause)_finally.getOtherEnd();
  }

  public void setFinallyClause(FinallyClause clause) {
    setAsParent(_finally,clause);
  }

  public TryStatement clone() {
    final TryStatement result = new TryStatement(getStatement().clone());
    new Visitor<CatchClause>() {
      public void visit(CatchClause element) {
        result.addCatchClause(element.clone());
      }
    }.applyTo(getCatchClauses());
    if(getFinallyClause() != null) {
      result.setFinallyClause(getFinallyClause().clone());
    }
    return result;
  }
  
  public List children() {
    List result = Util.createNonNullList(getStatement());
    result.addAll(getCatchClauses());
    Util.addNonNull(getFinallyClause(), result);
    return result;
  }
  
  /**
   * Check whether or not all catch clauses of this try statement are valid.
   */
 /*@
   @ public behavior
   @
   @ post \result == (\forall CatchClause cc; getCatchClauses().contains(cc);
   @                    cc.isValid());
   @*/
  public boolean hasValidCatchClauses() throws LookupException {
    try {
      return new AbstractPredicate() {
        public boolean eval(Object o) throws LookupException {
          return ((CatchClause)o).isValid();
        }
      }.forAll(getCatchClauses());
    }
    catch (LookupException e) {
      throw e;
    }
    catch (Exception e) {
      e.printStackTrace();
      throw new Error();
    }
  }
  
  public CheckedExceptionList getCEL() throws LookupException {
    final CheckedExceptionList cel = getStatement().getCEL();

    Iterator iter = getCatchClauses().iterator();
    // remove all handled exceptions
    while(iter.hasNext()) {
      CatchClause cc = (CatchClause)iter.next();
      cel.handleType(cc.getExceptionParameter().getType());
    }
    iter = getCatchClauses().iterator();
    while(iter.hasNext()) {
      CatchClause cc = (CatchClause)iter.next();
      cel.absorb(cc.getCEL());
    }
    if(getFinallyClause() != null) {
      cel.absorb(getFinallyClause().getCEL());
    }
    return cel;
  }
  
  public CheckedExceptionList getAbsCEL() throws LookupException {
    final CheckedExceptionList cel = getStatement().getAbsCEL();

    Iterator iter = getCatchClauses().iterator();
    // remove all handled exceptions
    while(iter.hasNext()) {
      CatchClause cc = (CatchClause)iter.next();
      cel.handleType(cc.getExceptionParameter().getType());
    }
    iter = getCatchClauses().iterator();
    while(iter.hasNext()) {
      CatchClause cc = (CatchClause)iter.next();
      cel.absorb(cc.getAbsCEL());
    }
    if(getFinallyClause() != null) {
      cel.absorb(getFinallyClause().getAbsCEL());
    }
    return cel;
  }
  
}
