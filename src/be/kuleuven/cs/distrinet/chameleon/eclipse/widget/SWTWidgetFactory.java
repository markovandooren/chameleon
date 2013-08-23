package be.kuleuven.cs.distrinet.chameleon.eclipse.widget;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

import be.kuleuven.cs.distrinet.chameleon.ui.widget.CheckboxListener;
import be.kuleuven.cs.distrinet.chameleon.ui.widget.SelectionController;
import be.kuleuven.cs.distrinet.chameleon.ui.widget.TreeContentProvider;
import be.kuleuven.cs.distrinet.chameleon.ui.widget.WidgetFactory;


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
	
//	public <T> Control createCheckboxList(TreeContentProvider<T> contentProvider, boolean enabled, String enableCheckboxText) {
//		Composite composite = new Composite(parent(), SWT.NONE);
//		GridData rightData = new GridData(GridData.FILL,GridData.FILL,true,true);
//		composite.setLayoutData(rightData);
//		GridLayout layout = new GridLayout();
//		layout.numColumns = 1;
//		composite.setLayout(layout);
//
//		final Button button = new Button(composite,SWT.CHECK);
//		button.setText(enableCheckboxText);
//		button.setSelection(enabled);
////		final CheckboxTreeViewer tree = createTree(composite,contentProvider);
//		final TristateTreeViewer tree = createTristateTree(composite,contentProvider);
//		button.addMouseListener(new MouseAdapter() {
//			@Override
//			public void mouseUp(MouseEvent e) {
//				tree.getTree().setEnabled(button.getSelection());
//			}
//		});
//		
//		return null;
//	}

	public SelectionController<TristateTreeViewer> createTristateTree(TreeContentProvider contentProvider) {
		final TristateTreeViewer tree = new TristateTreeViewer(parent(),SWT.NONE);
		final GridData layoutData = new GridData(SWT.FILL,SWT.FILL,true,true);
		tree.setLayoutData(layoutData);
		tree.setContentProvider(new SWTTreeContentAdapter(contentProvider));
		return new SelectionController<TristateTreeViewer>(){
		
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

	public abstract Composite parent();

}
