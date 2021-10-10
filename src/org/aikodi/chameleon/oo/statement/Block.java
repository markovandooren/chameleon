package org.aikodi.chameleon.oo.statement;

import org.aikodi.chameleon.core.element.Element;
import org.aikodi.chameleon.core.lookup.LookupContext;
import org.aikodi.chameleon.core.lookup.LookupException;
import org.aikodi.chameleon.core.validation.Valid;
import org.aikodi.chameleon.core.validation.Verification;
import org.aikodi.chameleon.exception.ChameleonProgrammerException;
import org.aikodi.chameleon.util.association.Multi;
import org.aikodi.rejuse.association.Association;

import java.util.List;

/**
 * @author Marko van Dooren
 */
public class Block extends StatementImpl implements StatementListContainer {

	/**
	 * STATEMENTS
	 */
	private Multi<Statement> _statements = new Multi<Statement>(this,"statements");
	{
		_statements.enableCache();
	}

  public void addStatement(Statement statement) {
  	add(_statements,statement);
  }
  
  public void addInFront(Statement statement) {
	  if(statement != null) {
		  _statements.addInFront((Association)statement.parentLink());
	  }
  }

  public void removeStatement(Statement statement) {
  	remove(_statements,statement);
  }

  public int nbStatements() {
  	return _statements.size();
  }
  
  @Override
public List<Statement> statements() {
    return _statements.getOtherEnds();
  }
  
  public Statement statement(int index) {
  	return _statements.elementAt(index);
  }

  @Override
public Block cloneSelf() {
    return new Block();
  }
  
  public void clear() {
  	for(Element child: lexical().children()) {
  		child.disconnect();
  	}
  }

  @Override
public int getIndexOf(Statement statement) {
    return statements().indexOf(statement) + 1;
  }
  
	@Override
   public List<Statement> statementsAfter(Statement statement) {
		List<Statement> statements = statements(); 
		int index = statements.indexOf(statement);
		// returns a view on a clone of _statements (getStatements() clones the list).
		// the view depends on the local variable, but since no other references exist
		// this is not a problem.
		return statements.subList(index, statements.size());
	}

	public List<Statement> statementsBefore(Statement statement) {
		List<Statement> statements = statements(); 
		int index = statements.indexOf(statement);
		// returns a view on a clone of _statements (getStatements() clones the list).
		// the view depends on the local variable, but since no other references exist
		// this is not a problem.
		return statements.subList(0,index);
	}

 /*@
   @ public behavior
   @
   @ post getStatements().indexOf(element) == 0) ==> \result == parent().lexicalContext(this);
   @ post getStatements().indexOf(Element) > 0) ==> 
   @      \result == getStatements().elementAt(getStatements().indexOf(element) - 1).lexicalContext();
   @*/
	@Override
   public LookupContext lookupContext(Element element) throws LookupException {
//		LINEAR.start();
		LookupContext result;
		List<Statement> declarations = statements();
		int index = declarations.indexOf(element);
		if(index == 0) {
			result = parent().lookupContext(this);
		} else if (index > 0) {
			result = declarations.get(index-1).linearLookupStrategy();
		} else {
		  throw new ChameleonProgrammerException("Invoking lexicalContext(element) with an element that is not a child.");
		}
//		LINEAR.stop();
		return result;
	}
	
//	public final static Timer LINEAR = new Timer();

	@Override
	public Verification verifySelf() {
		return Valid.create();
	}

	public void addBlock(Block block) {
		if (block != null) {
			for (Statement st : block.statements()) {
				addStatement(clone(st));
			}
		}
	}

	public void addStatements(List<? extends Statement> statements) {
		if (statements != null) {
			for (Statement st : statements)
				addStatement(st);
		}
	}

	
}
