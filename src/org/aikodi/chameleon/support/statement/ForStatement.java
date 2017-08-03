package org.aikodi.chameleon.support.statement;

import java.util.List;

import org.aikodi.chameleon.core.declaration.Declaration;
import org.aikodi.chameleon.core.declaration.DeclarationContainer;
import org.aikodi.chameleon.core.element.Element;
import org.aikodi.chameleon.core.lookup.DeclarationSelector;
import org.aikodi.chameleon.core.lookup.LookupContext;
import org.aikodi.chameleon.core.lookup.LookupException;
import org.aikodi.chameleon.core.lookup.SelectionResult;
import org.aikodi.chameleon.oo.statement.Statement;
import org.aikodi.chameleon.util.association.Single;

/**
 * @author Marko van Dooren
 */
public class ForStatement extends IterationStatement implements DeclarationContainer {

	@Override
   @SuppressWarnings("unchecked")
	public LookupContext lookupContext(Element element) throws LookupException {
		if(_lexical == null) {
			_lexical = language().lookupFactory().createLexicalLookupStrategy(localContext(),this);
		}
		return _lexical;
	}
	
	private LookupContext _lexical;

	@Override
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
  
  private Single<ForControl> _control = new Single<ForControl>(this, "control"); 

	@Override
	protected ForStatement cloneSelf() {
		return new ForStatement(null,null);
	}
	
	@Override
   public List<? extends Declaration> locallyDeclaredDeclarations() throws LookupException {
		return declarations();
	}


	@Override
   public List<? extends Declaration> declarations() throws LookupException {
		return forControl().declarations();
	}

	@Override
   public <D extends Declaration> List<? extends SelectionResult<D>> declarations(DeclarationSelector<D> selector) throws LookupException {
		return selector.selection(declarations());
	}

}
