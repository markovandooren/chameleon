package be.kuleuven.cs.distrinet.chameleon.core.document;

import java.util.List;

import be.kuleuven.cs.distrinet.chameleon.core.element.Element;
import be.kuleuven.cs.distrinet.chameleon.core.element.ElementImpl;
import be.kuleuven.cs.distrinet.chameleon.core.language.Language;
import be.kuleuven.cs.distrinet.chameleon.core.lookup.LookupContext;
import be.kuleuven.cs.distrinet.chameleon.core.lookup.LookupException;
import be.kuleuven.cs.distrinet.chameleon.core.namespacedeclaration.NamespaceDeclaration;
import be.kuleuven.cs.distrinet.chameleon.core.validation.Valid;
import be.kuleuven.cs.distrinet.chameleon.core.validation.Verification;
import be.kuleuven.cs.distrinet.chameleon.exception.ChameleonProgrammerException;
import be.kuleuven.cs.distrinet.chameleon.util.association.Multi;
import be.kuleuven.cs.distrinet.chameleon.workspace.DocumentScanner;
import be.kuleuven.cs.distrinet.chameleon.workspace.DocumentScannerImpl;
import be.kuleuven.cs.distrinet.chameleon.workspace.InputException;
import be.kuleuven.cs.distrinet.chameleon.workspace.DocumentLoader;
import be.kuleuven.cs.distrinet.chameleon.workspace.DocumentLoaderImpl;
import be.kuleuven.cs.distrinet.chameleon.workspace.ProjectException;
import be.kuleuven.cs.distrinet.chameleon.workspace.View;
import be.kuleuven.cs.distrinet.rejuse.association.SingleAssociation;

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

    //	private CreationStackTrace _trace = new CreationStackTrace();

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
     * Create a new compilation unit with the given namespace declaration.
     * The document is not activated.
     * 
     * @param namespaceDeclaration The namespace declaration that is added.
     */
   /*@
     @ public behavior
     @
     @ post children().size() == 1;
     @ post children().contains(namespaceDeclaration);
     @*/
    public Document(NamespaceDeclaration namespaceDeclaration) {
        add(namespaceDeclaration);
    }

    /**
     * Activate this document by letting all child namespace declarations
     * activate themselves. This adds the contents
     * of this document to the logical namespace structure of the project. 
     */
    public void activate() {
        for(NamespaceDeclaration part: namespaceDeclarations()) {
            part.activate();
        }
    }

    /************
     * Children *
     ************/

    /**
     * Return the namespace declarations in this document.
     */
    public List<NamespaceDeclaration> namespaceDeclarations() {
        return _subNamespaceParts.getOtherEnds();
    }

    /**
     * Indices start at 1.
     * @param baseOneIndex
     * @return
     */
    public NamespaceDeclaration namespaceDeclaration(int baseOneIndex) {
        return _subNamespaceParts.elementAt(baseOneIndex);
    }

    /**
     * Add the given namespace declaration to this document.
     * @param namespaceDeclaration The namespace declaration to be added.
     */
    /*@
   @ public behavior
   @
   @ ! \old(namespaceDeclarations().contains(namespaceDeclaration)) ==> 
   @        namespaceDeclaration(namespaceDeclarations().size()) == namespaceDeclaration;
   @ ! \old(namespaceDeclarations().contains(namespaceDeclaration) ==>
   @        namespaceDeclarations().size() == \old(namespaceDeclarations().size()) + 1;
   @*/
    public void add(NamespaceDeclaration namespaceDeclaration) {
        add(_subNamespaceParts,namespaceDeclaration);
    }

    /**
     * Remove the given namespace declaration to this document.
     * @param namespaceDeclaration The namespace declaration to be added.
     */
    /*@
   @ public behavior
   @
   @ ! namespaceDeclarations().contains(namespaceDeclaration);
   @ ! \old(namespaceDeclarations().contains(namespaceDeclaration) ==>
   @        namespaceDeclarations().size() == \old(namespaceDeclarations().size()) - 1;
   @*/
    public void remove(NamespaceDeclaration namespaceDeclaration) {
        remove(_subNamespaceParts,namespaceDeclaration);
    }

    private Multi<NamespaceDeclaration> _subNamespaceParts = new Multi<NamespaceDeclaration>(this);


    /**
     * Normally a document should not be involved in the lookup process. The child namespace declarations
     * should redirect the lookup towards the general namespace.
     */
    @Override
   public LookupContext lookupContext(Element child) throws LookupException {
        throw new ChameleonProgrammerException("A document should not be involved in the lookup");
    }

    //TODO Document why this implementation differs from the default and does not go via
    //     the document loader.
    @Override
   public Language language() {
        List<NamespaceDeclaration> parts = namespaceDeclarations();
        Language result = null;
        if(parts.size() > 0) {
            NamespaceDeclaration firstNamespace = parts.get(0);
            if(firstNamespace != null) {
                result = firstNamespace.language();
            }
        }
        return result;
    }


    @Override
    protected Document cloneSelf() {
        return new Document();
    }

    @Override
    public Verification verifySelf() {
        return Valid.create();
    }

    private static class FakeDocumentLoader extends DocumentLoaderImpl {

        public FakeDocumentLoader(Document document, DocumentScanner scanner) {
            init(scanner);
            setDocument(document);
        }

        /**
         * We do nothing for a fake document loader. The content was set directly.
         */
        @Override
        public void doRefresh() throws InputException {
        }
    }

    private static class FakeDocumentScanner extends DocumentScannerImpl {

        @Override
        public String label() {
            return "fake";
        }

    }

    @Deprecated
    public Document cloneTo(View view) {
        Document clone = clone(this);
        //		Document clone = (Document) clone();
        FakeDocumentScanner pl = new FakeDocumentScanner();
        DocumentLoader is = new FakeDocumentLoader(clone,pl);
        for(NamespaceDeclaration decl: descendants(NamespaceDeclaration.class)) {
            view.namespace().getOrCreateNamespace(decl.namespace().getFullyQualifiedName());
        }
        try {
            view.addSource(pl);
        } catch (ProjectException e) {
            throw new ChameleonProgrammerException(e);
        }
        clone.activate();
        return clone;
    }

    public SingleAssociation<Document, DocumentLoader> loaderLink() {
        return _loader;
    }

    /**
     * Return the document loader that is responsible for loading the contents of this document.
     * @return
     */
    public DocumentLoader loader() {
        return _loader.getOtherEnd();
    }

    protected SingleAssociation<Document, DocumentLoader> _loader = new SingleAssociation<Document, DocumentLoader>(this);

    /**
     * The view of a document is the view to which its document loader is connected.
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
