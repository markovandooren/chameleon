package chameleon.core.namespace;

import java.util.List;

import org.rejuse.association.OrderedReferenceSet;
import org.rejuse.association.ReferenceSet;

import chameleon.core.declaration.SimpleNameSignature;
import chameleon.core.element.Element;
import chameleon.core.lookup.LookupException;
import chameleon.core.lookup.LookupStrategy;
import chameleon.core.namespacepart.NamespacePart;
import chameleon.core.scope.Scope;
import chameleon.core.scope.UniversalScope;
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
	private OrderedReferenceSet<Namespace,Namespace> _namespaces = new OrderedReferenceSet<Namespace,Namespace>(this);


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

	private OrderedReferenceSet<Namespace,NamespacePart> _namespaceParts = new OrderedReferenceSet<Namespace,NamespacePart>(this);

	public OrderedReferenceSet getNamespacePartsLink(){
		return _namespaceParts;
	}

	public void addNamespacePart(NamespacePart namespacePart){
		if(namespacePart != null) {
		  namespacePart.getNamespaceLink().connectTo(_namespaceParts);
		}
	}

	public List getNamespaceParts(){
		return _namespaceParts.getOtherEnds();
	}

	@Override
	public Namespace clone() {
		return new RegularNamespace(signature().clone());
	}
	
	public Scope scope() {
		return new UniversalScope();
	}
	
  public LookupStrategy lexicalContext(Element element) throws LookupException {
  	return language().lookupFactory().createLexicalContext(this, targetContext());
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
	

}
