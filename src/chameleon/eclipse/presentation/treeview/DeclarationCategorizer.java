package chameleon.eclipse.presentation.treeview;

import chameleon.core.declaration.Declaration;

/**
 * An interface to assign categories (int values) to declarations. These categories are
 * used in the plugin to sort declarations in e.g. the outline. The categorizer is only
 * used if the user selects sorting of declarations using the 'a->z' button.
 * 
 * @author Marko van Dooren
 */
public interface DeclarationCategorizer {

	/**
	 * Return the category of the given declaration. The lower the return value, the higher
	 * the element is placed in the view.
	 */
 /*@
   @ public behavior
   @
   @ pre declaration != null;
   @*/
	public int category(Declaration declaration);
}
