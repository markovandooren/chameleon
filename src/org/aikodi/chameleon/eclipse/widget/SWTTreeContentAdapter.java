package org.aikodi.chameleon.eclipse.widget;

import org.aikodi.chameleon.ui.widget.tree.TreeContentProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

/**
 * An adapter class to use a {@link TreeContentProvider} when
 * an SWT {@link ITreeContentProvider} is needed.
 * 
 * @author Marko van Dooren
 */
public class SWTTreeContentAdapter implements ITreeContentProvider {
	
	private final TreeContentProvider<?> _contentProvider;

	/**
	 * Create a new adapter with the given tree content provider.
	 * 
	 * @param contentProvider The content provider that will provide the actual tree content.
	 */
	public SWTTreeContentAdapter(TreeContentProvider<?> contentProvider) {
		this._contentProvider = contentProvider;
	}

	/**
	 * For now this method does nothing.
	 */
	@Override
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
	}

	/**
	 * For now, this method does nothing.
	 */
	@Override
	public void dispose() {
	}

	@Override
	public boolean hasChildren(Object element) {
		return _contentProvider.universalHasChildren(element);
	}

	@Override
	public Object getParent(Object element) {
		return _contentProvider.universalParent(element);
	}

	@Override
	public Object[] getElements(Object inputElement) {
		return getChildren(inputElement);
	}

	@Override
	public Object[] getChildren(Object parentElement) {
		return _contentProvider.universalChildren(parentElement).toArray();
	}
}