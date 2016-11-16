package org.aikodi.chameleon.core.namespace;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.aikodi.chameleon.core.namespacedeclaration.NamespaceDeclaration;
import org.aikodi.chameleon.util.association.Multi;
import org.aikodi.rejuse.association.Association;

import com.google.common.collect.ImmutableList;

public class EagerNamespace extends RegularNamespace {

	public EagerNamespace(String name) {
		super(name);
	}

	/*******************
	 * NAMESPACE PARTS *
	 *******************/

	//private Multi<NamespaceDeclaration> _namespaceDeclarations = new Multi<NamespaceDeclaration>(this,"namespace parts") {
	private ImmutableList<NamespaceDeclaration> _namespaceDeclarations = new ImmutableList.Builder<NamespaceDeclaration>().build();

	@Override
	public synchronized void addNamespacePart(NamespaceDeclaration namespacePart){
		if(namespacePart == null) {
			throw new IllegalArgumentException();
		}
		_namespaceDeclarations = new ImmutableList.Builder<NamespaceDeclaration>().addAll(_namespaceDeclarations).add(namespacePart).build();
		namespacePart.connectNamespace(this);
		addCacheForNamespaceDeclaration(namespacePart);
	}

	@Override
	public void disconnectNamespaceDeclaration(NamespaceDeclaration namespaceDeclaration) {
		// Horribly inefficient.
		Stream<NamespaceDeclaration> stream = _namespaceDeclarations.stream().filter(nsd -> ! nsd.equals(namespaceDeclaration));
		_namespaceDeclarations = new ImmutableList.Builder<NamespaceDeclaration>().addAll(stream.collect(Collectors.toList())).build();
		namespaceDeclaration.declarations().forEach(d -> removeDeclaration(d));
	}

	@Override
	public List<NamespaceDeclaration> loadedNamespaceDeclarations() {
		return _namespaceDeclarations;
	}

	@Override
	public List<NamespaceDeclaration> namespaceDeclarations() {
		return loadedNamespaceDeclarations();
	}

	@Override
	protected EagerNamespace cloneSelf() {
		return new EagerNamespace(name());
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
	@Override
	public Namespace createSubNamespace(String name){
		Namespace result = new EagerNamespace(name);
		addNamespace(result);
		return result;
	}

}
