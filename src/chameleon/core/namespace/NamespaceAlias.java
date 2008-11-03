package chameleon.core.namespace;

import java.util.List;

import chameleon.core.declaration.SimpleNameSignature;
import chameleon.core.element.ChameleonProgrammerException;
import chameleon.core.namespacepart.NamespacePart;

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

}
