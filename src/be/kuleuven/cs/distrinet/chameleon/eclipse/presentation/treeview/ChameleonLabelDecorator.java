package be.kuleuven.cs.distrinet.chameleon.eclipse.presentation.treeview;

import org.eclipse.jface.viewers.DecorationOverlayIcon;
import org.eclipse.jface.viewers.ILabelDecorator;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.swt.graphics.Image;

import be.kuleuven.cs.distrinet.chameleon.core.element.Element;

public class ChameleonLabelDecorator implements ILabelDecorator {

	@Override
   public void addListener(ILabelProviderListener arg0) {
	}

	@Override
   public void dispose() {
	}

	@Override
   public boolean isLabelProperty(Object arg0, String arg1) {
		return true;
	}

	@Override
   public void removeListener(ILabelProviderListener arg0) {
	}

	@Override
   public Image decorateImage(Image baseImage, Object o) {
		Image result = null;
		if(o instanceof Element) {
			DecorationOverlayIcon icon = null;
		}
		return result;
	}

	@Override
   public String decorateText(String arg0, Object arg1) {
		return arg0;
	}

}
