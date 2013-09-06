package be.kuleuven.cs.distrinet.chameleon.eclipse.view.dependency;

import org.eclipse.draw2d.ConnectionRouter;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.Label;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
import org.eclipse.zest.core.viewers.IConnectionStyleProvider;
import org.eclipse.zest.core.viewers.IEntityStyleProvider;
import org.eclipse.zest.core.widgets.ZestStyles;

import be.kuleuven.cs.distrinet.chameleon.core.element.Element;
import be.kuleuven.cs.distrinet.chameleon.eclipse.connector.EclipseEditorExtension;
import be.kuleuven.cs.distrinet.chameleon.exception.ModelException;
import be.kuleuven.cs.distrinet.chameleon.oo.type.Type;
import be.kuleuven.cs.distrinet.chameleon.oo.variable.Variable;

class DependencyLabelProvider extends LabelProvider implements IConnectionStyleProvider, IEntityStyleProvider, org.eclipse.gef4.zest.core.viewers.IConnectionStyleProvider, org.eclipse.gef4.zest.core.viewers.IEntityStyleProvider {
		
		
		@Override
		public String getText(Object element) {
			if(element instanceof Element) {
				return ((Element)element).language().plugin(EclipseEditorExtension.class).getLabel((Element) element);
			} else {
				return "";
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
			//FIXME: This is bad design. This label provide should have no knowledge of types or variables.
			if(entity instanceof Type) {
			  return new Label(((Type)entity).getFullyQualifiedName());
			} else if(entity instanceof Variable) {
				Type type = ((Variable) entity).nearestAncestor(Type.class);
				return new Label(type.getFullyQualifiedName()+"."+((Variable)entity).name());
			}
			return null;
		}

		@Override
		public Color getNodeHighlightColor(Object entity) {
			return Display.getCurrent().getSystemColor(SWT.COLOR_TITLE_BACKGROUND_GRADIENT);
		}

		@Override
		public Color getBorderColor(Object entity) {
			return Display.getCurrent().getSystemColor(SWT.COLOR_GRAY);
		}

		@Override
		public Color getBorderHighlightColor(Object entity) {
			return Display.getCurrent().getSystemColor(SWT.COLOR_BLACK);
		}

		@Override
		public int getBorderWidth(Object entity) {
			return 1;
		}

		@Override
		public Color getBackgroundColour(Object entity) {
			return Display.getCurrent().getSystemColor(SWT.COLOR_TITLE_INACTIVE_BACKGROUND_GRADIENT);
		}

		@Override
		public Color getForegroundColour(Object entity) {
			return Display.getCurrent().getSystemColor(SWT.COLOR_TITLE_FOREGROUND);
		}

		@Override
		public boolean fisheyeNode(Object entity) {
			return false;
		}

		@Override
		public ConnectionRouter getRouter(Object rel) {
			// Default implementation as per the javadocs of Zest 2.
			return null;
		}
		
	}