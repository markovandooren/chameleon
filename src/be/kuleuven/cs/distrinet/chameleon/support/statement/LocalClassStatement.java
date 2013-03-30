package be.kuleuven.cs.distrinet.chameleon.support.statement;

import java.util.ArrayList;
import java.util.List;

import be.kuleuven.cs.distrinet.rejuse.association.SingleAssociation;
import be.kuleuven.cs.distrinet.chameleon.core.declaration.Declaration;
import be.kuleuven.cs.distrinet.chameleon.core.declaration.DeclarationContainer;
import be.kuleuven.cs.distrinet.chameleon.core.element.Element;
import be.kuleuven.cs.distrinet.chameleon.core.lookup.DeclarationSelector;
import be.kuleuven.cs.distrinet.chameleon.core.lookup.LocalLookupContext;
import be.kuleuven.cs.distrinet.chameleon.core.lookup.LookupException;
import be.kuleuven.cs.distrinet.chameleon.core.lookup.LookupContext;
import be.kuleuven.cs.distrinet.chameleon.core.lookup.LookupContextFactory;
import be.kuleuven.cs.distrinet.chameleon.core.scope.Scope;
import be.kuleuven.cs.distrinet.chameleon.core.validation.Valid;
import be.kuleuven.cs.distrinet.chameleon.core.validation.VerificationResult;
import be.kuleuven.cs.distrinet.chameleon.oo.statement.StatementImpl;
import be.kuleuven.cs.distrinet.chameleon.oo.statement.StatementListContainer;
import be.kuleuven.cs.distrinet.chameleon.oo.statement.StatementListScope;
import be.kuleuven.cs.distrinet.chameleon.oo.type.Type;
import be.kuleuven.cs.distrinet.chameleon.support.property.accessibility.HierarchyScope;
import be.kuleuven.cs.distrinet.chameleon.util.association.Single;

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

	public LookupContextFactory getContextFactory() {
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

  public LookupContext lexicalLookupStrategy(Element element) {
  	return language().lookupFactory().createLexicalLookupStrategy(localContext(), this);
  }

	public LocalLookupContext localContext() {
		return language().lookupFactory().createTargetLookupStrategy(this);
	}
  
  public LookupContext linearLookupStrategy() {
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
