package org.aikodi.chameleon.support.statement;

import java.util.Iterator;
import java.util.List;

import org.aikodi.chameleon.core.lookup.LookupException;
import org.aikodi.chameleon.oo.statement.CheckedExceptionList;
import org.aikodi.chameleon.oo.statement.Statement;
import org.aikodi.chameleon.util.association.Multi;
import org.aikodi.chameleon.util.association.Single;

import be.kuleuven.cs.distrinet.rejuse.predicate.AbstractPredicate;

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
	private Multi<CatchClause> _catchClausesLink = new Multi<CatchClause>(this);


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
	private Single<FinallyClause> _finally = new Single<FinallyClause>(this);

  public FinallyClause getFinallyClause() {
    return _finally.getOtherEnd();
  }

  public void setFinallyClause(FinallyClause clause) {
    set(_finally,clause);
  }

  @Override
protected TryStatement cloneSelf() {
    return new TryStatement(null);
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
        @Override
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
  
  @Override
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
  
  @Override
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
