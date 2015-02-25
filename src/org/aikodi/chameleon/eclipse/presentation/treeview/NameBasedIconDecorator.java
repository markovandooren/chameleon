package org.aikodi.chameleon.eclipse.presentation.treeview;

import org.aikodi.chameleon.core.element.Element;
import org.aikodi.chameleon.exception.ModelException;

/**
 * An interface for decorating icons by changing the name of the icon that must be used.
 * 
 * A real Eclipse decorators composes two images. For some icons, however, it uses different version of basically
 * the same icon. An example of this is the icon for a class. Instead of composing the access modifier on top of it,
 * there are 4 different icons: public, private, protected, default.
 * 
 * To simplify the implementation of this approach, the icon names (the keys in the image registry) follow a pattern.
 * First, a base name is chosen to denote the kind of the icon. Then, one or more NameBasedIconDecorator objects are used to map the
 * base name onto the final icon name. 
 * 
 * One possibility is to work with a prefix (PrefixIconDecorator), for example "public" in case of a public method. The name under which an icon is stored is then prefix followed by the kind name. For example, the icon for a public method is stored
 * under the name "publicmethod".
 * 
 * @author Marko van Dooren
 */
public interface NameBasedIconDecorator {

	/**
	 * Determine
	 */
	public String decorate(Element element, String baseName) throws ModelException;
}
