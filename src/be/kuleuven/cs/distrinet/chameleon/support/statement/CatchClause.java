package be.kuleuven.cs.distrinet.chameleon.support.statement;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import be.kuleuven.cs.distrinet.chameleon.core.declaration.Declaration;
import be.kuleuven.cs.distrinet.chameleon.core.element.Element;
import be.kuleuven.cs.distrinet.chameleon.core.lookup.DeclarationSelector;
import be.kuleuven.cs.distrinet.chameleon.core.lookup.LocalLookupContext;
import be.kuleuven.cs.distrinet.chameleon.core.lookup.LookupContext;
import be.kuleuven.cs.distrinet.chameleon.core.lookup.LookupException;
import be.kuleuven.cs.distrinet.chameleon.core.validation.Valid;
import be.kuleuven.cs.distrinet.chameleon.core.validation.Verification;
import be.kuleuven.cs.distrinet.chameleon.oo.statement.CheckedExceptionList;
import be.kuleuven.cs.distrinet.chameleon.oo.statement.Statement;
import be.kuleuven.cs.distrinet.chameleon.oo.type.Type;
import be.kuleuven.cs.distrinet.chameleon.oo.variable.FormalParameter;
import be.kuleuven.cs.distrinet.chameleon.oo.variable.Variable;
import be.kuleuven.cs.distrinet.chameleon.oo.variable.VariableContainer;
import be.kuleuven.cs.distrinet.chameleon.util.association.Single;
import be.kuleuven.cs.distrinet.rejuse.predicate.UnsafePredicate;

/**
 * @author Marko van Dooren
 */
public class CatchClause extends Clause implements VariableContainer {
  
  public CatchClause(FormalParameter exc, Statement statement) {
    super(statement);
    setException(exc);
  }
  
  /*************
   * EXCEPTION * 
   *************/
  
	private Single<FormalParameter> _exceptionLink = new Single<FormalParameter>(this);

	public Single getExceptionLink() {
    return _exceptionLink;
  }
  
  public void setException(FormalParameter exc) {
    set(_exceptionLink,exc);
  }
  
  public FormalParameter getExceptionParameter() {
    return _exceptionLink.getOtherEnd();
  }

//  /***********
//   * Context *
//   ***********/
//  
//	private Reference<CatchClause,StaticContext> _context = new Reference<CatchClause,StaticContext>(this);
//
//	public Context getContext(Element element) {
//    return _context.getOtherEnd();
//  }
//
//  public void setContext(StaticContext context) {
//  	if(context != null) {
//  		_context.connectTo(context.getParentLink());
//  	} else {
//  		_context.connectTo(null);
//  	}
//  }

  public CatchClause clone() {
    return new CatchClause((FormalParameter)getExceptionParameter().clone(), statement().clone());
  }

  /**
   * @return
   */
  public boolean isValid() {
    try {
      CheckedExceptionList cel = nearestAncestor(TryStatement.class).getStatement().getCEL();
      Collection checkedExceptionTypes = cel.getExceptions();
      return new UnsafePredicate<Element,LookupException>() {
        public boolean eval(Element o) throws LookupException {
          return ((Type)o).assignableTo(getExceptionParameter().getType());
        }
      }.exists(checkedExceptionTypes);
    }
    catch (LookupException e) {
      return false;
    }
  }

  public List<? extends Variable> declarations() {
    List<Variable> result = new ArrayList<Variable>();
    result.add(getExceptionParameter());
    return result;
  }
  
	public <D extends Declaration> List<D> declarations(DeclarationSelector<D> selector) throws LookupException {
		return selector.selection(declarations());
	}
	
	@Override
	public LookupContext lookupContext(Element element) {
		return language().lookupFactory().createLexicalLookupStrategy(localContext(), this);
	}

	public LocalLookupContext localContext() {
		return language().lookupFactory().createTargetLookupStrategy(this);
	}

  
//  //COPIED FROM chameleon.core.type.Type
//  public <T extends Declaration> Set<T> declarations(DeclarationSelector<T> selector) throws LookupException {
//    Set<Declaration> tmp = declarations();
//    Set<T> result = selector.selection(tmp);
//    return result;
//  }

	public Element variableScopeElement() {
		return this;
	}

	@Override
	public Verification verifySelf() {
		return checkNull(getExceptionParameter(), "Exception parameter is missing", Valid.create());
	}

	public List<? extends Declaration> locallyDeclaredDeclarations() throws LookupException {
		return declarations();
	}

}
