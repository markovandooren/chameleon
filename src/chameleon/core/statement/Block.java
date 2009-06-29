package chameleon.core.statement;

import java.util.List;

import org.rejuse.association.OrderedReferenceSet;
import org.rejuse.java.collections.Visitor;

import chameleon.core.context.Context;
import chameleon.core.context.LookupException;
import chameleon.core.element.ChameleonProgrammerException;
import chameleon.core.element.Element;

/**
 * @author Marko van Dooren
 */
public class Block extends Statement<Block> implements StatementContainer<Block,StatementContainer>, StatementListContainer<Block,StatementContainer> {
	//TODO: should this be a member, or should there be a separate ObjectInitializer that contains a block ?
	//TODO: can any statement be a member ? In this case the methods of Member have to move up.

	/**
	 * STATEMENTS
	 */
	private OrderedReferenceSet<Block,Statement> _statements = new OrderedReferenceSet<Block,Statement>(this);

  public OrderedReferenceSet<Block,Statement> getStatementsLink() {
    return _statements;
  }

  public void addStatement(Statement statement) {
    _statements.add(statement.parentLink());
  }

  public void removeStatement(Statement statement) {
    _statements.add(statement.parentLink());
  }

  public List<Statement> getStatements() {
    return _statements.getOtherEnds();
  }

  public Block clone() {
    final Block result = new Block();
    new Visitor<Statement>() {
      public void visit(Statement element) {
        result.addStatement(element.clone());
      }
    }.applyTo(getStatements());
    return result;
  }

  public List<? extends Element> children() {
    return getStatements();
  }

  public int getIndexOf(Statement statement) {
    return getStatements().indexOf(statement) + 1;
  }

	public List<Statement> statementsAfter(Statement statement) {
		List<Statement> statements = getStatements(); 
		int index = statements.indexOf(statement);
		// returns a view on a clone of _statements (getStatements() clones the list).
		// the view depends on the local variable, but since no other references exist
		// this is not a problem.
		return statements.subList(index, statements.size());
	}

 /*@
   @ public behavior
   @
   @ post getStatements().indexOf(element) == 0) ==> \result == parent().lexicalContext(this);
   @ post getStatements().indexOf(Element) > 0) ==> 
   @      \result == getStatements().elementAt(getStatements().indexOf(element) - 1).lexicalContext();
   @*/
	public Context lexicalContext(Element element) throws LookupException {
		List<Statement> declarations = getStatements();
		int index = declarations.indexOf(element);
		if(index == 0) {
			return parent().lexicalContext(this);
		} else if (index > 0) {
			return declarations.get(index-1).lexicalContext();
		} else {
		  throw new ChameleonProgrammerException("Invoking lexicalContext(element) with an element that is not a child.");
		}
	}

	
}
