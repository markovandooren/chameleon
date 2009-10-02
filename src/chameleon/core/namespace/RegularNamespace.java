package chameleon.core.namespace;

import java.util.List;

import org.rejuse.association.OrderedMultiAssociation;
import org.rejuse.association.MultiAssociation;

import chameleon.core.declaration.SimpleNameSignature;
import chameleon.core.element.Element;
import chameleon.core.lookup.LookupException;
import chameleon.core.lookup.LookupStrategy;
import chameleon.core.namespacepart.NamespacePart;
import chameleon.core.scope.Scope;
import chameleon.core.scope.UniversalScope;
import chameleon.core.validation.Valid;
import chameleon.core.validation.VerificationResult;
import chameleon.util.Util;

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
	private OrderedMultiAssociation<Namespace,Namespace> _namespaces = new OrderedMultiAssociation<Namespace,Namespace>(this);


	protected void addNamespace(Namespace namespace) {
		if(namespace != null) {
			_namespaces.add(namespace.parentLink());
		}
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

	private OrderedMultiAssociation<Namespace,NamespacePart> _namespaceParts = new OrderedMultiAssociation<Namespace,NamespacePart>(this);

	public OrderedMultiAssociation getNamespacePartsLink(){
		return _namespaceParts;
	}

	public void addNamespacePart(NamespacePart namespacePart){
		if(namespacePart != null) {
		  namespacePart.getNamespaceLink().connectTo(_namespaceParts);
		}
	}

	public List<NamespacePart> getNamespaceParts(){
		return _namespaceParts.getOtherEnds();
	}

	@Override
	public Namespace clone() {
		RegularNamespace result = new RegularNamespace(signature().clone());
		for(Namespace sub:getSubNamespaces()) {
			result.addNamespace(sub.clone());
		}
		for(NamespacePart part:getNamespaceParts()) {
			result.addNamespacePart(part.clone());
		}
		return result;
	}
	
	public Scope scope() {
		return new UniversalScope();
	}
	
  public LookupStrategy lexicalLookupStrategy(Element element) throws LookupException {
  	return language().lookupFactory().createLexicalLookupStrategy(targetContext(), this);
  }
  
	public Namespace getOrCreateNamespace(final String name) throws LookupException {
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
}
