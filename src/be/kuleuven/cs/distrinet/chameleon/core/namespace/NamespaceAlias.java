package be.kuleuven.cs.distrinet.chameleon.core.namespace;

import java.util.List;

import be.kuleuven.cs.distrinet.rejuse.property.Property;
import be.kuleuven.cs.distrinet.rejuse.property.PropertySet;
import be.kuleuven.cs.distrinet.chameleon.core.declaration.Declaration;
import be.kuleuven.cs.distrinet.chameleon.core.declaration.SimpleNameSignature;
import be.kuleuven.cs.distrinet.chameleon.core.element.Element;
import be.kuleuven.cs.distrinet.chameleon.core.lookup.LookupException;
import be.kuleuven.cs.distrinet.chameleon.core.lookup.LookupStrategy;
import be.kuleuven.cs.distrinet.chameleon.core.namespacedeclaration.NamespaceDeclaration;
import be.kuleuven.cs.distrinet.chameleon.core.property.ChameleonProperty;
import be.kuleuven.cs.distrinet.chameleon.core.scope.Scope;
import be.kuleuven.cs.distrinet.chameleon.core.scope.ScopeProperty;
import be.kuleuven.cs.distrinet.chameleon.core.validation.Valid;
import be.kuleuven.cs.distrinet.chameleon.core.validation.VerificationResult;
import be.kuleuven.cs.distrinet.chameleon.exception.ChameleonProgrammerException;
import be.kuleuven.cs.distrinet.chameleon.exception.ModelException;
import be.kuleuven.cs.distrinet.chameleon.oo.language.ObjectOrientedLanguage;

public class NamespaceAlias extends NamespaceImpl {

	public NamespaceAlias(SimpleNameSignature sig, Namespace aliasedNamespace) {
		super(sig);
		_aliasedNamespace = aliasedNamespace;
	}
	
	private final Namespace _aliasedNamespace;

	public Namespace aliasedNamespace() {
		return _aliasedNamespace;
	}

	@Override
	public void addNamespacePart(NamespaceDeclaration namespacePart) {
		throw new ChameleonProgrammerException("Trying to add a namespace part to an aliased namespace");
	}

	@Override
	public List<NamespaceDeclaration> getNamespaceParts() {
		return aliasedNamespace().getNamespaceParts();
	}

	@Override
	public Namespace clone() {
		return new NamespaceAlias(signature().clone(),aliasedNamespace());
	}

	private PropertySet<Element,ChameleonProperty> myDefaultProperties() {
		return language().defaultProperties(this);
	}

  public PropertySet<Element,ChameleonProperty> defaultProperties() {
    return filterProperties(myDefaultProperties(), aliasedNamespace().defaultProperties());
  }

  public PropertySet<Element,ChameleonProperty> declaredProperties() {
  	return aliasedNamespace().declaredProperties();
  }
	
  public Scope scope() throws ModelException {
  	Scope result = null;
  	ChameleonProperty scopeProperty = property(language().SCOPE_MUTEX());
  	if(scopeProperty instanceof ScopeProperty) {
  		result = ((ScopeProperty)scopeProperty).scope(this);
  	} else if(scopeProperty != null){
  		throw new ChameleonProgrammerException("Scope property is not a ScopeProperty");
  	}
  	return result;
  }
  
  public LookupStrategy lexicalLookupStrategy(Element element) throws LookupException {
  	return aliasedNamespace().lexicalLookupStrategy();
  }

	@Override
	public List<Namespace> getSubNamespaces() {
		return aliasedNamespace().getSubNamespaces();
	}

	@Override
	public Namespace createSubNamespace(String name) {
		return aliasedNamespace().createSubNamespace(name);
	}

	@Override
	public VerificationResult verifySelf() {
		return Valid.create();
	}

	public Declaration declarator() {
		return aliasedNamespace().declarator();
	}

	public List locallyDeclaredDeclarations() throws LookupException {
		return aliasedNamespace().locallyDeclaredDeclarations();
	}

	@Override
	public boolean complete() throws LookupException {
		return aliasedNamespace().complete();
	}

}
