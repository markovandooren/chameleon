package chameleon.core.namespacepart;

import java.util.Set;

import chameleon.core.MetamodelException;
import chameleon.core.declaration.Declaration;
import chameleon.core.element.Element;

/**
 * @author Marko van Dooren
 */
public abstract class Import<E extends Element> extends NamespacePartElementImpl<E,NamespacePart> {

  public Import() {
	}

  
// THIS IS COVERED IN NAMESPACEPART
  
//  /**
//   * Get the context of this import statement.
//   * This import statement is in a package, the context of the defaultpackage
//   * of that package is returned
// * @throws MetamodelException 
//   */
//  public Context lexicalContext(Element element) throws MetamodelException {
//    return getParent().getDeclaredNamespace().getDefaultNamespace().lexicalContext(this);
//  }

//  /**
//   * An import statement is an element of a NamespacePart.
//   * The Namespace to which that NamespacePart belongs is returned.
//   */
//  public Namespace getNamespace() {
//    return getParent().getDeclaredNamespace();
//  }

  public abstract Set<Declaration> directImports() throws MetamodelException;
  
  public abstract Set<Declaration> demandImports() throws MetamodelException;
}
