package chameleon.eclipse.presentation.treeview;

import org.eclipse.jface.viewers.DecorationOverlayIcon;
import org.eclipse.jface.viewers.ILabelDecorator;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.swt.graphics.Image;

import chameleon.core.element.Element;

public class ChameleonLabelDecorator implements ILabelDecorator {

	public void addListener(ILabelProviderListener arg0) {
	}

	public void dispose() {
	}

	public boolean isLabelProperty(Object arg0, String arg1) {
		return true;
	}

	public void removeListener(ILabelProviderListener arg0) {
	}

	public Image decorateImage(Image baseImage, Object o) {
		Image result = null;
		if(o instanceof Element) {
			DecorationOverlayIcon icon = null;
		}
		return result;
	}

	public String decorateText(String arg0, Object arg1) {
		return arg0;
	}

}
