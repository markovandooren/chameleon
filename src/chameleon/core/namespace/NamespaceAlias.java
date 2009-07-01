package chameleon.core.namespace;

import java.util.List;

import org.rejuse.property.Property;
import org.rejuse.property.PropertySet;

import chameleon.core.MetamodelException;
import chameleon.core.declaration.SimpleNameSignature;
import chameleon.core.element.ChameleonProgrammerException;
import chameleon.core.element.Element;
import chameleon.core.lookup.LookupException;
import chameleon.core.lookup.LookupStrategy;
import chameleon.core.namespacepart.NamespacePart;
import chameleon.core.scope.Scope;
import chameleon.core.scope.ScopeProperty;

public class NamespaceAlias extends Namespace {

	public NamespaceAlias(SimpleNameSignature sig, Namespace aliasedNamespace) {
		super(sig);
		_aliasedNamespace = aliasedNamespace;
	}
	
	private final Namespace _aliasedNamespace;

	public Namespace aliasedNamespace() {
		return _aliasedNamespace;
	}

	@Override
	public void addNamespacePart(NamespacePart namespacePart) {
		throw new ChameleonProgrammerException("Trying to add a namespace part to an aliased namespace");
	}

	@Override
	public List<NamespacePart> getNamespaceParts() {
		return aliasedNamespace().getNamespaceParts();
	}

	@Override
	public Namespace clone() {
		return new NamespaceAlias(signature().clone(),aliasedNamespace());
	}

	private PropertySet<Element> myDefaultProperties() {
		return language().defaultProperties(this);
	}

  public PropertySet<Element> defaultProperties() {
    return filterProperties(myDefaultProperties(), aliasedNamespace().defaultProperties());
  }

  public PropertySet<Element> declaredProperties() {
  	return aliasedNamespace().declaredProperties();
  }
	
  public Scope scope() throws MetamodelException {
  	Scope result = null;
  	Property<Element> scopeProperty = property(language().SCOPE_MUTEX);
  	if(scopeProperty instanceof ScopeProperty) {
  		result = ((ScopeProperty)scopeProperty).scope(this);
  	} else if(scopeProperty != null){
  		throw new ChameleonProgrammerException("Scope property is not a ScopeProperty");
  	}
  	return result;
  }
  
  public LookupStrategy lexicalContext(Element element) throws LookupException {
  	return aliasedNamespace().lexicalContext();
  }

	@Override
	public List<Namespace> getSubNamespaces() {
		return aliasedNamespace().getSubNamespaces();
	}

	@Override
	public Namespace getOrCreateNamespace(String name) throws LookupException {
		return aliasedNamespace().getOrCreateNamespace(name);
	}

}
