package org.aikodi.chameleon.eclipse.presentation.treeview;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.aikodi.chameleon.core.declaration.Declarator;
import org.aikodi.chameleon.core.element.Element;
import org.aikodi.chameleon.core.language.Language;
import org.aikodi.chameleon.core.modifier.ElementWithModifiers;
import org.aikodi.chameleon.core.modifier.Modifier;
import org.aikodi.chameleon.eclipse.ChameleonEditorPlugin;
import org.aikodi.chameleon.eclipse.connector.EclipseEditorExtension;
import org.aikodi.chameleon.eclipse.connector.EclipseEditorTag;
import org.aikodi.chameleon.eclipse.presentation.hierarchy.HierarchyTypeNode;
import org.aikodi.chameleon.eclipse.view.outline.ChameleonOutlineTree;
import org.aikodi.chameleon.exception.ChameleonProgrammerException;
import org.aikodi.chameleon.exception.ModelException;
import org.aikodi.chameleon.oo.language.ObjectOrientedLanguage;
import org.aikodi.chameleon.oo.method.Method;
import org.aikodi.chameleon.oo.type.Type;
import org.aikodi.chameleon.plugin.output.Syntax;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.swt.graphics.Image;

import be.kuleuven.cs.distrinet.rejuse.logic.ternary.Ternary;

/**
 * @author Manuel Van Wesemael 
 * @author Joeri Hendrickx
 * @author Tim Vermeiren
 * 
 * Gets the image and text for the tree elements in the outline view
 */
public class ChameleonLabelProvider implements ILabelProvider {

	/**
	 * Creates a new labelProvider with for the given language and the paths for the icons
	 * @param language
	 * 	the language used, must be effective
	 * @param showDefingType
	 *  Whether to show the (defining) types of the members
	 * @param showDeclaringElementFqn
	 *  Whether to show the fully qualified names of the declaring elements (parent elements)
	 * @param showElementClassName Show the classname (e.g. JavaClass or RegularMethodInvocation) of the elements
	 */
	public ChameleonLabelProvider(Language language, boolean showDefingType, boolean showDeclaringElementFqn, boolean showElementClassName){
		if(language == null) {
			throw new ChameleonProgrammerException("The language of a label provider cannot be set to null");
		}
		_language = language;
		this.showDeclaringElementFqn = showDeclaringElementFqn;
		this._showDefingType = showDefingType;
		this.showElementClassName = showElementClassName;
	}

	//a cache for the images, used for faster lookup
	private HashMap<ImageDescriptor,Image> imageCache = new HashMap<ImageDescriptor,Image>(11);

	/**
	 * Whether to show the defining types (return types or field types) of the elements
	 */
	private boolean _showDefingType;

	/**
	 * Whether to show the fully qualified names of the declaring elements
	 */
	private boolean showDeclaringElementFqn;

	/**
	 * Wheter to show the simple class name of this element
	 */
	private boolean showElementClassName;

	private Language _language;

	public Language getLanguage() {
		return _language;
	}

	public void setShowDeclaringElementFqn(boolean showDeclaringElementFqn){
		this.showDeclaringElementFqn = showDeclaringElementFqn;
	}

	public boolean isShowingDefiningTypes(){
		return _showDefingType;
	}

	public void setShowDefiningTypes(boolean showDefingType){
		this._showDefingType = showDefingType;
	}

	public void invertShowDefiningType() {
		setShowDefiningTypes(! isShowingDefiningTypes());
	}
	
	/**
	 * @return the image corresponding to the given element.
	 * 			No image is returned if the element does not have image available
	 */
	@Override
   public Image getImage(Object modelObject) {
		Image image = null;
		Element element = getElement(modelObject);
			Language language = element.language();
			if(language != null) {
				EclipseEditorExtension extension = language.plugin(EclipseEditorExtension.class);
				try {
					image = extension.icon(element);
				} catch (ModelException e) {
				}
			}
		// FIXME I think the remainder (excluding the return statement) can be removed. Then remove private getDescriptor method as well.
		if(image == null) {
			ImageDescriptor descriptor = getDescriptor(modelObject);
			//obtain the cached image corresponding to the descriptor
			image = imageCache.get(descriptor);
			if (image == null) {
				image = descriptor.createImage();
				if(image != null) { //this occurs when no icon is found from the description
					imageCache.put(descriptor, image);
				}
			}
		}
		return image;
	}
	
  public Image decorateImage(Image image, Object object)  {
  	return null;
  }

	/**
	 * 
	 * @param element
	 * 	the element the descriptor is looked-up for
	 * 
	 * @return the descriptor for the image of the given element.
	 */
	private ImageDescriptor getDescriptor(Object modelObject) {
		ImageDescriptor descriptor=null;
		// convert object to ChameleonElement:
		Element element = getElement(modelObject);
		if(element != null){
			String name = element.getClass().getSimpleName();
			if(element instanceof ElementWithModifiers) {
				List modifiers = ((ElementWithModifiers)element).modifiers();
				for (Iterator iter = modifiers.iterator(); iter.hasNext();) {
					Modifier modifier = (Modifier) iter.next();
					name = name+ ((modifier.getClass().getSimpleName()).toLowerCase());
				}
				name =name.concat(".png");
			} else {
				name =name.concat(".png");
			}
//			System.out.println("Looking for "+name);
			descriptor = ChameleonEditorPlugin.getImageDescriptor(name, getLanguage());
		}
		if(descriptor==null) {
			descriptor = ChameleonEditorPlugin.getDefaultImageDescriptor();
		}
		if(descriptor.getImageData()==null) {
			descriptor = ChameleonEditorPlugin.getDefaultImageDescriptor();
		}
		return descriptor;
	}

	/**
	 * Convert an element as presented in the StructeredViewer to a Chameleon element
	 * 
	 * @param modelObject
	 * @return a chameleon element
	 */
	public static Element getElement(Object modelObject){
		try {
			if (modelObject instanceof ChameleonOutlineTree) {
				ChameleonOutlineTree outlineElement = (ChameleonOutlineTree) modelObject;
				return outlineElement.getElement();
			} else if (modelObject instanceof HierarchyTypeNode) {
				HierarchyTypeNode node = (HierarchyTypeNode) modelObject;
				return node.getType();
			} else if (modelObject instanceof Element) {
				return (Element) modelObject;
			} else if(modelObject instanceof EclipseEditorTag){
				return ((EclipseEditorTag)modelObject).getElement();
			}
		} catch (ModelException e) {
			e.printStackTrace();
		}
		return null;
		// throw new Error("No label found for object of class "+ modelObject.getClass().getName());
	}

	/**
	 * @return the text for the given element.
	 * 
	 */
	@Override
   public String getText(Object modelObject) {
		String label = "";
		try {
		Element element = getElement(modelObject);
		label = getLabel(element);
		if( element != null ){
			label += getExtraInfo(element, false);
			// prepend the simple class name if wanted
			if(showElementClassName){
				String className = element.getClass().getSimpleName();
				if(! label.equals(className))
					label = className + " " + label;
			}
		}
		// For EditorTags, append their name and offsets
		if(modelObject instanceof EclipseEditorTag){
			EclipseEditorTag tag = (EclipseEditorTag)modelObject;
			int beginOffset = tag.getOffset();
			int endOffset = beginOffset + tag.getLength();
			label = label + " - " + tag.getName() + " (" + beginOffset + " - " + endOffset + ")";
		}
		} catch(Exception exc) {
			exc.printStackTrace();
		}
		return label;
	}

	/**
	 * Get the extra info for an element (depends on the settings given in the constructor)
	 * 
	 * @param element
	 * @param noFirstSeparator if true, the first separator won't be set
	 */
	public String getExtraInfo(Element element, boolean noFirstSeparator) {
		String label = "";
		// don't show extra info for types and constructors:
		if(element != null && !(element instanceof Type) && (!(element instanceof Method) || 
				 ((element.language() != null) && !(((Method)element).is(element.language(ObjectOrientedLanguage.class).CONSTRUCTOR) == Ternary.TRUE ))) ){
			// show types for type elements
			
			//CHANGE: NO TYPE LABELS FOR NOW, Need alternative for TypeDefiningElement (whose name is wrong) 
			
//			if(showDefingType && element instanceof TypeDefiningElement){
//				Type type;
//				try {
//					type = ((TypeDefiningElement)element).getType();
//					if(!noFirstSeparator)
//						label += " : ";
//					else
//						noFirstSeparator = false;
//					label += type.getName();
//				} catch (Exception e) {
//					// e.printStackTrace(); //TODO: zorgen dat deze exc niet meer geworpen wordt
//				}
//			}
			if(showDeclaringElementFqn && element instanceof Declarator){
				Type type = ((Declarator)element).lexical().nearestAncestorOrSelf(Type.class);
				if(type != null){
					if(!noFirstSeparator)
						label += " - ";
					label += type.getFullyQualifiedName();
				}
			}
		}
		return label;
	}

	/**
	 * Returns a simple label for the given object. This is the label as given by the 
	 * {@link EclipseEditorExtension#getLabel(Element)} method of the tool extension
	 * of the current language.
	 * 
	 * @param modelObject
	 */
	public String getLabel(Object modelObject){
		Element element = getElement(modelObject);
		if(element != null) {
			EclipseEditorExtension ext = editorExtension();
			return ext.label(element);
		} else {
			if(modelObject != null) {
				throw new ChameleonProgrammerException("Requesting label of an object that is not an Element.");
			} else {
				throw new ChameleonProgrammerException("Requesting label of null.");
			}
		}
	}

	private EclipseEditorExtension editorExtension() {
		if(_editorExtension == null) {
			_editorExtension = getLanguage().plugin(EclipseEditorExtension.class);
		}
		return _editorExtension;
	}
	
	private EclipseEditorExtension _editorExtension;

	/**
	 * Returns the source code of the given element. 
	 * The CodeWriter is used for this purpose.
	 * 
	 * @param element
	 */
	public String getCode(Element element){
		String result;
		try {
			if(element instanceof Declarator){
//				EclipseBootstrapper languageModel = LanguageMgt.getInstance().getLanguageModelID(getLanguage().name());
				Syntax syntax = element.language().plugin(Syntax.class);
				String code = syntax.toCode(element);
				result = code;
			} else {
				result = getLabel(element);
			}
		} catch(Throwable e){
			String label = getLabel(element);
			System.err.println("Following element couldn't be translated to code : " + label);
			// e.printStackTrace();
			result = label;
		}
		return result;
	}

	/**
	 * clears the image cache, to prevent leaks
	 */
	@Override
   public void dispose() {
		for (Iterator i = imageCache.values().iterator(); i.hasNext();) {
			((Image) i.next()).dispose();
		}
		imageCache.clear();
	}

	/**
	 * 
	 * @return false
	 */
	@Override
   public boolean isLabelProperty(Object element, String property) {
		return false;
	}

	/**
	 * UNUSED AT THE MOMENT
	 */
	@Override
   public void addListener(ILabelProviderListener listener) {

	}


	/**
	 * UNUSED AT THE MOMENT
	 */
	@Override
   public void removeListener(ILabelProviderListener listener) {

	}

}
