package be.kuleuven.cs.distrinet.chameleon.analysis.predicate;

import java.util.List;

import be.kuleuven.cs.distrinet.chameleon.core.element.Element;
import be.kuleuven.cs.distrinet.chameleon.core.namespace.Namespace;
import be.kuleuven.cs.distrinet.chameleon.workspace.DocumentLoader;
import be.kuleuven.cs.distrinet.chameleon.workspace.View;
import be.kuleuven.cs.distrinet.rejuse.action.Nothing;
import be.kuleuven.cs.distrinet.rejuse.predicate.UniversalPredicate;

public class IsBinary extends UniversalPredicate<Element,Nothing> {

	public IsBinary() {
		super(Element.class);
	}

	@Override
	public boolean uncheckedEval(Element element) throws Nothing {
		View view = element.view();
		if(element instanceof Namespace) {
			List<DocumentLoader> binaryLoaders = view.binaryLoaders();
			for(DocumentLoader loader:binaryLoaders) {
				if(loader.namespaces().contains(element)) {
					return true;
				}
			}
			return false;
		} else {
			return view.isBinary(element);
		}
	} 

}
