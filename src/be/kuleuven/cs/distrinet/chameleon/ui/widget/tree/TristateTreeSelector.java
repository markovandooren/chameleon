package be.kuleuven.cs.distrinet.chameleon.ui.widget.tree;

import java.util.HashSet;
import java.util.Set;

import be.kuleuven.cs.distrinet.chameleon.ui.widget.LabelProvider;
import be.kuleuven.cs.distrinet.chameleon.ui.widget.PredicateSelector;
import be.kuleuven.cs.distrinet.chameleon.ui.widget.SelectionController;
import be.kuleuven.cs.distrinet.chameleon.ui.widget.WidgetFactory;
import be.kuleuven.cs.distrinet.chameleon.util.Util;
import be.kuleuven.cs.distrinet.rejuse.action.Nothing;
import be.kuleuven.cs.distrinet.rejuse.predicate.True;
import be.kuleuven.cs.distrinet.rejuse.predicate.UniversalPredicate;

/**
 * 
 * @author Marko van Dooren
 *
 * @param <V>
 * @param <D>
 * @param <E>
 */
public class TristateTreeSelector<D,E> extends PredicateSelector<E>{

	public TristateTreeSelector(TreeContentProvider<D> contentProvider, 
			                        TristatePredicateGenerator<D,E> generator, LabelProvider provider) {
		_contentProvider = contentProvider;
		_generator = generator;
		_labelProvider = provider;
	}
	
	private TreeContentProvider<D> _contentProvider;
	
	private LabelProvider _labelProvider;
	
	private SelectionController<?> _controller;
	
	@Override
	public <W> SelectionController<? extends W> createControl(WidgetFactory<W> factory) {
		SelectionController<? extends W> createTristateTree = 
				factory.createTristateTree(_contentProvider, _labelProvider,new TreeListener<D>(){
				
					@SuppressWarnings("unchecked")
					@Override
					public void itemChanged(TreeNode<D> data, boolean checked, boolean grayed) {
						if(data != null) {
//							D domainObject = _contentProvider.domainData((V)data);
							TreeNode<D> domainObject = data;
							if(! checked) {
								_selected.remove(domainObject);
								_grayed.remove(domainObject);
							} else {
								if(grayed) {
									_selected.remove(domainObject);
									_grayed.add(domainObject);
								} else {
									_selected.add(domainObject);
									_grayed.remove(domainObject);
								}
							}
						}
					}
				});
		_controller = createTristateTree;
		return createTristateTree;
	}
	
	private Set<TreeNode<? extends D>> _selected= new HashSet<>();
	private Set<TreeNode<? extends D>> _grayed= new HashSet<>();
	
	public static interface TristatePredicateGenerator<X,Y> {
		
		public UniversalPredicate<? super Y, Nothing> create(TreeNode<? extends X> root, Set<TreeNode<? extends X>> checked, Set<TreeNode<? extends X>> grayed); 
		
	}
	
	private TristatePredicateGenerator<D,E> _generator;
	
	@Override
	public UniversalPredicate<? super E, Nothing> predicate() {
		return _generator.create(_root, _selected, _grayed);
	}
	
	private TreeNode<? extends D> _root;
	
	@Override
	public void setContext(Object context) {
		_root = _contentProvider.createNode((D)context);
		_selected.clear();
		_grayed.clear();
		_grayed.add(_root);
		_controller.setContext(_root);
	}

}
