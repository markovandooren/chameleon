package org.aikodi.chameleon.workspace;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.aikodi.chameleon.core.declaration.Declaration;
import org.aikodi.chameleon.core.document.Document;
import org.aikodi.chameleon.core.element.Element;
import org.aikodi.chameleon.core.lookup.LookupException;
import org.aikodi.chameleon.core.namespace.DocumentLoaderNamespace;
import org.aikodi.chameleon.core.namespace.Namespace;
import org.aikodi.chameleon.core.namespacedeclaration.NamespaceDeclaration;
import org.aikodi.chameleon.exception.ChameleonProgrammerException;

import be.kuleuven.cs.distrinet.rejuse.association.SingleAssociation;

/**
 * A default implementation for document loaders.
 * 
 * @author Marko van Dooren
 */
public abstract class DocumentLoaderImpl implements DocumentLoader {
	
	protected void init(DocumentScanner scanner) {
		setScanner(scanner);
	}
	
	protected void init(DocumentLoaderNamespace ns, DocumentScanner scanner) throws InputException {
		init(scanner);
		setNamespace(ns);
	}
	
	private void setScanner(DocumentScanner scanner) {
		if(scanner == null) {
			throw new ChameleonProgrammerException();
		}
		scanner.add(this);
	}
	
	@Override
   public void setNamespace(DocumentLoaderNamespace ns) throws InputException {
		ns.addDocumentLoader(this);
	}
	
	@Override
   public DocumentLoaderNamespace namespace() {
		return _namespace.getOtherEnd();
	}
	
	@Override
   public SingleAssociation<DocumentLoader, DocumentLoaderNamespace> namespaceLink() {
		return _namespace;
	}
	
	protected SingleAssociation<DocumentLoader, DocumentLoaderNamespace> _namespace = new SingleAssociation<DocumentLoader, DocumentLoaderNamespace>(this);

	/**
	 * Return a direct reference to the managed document. This may return null
	 * as this method does not load the document. If that is required, use 
	 * {@link #document()} instead.
	 * 
	 * @return the document that is managed by this document loader.
	 */
	protected Document rawDocument() {
		return _document.getOtherEnd();
	}
	
//	/**
//	 * Loads ({@link #load()}) and returns the document managed by this document loader.
//	 * 
//	 * @return the document managed by this document loader.
//	 * @throws InputException
//	 */
//	public Document document() throws InputException {
//		return load();
//	}
		
	/**
	 * Set the document managed by this document loader.
	 * 
	 * @param document The document that is managed by this document loader.
	 */
	protected void setDocument(Document document) {
		if(document != null) {
			_document.connectTo(document.loaderLink());
		} else {
			_document.connectTo(null);
		}
	}
	
	/**
	 * @return True if and only if the document is loaded.
	 */
	public boolean isLoaded() {
		return _document.getOtherEnd() != null;
	}
	
	@Override
   public final synchronized Document load() throws InputException {
		if(! isLoaded()) {
			return refresh();
		} else {
			return rawDocument();
		}
	}
	
//	public static Stopwatch LOADING_TIME = new Stopwatch();

	@Override
   public final Document refresh() throws InputException {
//		LOADING_TIME.start();
		doRefresh();
		Document result = rawDocument();
		result.activate();
		notifyLoaded(result);
//		LOADING_TIME.stop();
		return result;
	}
	
	/**
	 * Actuall load the document. Invoke {@link #setDocument(Document)} to
	 * store the loaded document.
	 * 
	 * @throws InputException The document could not be loaded.
	 */
	protected abstract void doRefresh() throws InputException;
	
	@Override
	public Project project() {
		return scanner().project();
	}
	
	@Override
   public View view() {
		DocumentScanner scanner = scanner();
		if(scanner != null) {
			return scanner.view();
		} else {
			throw new IllegalArgumentException();
		}
	}
	
	protected SingleAssociation<DocumentLoaderImpl, Document> _document = new SingleAssociation<DocumentLoaderImpl, Document>(this);
	
	@Override
   public DocumentScanner scanner() {
		return _scanner.getOtherEnd();
	}
	
	@Override
   public SingleAssociation<DocumentLoader, DocumentScanner> scannerLink() {
		return _scanner;
	}
	
	protected SingleAssociation<DocumentLoader, DocumentScanner> _scanner = new SingleAssociation<DocumentLoader, DocumentScanner>(this);
	
	@Override
	public List<Declaration> targetDeclarations(String name) throws LookupException {
		try {
			load();
		} catch (InputException e) {
			throw new LookupException("Error opening file",e);
		}
		NamespaceDeclaration namespaceDeclaration = rawDocument().namespaceDeclaration(0);
		if(namespaceDeclaration != null) {
			List<Element> children = (List)namespaceDeclaration.children();
			List<Declaration> result = new ArrayList<Declaration>(1);
			for(Element t: children) {
				if(t instanceof Declaration && ((Declaration)t).name().equals(name)) {
					result.add((Declaration) t);
				}
			}
			return result;
		} else {
			throw new LookupException("No target declarations are defined in document loader "+toString());
		}
	}

	@Override
	public List<String> targetDeclarationNames(Namespace ns) throws InputException {
		load();
		List<NamespaceDeclaration> cs = rawDocument().children(NamespaceDeclaration.class);
		if(! cs.isEmpty()) {
			NamespaceDeclaration namespaceDeclaration = cs.get(0);
			if(namespaceDeclaration != null) {
				List<Declaration> children = namespaceDeclaration.children(Declaration.class);
				List<String> result = new ArrayList<String>();
				for(Declaration t: children) {
					result.add(t.name());
				}
				return result;
			}
		} 
		// Lets make it robust and return an empty collection if there is no content. This typically
		// indicates the addition of a file of a language that doesn't support lazy loading to a namespace.
		// Since we load the file anyway, we know that it doesn't contain namespace declarations. If that
		// changes, the document must have changed, and any loaded namespace declarations will be activated.
		
		//throw new InputException("No target declarations are defined in document loader "+toString());
		return Collections.EMPTY_LIST;
	}

	@Override
	public void flushCache() {
		Document document = rawDocument();
		if(document != null) {
			document.flushCache();
		}
	}
	
	public void addLoadListener(DocumentLoadingListener listener) {
		if(_listeners == null) {
			_listeners = new ArrayList<DocumentLoadingListener>();
		}
		_listeners.add(listener);
	}
	
	public void removeLoadListener(DocumentLoadingListener listener) {
		if(_listeners != null) {
			_listeners.remove(listener);
		}
	}
	
	protected void notifyLoaded(Document document) {
		if(_listeners != null) {
			for(DocumentLoadingListener listener: _listeners) {
				listener.notifyLoaded(document);
			}
		}
	}
	
	private List<DocumentLoadingListener> _listeners;
	
	@Override
	public void destroy() {
		// This will break down the bidirectional association with the namespace
		// the association end of the namespace will send an event to the namespace
		// which will then remove this document loader from its caches.
    DocumentLoaderNamespace namespace = namespace();
    if(namespace != null) {
      namespace.flushCache();
    }
		_namespace.clear();
		_scanner.clear();
		Document doc = rawDocument();
		if(doc != null) {
      doc.disconnect();
		}
		_document.clear();
		_listeners = null;
	}
	
	/**
	 * Compare this document loader with the given other document loader. 
	 * A document loader that is bigger than another has a higher priority.
	 * 
	 * FIXME: this is a bad design. A document scanner path (similar to classpath)
	 * or a proper module system is needed.
	 */
	@Override
   public int compareTo(DocumentLoader other) {
		if(other == null) {
			// I know that I should throw an exception,
			// but I want to make it robust.
			return 1;
		} else {
			return scanner().compareTo(other.scanner());
		}
	}
}
