package be.kuleuven.cs.distrinet.chameleon.eclipse.view.dependency;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Display;

import be.kuleuven.cs.distrinet.chameleon.analysis.dependency.DefaultDependencyOptions;
import be.kuleuven.cs.distrinet.chameleon.analysis.dependency.DependencyOptions;
import be.kuleuven.cs.distrinet.chameleon.core.language.Language;
import be.kuleuven.cs.distrinet.chameleon.eclipse.widget.SWTWidgetFactory;
import be.kuleuven.cs.distrinet.chameleon.ui.widget.PredicateSelector;
import be.kuleuven.cs.distrinet.chameleon.ui.widget.WidgetFactory;
import be.kuleuven.cs.distrinet.chameleon.workspace.Project;
import be.kuleuven.cs.distrinet.rejuse.action.Nothing;
import be.kuleuven.cs.distrinet.rejuse.function.Function;
import be.kuleuven.cs.distrinet.rejuse.predicate.True;
import be.kuleuven.cs.distrinet.rejuse.predicate.UniversalPredicate;

public class OptionsTab {
	public OptionsTab(Composite tabCanvas, Function<DependencyConfiguration, List<PredicateSelector>, Nothing> selector) {
		this._tabCanvas = tabCanvas;
		_tabWidgetFactory = new SWTWidgetFactory(){
			@Override
			public Composite parent() {
				return _tabCanvas;
			}
		};
		_configurationSelector = selector;
	}
	
	private Function<DependencyConfiguration, List<PredicateSelector>, Nothing> _configurationSelector;

	public void populate(final Project chameleonProject) {
		Display.getDefault().syncExec(new Runnable(){

			@Override
			public void run() {
				doClear();
				final Language language = chameleonProject.views().get(0).language();
				DependencyConfiguration configuration = _tabConfigurations.get(language);
				//					DependencyConfiguration configuration = null;
				if(configuration == null) {
					DependencyOptions plugin = language.plugin(DependencyOptions.class);
					if(plugin == null) {
						plugin = new DefaultDependencyOptions();
					}
					configuration = plugin.createConfiguration();
					_tabConfigurations.put(language, configuration);
				}
				for(PredicateSelector selector: _configurationSelector.apply(configuration)) {
					selector.createControl(_tabWidgetFactory);
					_tabSelectors.add(selector);
				}
				new SWTWidgetFactory() {
					@Override
					public Composite parent() {
						return _tabCanvas;
					}
				}.createCheckboxList();

				// The layout call on the view is required to make the tab expand in size, but
				// for some reason it is not enough. We must invoke layout() also directly on the
				// canvasses.
				_tabCanvas.layout(true);
			}
		});

	}

	public void clear() {

		Display.getDefault().syncExec(new Runnable(){
			@Override
			public void run() {
				doClear();
			}
		});
	}

	private void doClear() {
		for(Control control:_tabCanvas.getChildren()) {
			control.dispose();
		}
		_tabSelectors.clear();

	}

	public UniversalPredicate predicate() {
		UniversalPredicate result = new True();
		for(PredicateSelector selector: _tabSelectors) {
			result = result.and(selector.predicate());
		}
		return result;
	}


	private WidgetFactory<Control> _tabWidgetFactory;
	private List<PredicateSelector> _tabSelectors = new ArrayList<>();
	private Composite _tabCanvas;	
	private Map<Language, DependencyConfiguration> _tabConfigurations = new HashMap<>();
}