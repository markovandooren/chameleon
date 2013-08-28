package be.kuleuven.cs.distrinet.chameleon.core.namespace;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import be.kuleuven.cs.distrinet.chameleon.core.declaration.Declaration;
import be.kuleuven.cs.distrinet.chameleon.core.declaration.SimpleNameSignature;
import be.kuleuven.cs.distrinet.chameleon.core.element.Element;
import be.kuleuven.cs.distrinet.chameleon.core.lookup.LookupContext;
import be.kuleuven.cs.distrinet.chameleon.core.lookup.LookupException;
import be.kuleuven.cs.distrinet.chameleon.core.namespacedeclaration.NamespaceDeclaration;
import be.kuleuven.cs.distrinet.chameleon.core.scope.Scope;
import be.kuleuven.cs.distrinet.chameleon.core.scope.UniversalScope;
import be.kuleuven.cs.distrinet.chameleon.core.validation.Valid;
import be.kuleuven.cs.distrinet.chameleon.core.validation.Verification;
import be.kuleuven.cs.distrinet.chameleon.util.association.Multi;
import be.kuleuven.cs.distrinet.rejuse.association.Association;

public class RegularNamespace extends NamespaceImpl {
	
	public RegularNamespace(String name) {
		this(new SimpleNameSignature(name));
	}
	
	public RegularNamespace(SimpleNameSignature sig) {
		super(sig);
	}

	protected RegularNamespace() {
	}
	/**
	 * SUBNAMESPACES
	 */
	private Multi<Namespace> _namespaces = new Multi<Namespace>(this,"namespaces") {
		@Override
		protected void fireElementAdded(Namespace addedElement) {
			super.fireElementAdded(addedElement);
			registerNamespace(addedElement);
		}
		
		protected void fireElementRemoved(Namespace addedElement) {
			super.fireElementRemoved(addedElement);
			unregisterNamespace(addedElement);
		};
		
		protected void fireElementReplaced(Namespace oldElement, Namespace newElement) {
			super.fireElementReplaced(oldElement, newElement);
			fireElementAdded(newElement);
			fireElementRemoved(oldElement);
		};
	};
	{
		_namespaces.enableCache();
	}

	protected synchronized void addNamespace(Namespace namespace) {
		add(_namespaces,namespace);
		updateCacheNamespaceAdded(namespace);
	}
	
	protected void updateCacheNamespaceAdded(Namespace namespace) {
		flushLocalCache();
	}

	/**
	 * Return all subpackages of this package.
	 */
	public List<Namespace> getSubNamespaces() {
		return _namespaces.getOtherEnds();
	}


	
	/*******************
	 * NAMESPACE PARTS *
	 *******************/

	private Multi<NamespaceDeclaration> _namespaceParts = new Multi<NamespaceDeclaration>(this,"namespace parts");
	{
		_namespaceParts.enableCache();
	}

	public synchronized void addNamespacePart(NamespaceDeclaration namespacePart){
		_namespaceParts.add((Association)namespacePart.namespaceLink());
		flushLocalCache();
	}

	public List<NamespaceDeclaration> getNamespaceParts(){
		return _namespaceParts.getOtherEnds();
	}

	protected RegularNamespace cloneSelf() {
		return new RegularNamespace();
	}
	
	public Scope scope() {
		return new UniversalScope();
	}
	
  public LookupContext lookupContext(Element element) throws LookupException {
  	return language().lookupFactory().createLexicalLookupStrategy(targetContext(), this);
  }
  
	/**
	 * Create a new package with the given name
	 * @param name
	 *        The name of the new package.
	 */
	/*@
	 @ protected behavior
	 @
	 @ post \result != null;
	 @*/
	public Namespace createSubNamespace(String name){
	  Namespace result = new RegularNamespace(name);
	  addNamespace(result);
		return result;
	}

	@Override
	public Verification verifySelf() {
		return Valid.create();
	}

	public Declaration declarator() {
		return this;
	}

	public List<? extends Declaration> locallyDeclaredDeclarations() throws LookupException {
		return declarations();
	}

	@Override
	public boolean complete() {
		return true;
	}
	
	@Override
	public boolean hasSubNamespaces() {
		return _namespaces.size() > 0;
	}
}
