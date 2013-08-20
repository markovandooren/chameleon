package be.kuleuven.cs.distrinet.chameleon.eclipse.widget;

import java.util.List;

import org.eclipse.jface.viewers.CellEditor;
import org.eclipse.jface.viewers.CheckboxTreeViewer;
import org.eclipse.jface.viewers.IBaseLabelProvider;
import org.eclipse.jface.viewers.ICellModifier;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.TreePath;
import org.eclipse.jface.viewers.TreeSelection;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

import be.kuleuven.cs.distrinet.chameleon.core.namespace.Namespace;
import be.kuleuven.cs.distrinet.chameleon.core.namespace.RegularNamespaceFactory;
import be.kuleuven.cs.distrinet.chameleon.core.namespace.RootNamespace;
import be.kuleuven.cs.distrinet.chameleon.ui.widget.CheckboxListener;
import be.kuleuven.cs.distrinet.chameleon.ui.widget.WidgetFactory;


public abstract class SWTWidgetFactory implements WidgetFactory<Control> {

	@Override
	public Control createCheckbox(String text, boolean initialState, final CheckboxListener listener) {
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
		return button;
	}
	
	public Control createCheckboxList() {
		Composite composite = new Composite(parent(), SWT.NONE);
		GridData rightData = new GridData(GridData.FILL,GridData.FILL,true,true);
composite.setLayoutData(rightData);
		GridLayout layout = new GridLayout();
		layout.numColumns = 1;
		composite.setLayout(layout);

		Button button = new Button(composite,SWT.CHECK);
		button.setText("Enable namespace filter");
		
		final CheckboxTreeViewer pack = new CheckboxTreeViewer(composite,SWT.NONE);
		pack.getTree().setLayoutData(new GridData(GridData.FILL_BOTH));
		pack.setContentProvider(new ITreeContentProvider(){
		
			@Override
			public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
				System.out.println("*** Input changed ***");
			}
		
			@Override
			public void dispose() {
				
			}
		
			@Override
			public boolean hasChildren(Object element) {
				return ! ((Namespace)element).getSubNamespaces().isEmpty();
			}
		
			@Override
			public Object getParent(Object element) {
				return ((Namespace)element).parent();
			}
		
			@Override
			public Object[] getElements(Object inputElement) {
				return getChildren(inputElement);
			}
		
			@Override
			public Object[] getChildren(Object parentElement) {
				List<Namespace> subNamespaces = ((Namespace)parentElement).getSubNamespaces();
				return subNamespaces.toArray();
			}
		});
		pack.setLabelProvider(new ILabelProvider(){
		
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
		
//		button.addListener (SWT.Selection, new Listener() {
//			public void handleEvent (Event e) {
//				if (button.getSelection()) {
//					if (!button.getGrayed()) {
//						button.setGrayed(true);
//					}
//				} else {
//					if (button.getGrayed()) {
//						button.setGrayed(false);
//						button.setSelection (true);
//					}
//				}
//			}
//		});
		
		pack.addSelectionChangedListener(new ISelectionChangedListener(){
		
			@Override
			public void selectionChanged(SelectionChangedEvent event) {
				Object source = event.getSource();
				ISelection selection = event.getSelection();
				if(selection instanceof TreeSelection) {
					TreeSelection treeSelection = (TreeSelection) selection;
					TreePath[] paths = treeSelection.getPaths();
					System.out.println("debug");
				}
				System.out.println("debug");
			}
		});
		
		
		pack.setAutoExpandLevel(2);
		Namespace ns = new RootNamespace(new RegularNamespaceFactory());
		ns.getOrCreateNamespace("a.b.c.d");
		pack.setInput(ns);
		
		return null;
	}
	
	public abstract Composite parent();

}
