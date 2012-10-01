package chameleon.core.namespace;

import java.util.List;

import chameleon.core.declaration.Declaration;
import chameleon.core.declaration.DeclarationContainer;
import chameleon.core.declaration.SimpleNameSignature;
import chameleon.core.lookup.LookupException;
import chameleon.core.namespacedeclaration.NamespaceDeclaration;

public interface Namespace extends Declaration, DeclarationContainer {

	public String getFullyQualifiedName();
	
	public Namespace defaultNamespace();
	
	public Namespace getOrCreateNamespace(final String name) throws LookupException;
	
	public <T extends Declaration> List<T> allDeclarations(Class<T> kind) throws LookupException;
	
	public Namespace clone();
	
	public List<NamespaceDeclaration> getNamespaceParts();
	
	public void addNamespacePart(NamespaceDeclaration namespacePart);
	
	public List<Namespace> getSubNamespaces();
	
	public Namespace getSubNamespace(final String name) throws LookupException;
	
	public NamespaceAlias alias(SimpleNameSignature sig);
	
	public Namespace createSubNamespace(String name);
	
	public <T extends Declaration> List<T> declarations(Class<T> kind) throws LookupException;
}
