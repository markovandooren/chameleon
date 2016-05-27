package org.aikodi.chameleon.eclipse.widget;

import java.util.List;

import org.aikodi.chameleon.ui.widget.LabelProvider;
import org.aikodi.chameleon.ui.widget.SelectionController;
import org.aikodi.chameleon.ui.widget.WidgetFactory;
import org.aikodi.chameleon.ui.widget.checkbox.CheckboxListener;
import org.aikodi.chameleon.ui.widget.list.ComboBoxController;
import org.aikodi.chameleon.ui.widget.list.ComboBoxListener;
import org.aikodi.chameleon.ui.widget.list.ListContentProvider;
import org.aikodi.chameleon.ui.widget.tree.CheckStateProvider;
import org.aikodi.chameleon.ui.widget.tree.TreeContentProvider;
import org.aikodi.chameleon.ui.widget.tree.TreeListener;
import org.aikodi.chameleon.ui.widget.tree.TristateTreeController;
import org.eclipse.jface.viewers.ICheckStateProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

import com.google.common.collect.ImmutableList;


public abstract class SWTWidgetFactory implements WidgetFactory<Control> {

	@Override
	public SelectionController<Button> createCheckbox(String text, boolean initialState, final CheckboxListener listener) {
		final Button button = new Button(parent(), SWT.CHECK);
		final GridData layoutData = new GridData();
		button.setLayoutData(layoutData);
		button.setText(text);
		button.setSelection(initialState);
		button.addSelectionListener(new SelectionListener(){
		
			@Override
			public void widgetSelected(SelectionEvent e) {
				listener.selectionChanged(button.getSelection());
			}
		
			@Override
			public void widgetDefaultSelected(SelectionEvent e) {
				listener.selectionChanged(button.getSelection());
			}
		});
		return new SelectionController<Button>(){
			@Override
			public void setContext(Object element) {
			}

			@Override
			public void show() {
				layoutData.exclude=false;
				button.setVisible(true);
			}

			@Override
			public void hide() {
				layoutData.exclude=true;
				button.setVisible(false);
			}
			
			@Override
			public Button widget() {
				return button;
			}
		};
	}

	@Override
	public <V> TristateTreeController<TristateTreeViewer> createTristateTree(
			TreeContentProvider<V> contentProvider,
			LabelProvider labelProvider,
			TreeListener<V> listener,
			final CheckStateProvider<V> stateProvider) {
		final TristateTreeViewer tree = new TristateTreeViewer(parent(),SWT.NONE);
		if(listener != null) {
			tree.addTreeListener(listener);
		}
		if(stateProvider != null) {
			tree.setCheckStateProvider(new CheckStateAdapter<V>(stateProvider));
		}
//		tree.addTreeListener(new TreeListener<Object>() {
//
//			@Override
//			public void itemChanged(TreeNode<Object> data, boolean checked, boolean grayed) {
//				if(checked) {
//					_grayed.remove(data);
//					_selected.add(data);
//				} else if(grayed) {
//					_grayed.add(data);
//					_selected.remove(data);
//				} else {
//					_grayed.remove(data);
//					_selected.remove(data);
//				}
//			}
//		});
		final GridData layoutData = new GridData(SWT.FILL,SWT.FILL,true,true);
		tree.setLayoutData(layoutData);
		tree.setContentProvider(new SWTTreeContentAdapter(contentProvider));
		tree.setLabelProvider(new TreeViewLabelAdapter(labelProvider));
		return new TristateTreeController<TristateTreeViewer>(){
		
			@Override
			public void setCheckStateProvider(CheckStateProvider stateProvider) {
				tree.setCheckStateProvider(new CheckStateAdapter<V>(stateProvider));
			}
			
			@Override
			public void setContext(Object element) {
			  tree.setRedraw(false);
				tree.setInput(element);
				tree.setRedraw(true);
			}

			@Override
			public void show() {
				layoutData.exclude = false;
				tree.setVisible(true);
			}

			@Override
			public void hide() {
				layoutData.exclude = true;
				tree.setVisible(false);
			}
			
			@Override
			public TristateTreeViewer widget() {
				return tree;
			}
		};
	}

	
	@SuppressWarnings("unchecked")
	private static final class CheckStateAdapter<V> implements
			ICheckStateProvider {
		private final CheckStateProvider<V> stateProvider;

		private CheckStateAdapter(CheckStateProvider<V> stateProvider) {
			this.stateProvider = stateProvider;
		}

		@Override
		public boolean isGrayed(Object element) {
			boolean grayed = stateProvider.isGrayed((V)element);
			if(grayed) {
				
			}
			return grayed;
		}

		@Override
		public boolean isChecked(Object element) {
			return stateProvider.isChecked((V)element);
		}
	}

	protected class ComboBoxSelectionListener<V> extends SelectionAdapter {
		private final Combo combo;

		private final Container<V> items;

		private final ComboBoxListener listener;

		protected ComboBoxSelectionListener(Combo combo, Container<V> items, ComboBoxListener listener) {
			this.combo = combo;
			this.items = items;
			this.listener = listener;
		}

		@Override
		public void widgetDefaultSelected(SelectionEvent e) {
			notifyComboBoxListener();
		}

		public void notifyComboBoxListener() {
			listener.itemSelected(items._list.get(combo.getSelectionIndex()));
		}

		@Override
		public void widgetSelected(SelectionEvent e) {
			notifyComboBoxListener();
		}
	}

	private static class Container<T> {
		//Only to replace a local variable that has to be final.
		private List<T> _list;
	}
	
	@Override
	public <V> ComboBoxController<Combo> createComboBox(
			final ListContentProvider<V> contentProvider, 
			final LabelProvider provider,
			final ComboBoxListener listener,
			final int baseOneDefaultSelection) {
		final Combo combo = new Combo(parent(), SWT.READ_ONLY);
		final Container<V> items = new Container<>();
		final ComboBoxSelectionListener selectionAdapter = new ComboBoxSelectionListener<V>(combo, items, listener);
		combo.addSelectionListener(selectionAdapter);
		return new ComboBoxController<Combo>() {

			@Override
			public void setContext(Object element) {
				items._list = ImmutableList.copyOf(contentProvider.items(element));
				int size = items._list.size();
				String[] strings = new String[size];
				for(int i=0; i<size; i++) {
					strings[i] = provider.text(items._list.get(i));
				}
				combo.setItems(strings);
				if(size > baseOneDefaultSelection-1) {
					combo.select(baseOneDefaultSelection-1);
					selectionAdapter.notifyComboBoxListener();
				}
			}

			@Override
			public void show() {
			}

			@Override
			public void hide() {
			}

			@Override
			public Combo widget() {
				return combo;
			}
			
			@Override
			public void select(int baseOneIndex) {
				combo.select(baseOneIndex-1);
			}
		};
	}
	
	public abstract Composite parent();

}
