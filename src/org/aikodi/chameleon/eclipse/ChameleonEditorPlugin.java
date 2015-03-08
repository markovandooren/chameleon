package org.aikodi.chameleon.eclipse;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.aikodi.chameleon.core.Config;
import org.aikodi.chameleon.core.language.Language;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;



/**
 * @author Jef Geerinckx
 * @author Manuel Van Wesemael 
 * @author Joeri Hendrickx 
 * 
 * The main plugin class to be used in the desktop.
 */
public class ChameleonEditorPlugin extends AbstractUIPlugin {
	//The shared instance.
	private static ChameleonEditorPlugin plugin;
	
	public final static String PLUGIN_ID="be.chameleon.eclipse";

	public final static String CHAMELEON_EDITOR_ID = "chameleon.eclipse.editors.ChameleonEditor";

	public final static String CHAMELEON_RESOURCEBUNDLE_BASENAME = "resourcebundle";

	/**
	 * The directory with the icons, this must be a subdirectory
	 * Must end with a slash
	 */
	public final static String ICON_DIR = "icons/";

	/**
	 * 
	 * @uml.property name="resourceBundle"
	 */
	//Resource bundle.
	private ResourceBundle resourceBundle;

	
	/**
	 * The constructor.
	 */
	public ChameleonEditorPlugin() {
		super();
		plugin = this;
    Config.setCaching(true);
	}

	/**
	 * This method is called upon plug-in activation
	 */
	@Override
   public void start(BundleContext context) throws Exception {
		try {
		super.start(context);
		} catch (Exception e)
		{
			System.out.println(e.getMessage());
			System.out.println(e.getStackTrace());
			throw e;
			
			
		}
	}

	/**
	 * This method is called when the plug-in is stopped
	 */
	@Override
   public void stop(BundleContext context) throws Exception {
		super.stop(context);
		plugin = null;
		resourceBundle = null;
	}

	/**
	 * Returns the shared instance.
	 */
	public static ChameleonEditorPlugin getDefault() {
		return plugin;
	}

	/**
	 * Returns the string from the plugin's resource bundle,
	 * or 'key' if not found.
	 */
	public static String getResourceString(String key) {
		ResourceBundle bundle = ChameleonEditorPlugin.getDefault().getResourceBundle();
		try {
			return (bundle != null) ? bundle.getString(key) : key;
		} catch (MissingResourceException e) {
			return key;
		}
	}

	public static IWorkbenchPage getActivePage() {
		return getDefault().internalGetActivePage();
	}

	private IWorkbenchPage internalGetActivePage() {
		IWorkbenchWindow window= getWorkbench().getActiveWorkbenchWindow();
		if (window == null)
			return null;
		return window.getActivePage();
	}


	/**
	 * Returns the plugin's resource bundle,
	 * 
	 * @uml.property name="resourceBundle"
	 */
	public ResourceBundle getResourceBundle() {
		try {
			if (resourceBundle == null)
				resourceBundle = ResourceBundle.getBundle(CHAMELEON_RESOURCEBUNDLE_BASENAME);
		} catch (MissingResourceException x) {
			resourceBundle = null;
		}
		return resourceBundle;
	}

	/**
	 * Show a message box to the user.
	 * 
	 * @param 	title
	 * 			the title of the message box
	 * @param 	message
	 * 			the message in the message box
	 * @param 	style
	 * 			the style of the message box
	 * <dl>
	 * <dt><b>Styles:</b></dt>
	 * <dd>The different possibilities are separated by a comma. If used more than one style, this should be separated by a | </dd>
	 * <dd>SWT.ICON_ERROR, SWT.ICON_INFORMATION, SWT.ICON_QUESTION, SWT.ICON_WARNING, SWT.ICON_WORKING</dd>
	 * <dd>SWT.OK, SWT.OK | SWT.CANCEL</dd>
	 * <dd>SWT.YES | SWT.NO, SWT.YES | SWT.NO | SWT.CANCEL</dd>
	 * <dd>SWT.RETRY | SWT.CANCEL</dd>
	 * <dd>SWT.ABORT | SWT.RETRY | SWT.IGNORE</dd>
	 * </dl>
	 * 	@see	MessageBox
	 */
	public static void showMessageBox(String title, String message, int style){
		MessageBox box =  new MessageBox(PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell(), style);
		box.setText(title);
		box.setMessage(message);
		box.open();
	}

	/**
	 * 
	 * @param name
	 * 		name of the file to be searched
	 * @param language
	 * 		the currently used language in the chameleonEditor
	 * @return
	 * 		A search is made in the icons/[language] folder for the given filename
	 * 		A proper descriptor is returned if the name exists
	 * 		else a Descriptor for missing images is returned 
	 */
	public static ImageDescriptor getImageDescriptor(String name, Language language) {
		 
			String iconPath = "icons/";
			iconPath = iconPath.concat(language.name().toLowerCase()).concat("/");
		   try {
		       URL installURL = getDefault().getBundle().getEntry("/");
		       URL url = new URL(installURL, iconPath + name);
		       return ImageDescriptor.createFromURL(url);
		   } catch (MalformedURLException e) {
		       return ImageDescriptor.getMissingImageDescriptor();
	   }
	}

	
	/**
	 * Gets an ImageDescriptor for icons that are not language specific
	 *  
	 * @param name the filename of the icon
	 * @return an imagedescriptor for the icon
	 */
	public static ImageDescriptor getImageDescriptor(String name) {
		try {
			URL url = new URL(getIconURL(), name);
			return ImageDescriptor.createFromURL(url);
		} catch (MalformedURLException e) {
			return ImageDescriptor.getMissingImageDescriptor();
		}
	}
	/**
	 * Returns the image descriptor for the default (list) icon.
	 * @return this will return a bullet
	 */
	public static ImageDescriptor getDefaultImageDescriptor() {
		// return ImageDescriptor.getMissingImageDescriptor();
		return getImageDescriptor("default_gray.png");
	}
	
	/**
	 * 
	 * @return The URL of the directory with the icons
	 */
	public static URL getIconURL(){
		try {
			return new URL(getDefault().getBundle().getEntry("/"), ICON_DIR);
		} catch (MalformedURLException e) {
			e.printStackTrace();
			return null;
		} catch (NullPointerException e) {
			e.printStackTrace();
			return null;
		}
	}

}
