package be.kuleuven.cs.distrinet.chameleon.eclipse.view.dependency;

import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;

import be.kuleuven.cs.distrinet.chameleon.analysis.OptionGroup;
import be.kuleuven.cs.distrinet.chameleon.eclipse.widget.SWTWidgetFactory;
import be.kuleuven.cs.distrinet.chameleon.workspace.Project;

public class SelectorList extends Composite {


	public SelectorList(Composite parent, int style, OptionGroup group, Project project) {
		super(parent, style);
		GridLayout layout = new GridLayout();
		layout.numColumns = 1;
		setLayout(layout);

//		final Language language = project.views().get(0).language();
//		DependencyOptionsFactory plugin = language.plugin(DependencyOptionsFactory.class);
//		if(plugin == null) {
//			plugin = new DefaultDependencyOptionsFactory();
//		}
//		DependencyOptions config = plugin.createConfiguration();
//		_currentSelectors = new ArrayList<>();
//		for(PredicateSelector selector: mapper.apply(config)) {
//			_currentSelectors.add(selector);
//		}
//		_currentSelectors = group.createControls(widgetFactory);
		// Create the controls.
		SWTWidgetFactory widgetFactory = new SWTWidgetFactory(){
			@Override
			public Composite parent() {
				return SelectorList.this;
			}
		};
		group.createControls(widgetFactory);
//		for(PredicateSelector selector: _currentSelectors) {
//			selector.createControl(widgetFactory);
//			selector.setContext(project);
//		}
		
		SelectorList.this.layout(true);
	}

//	private void test(Project project) {
//		TreeContentProvider contentProvider = new LexicalTreeContentProvider();
//		LabelProvider provider = new TreeViewNodeLabelProvider();
//		SelectionController<TristateTreeViewer> tristateTree = new SWTWidgetFactory() {
//			@Override
//			public Composite parent() {
//				return SelectorList.this;
//			}
//		}.createTristateTree(contentProvider, provider,new TreeListener(){
//		
//			@Override
//			public void itemChanged(TreeNode data, boolean checked, boolean grayed) {
//				
//			}
//		});
//		tristateTree.setContext(contentProvider.createNode(project));
//		tristateTree.widget().getTree().getItem(0).setChecked(true);
//	}
	
	
	

//	public UniversalPredicate predicate() {
//		UniversalPredicate result = new True();
//		for(PredicateSelector selector: _currentSelectors) {
//			result = result.and(selector.predicate());
//		}
//		return result;
//	}

//	private List<PredicateSelector> _currentSelectors = new ArrayList<>();
}