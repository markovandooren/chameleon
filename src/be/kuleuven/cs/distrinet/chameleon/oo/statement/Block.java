package be.kuleuven.cs.distrinet.chameleon.oo.statement;

import java.util.List;

import be.kuleuven.cs.distrinet.chameleon.core.element.Element;
import be.kuleuven.cs.distrinet.chameleon.core.lookup.LookupContext;
import be.kuleuven.cs.distrinet.chameleon.core.lookup.LookupException;
import be.kuleuven.cs.distrinet.chameleon.core.validation.Valid;
import be.kuleuven.cs.distrinet.chameleon.core.validation.Verification;
import be.kuleuven.cs.distrinet.chameleon.exception.ChameleonProgrammerException;
import be.kuleuven.cs.distrinet.chameleon.util.association.Multi;
import be.kuleuven.cs.distrinet.rejuse.association.Association;
import be.kuleuven.cs.distrinet.rejuse.java.collections.Visitor;

/**
 * @author Marko van Dooren
 */
public class Block extends StatementImpl implements StatementListContainer {
	//TODO: should this be a member, or should there be a separate ObjectInitializer that contains a block ?
	//TODO: can any statement be a member ? In this case the methods of Member have to move up.

	/**
	 * STATEMENTS
	 */
	private Multi<Statement> _statements = new Multi<Statement>(this);

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
  
  public List<Statement> statements() {
    return _statements.getOtherEnds();
  }
  
  public Statement statement(int baseOneIndex) {
  	return _statements.elementAt(baseOneIndex);
  }

  public Block clone() {
    final Block result = new Block();
    new Visitor<Statement>() {
      public void visit(Statement element) {
        result.addStatement(element.clone());
      }
    }.applyTo(statements());
    return result;
  }
  
  public void clear() {
  	for(Element child: children()) {
  		child.disconnect();
  	}
  }

  public int getIndexOf(Statement statement) {
    return statements().indexOf(statement) + 1;
  }
  
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
	public LookupContext lookupContext(Element element) throws LookupException {
		List<Statement> declarations = statements();
		int index = declarations.indexOf(element);
		if(index == 0) {
			return parent().lookupContext(this);
		} else if (index > 0) {
			return declarations.get(index-1).linearLookupStrategy();
		} else {
		  throw new ChameleonProgrammerException("Invoking lexicalContext(element) with an element that is not a child.");
		}
	}

	@Override
	public Verification verifySelf() {
		return Valid.create();
	}

	public void addBlock(Block block) {
		if (block != null) {
			for (Statement st : block.statements()) {
				addStatement(st.clone());
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
