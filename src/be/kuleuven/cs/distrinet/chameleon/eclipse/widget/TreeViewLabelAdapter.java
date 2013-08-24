package be.kuleuven.cs.distrinet.chameleon.eclipse.widget;

import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.swt.graphics.Image;

import be.kuleuven.cs.distrinet.chameleon.ui.widget.LabelProvider;

public class TreeViewLabelAdapter implements ILabelProvider {

	public TreeViewLabelAdapter(LabelProvider provider) {
		_labelProvider = provider;
	}
	
	private LabelProvider _labelProvider;

	@Override
	public void addListener(ILabelProviderListener listener) {}

	@Override
	public void dispose() {}

	@Override
	public boolean isLabelProperty(Object element, String property) {
		return false;
	}

	@Override
	public void removeListener(ILabelProviderListener listener) {}

	@Override
	public Image getImage(Object element) {
		return null;
	}

	@Override
	public String getText(Object element) {
		return _labelProvider.text(element);
	}
	
}
