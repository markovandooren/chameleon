package be.kuleuven.cs.distrinet.chameleon.support.statement;

import java.util.List;

import be.kuleuven.cs.distrinet.chameleon.core.declaration.Declaration;
import be.kuleuven.cs.distrinet.chameleon.core.declaration.DeclarationContainer;
import be.kuleuven.cs.distrinet.chameleon.core.element.Element;
import be.kuleuven.cs.distrinet.chameleon.core.lookup.DeclarationSelector;
import be.kuleuven.cs.distrinet.chameleon.core.lookup.LexicalLookupContext;
import be.kuleuven.cs.distrinet.chameleon.core.lookup.LookupContext;
import be.kuleuven.cs.distrinet.chameleon.core.lookup.LookupException;
import be.kuleuven.cs.distrinet.chameleon.core.lookup.SelectionResult;
import be.kuleuven.cs.distrinet.chameleon.oo.statement.Statement;
import be.kuleuven.cs.distrinet.chameleon.util.association.Single;

/**
 * @author Marko van Dooren
 */
public class ForStatement extends IterationStatement implements DeclarationContainer {

	@SuppressWarnings("unchecked")
	public LookupContext lookupContext(Element element) throws LookupException {
		if(_lexical == null) {
			_lexical = language().lookupFactory().createLexicalLookupStrategy(localContext(),this);
		}
		return _lexical;
	}
	
	private LookupContext _lexical;

	public LookupContext localContext() {
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
  	set(_control,control);
  }
  
  private Single<ForControl> _control = new Single<ForControl>(this); 

	@Override
	protected ForStatement cloneSelf() {
		return new ForStatement(null,null);
	}
	
	public List<? extends Declaration> locallyDeclaredDeclarations() throws LookupException {
		return declarations();
	}


	public List<? extends Declaration> declarations() throws LookupException {
		return forControl().declarations();
	}

	public <D extends Declaration> List<? extends SelectionResult> declarations(DeclarationSelector<D> selector) throws LookupException {
		return selector.selection(declarations());
	}

}
