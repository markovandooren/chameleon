package chameleon.core.namespace;

import java.util.List;

import org.rejuse.association.ReferenceSet;

import chameleon.core.declaration.SimpleNameSignature;
import chameleon.core.namespacepart.NamespacePart;
import chameleon.core.scope.Scope;
import chameleon.core.scope.UniversalScope;

public class RegularNamespace extends Namespace {
	
	public RegularNamespace(SimpleNameSignature sig) {
		super(sig);
	}

	/*******************
	 * NAMESPACE PARTS *
	 *******************/

	private ReferenceSet<Namespace,NamespacePart> _namespaceParts = new ReferenceSet<Namespace,NamespacePart>(this);

	public ReferenceSet getNamespacePartsLink(){
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

}
