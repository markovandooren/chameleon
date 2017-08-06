package org.aikodi.chameleon.eclipse.view.outline;


import java.util.Iterator;
import java.util.List;

import org.aikodi.chameleon.core.element.Element;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;

/**
 * 
 * @author Manuel Van Wesemael
 * 
 * content provider for ChameleonOutlineTree viewer
 *
 */
public class ChameleonOutlineTreeContentProvider implements ITreeContentProvider,IChameleonOutlineTreeListener  {


	private TreeViewer viewer;

	/*
	 *  (non-Javadoc)
	 * @see org.eclipse.jface.viewers.IContentProvider#dispose()
	 */
	@Override
   public void dispose() {

	}

	/*
	 *  (non-Javadoc)
	 * @see org.eclipse.jface.viewers.IContentProvider#inputChanged(org.eclipse.jface.viewers.Viewer, java.lang.Object, java.lang.Object)
	 */
	@Override
   public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		System.out.println("ChameleonTreeContents inputChanged");
		
		this.viewer = (TreeViewer)viewer;
	    if(oldInput != null) {
	        removeListenerFrom((ChameleonOutlineTree)oldInput);
	    }
	    if(newInput != null) {
	       addListenerTo((ChameleonOutlineTree)newInput);
	    }
		
	}
	
	/** Because the domain model does not have a richer
	 * listener model, recursively remove this listener
	 * from each child box of the given box. */
	protected void removeListenerFrom(ChameleonOutlineTree tree) {
		tree.removeListener(this);
		for (Iterator iterator = tree.getChildren().iterator(); iterator.hasNext();) {
			ChameleonOutlineTree childTree = (ChameleonOutlineTree) iterator.next();
			removeListenerFrom(childTree);
		}
	}
	
	/** Because the domain model does not have a richer
	 * listener model, recursively add this listener
	 * to each child box of the given box. */
	protected void addListenerTo(ChameleonOutlineTree box) {
		box.addListener(this);
		for (ChameleonOutlineTree childTree: box.getChildren()) {
			addListenerTo(childTree);
		}
	}

	
	/**
	 * @return the children for the parentelement
	 */
	@Override
   public Object[] getChildren(Object parentElement) {
		List<ChameleonOutlineTree> children = ((ChameleonOutlineTree) parentElement).getChildren();
		return children.toArray();
	}

	@Override
   public Object getParent(Object object) {
		if(object instanceof ChameleonOutlineTree){
			ChameleonOutlineTree node = (ChameleonOutlineTree)object;
			Element element = node.getElement();
			// check if node is still valid (can be removed)
			if(element!=null){
				Element parentElement = element.lexical().parent();
				return new ChameleonOutlineTree(parentElement);
			}
		}
		return null;
	}

	/**
	 * checks whether the given element has children
	 */
	@Override
   public boolean hasChildren(Object element) {
		return ((ChameleonOutlineTree) element).hasChildren();
	}

	/**
	 * @see getChildren(object inputelement)
	 */
	@Override
   public Object[] getElements(Object inputElement) {
		return getChildren(inputElement);
	}

	@Override
   public void add(ChameleonOutlineTreeEvent event) {
		Element elem = event.getInvolved();
		viewer.refresh(elem,true);
	}
		

	@Override
   public void remove(ChameleonOutlineTreeEvent event) {
		add(event);
		
	}

	@Override
   public void fireChanged() {
		System.out.println("fire changed");
		viewer.refresh();
	}
}

	



	


