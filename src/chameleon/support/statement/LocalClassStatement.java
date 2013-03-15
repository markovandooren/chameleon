package chameleon.support.statement;

import java.util.ArrayList;
import java.util.List;

import be.kuleuven.cs.distrinet.rejuse.association.SingleAssociation;
import chameleon.core.declaration.Declaration;
import chameleon.core.declaration.DeclarationContainer;
import chameleon.core.element.Element;
import chameleon.core.lookup.DeclarationSelector;
import chameleon.core.lookup.LocalLookupStrategy;
import chameleon.core.lookup.LookupException;
import chameleon.core.lookup.LookupStrategy;
import chameleon.core.lookup.LookupStrategyFactory;
import chameleon.core.scope.Scope;
import chameleon.core.validation.Valid;
import chameleon.core.validation.VerificationResult;
import chameleon.oo.statement.StatementImpl;
import chameleon.oo.statement.StatementListContainer;
import chameleon.oo.statement.StatementListScope;
import chameleon.oo.type.Type;
import chameleon.support.property.accessibility.HierarchyScope;
import chameleon.util.association.Single;

/**
 * @author Marko van Dooren
 */
public class LocalClassStatement extends StatementImpl implements DeclarationContainer {

	public LocalClassStatement() {
	}

	public LocalClassStatement(Type type) {
		setType(type);
	}

  /*********
   * CLASS *
   *********/

	private Single<Type> _type = new Single<Type>(this);

	public void setType(Type type) {
		set(_type, type);
  }

  public Type getType() {
    return _type.getOtherEnd();
  }

	public LocalClassStatement clone() {
		return new LocalClassStatement(getType().clone());
	}

	public Scope getTypeAccessibilityDomain() throws LookupException {
		if (parent() instanceof StatementListContainer) {
			return new StatementListScope((StatementListContainer) parent(), this);
		} else {
			return new HierarchyScope(getType());
		}
	}

	public LookupStrategyFactory getContextFactory() {
		return language().lookupFactory();
	}
	
	public List<? extends Declaration> locallyDeclaredDeclarations() throws LookupException {
		return declarations();
	}

  public List<Declaration> declarations() {
    List<Declaration> result = new ArrayList<Declaration>();
    result.add(getType());
    return result;
  }
  
//  //COPIED FROM chameleon.core.type.Type
//  @SuppressWarnings("unchecked")
//  public <T extends Declaration> Set<T> declarations(DeclarationSelector<T> selector) throws LookupException {
//    List<Declaration> tmp = declarations();
//    List<T> result = selector.selection(tmp);
//    return result;
//  }

  public LookupStrategy lexicalLookupStrategy(Element element) {
  	return language().lookupFactory().createLexicalLookupStrategy(localStrategy(), this);
  }

	public LocalLookupStrategy localStrategy() {
		return language().lookupFactory().createTargetLookupStrategy(this);
	}
  
  public LookupStrategy linearLookupStrategy() {
  	return lexicalLookupStrategy(getType());
  }

	public <D extends Declaration> List<D> declarations(DeclarationSelector<D> selector) throws LookupException {
		return selector.selection(declarations());
	}

	@Override
	public VerificationResult verifySelf() {
		return checkNull(getType(), "The type declaration statement does not declare a type.", Valid.create());
	}
}
