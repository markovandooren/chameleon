package be.kuleuven.cs.distrinet.chameleon.eclipse.view.dependency;

import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
import org.eclipse.zest.core.viewers.IConnectionStyleProvider;
import org.eclipse.zest.core.widgets.ZestStyles;

import be.kuleuven.cs.distrinet.chameleon.core.element.Element;
import be.kuleuven.cs.distrinet.chameleon.eclipse.connector.EclipseEditorExtension;
import be.kuleuven.cs.distrinet.chameleon.exception.ModelException;
import be.kuleuven.cs.distrinet.chameleon.oo.type.Type;

class DependencyLabelProvider extends LabelProvider implements IConnectionStyleProvider {
		
		
		@Override
		public String getText(Object element) {
			if(element instanceof Element) {
				return ((Element)element).language().plugin(EclipseEditorExtension.class).getLabel((Element) element);
			} else {
				return "X";
			}
		}
		
		@Override
		public Image getImage(Object element) {
			if(element instanceof Element) {
				try {
				return ((Element)element).language().plugin(EclipseEditorExtension.class).getIcon((Element) element);
				} catch(ModelException exc) {
					exc.printStackTrace();
				}
			}
			return super.getImage(element);
		}

		@Override
		public int getConnectionStyle(Object rel) {
			return ZestStyles.CONNECTIONS_DIRECTED + ZestStyles.CONNECTIONS_SOLID;
		}

		@Override
		public Color getColor(Object rel) {
			return Display.getCurrent().getSystemColor(SWT.COLOR_DARK_GRAY);
		}

		@Override
		public Color getHighlightColor(Object rel) {
			return Display.getCurrent().getSystemColor(SWT.COLOR_BLUE);
		}

		@Override
		public int getLineWidth(Object rel) {
			return 1;
		}

		@Override
		public IFigure getTooltip(Object entity) {
			if(entity instanceof Type) {
			  return new Label(((Type)entity).name());
			}
			return null;
		}
		
	}