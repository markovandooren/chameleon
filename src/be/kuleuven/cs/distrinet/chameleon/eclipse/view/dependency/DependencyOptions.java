package be.kuleuven.cs.distrinet.chameleon.eclipse.view.dependency;

import java.util.List;

import be.kuleuven.cs.distrinet.chameleon.core.element.Element;
import be.kuleuven.cs.distrinet.chameleon.eclipse.widget.PredicateSelector;
import be.kuleuven.cs.distrinet.chameleon.plugin.LanguagePlugin;

public interface DependencyOptions extends LanguagePlugin {

	public List<PredicateSelector<? super Element, ?>> sourceOptions();

	public List<PredicateSelector<? super Element, ?>> targetOptions();
}
