package be.kuleuven.cs.distrinet.chameleon.ui.widget;

import java.util.HashSet;
import java.util.Set;

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
public class TristateTreeSelector<V,D,E> extends PredicateSelector<E>{

	public TristateTreeSelector(TreeContentProvider<V,D> contentProvider, 
			                        TristatePredicateGenerator<E> generator, LabelProvider provider) {
		_contentProvider = contentProvider;
		_generator = generator;
		_labelProvider = provider;
	}
	
	private TreeContentProvider<V,D> _contentProvider;
	
	private LabelProvider _labelProvider;
	
	private SelectionController<?> _controller;
	
	@Override
	public <W> SelectionController<? extends W> createControl(WidgetFactory<W> factory) {
		SelectionController<? extends W> createTristateTree = 
				factory.createTristateTree(_contentProvider, _labelProvider,new TreeListener(){
				
					@SuppressWarnings("unchecked")
					@Override
					public void itemChanged(Object data, boolean checked, boolean grayed) {
						if(data != null) {
							D domainObject = _contentProvider.domainData((V)data);
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
	
	private Set<D> _selected= new HashSet<>();
	private Set<D> _grayed= new HashSet<>();
	
	public static interface TristatePredicateGenerator<X> {
		
		public UniversalPredicate<? super X, Nothing> create(Object domainObject, boolean checked, boolean grayed); 
		
	}
	
	private TristatePredicateGenerator<E> _generator;
	
	@Override
	public UniversalPredicate<? super E, Nothing> predicate() {
		UniversalPredicate<? super E, Nothing> result = new True();
		for(D t: _selected) {
			result = result.or((UniversalPredicate)_generator.create(t, true, false));
		}
		for(D t: _grayed) {
			result = result.or((UniversalPredicate)_generator.create(t, false, true));
		}
		return result;
	}
	
	@Override
	public void setContext(Object context) {
		_controller.setContext(_contentProvider.createNode(context));
	}

}
