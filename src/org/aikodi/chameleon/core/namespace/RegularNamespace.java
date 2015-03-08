package org.aikodi.chameleon.core.namespace;

import java.util.List;

import org.aikodi.chameleon.core.declaration.Declaration;
import org.aikodi.chameleon.core.element.Element;
import org.aikodi.chameleon.core.lookup.LookupContext;
import org.aikodi.chameleon.core.lookup.LookupException;
import org.aikodi.chameleon.core.namespacedeclaration.NamespaceDeclaration;
import org.aikodi.chameleon.core.scope.Scope;
import org.aikodi.chameleon.core.scope.UniversalScope;
import org.aikodi.chameleon.core.validation.Valid;
import org.aikodi.chameleon.core.validation.Verification;
import org.aikodi.chameleon.util.association.Multi;

import be.kuleuven.cs.distrinet.rejuse.association.Association;

import com.google.common.collect.ImmutableList;

public class RegularNamespace extends NamespaceImpl {
	
	public RegularNamespace(String name) {
		super(name);
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
		
		@Override
      protected void fireElementRemoved(Namespace addedElement) {
			super.fireElementRemoved(addedElement);
			unregisterNamespace(addedElement);
		};
		
		@Override
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
	@Override
   public List<Namespace> subNamespaces() {
		return _namespaces.getOtherEnds();
	}


	
	/*******************
	 * NAMESPACE PARTS *
	 *******************/

	private Multi<NamespaceDeclaration> _namespaceDeclarations = new Multi<NamespaceDeclaration>(this,"namespace parts");
	{
		_namespaceDeclarations.enableCache();
	}

	@Override
   public synchronized void addNamespacePart(NamespaceDeclaration namespacePart){
		_namespaceDeclarations.add((Association)namespacePart.namespaceLink());
		flushLocalCache();
	}

	@Override
   public synchronized List<NamespaceDeclaration> namespaceDeclarations(){
		return _namespaceDeclarations.getOtherEnds();
	}
	
	@Override
	public List<NamespaceDeclaration> loadedNamespaceDeclarations() {
		return _namespaceDeclarations.getOtherEnds();
	}

	@Override
   protected RegularNamespace cloneSelf() {
		return new RegularNamespace(name());
	}
	
	@Override
   public Scope scope() {
		return new UniversalScope();
	}
	
  @Override
public LookupContext lookupContext(Element element) throws LookupException {
  	if(_context == null) {
  		_context = language().lookupFactory().createLexicalLookupStrategy(targetContext(), this);
  	}
		return _context;
  }
  
  private LookupContext _context;
  
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
	@Override
   public Namespace createSubNamespace(String name){
	  Namespace result = new RegularNamespace(name);
	  addNamespace(result);
		return result;
	}

	@Override
	public Verification verifySelf() {
		return Valid.create();
	}

	@Override
   public Declaration declarator() {
		return this;
	}

	@Override
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

	@Override
	public List<Namespace> descendantNamespaces() {
		ImmutableList.Builder<Namespace> builder = ImmutableList.builder();
		builder.addAll(_namespaces.getOtherEnds());
		for(Namespace ns:_namespaces.getOtherEnds()) {
			builder.addAll(ns.descendantNamespaces());
		}
		return builder.build();
	}
}
