package be.kuleuven.cs.distrinet.chameleon.ui.widget.tree;

import java.util.HashSet;
import java.util.Set;

import be.kuleuven.cs.distrinet.chameleon.core.element.Element;
import be.kuleuven.cs.distrinet.chameleon.ui.widget.LabelProvider;
import be.kuleuven.cs.distrinet.chameleon.ui.widget.PredicateSelector;
import be.kuleuven.cs.distrinet.chameleon.ui.widget.SelectionController;
import be.kuleuven.cs.distrinet.chameleon.ui.widget.WidgetFactory;
import be.kuleuven.cs.distrinet.chameleon.util.Util;
import be.kuleuven.cs.distrinet.rejuse.action.Nothing;
import be.kuleuven.cs.distrinet.rejuse.predicate.False;
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
	
	public static abstract class TristatePredicateGenerator<X,Y> {
		
		public TristatePredicateGenerator(TristatePredicateGenerator<X,Y> next) {
			_next = next;
		}
		
		public UniversalPredicate<? super Y, Nothing> create(TreeNode<?, X> treeNode, Set<TreeNode<?,X>> checked, Set<TreeNode<?,X>> grayed) {
//			UniversalPredicate<? super Y, Nothing> childrenPredicate = childrenDisjunction(treeNode, checked, grayed);
			return create(treeNode, checked, grayed, this);
		}
		
		
		
		private TristatePredicateGenerator<X, Y> _next;

		protected UniversalPredicate<? super Y, Nothing> create(
				TreeNode<?, X> treeNode, 
				Set<TreeNode<?,X>> checked, 
				Set<TreeNode<?,X>> grayed,
				TristatePredicateGenerator<X,Y> first) {
			
			UniversalPredicate<? super Y, Nothing> result = null;
			if(checked.contains(treeNode)) {
				result = checked(treeNode,checked,grayed, first);
			} else if(grayed.contains(treeNode)) {
				result = grayed(treeNode,checked,grayed, first);
			}	else {
				result = new False();
			}
			if(result == null) {
				if(_next != null) {
					result = _next.create(treeNode, checked, grayed, first);
				} else {
					result = new False();
				}
			}
			return result;
		}
		
		public UniversalPredicate<? super Y, Nothing> childrenDisjunction(TreeNode<?,X> node, Set<TreeNode<?,X>> checked,
				Set<TreeNode<?,X>> grayed,TristatePredicateGenerator<X,Y>  first) {
			UniversalPredicate<? super Y, Nothing> result = new False();
			for(TreeNode<?,X> child: node.children()) {
				result = result.or((UniversalPredicate)create(child, checked, grayed,first));
			}
			return result;
		}


		protected abstract UniversalPredicate<? super Y, Nothing> grayed(TreeNode<?,X> node, Set<TreeNode<?,X>> checked, Set<TreeNode<?,X>> grayed,TristatePredicateGenerator<X,Y>  first);

		protected abstract UniversalPredicate<? super Y, Nothing> checked(TreeNode<?,X> node, Set<TreeNode<?,X>> checked, Set<TreeNode<?,X>> grayed,TristatePredicateGenerator<X,Y>  first);
	}
	
	private TristatePredicateGenerator<D,E> _generator;
	
	@Override
	public UniversalPredicate<? super E, Nothing> predicate() {
		return _generator.create(_root, _selected, _grayed);
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
