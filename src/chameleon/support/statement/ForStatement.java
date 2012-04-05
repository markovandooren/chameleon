package chameleon.support.statement;

import java.util.List;

import org.rejuse.association.SingleAssociation;

import chameleon.core.declaration.Declaration;
import chameleon.core.declaration.DeclarationContainer;
import chameleon.core.element.Element;
import chameleon.core.lookup.DeclarationSelector;
import chameleon.core.lookup.LexicalLookupStrategy;
import chameleon.core.lookup.LocalLookupStrategy;
import chameleon.core.lookup.LookupException;
import chameleon.core.lookup.LookupStrategy;
import chameleon.oo.statement.Statement;
import chameleon.oo.variable.Variable;
import chameleon.util.Util;

/**
 * @author Marko van Dooren
 */
public class ForStatement extends IterationStatement implements DeclarationContainer {

	@SuppressWarnings("unchecked")
	public LookupStrategy lexicalLookupStrategy(Element element) throws LookupException {
		return new LexicalLookupStrategy(localStrategy(),this);
	}

	public LookupStrategy localStrategy() {
		return language().lookupFactory().createLocalLookupStrategy(this);
	}
	
  /**
   * @param expression
   * @param statement
   */
  public ForStatement(ForControl control, Statement statement) {
    super(statement);
  	setForControl(control);
  }
  
  public ForControl forControl() {
  	return _control.getOtherEnd();
  }
  
  public void setForControl(ForControl control) {
  	setAsParent(_control,control);
  }
  
  private SingleAssociation<ForStatement,ForControl> _control = new SingleAssociation<ForStatement, ForControl>(this); 

	@Override
	public ForStatement clone() {
		return new ForStatement(forControl().clone(), getStatement().clone());
	}
	
	public List<? extends Declaration> locallyDeclaredDeclarations() throws LookupException {
		return declarations();
	}


	public List<? extends Declaration> declarations() throws LookupException {
		return forControl().declarations();
	}

	public <D extends Declaration> List<D> declarations(DeclarationSelector<D> selector) throws LookupException {
		return selector.selection(declarations());
	}

}
