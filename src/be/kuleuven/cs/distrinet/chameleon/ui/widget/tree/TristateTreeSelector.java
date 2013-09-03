package be.kuleuven.cs.distrinet.chameleon.ui.widget.tree;

import java.util.HashSet;
import java.util.Set;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;

import be.kuleuven.cs.distrinet.chameleon.ui.widget.LabelProvider;
import be.kuleuven.cs.distrinet.chameleon.ui.widget.SelectionController;
import be.kuleuven.cs.distrinet.chameleon.ui.widget.Selector;
import be.kuleuven.cs.distrinet.chameleon.ui.widget.WidgetFactory;

/**
 * 
 * @author Marko van Dooren
 *
 * @param <V>
 * @param <D>
 * @param <E>
 */
public class TristateTreeSelector<D> extends Selector {

	public TristateTreeSelector(TreeContentProvider<D> contentProvider, LabelProvider provider) {
		_contentProvider = contentProvider;
//		_generator = generator;
		_labelProvider = provider;
	}
	
	private TreeContentProvider<D> _contentProvider;
	
	private LabelProvider _labelProvider;
	
	private TristateTreeController<?> _controller;
	
	@Override
	public <W> SelectionController<? extends W> createControl(WidgetFactory<W> factory) {
		TristateTreeController<? extends W> createTristateTree = 
				factory.createTristateTree(_contentProvider, _labelProvider,new TreeListener<D>(){
				
					@SuppressWarnings("unchecked")
					@Override
					public void itemChanged(TreeNode<?,D> data, boolean checked, boolean grayed) {
						if(data != null) {
//							D domainObject = _contentProvider.domainData((V)data);
							TreeNode<?,D> domainObject = data;
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
	
	private Set<TreeNode<? , D>> _selected= new HashSet<>();
	private Set<TreeNode<? , D>> _grayed= new HashSet<>();
	
	public Set<TreeNode<?,D>> grayed() {
		return ImmutableSet.copyOf(_grayed);
	}

	public Set<TreeNode<?,D>> checked() {
		return ImmutableSet.copyOf(_selected);
	}
	
	public TreeNode<?, D> root() {
		return _root;
	}

	private TreeNode<?, D> _root;
	
	@Override
	public void setContext(Object context) {
		_root = _contentProvider.createNode((D)context);
		_selected.clear();
		_grayed.clear();
		_grayed.add(_root);
		_controller.setContext(_root);
		for(TreeNode<?, D> node: _initialSelection) {
			_controller.setChecked(node);
		}
	}

	private Set<TreeNode<?,D>> _initialSelection = new HashSet<>();
}
