package org.aikodi.chameleon.eclipse.connector;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.aikodi.chameleon.core.declaration.Declaration;
import org.aikodi.chameleon.core.declaration.Signature;
import org.aikodi.chameleon.core.element.Element;
import org.aikodi.chameleon.core.language.Language;
import org.aikodi.chameleon.core.modifier.Modifier;
import org.aikodi.chameleon.core.namespace.Namespace;
import org.aikodi.chameleon.core.namespacedeclaration.NamespaceDeclaration;
import org.aikodi.chameleon.eclipse.ChameleonEditorPlugin;
import org.aikodi.chameleon.eclipse.presentation.treeview.DeclarationCategorizer;
import org.aikodi.chameleon.eclipse.presentation.treeview.IconProvider;
import org.aikodi.chameleon.eclipse.view.outline.ChameleonOutlineSelector;
import org.aikodi.chameleon.exception.ModelException;
import org.aikodi.chameleon.plugin.LanguagePlugin;
import org.aikodi.chameleon.plugin.LanguagePluginImpl;
import org.aikodi.chameleon.workspace.View;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.swt.graphics.Image;

/**
 * This class defines the extensions that a language can do to the IDE functionality.
 * Examples are customizing the labels of elements in the outline.
 * 
 * @author Marko van Dooren
 */
public class EclipseEditorExtension extends LanguagePluginImpl {

	public EclipseEditorExtension() {
		setImageRegistry(ChameleonEditorPlugin.getDefault().getImageRegistry());
		_outlineSelector = createOutlineSelector();
	}

	/**
	 * On connection to a language, the image registry is initialized.
	 */
	@Override
	protected <T extends LanguagePlugin> void containerConnected(Language oldContainer, Language newContainer, Class<T> keyInterface) {
		if(newContainer != null) {
			initializeRegistry();
		}
	}
	
	private ImageRegistry _imageRegistry;
	
	/**
	 * Retrieves a URL to the file specified by path,
	 * and relative to the root of the plugin with the specified pluginID.
	 * 
	 * @param pluginID String representing the ID of the plugin containing the requested file.
	 * @param path Path relative to the plugin root of the requested file, in an org.eclipse.core.Path-compatible format.
	 * @return A URL to that file.
	 * @throws IOException The specified path can not be resolved.
	 */
	public URL getPluginFile(String pluginID, String path) throws IOException {
		URL url = FileLocator.find(Platform.getBundle(pluginID), new Path(path), null);
		if(url != null) {
			return FileLocator.resolve(url);
		}
		return null;
	}
	
	/**
	 * Set the image registry of this extension.
	 * 
	 * @param imageRegistry The new image registry. The value cannot be null.
	 */
	protected void setImageRegistry(ImageRegistry imageRegistry) {
		_imageRegistry = imageRegistry;
	}
	
	/**
	 * Return the image registry for this language.
	 * @return The image registry for this language.
	 *         The result is not null.
	 */
	protected ImageRegistry imageRegistry() {
		return _imageRegistry;
	}
	
	/**
	 * Return the prefix that is used for the names of the images in the
	 * image registry.
	 * 
	 * @return The prefix for the names in the image registry. 
	 *         The result is not null.
	 */
	protected String imageRegistryPrefix() {
		return language().name()+"_";
	}
	
	/**
	 * Return the prefix for the given name.
	 * 
	 * @param name The name for which the prefix is requested.
	 *             The name cannot be null.
	 * @return The prefix of the image registry appended with the given name.
	 */
	protected String prefix(String name) {
		if(name == null) {
			throw new IllegalArgumentException("The name of the prefix cannot be null.");
		}
		return imageRegistryPrefix()+name;
	}
	
	/**
	 * Add the image descriptors to the registry.
	 * 
	 * By default, no images are added.
	 */
	protected void initializeRegistry() {
	}

	/**
	 * Return the label provider for the language.
	 * The default implementation returns a label provider
	 * that delegates the methods for determining the icon
	 * and the label to {@link #getLabel(Element)} and {@link #getIcon(Element)}.
	 * 
	 * @return A label provider for the language. 
	 */
	public ILabelProvider labelProvider() {
		return new ILabelProvider(){
		
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
				if(element instanceof Element) {
					return EclipseEditorExtension.this.label((Element) element);
				} else {
					throw new IllegalArgumentException();
				}
			}
		
			@Override
			public Image getImage(Object element) {
				if(element instanceof Element) {
					try {
						return EclipseEditorExtension.this.icon((Element) element);
					} catch (ModelException e) {
						return null;
					}
				} else {
					throw new IllegalArgumentException();
				}
			}
		};
	}
	
	/**
	 * Return a text label for the given element. 
	 * This is used for example in the outline.
	 * 
	 * The default implementation 
	 * 
	 * @param element The element for which the label is requested.
	 * @return A label to visualize the given element.
	 */
  public String label(Element element) {
  	String result = "";
  	if (element instanceof Declaration) {
			result = ((Declaration)element).name();
		} else if (element instanceof Signature) {
			return ((Signature)element).name();
		}
		else if (element instanceof NamespaceDeclaration) {
			Namespace namespace = ((NamespaceDeclaration)element).namespace();
			if(namespace != null) {
				result = namespace.fullyQualifiedName();
			} else {
				result = "Error in namespace declaration.";
			}
		}
  	return result;
  }
  
  /**
   * Return an icon to represent the given element.
   * @throws IOException 
   * @throws ModelException 
   */
	public Image icon(Element element) throws ModelException {
		IconProvider iconProvider = iconProvider();
		try {
			return iconProvider != null ? image(iconProvider.iconName(element)) : null;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public IconProvider createIconProvider() {
		return null;
	}

	public IconProvider iconProvider() {
		if(_iconProvider == null) {
			_iconProvider = createIconProvider();
		}
		return _iconProvider;
	}
	
	private IconProvider _iconProvider;
	
//  /**
//   * Returns the string for the template for the given method.
//   * The parameters will be editable regions, if used as template patternstring.
//   * So the parameters are replaced by "${parametername}".
//   * E.g. In the java syntax this could be: methodname(${param1}, ${param2})
//   * Used for auto-completion of methods.
//   * 
//   * @param method the method to be converted to a (pattern) string
//   * @return pattern string for a template
//   */
//  public abstract String getMethodTemplatePattern(Method method);
      
  /**
   * Returns all modifiers for which we want to define a filter
   * (to filter the type hierarchy or the outline)
   */
  //FIXME: filter based on properties!!!!
  public List<Modifier> getFilterModifiers() {
  	return new ArrayList<Modifier>();
  }
  
  /**
   * Return the directory in which the output of the builder must be written.
   * 
   * @param projectRoot The root directory of the project.
   * @return
   */
 /*@
   @ public behavior
   @
   @ post \result != null;
   @*/
  public File buildDirectory(File projectRoot) {
  	return projectRoot;
  }
  
  /**
   * Return a declaration categorizer. A declaration categorizer is used to group declarations
   * into categories. When e.g. an outline is sorted, elements of the same category are grouped
   * together, and sorted within their own group. The categorizer also imposes an order on the
   * groups.
   */
	public DeclarationCategorizer declarationCategorizer() {
		return new DeclarationCategorizer() {
			@Override
         public int category(Declaration declaration) {
				return 0;
			}
		};
	}

	/**
	 * Register an icon in the image registry. The icon must be in the icons/ directory
	 * of this language module editor plugin..
	 */
	public void register(String fileName, String iconName, String pluginID) throws MalformedURLException {
		Image image = loadIcon(fileName, pluginID);
		String name = prefix(iconName);
		if(imageRegistry().get(name) == null) {
			imageRegistry().put(name, image);
		}
	}

	/**
	 * Create an image from the file in the icons directory of this language module editor plugin
	 * that has the same name as the given name. 
	 */
	public static Image loadIcon(String fileName, String pluginID) throws MalformedURLException {
		return iconDescriptor(fileName, pluginID).createImage();
	}
	
	public static ImageDescriptor iconDescriptor(String fileName, String pluginID) throws MalformedURLException {
		URL url = icon(fileName, pluginID);
		return ImageDescriptor.createFromURL(url);
	}
	
	public Image image(String iconName) {
		return imageRegistry().get(prefix(iconName));
	}

	/**
	 * Return the URL for the icons directory of this language module editor plugin.
	 */
	public static URL icon(String name, String pluginID) throws MalformedURLException {
		URL root = Platform.getBundle(pluginID).getEntry("/");
		URL icons = new URL(root,"icons/");
		URL url = new URL(icons, name);
		return url;
	}
	
	public ChameleonOutlineSelector createOutlineSelector() {
		return new ChameleonOutlineSelector();
	}
	
	private ChameleonOutlineSelector _outlineSelector;
	
	public ChameleonOutlineSelector outlineSelector() {
		return _outlineSelector;
	}

	@Override
	public EclipseEditorExtension clone() {
		return new EclipseEditorExtension();
	}
	
//  	public abstract ICompletionProposal completionProposal(Element element, ChameleonDocument document, int offset);

	public void initialize(View view) {
	}
	
}
