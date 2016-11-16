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
import org.aikodi.rejuse.association.Association;

import com.google.common.collect.ImmutableList;

public abstract class RegularNamespace extends NamespaceImpl {
	
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
	}
	
	/**
	 * Return all subpackages of this package.
	 */
	@Override
   public List<Namespace> subNamespaces() {
		return _namespaces.getOtherEnds();
	}

	@Override
   public Scope scope() {
		return new UniversalScope();
	}

	@Override
	public LookupContext lookupContext(Element element) throws LookupException {
		//FIXME Why is this even lazy? The chances of the lookup context of a namespace
		// not being requested is close to zero.
		if(_context == null) {
			_context = language().lookupFactory().createLexicalLookupStrategy(targetContext(), this);
		}
		return _context;
	}

	private LookupContext _context;


	@Override
   public List<? extends Declaration> locallyDeclaredDeclarations() throws LookupException {
		return declarations();
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
