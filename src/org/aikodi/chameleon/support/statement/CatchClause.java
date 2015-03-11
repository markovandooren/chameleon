package org.aikodi.chameleon.support.statement;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.aikodi.chameleon.core.declaration.Declaration;
import org.aikodi.chameleon.core.element.Element;
import org.aikodi.chameleon.core.lookup.DeclarationSelector;
import org.aikodi.chameleon.core.lookup.LocalLookupContext;
import org.aikodi.chameleon.core.lookup.LookupContext;
import org.aikodi.chameleon.core.lookup.LookupException;
import org.aikodi.chameleon.core.lookup.SelectionResult;
import org.aikodi.chameleon.core.validation.Valid;
import org.aikodi.chameleon.core.validation.Verification;
import org.aikodi.chameleon.core.variable.Variable;
import org.aikodi.chameleon.core.variable.VariableContainer;
import org.aikodi.chameleon.oo.statement.CheckedExceptionList;
import org.aikodi.chameleon.oo.statement.Statement;
import org.aikodi.chameleon.oo.type.Type;
import org.aikodi.chameleon.oo.variable.FormalParameter;
import org.aikodi.chameleon.util.association.Single;

import be.kuleuven.cs.distrinet.rejuse.predicate.Predicate;

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

  @Override
public CatchClause cloneSelf() {
    return new CatchClause(null, null);
  }

  /**
   * @return
   */
  public boolean isValid() {
    try {
      CheckedExceptionList cel = nearestAncestor(TryStatement.class).getStatement().getCEL();
      Collection checkedExceptionTypes = cel.getExceptions();
      return new Predicate<Element,LookupException>() {
        @Override
      public boolean eval(Element o) throws LookupException {
          return ((Type)o).assignableTo(getExceptionParameter().getType());
        }
      }.exists(checkedExceptionTypes);
    }
    catch (LookupException e) {
      return false;
    }
  }

  @Override
public List<? extends Variable> declarations() {
    List<Variable> result = new ArrayList<Variable>();
    result.add(getExceptionParameter());
    return result;
  }
  
	@Override
   public <D extends Declaration> List<? extends SelectionResult> declarations(DeclarationSelector<D> selector) throws LookupException {
		return selector.selection(declarations());
	}
	
	@Override
	public LookupContext lookupContext(Element element) {
		return language().lookupFactory().createLexicalLookupStrategy(localContext(), this);
	}

	@Override
   public LocalLookupContext localContext() {
		return language().lookupFactory().createTargetLookupStrategy(this);
	}

  
//  //COPIED FROM chameleon.core.type.Type
//  public <T extends Declaration> Set<T> declarations(DeclarationSelector<T> selector) throws LookupException {
//    Set<Declaration> tmp = declarations();
//    Set<T> result = selector.selection(tmp);
//    return result;
//  }

	@Override
   public Element variableScopeElement() {
		return this;
	}

	@Override
	public Verification verifySelf() {
		return checkNull(getExceptionParameter(), "Exception parameter is missing", Valid.create());
	}

	@Override
   public List<? extends Declaration> locallyDeclaredDeclarations() throws LookupException {
		return declarations();
	}

}
