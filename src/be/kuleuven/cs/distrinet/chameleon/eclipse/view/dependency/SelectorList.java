package be.kuleuven.cs.distrinet.chameleon.eclipse.view.dependency;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;

import be.kuleuven.cs.distrinet.chameleon.analysis.dependency.DefaultDependencyOptions;
import be.kuleuven.cs.distrinet.chameleon.analysis.dependency.DependencyOptions;
import be.kuleuven.cs.distrinet.chameleon.core.language.Language;
import be.kuleuven.cs.distrinet.chameleon.eclipse.widget.SWTWidgetFactory;
import be.kuleuven.cs.distrinet.chameleon.ui.widget.PredicateSelector;
import be.kuleuven.cs.distrinet.chameleon.ui.widget.SelectionController;
import be.kuleuven.cs.distrinet.chameleon.workspace.Project;
import be.kuleuven.cs.distrinet.rejuse.action.Nothing;
import be.kuleuven.cs.distrinet.rejuse.function.Function;
import be.kuleuven.cs.distrinet.rejuse.predicate.True;
import be.kuleuven.cs.distrinet.rejuse.predicate.UniversalPredicate;

public class SelectorList extends Composite {


	public SelectorList(Composite parent, int style, Function<DependencyConfiguration, List<PredicateSelector>, Nothing> mapper, Project project) {
		super(parent, style);
		GridLayout layout = new GridLayout();
		layout.numColumns = 1;
		setLayout(layout);
//		init(project, selector, widgetFactory);
//	}
//
//	private void init(final Project chameleonProject, Function<DependencyConfiguration, List<PredicateSelector>, Nothing> mapper, SWTWidgetFactory factory) {
		final Language language = project.views().get(0).language();
		DependencyOptions plugin = language.plugin(DependencyOptions.class);
		if(plugin == null) {
			plugin = new DefaultDependencyOptions();
		}
		DependencyConfiguration config = plugin.createConfiguration();
		_currentSelectors = new ArrayList<>();
		for(PredicateSelector selector: mapper.apply(config)) {
			_currentSelectors.add(selector);
		}
		
		// Create the controls.
		SWTWidgetFactory widgetFactory = new SWTWidgetFactory(){
			@Override
			public Composite parent() {
				return SelectorList.this;
			}
		};
		for(PredicateSelector selector: _currentSelectors) {
			SelectionController control = selector.createControl(widgetFactory);
		}
		
		
		SelectorList.this.layout(true);
		//				TypePredicate<Namespace> typePredicate = new TypePredicate<>(Namespace.class);
		//				SelectionController createTristateTree = new SWTWidgetFactory() {
		//					@Override
		//					public Composite parent() {
		//						return _tabCanvas;
		//					}
		//				}.createTristateTree(new PredicateContentProvider<>(typePredicate));
		//				createTristateTree.setContext(chameleonProject.views().get(0).namespace());

	}

	public UniversalPredicate predicate() {
		UniversalPredicate result = new True();
		for(PredicateSelector selector: _currentSelectors) {
			result = result.and(selector.predicate());
		}
		return result;
	}

	private List<PredicateSelector> _currentSelectors = new ArrayList<>();
}