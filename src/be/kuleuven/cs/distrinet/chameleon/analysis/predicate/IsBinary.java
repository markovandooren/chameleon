package be.kuleuven.cs.distrinet.chameleon.analysis.predicate;

import java.util.List;

import be.kuleuven.cs.distrinet.chameleon.core.element.Element;
import be.kuleuven.cs.distrinet.chameleon.core.namespace.Namespace;
import be.kuleuven.cs.distrinet.chameleon.workspace.DocumentScanner;
import be.kuleuven.cs.distrinet.chameleon.workspace.View;
import be.kuleuven.cs.distrinet.rejuse.action.Nothing;
import be.kuleuven.cs.distrinet.rejuse.tree.TreePredicate;

public class IsBinary extends TreePredicate<Element,Nothing> {

	public IsBinary() {
		super(Element.class);
	}

	@Override
	public boolean uncheckedEval(Element element) throws Nothing {
		View view = element.view();
		if(element instanceof Namespace) {
			List<DocumentScanner> binaryLoaders = view.binaryLoaders();
			for(DocumentScanner loader:binaryLoaders) {
				if(loader.namespaces().contains(element)) {
					return true;
				}
			}
			return false;
		} else {
			return view.isBinary(element);
		}
	}

	@Override
	public boolean canSucceedBeyond(Element element) throws Nothing {
		View view = element.view();
		if(element instanceof Namespace) {
			List<DocumentScanner> binaryLoaders = view.binaryLoaders();
			for(DocumentScanner loader:binaryLoaders) {
				for(Namespace ns: loader.namespaces()) {
					if(ns == element || ns.hasAncestor(element)) {
						return true;
					}
				}
			}
			return false;
		} else {
			return view.isBinary(element);
		}
	} 

}
