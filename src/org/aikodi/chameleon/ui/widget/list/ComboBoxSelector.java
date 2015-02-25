package org.aikodi.chameleon.ui.widget.list;

import org.aikodi.chameleon.ui.widget.LabelProvider;
import org.aikodi.chameleon.ui.widget.PredicateSelector;
import org.aikodi.chameleon.ui.widget.SelectionController;
import org.aikodi.chameleon.ui.widget.WidgetFactory;

import be.kuleuven.cs.distrinet.rejuse.action.Nothing;
import be.kuleuven.cs.distrinet.rejuse.function.Function;
import be.kuleuven.cs.distrinet.rejuse.predicate.UniversalPredicate;

public class ComboBoxSelector<T,E> implements PredicateSelector<E> {

	/**
	 * Create a new combo box selector.   The
	 * default selection is used to set the initially selected element. NOTE THAT
	 * THIS INDEX IS BASE 1. If you provide 0 or less, no element is selected by default.
	 * 
	 * @param contentProvider The content provider used to create a list
	 * of items from a context object.
	 * @param labelProvider The label provider that generates labels for these 
	 * items. 
	 * @param predicateFactory The function that creates a predicate based on the current selection.
	 * @param baseOneSelectionIndex The base-1 index of the element that should be selected initially.
	 */
	public ComboBoxSelector(
			ListContentProvider<T> contentProvider, 
			LabelProvider labelProvider,
			Function<? super T,UniversalPredicate<? super E, Nothing>,Nothing> predicateFactory, int baseOneSelectionIndex) {
		this._labelProvider = labelProvider;
		this._contentProvider = contentProvider;
		_predicateFactory = predicateFactory;
		_defaultSelection = baseOneSelectionIndex;
	}
	
	private int _defaultSelection;

	@Override
	public <W> SelectionController createControl(WidgetFactory<W> factory) {
		_controller = factory.createComboBox(_contentProvider, _labelProvider, new ComboBoxListener(){
		
			@Override
			public void itemSelected(Object item) {
				_selected = (T)item;
			}
		},_defaultSelection);
		return _controller;
	}

	private Function<? super T,UniversalPredicate<? super E, Nothing>,Nothing> _predicateFactory;
	
	private T _selected;
	
	@Override
	public UniversalPredicate<? super E, Nothing> predicate() {
		return _predicateFactory.apply(_selected);
	}

	private LabelProvider _labelProvider;

	private ListContentProvider<T> _contentProvider;

	private SelectionController<?> _controller;
	
	@Override
	public void setContext(Object context) {
		_controller.setContext(context);
	}


}
