package chameleon.eclipse.presentation.treeview;

import org.eclipse.jface.viewers.ViewerComparator;

import chameleon.core.declaration.Declaration;
import chameleon.core.element.Element;
import chameleon.core.language.Language;
import chameleon.eclipse.connector.EclipseEditorExtension;

/**
 * A class for comparing objects in order to sort them in an Eclipse view. The comparator
 * uses the Eclipse mechanism of assigning a category to different kinds of elements.
 * The super class uses alphabetical sorting of the lowercase value return by the label provider
 * to sort elements within a category.
 * 
 * If you want a completely different sorting, you must directly extend ViewerComparator and override the compare
 * method.
 * 
 * The class wrap a DeclarationCategorizer because otherwise, the connector classes for language modules depend on
 * Eclipse classes, resulting in classpath problems.
 * 
 * @author Marko van Dooren
 */
public class ChameleonViewComparator extends ViewerComparator {
	
	public ChameleonViewComparator() {
		// Only explicit to be able to view the constructor call chain.
	}
	
	/**
	 * This method determines the order in which the elements are shown in the outline. Elements
	 * are grouped per category. The work is delegated to the declarationCategorize() of the
	 * editor extension.
	 */
	@Override
	public int category(Object object) {
	  Element element = ChameleonLabelProvider.getElement(object);
	  int result = 0;
		if(element instanceof Declaration) {
			Language language = element.language();
			DeclarationCategorizer categorizer = language.plugin(EclipseEditorExtension.class).declarationCategorizer();
			result = categorizer.category((Declaration)element);
		}
		return result;
	}
	
}
