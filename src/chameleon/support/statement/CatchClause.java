package chameleon.support.statement;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.rejuse.association.SingleAssociation;
import org.rejuse.predicate.UnsafePredicate;

import chameleon.core.declaration.Declaration;
import chameleon.core.element.Element;
import chameleon.core.lookup.DeclarationSelector;
import chameleon.core.lookup.LocalLookupStrategy;
import chameleon.core.lookup.LookupException;
import chameleon.core.lookup.LookupStrategy;
import chameleon.core.validation.Valid;
import chameleon.core.validation.VerificationResult;
import chameleon.oo.statement.CheckedExceptionList;
import chameleon.oo.statement.Statement;
import chameleon.oo.type.Type;
import chameleon.oo.variable.FormalParameter;
import chameleon.oo.variable.Variable;
import chameleon.oo.variable.VariableContainer;
import chameleon.util.Util;

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
  
	private SingleAssociation<CatchClause,FormalParameter> _exceptionLink = new SingleAssociation<CatchClause,FormalParameter>(this);

	public SingleAssociation getExceptionLink() {
    return _exceptionLink;
  }
  
  public void setException(FormalParameter exc) {
    setAsParent(_exceptionLink,exc);
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
	public LookupStrategy lexicalLookupStrategy(Element element) {
		return language().lookupFactory().createLexicalLookupStrategy(localStrategy(), this);
	}

	public LocalLookupStrategy localStrategy() {
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
	public VerificationResult verifySelf() {
		return checkNull(getExceptionParameter(), "Exception parameter is missing", Valid.create());
	}

	public List<? extends Declaration> locallyDeclaredDeclarations() throws LookupException {
		return declarations();
	}

}
