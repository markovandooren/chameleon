package be.kuleuven.cs.distrinet.chameleon.ui.widget;

import be.kuleuven.cs.distrinet.chameleon.core.element.Element;
import be.kuleuven.cs.distrinet.rejuse.action.Nothing;
import be.kuleuven.cs.distrinet.rejuse.predicate.UniversalPredicate;

public class TristateTreeSelector<T extends Element> extends PredicateSelector<T>{

	@Override
	public <W> Input createControl(WidgetFactory<W> factory) {
		Input createTristateTree = factory.createTristateTree(new PredicateContentProvider<T>(predicate()));
		return createTristateTree;
	}
	
	private UniversalPredicate<T, Nothing> _predicate;

	@Override
	public UniversalPredicate<T, Nothing> predicate() {
		return _predicate;
	}

}
