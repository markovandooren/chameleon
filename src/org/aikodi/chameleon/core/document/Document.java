package org.aikodi.chameleon.core.document;

import java.util.List;

import org.aikodi.chameleon.core.element.Element;
import org.aikodi.chameleon.core.element.ElementImpl;
import org.aikodi.chameleon.core.language.Language;
import org.aikodi.chameleon.core.lookup.LookupContext;
import org.aikodi.chameleon.core.lookup.LookupException;
import org.aikodi.chameleon.core.namespacedeclaration.NamespaceDeclaration;
import org.aikodi.chameleon.exception.ChameleonProgrammerException;
import org.aikodi.chameleon.util.Util;
import org.aikodi.chameleon.util.association.Multi;
import org.aikodi.chameleon.workspace.DocumentLoader;
import org.aikodi.chameleon.workspace.FakeDocumentLoader;
import org.aikodi.chameleon.workspace.FakeDocumentScanner;
import org.aikodi.chameleon.workspace.ProjectException;
import org.aikodi.chameleon.workspace.View;
import org.aikodi.contract.Contracts;
import org.aikodi.rejuse.association.SingleAssociation;

/**
 * <p>
 * A document represents an artefact in which elements of the program/model are
 * defined. This will often correspond to a file.
 * </p>
 * 
 * <p>
 * A document does not directly contain the source code elements. It contains
 * the namespace declarations ({@link NamespaceDeclaration}) that contain the
 * elements and associate them with a namespace. Namespace declarations can be
 * nested. If a language does not explicitly mention a general namespace (such
 * as Eiffel), a namespace declaration should be used that puts its contents in
 * the root namespace.
 * </p>
 * 
 * <p>
 * Each document is linked to the {@link DocumentLoader} that is responsible for
 * populating the document. The document only connects itself to a project after
 * invoking ({@link #activate()})! This should be done automatically by the
 * {@link DocumentLoader} when a document is loaded by the lookup mechanism and
 * when it is reparsed.
 * </p>
 * 
 * @author Marko van Dooren
 */
public class Document extends ElementImpl {

	/**
	 * The elements in this document.
	 */
  private Multi<Element> _elements = new Multi<>(this);

  /**
   * Create a new empty document. The document is not activated.
   */
  /*@
    @ public behavior
    @
    @ post children().isEmpty();
    @*/
  public Document() {
  }

  /**
   * Create a new compilation unit with the given namespace declaration. The
   * document is not activated.
   * 
   * @param namespaceDeclaration
   *          The namespace declaration that is added.
   */
  /*@
    @ public behavior
    @
    @ post children().size() == 1;
    @ post children().contains(namespaceDeclaration);
    @*/
  public Document(Element element) {
    add(element);
  }

  /**
   * Activate this document by letting all child namespace declarations activate
   * themselves. This adds the contents of this document to the logical
   * namespace structure of the project.
   */
  public void activate() {
    for (NamespaceDeclaration part : children(NamespaceDeclaration.class)) {
      part.activate();
    }
  }

  /************
   * Children *
   ************/

  /**
   * Add the given namespace declaration to this document.
   * 
   * @param namespaceDeclaration
   *          The namespace declaration to be added.
   */
  /*@
    @ public behavior
    @
    @ ! \old(namespaceDeclarations().contains(namespaceDeclaration)) ==> 
    @        namespaceDeclaration(namespaceDeclarations().size()) == namespaceDeclaration;
    @ ! \old(namespaceDeclarations().contains(namespaceDeclaration) ==>
    @        namespaceDeclarations().size() == \old(namespaceDeclarations().size()) + 1;
    @*/
  public void add(Element element) {
    add(_elements, element);
  }

  /**
   * Remove the given namespace declaration to this document.
   * 
   * @param namespaceDeclaration
   *          The namespace declaration to be added.
   */
  /*
   @ public behavior
   @
   @ ! namespaceDeclarations().contains(namespaceDeclaration);
   @ ! \old(namespaceDeclarations().contains(namespaceDeclaration) ==>
   @        namespaceDeclarations().size() == \old(namespaceDeclarations().size()) - 1;
   */
  public void remove(Element namespaceDeclaration) {
    remove(_elements, namespaceDeclaration);
  }

  /**
   * <p>
   * Always throws a {@link LookupException}. A document should not be involved
   * in the lookup process. The child namespace declarations should redirect the
   * lookup towards the general namespace.
   * </p>
   */
  @Override
  public LookupContext lookupContext(Element child) throws LookupException {
    throw new ChameleonProgrammerException("A document should not be involved in the lookup");
  }

  /**
   * {@inheritDoc}
   * 
   * @return The language of the view of the loader.
   */
  @Override
  public Language language() {
    return loader().scanner().view().language();
  }

  @Override
  protected Document cloneSelf() {
    return new Document();
  }

  /**
   * <p>Copy this document to the given view and activate it.</p>
   * 
   * <p>The result is a clone of this document. All elements in the
   * clone will have the corresponding element in the descendant tree
   * of this document as their {@link Element#origin()}. All namespaces
   * that are populated by namespace declarations in this document
   * will be created if necessary. The document is activated.</p>
   * 
   * @param view The view to which this document must be copied.
   * 
   * @return A clone of the document in the given view.
   */
  public Document cloneTo(View view) {
    Contracts.notNull(view);
    Document clone = (Document) clone((e1,e2) -> {e2.setOrigin(e1);}, Element.class);
    // Document clone = (Document) clone();
    FakeDocumentScanner pl = new FakeDocumentScanner();
    DocumentLoader is = new FakeDocumentLoader(clone, pl);
    // FIXME TODO Get rid of this dependency.
    for (NamespaceDeclaration decl : lexical().descendants(NamespaceDeclaration.class)) {
      view.namespace().getOrCreateNamespace(decl.namespace().fullyQualifiedName());
    }
    try {
      view.addSource(pl);
    } catch (ProjectException e) {
      throw new ChameleonProgrammerException(e);
    }
    clone.activate();
    return clone;
  }

  /**
   * @return The association object that links this document to its loader.
   */
  public SingleAssociation<Document, DocumentLoader> loaderLink() {
    return _loader;
  }

  /**
   * @return the document loader that is responsible for loading the contents of
   *         this document.
   */
  public DocumentLoader loader() {
    return _loader.getOtherEnd();
  }

  protected SingleAssociation<Document, DocumentLoader> _loader = new SingleAssociation<Document, DocumentLoader>(this);

  /**
   * @return The view of a document is the view to which its document loader is
   *         connected.
   */
  /*@
    @ public behavior
    @
    @ post \result == documentLoader().view();
    @*/
  @Override
  public View view() {
    return loader().view();
  }
}
