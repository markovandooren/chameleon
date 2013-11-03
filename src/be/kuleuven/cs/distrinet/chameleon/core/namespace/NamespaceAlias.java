package be.kuleuven.cs.distrinet.chameleon.core.namespace;

import java.util.List;

import be.kuleuven.cs.distrinet.chameleon.core.declaration.Declaration;
import be.kuleuven.cs.distrinet.chameleon.core.declaration.SimpleNameSignature;
import be.kuleuven.cs.distrinet.chameleon.core.element.Element;
import be.kuleuven.cs.distrinet.chameleon.core.lookup.LookupContext;
import be.kuleuven.cs.distrinet.chameleon.core.lookup.LookupException;
import be.kuleuven.cs.distrinet.chameleon.core.namespacedeclaration.NamespaceDeclaration;
import be.kuleuven.cs.distrinet.chameleon.core.property.ChameleonProperty;
import be.kuleuven.cs.distrinet.chameleon.core.scope.Scope;
import be.kuleuven.cs.distrinet.chameleon.core.scope.ScopeProperty;
import be.kuleuven.cs.distrinet.chameleon.core.validation.Valid;
import be.kuleuven.cs.distrinet.chameleon.core.validation.Verification;
import be.kuleuven.cs.distrinet.chameleon.exception.ChameleonProgrammerException;
import be.kuleuven.cs.distrinet.chameleon.exception.ModelException;
import be.kuleuven.cs.distrinet.rejuse.property.PropertySet;

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
	protected Namespace cloneSelf() {
		return new NamespaceAlias(null,aliasedNamespace());
	}

	private PropertySet<Element,ChameleonProperty> myDefaultProperties() {
		return language().defaultProperties(this,this.explicitProperties());
	}

  public PropertySet<Element,ChameleonProperty> defaultProperties() {
    return filterProperties(myDefaultProperties(), aliasedNamespace().defaultProperties());
  }

  public PropertySet<Element,ChameleonProperty> declaredProperties() {
  	return aliasedNamespace().declaredProperties();
  }
	
  public PropertySet<Element,ChameleonProperty> inherentProperties() {
    return filterProperties(myInherentProperties(), aliasedNamespace().inherentProperties());
  }

	protected PropertySet<Element,ChameleonProperty> myInherentProperties() {
		return new PropertySet<Element,ChameleonProperty>();
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
  
  public LookupContext lookupContext(Element element) throws LookupException {
  	return aliasedNamespace().lexicalContext();
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
	public Verification verifySelf() {
		return Valid.create();
	}

	public Declaration declarator() {
		return aliasedNamespace().declarator();
	}

	public List<? extends Declaration> locallyDeclaredDeclarations() throws LookupException {
		return aliasedNamespace().locallyDeclaredDeclarations();
	}

	@Override
	public boolean complete() throws LookupException {
		return aliasedNamespace().complete();
	}

	@Override
	public boolean hasSubNamespaces() {
		return aliasedNamespace().hasSubNamespaces();
	}

	@Override
	public List<Namespace> getAllSubNamespaces() {
		return _aliasedNamespace.getAllSubNamespaces();
	}

	@Override
	public List<NamespaceDeclaration> loadedNamespaceParts() {
		return _aliasedNamespace.loadedNamespaceParts();
	}
}
