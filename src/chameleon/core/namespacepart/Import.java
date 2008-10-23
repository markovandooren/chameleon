package chameleon.core.namespacepart;

import chameleon.core.MetamodelException;
import chameleon.core.context.LexicalContext;
import chameleon.core.element.Element;
import chameleon.core.namespace.Namespace;

/**
 * @author marko
 */
public abstract class Import<E extends Element> extends NamespacePartElementImpl<E,NamespacePart> {

  public Import() {
	}

  /**
   * Get the context of this import statement.
   * This import statement is in a package, the context of the defaultpackage
   * of that package is returned
 * @throws MetamodelException 
   */
  public LexicalContext lexicalContext(Element element) throws MetamodelException {
    return getParent().getDeclaredNamespace().getDefaultNamespace().lexicalContext(this);
  }

  /**
   * An import statement is an element of a NamespacePart.
   * The Namespace to wich that NamespacePart belongs is returned.
   */
  public Namespace getNamespace() {
    return getParent().getDeclaredNamespace();
  }

}
