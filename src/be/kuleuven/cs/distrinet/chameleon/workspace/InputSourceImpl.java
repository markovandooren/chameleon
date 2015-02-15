package be.kuleuven.cs.distrinet.chameleon.workspace;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import be.kuleuven.cs.distrinet.chameleon.core.declaration.Declaration;
import be.kuleuven.cs.distrinet.chameleon.core.document.Document;
import be.kuleuven.cs.distrinet.chameleon.core.element.Element;
import be.kuleuven.cs.distrinet.chameleon.core.lookup.LookupException;
import be.kuleuven.cs.distrinet.chameleon.core.namespace.InputSourceNamespace;
import be.kuleuven.cs.distrinet.chameleon.core.namespace.Namespace;
import be.kuleuven.cs.distrinet.chameleon.core.namespacedeclaration.NamespaceDeclaration;
import be.kuleuven.cs.distrinet.chameleon.exception.ChameleonProgrammerException;
import be.kuleuven.cs.distrinet.rejuse.association.SingleAssociation;

public abstract class InputSourceImpl implements InputSource {
	
	protected void init(DocumentLoader loader) {
		setLoader(loader);
	}
	
	protected void init(InputSourceNamespace ns, DocumentLoader loader) throws InputException {
		init(loader);
		setNamespace(ns);
	}
	
	private void setLoader(DocumentLoader loader) {
		if(loader == null) {
			throw new ChameleonProgrammerException();
		}
		loader.addInputSource(this);
	}
	
	@Override
   public void setNamespace(InputSourceNamespace ns) throws InputException {
		ns.addInputSource(this);
	}
	
	@Override
   public InputSourceNamespace namespace() {
		return _namespace.getOtherEnd();
	}
	
	@Override
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
	
	protected abstract void doRefresh() throws InputException;
	
	@Override
	public Project project() {
		return loader().project();
	}
	
	@Override
   public View view() {
		DocumentLoader loader = loader();
		if(loader != null) {
			return loader.view();
		} else {
			throw new IllegalArgumentException();
		}
	}
	
	protected SingleAssociation<InputSourceImpl, Document> _document = new SingleAssociation<InputSourceImpl, Document>(this);
	
	@Override
   public DocumentLoader loader() {
		return _loader.getOtherEnd();
	}
	
	@Override
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
		NamespaceDeclaration namespaceDeclaration = rawDocument().namespaceDeclaration(1);
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
	
	/**
	 * Compare this input source with the given other input source. An input source
	 * that is bigger than another has priority
	 */
	@Override
   public int compareTo(InputSource other) {
		if(other == null) {
			// I know that I should throw an exception,
			// but I want to make it robust.
			return 1;
		} else {
			return loader().compareTo(other.loader());
		}
	}
}
