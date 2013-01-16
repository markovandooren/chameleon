package chameleon.workspace;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.rejuse.association.SingleAssociation;

import chameleon.core.declaration.Declaration;
import chameleon.core.document.Document;
import chameleon.core.lookup.LookupException;
import chameleon.core.namespace.InputSourceNamespace;
import chameleon.core.namespace.Namespace;
import chameleon.core.namespacedeclaration.NamespaceDeclaration;

public abstract class InputSourceImpl implements InputSource {
	
	protected InputSourceImpl() {
	}
	
	public InputSourceImpl(InputSourceNamespace ns) throws InputException {
		setNamespace(ns);
	}
	
	public void setNamespace(InputSourceNamespace ns) throws InputException {
		ns.addInputSource(this);
	}
	
	public InputSourceNamespace namespace() {
		return _namespace.getOtherEnd();
	}
	
	public SingleAssociation<InputSource, InputSourceNamespace> namespaceLink() {
		return _namespace;
	}
	
	protected SingleAssociation<InputSource, InputSourceNamespace> _namespace = new SingleAssociation<InputSource, InputSourceNamespace>(this);

	/**
	 * Return the document that is managed by this input source.
	 * @return
	 */
	public Document rawDocument() {
		return _document.getOtherEnd();
	}
	
	public Document document() throws InputException {
		return load();
	}
		
	protected void setDocument(Document doc) {
		if(doc != null) {
			_document.connectTo(doc.inputSourceLink());
		} else {
			_document.connectTo(null);
		}
	}
	
	public boolean isLoaded() {
		return _document.getOtherEnd() != null;
	}
	
	public final Document load() throws InputException {
		if(! isLoaded()) {
			doLoad();
			Document result = rawDocument();
			result.activate();
			notifyLoaded(result);
			return result;
		} else {
			return _document.getOtherEnd();
		}
	}
	
	protected abstract void doLoad() throws InputException;
	
	@Override
	public Project project() {
		return loader().project();
	}
	
	public View view() {
		DocumentLoader loader = loader();
		if(loader != null) {
			return loader.view();
		} else {
			throw new IllegalArgumentException();
		}
	}
	
	protected SingleAssociation<InputSourceImpl, Document> _document = new SingleAssociation<InputSourceImpl, Document>(this);
	
	public DocumentLoader loader() {
		return _loader.getOtherEnd();
	}
	
	public SingleAssociation<InputSource, DocumentLoader> loaderLink() {
		return _loader;
	}
	
	protected SingleAssociation<InputSource, DocumentLoader> _loader = new SingleAssociation<InputSource, DocumentLoader>(this);
	
	@Override
	public List<Declaration> targetDeclarations(String name) throws LookupException {
		try {
			load();
		} catch (InputException e) {
			throw new LookupException("Error opening file",e);
		}
		NamespaceDeclaration namespaceDeclaration = rawDocument().children(NamespaceDeclaration.class).get(0);
		if(namespaceDeclaration != null) {
			List<Declaration> children = (List)namespaceDeclaration.children(Declaration.class);
			List<Declaration> result = new ArrayList<Declaration>(1);
			for(Declaration t: children) {
				if(t.name().equals(name)) {
					result.add(t);
				}
			}
			return result;
		} else {
			throw new LookupException("No target declarations are defined in input source "+toString());
		}
	}

	@Override
	public List<String> targetDeclarationNames(Namespace ns) throws InputException {
		load();
		List<NamespaceDeclaration> cs = rawDocument().children(NamespaceDeclaration.class);
		if(! cs.isEmpty()) {
			NamespaceDeclaration namespaceDeclaration = cs.get(0);
			if(namespaceDeclaration != null) {
				List<Declaration> children = (List)namespaceDeclaration.children(Declaration.class);
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
		
		//throw new InputException("No target declarations are defined in input source "+toString());
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
	
//	protected void notifyUnloaded(Document document) {
//		for(DocumentLoadingListener listener: _listeners) {
//			listener.notifyUnloaded(document);
//		}
//	}
	
	private List<DocumentLoadingListener> _listeners;
	
	@Override
	public void destroy() {
		// This will break down the bidirectional association with the namespace
		// the association end of the namespace will send an event to the namespace
		// which will then remove this input source from its caches.
		_namespace.clear();
		_loader.clear();
		_document.clear();
		_listeners = null;
	}
}
