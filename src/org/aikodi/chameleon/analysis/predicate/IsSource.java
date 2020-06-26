package org.aikodi.chameleon.analysis.predicate;

import java.util.List;

import org.aikodi.chameleon.core.element.Element;
import org.aikodi.chameleon.core.namespace.Namespace;
import org.aikodi.chameleon.workspace.DocumentScanner;
import org.aikodi.chameleon.workspace.View;
import org.aikodi.rejuse.action.Nothing;
import org.aikodi.rejuse.data.tree.TreePredicate;

public class IsSource extends TreePredicate<Element,Nothing> {

	public IsSource() {
		super(Element.class);
	}

	@Override
	public boolean uncheckedEval(Element element) throws Nothing {
		View view = element.view();
		if(element instanceof Namespace) {
			List<DocumentScanner> sourceScanners = view.sourceScanners();
			for(DocumentScanner scanner : sourceScanners) {
				if(scanner.namespaces().contains(element)) {
					return true;
				}
			}
			return false;
		} else {
			return view.isSource(element);
		}
	}

	@Override
	public boolean canSucceedBeyond(Element element) throws Nothing {
		View view = element.view();
		if(element instanceof Namespace) {
			List<DocumentScanner> sourceScanners = view.sourceScanners();
			for(DocumentScanner scanner : sourceScanners) {
				for(Namespace ns: scanner.namespaces()) {
					if(ns == element || ns.lexical().hasAncestor(element)) {
						return true;
					}
				}
			}
			return false;
		} else {
			return view.isSource(element);
		}
	} 

}
