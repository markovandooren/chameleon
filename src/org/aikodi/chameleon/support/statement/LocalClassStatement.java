package org.aikodi.chameleon.support.statement;

import org.aikodi.chameleon.core.declaration.Declaration;
import org.aikodi.chameleon.core.declaration.DeclarationContainer;
import org.aikodi.chameleon.core.element.Element;
import org.aikodi.chameleon.core.lookup.*;
import org.aikodi.chameleon.core.scope.Scope;
import org.aikodi.chameleon.core.validation.Valid;
import org.aikodi.chameleon.core.validation.Verification;
import org.aikodi.chameleon.oo.statement.StatementImpl;
import org.aikodi.chameleon.oo.statement.StatementListContainer;
import org.aikodi.chameleon.oo.statement.StatementListScope;
import org.aikodi.chameleon.oo.type.Type;
import org.aikodi.chameleon.support.property.accessibility.HierarchyScope;
import org.aikodi.chameleon.util.association.Single;

import java.util.ArrayList;
import java.util.List;

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

	@Override
   protected LocalClassStatement cloneSelf() {
		return new LocalClassStatement(null);
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
	
	@Override
   public List<? extends Declaration> locallyDeclaredDeclarations() throws LookupException {
		return declarations();
	}

  @Override
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

  @Override
public LookupContext lookupContext(Element element) {
  	return language().lookupFactory().createLexicalLookupStrategy(localContext(), this);
  }

	@Override
   public LocalLookupContext localContext() {
		return language().lookupFactory().createTargetLookupStrategy(this);
	}
  
  @Override
public LookupContext linearLookupStrategy() {
  	return lookupContext(getType());
  }

	@Override
   public <D extends Declaration> List<? extends SelectionResult<D>> declarations(DeclarationSelector<D> selector) throws LookupException {
		return selector.selection(declarations());
	}

	@Override
	public Verification verifySelf() {
		return checkNull(getType(), "The type declaration statement does not declare a type.", Valid.create());
	}
}
