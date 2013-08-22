package be.kuleuven.cs.distrinet.chameleon.eclipse.widget;

import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

import be.kuleuven.cs.distrinet.chameleon.core.element.Element;
import be.kuleuven.cs.distrinet.chameleon.core.namespace.Namespace;
import be.kuleuven.cs.distrinet.chameleon.core.namespace.RegularNamespaceFactory;
import be.kuleuven.cs.distrinet.chameleon.core.namespace.RootNamespace;
import be.kuleuven.cs.distrinet.chameleon.ui.widget.CheckboxListener;
import be.kuleuven.cs.distrinet.chameleon.ui.widget.Input;
import be.kuleuven.cs.distrinet.chameleon.ui.widget.TreeContentProvider;
import be.kuleuven.cs.distrinet.chameleon.ui.widget.WidgetFactory;


public abstract class SWTWidgetFactory implements WidgetFactory<Control> {

	@Override
	public Input createCheckbox(String text, boolean initialState, final CheckboxListener listener) {
		final Button button = new Button(parent(), SWT.CHECK);
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
		return new Input(){
			@Override
			public void setInput(Element element) {
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

	public Input createTristateTree(TreeContentProvider contentProvider) {
		final TristateTreeViewer tree = new TristateTreeViewer(parent(),SWT.NONE);
		tree.setContentProvider(new SWTTreeContentAdapter(contentProvider));
		tree.setLabelProvider(new ILabelProvider(){
		
			@Override
			public void removeListener(ILabelProviderListener listener) {
				
			}
		
			@Override
			public boolean isLabelProperty(Object element, String property) {
				return false;
			}
		
			@Override
			public void dispose() {
				
			}
		
			@Override
			public void addListener(ILabelProviderListener listener) {
				
			}
		
			@Override
			public String getText(Object element) {
				return ((Namespace)element).name();
			}
		
			@Override
			public Image getImage(Object element) {
				return null;
			}
		});
		// We must do a full expand to make recursive descend work
		// otherwise never-expanded tree item are simply ignore. 
		return new Input(){
		
			@Override
			public void setInput(Element element) {
			  tree.setRedraw(false);
				tree.setInput(element);
//				tree.expandAll();
//				tree.collapseAll();
//				tree.expandToLevel(2);
				tree.setRedraw(true);
			}
		};
	}

	public abstract Composite parent();

}
