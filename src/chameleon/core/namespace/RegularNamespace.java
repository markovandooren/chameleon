package chameleon.core.namespace;

import java.util.List;

import org.rejuse.association.Association;
import org.rejuse.association.OrderedMultiAssociation;

import chameleon.core.declaration.Declaration;
import chameleon.core.declaration.SimpleNameSignature;
import chameleon.core.element.Element;
import chameleon.core.lookup.LookupException;
import chameleon.core.lookup.LookupStrategy;
import chameleon.core.namespacepart.NamespaceDeclaration;
import chameleon.core.scope.Scope;
import chameleon.core.scope.UniversalScope;
import chameleon.core.validation.Valid;
import chameleon.core.validation.VerificationResult;
import chameleon.util.Util;
import chameleon.util.association.Multi;

public class RegularNamespace extends Namespace {
	
	public RegularNamespace(SimpleNameSignature sig) {
		super(sig);
	}

	public RegularNamespace(SimpleNameSignature sig, RegularNamespace parent) {
		super(sig);
		parent.addNamespace(this);
	}
	
	/**
	 * SUBNAMESPACES
	 */
	private Multi<Namespace> _namespaces = new Multi<Namespace>(this);


	protected void addNamespace(Namespace namespace) {
		add(_namespaces,namespace);
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

	private Multi<NamespaceDeclaration> _namespaceParts = new Multi<NamespaceDeclaration>(this);

	public OrderedMultiAssociation getNamespacePartsLink(){
		return _namespaceParts;
	}

	public synchronized void addNamespacePart(NamespaceDeclaration namespacePart){
		_namespaceParts.add((Association)namespacePart.namespaceLink());
		flushLocalCache();
	}

	public List<NamespaceDeclaration> getNamespaceParts(){
		return _namespaceParts.getOtherEnds();
	}

	@Override
	public RegularNamespace clone() {
		RegularNamespace result = cloneThis();
		for(Namespace sub:getSubNamespaces()) {
			result.addNamespace(sub.clone());
		}
		for(NamespaceDeclaration part:getNamespaceParts()) {
			result.addNamespacePart(part.clone());
		}
		return result;
	}
	
	protected RegularNamespace cloneThis() {
		return new RegularNamespace(signature().clone());
	}
	
	public Scope scope() {
		return new UniversalScope();
	}
	
  public LookupStrategy lexicalLookupStrategy(Element element) throws LookupException {
  	return language().lookupFactory().createLexicalLookupStrategy(targetContext(), this);
  }
  
	public synchronized Namespace getOrCreateNamespace(final String name) throws LookupException {
		if ((name == null) || name.equals("")) {
			return this;
		}
//		System.out.println("Before getting or creating namespace: "+name +" I have "+getSubNamespaces().size()+" sub namespaces.");
		final String current = Util.getFirstPart(name);
		final String next = Util.getSecondPart(name); //rest
		Namespace currentPackage = getSubNamespace(current);
		if(currentPackage == null) {
//			System.out.println("Namespace "+getFullyQualifiedName() + " is creating sub namespace "+current);
			currentPackage = createNamespace(current);
		}
//		System.out.println("After getting or creating namespace: "+name +" I have "+getSubNamespaces().size()+" sub namespaces.");
		return currentPackage.getOrCreateNamespace(next);
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
	protected Namespace createNamespace(String name){
	  return new RegularNamespace(new SimpleNameSignature(name), this);
	}

	@Override
	public VerificationResult verifySelf() {
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
}
