package be.kuleuven.cs.distrinet.chameleon.eclipse.widget;

import java.util.List;

import org.eclipse.jface.viewers.CheckboxTreeViewer;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.MouseAdapter;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.TreeItem;

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

		final Button button = new Button(composite,SWT.CHECK);
		button.setText("Enable namespace filter");
		button.setSelection(true);
		final CheckboxTreeViewer tree = createTree(composite);
		button.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseUp(MouseEvent e) {
				tree.getTree().setEnabled(button.getSelection());
			}
		});
		
		return null;
	}

	protected CheckboxTreeViewer createTree(Composite composite) {
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
		pack.setAutoExpandLevel(2);
		pack.getTree().addListener(SWT.Selection, new Listener(){
		
			void updateParents(TreeItem changed) {
				TreeItem item = changed.getParentItem();
		    if (item == null) return;
		    boolean checked = item.getChecked();
		    boolean grayed = item.getGrayed();
		    boolean allUnchecked = true;
		    boolean allChecked = true;
		    for(TreeItem child:  item.getItems()) {
		    	if(child.getGrayed() || (! (allUnchecked | allChecked))) {
		    		grayed = true;
		    		break;
		    	} else if(child.getChecked() && allUnchecked) {
		    		allUnchecked = false;
		    	} else if((! child.getChecked()) && allChecked) {
		    		allChecked = false;
		    	}
		    }
//		    if(allUnchecked) {
//		    	grayed = false;
//		    	checked = false;
//		    } else 
		    if(allChecked) {
		    	grayed = false;
		    	checked = true;
		    } else {
		    	grayed = true;
		    }
		    if(grayed) {
		    	checked = true;
		    } 
		    item.setChecked(checked);
		    item.setGrayed(grayed);
		    updateParents(item);
		}
			
			@Override
			public void handleEvent(Event event) {
				if(event.detail == SWT.CHECK) {
					TreeItem item = (TreeItem) event.item;
					itemClicked(item);
					updateParents(item);
				} else {
					System.out.println("Other");
				}
			}
			
			private void itemClicked(TreeItem item) {
//				if(item.getGrayed()) {
//					setUnchecked(item);
//				} else 
				if(! item.getChecked()) {
					setUnchecked(item);
				} else {
					setChecked(item);
				}
			}
			
			private void setChecked(TreeItem item) {
				System.out.println("Setting checked for: "+item.getText());
				item.setChecked(true);
				item.setGrayed(false);
				for(TreeItem child: item.getItems()) {
					setChecked(child);
				}
			}
			
			private void setMaybe(TreeItem item) {
				item.setGrayed(true);
				item.setChecked(true);
			}
			
			private void setUnchecked(TreeItem item) {
				item.setChecked(false);
				item.setGrayed(false);
				for(TreeItem child: item.getItems()) {
					setUnchecked(child);
				}
			}
		});
		Namespace ns = new RootNamespace(new RegularNamespaceFactory());
		ns.getOrCreateNamespace("a.b.c.d");
		ns.getOrCreateNamespace("a.b.e.f");
		ns.getOrCreateNamespace("a.m.n.o");
		ns.getOrCreateNamespace("x.y.z");
		pack.setInput(ns);
		return pack;
	}
	
//	protected void treeExperiment1(Composite composite) {
//		Tree tree = new Tree(composite, SWT.CHECK | SWT.V_SCROLL| SWT.H_SCROLL);
//		
//	}
	
	public abstract Composite parent();

}
