package chameleon.workspace;

import org.rejuse.association.SingleAssociation;

import chameleon.core.document.Document;
import chameleon.core.namespace.InputSourceNamespace;
import chameleon.core.namespace.Namespace;
import chameleon.core.namespacedeclaration.NamespaceDeclaration;

public abstract class InputSourceImpl implements InputSource {
	
	public InputSourceImpl() {
		
	}
	
	protected void setNamespace(InputSourceNamespace ns) {
		if(ns != null) {
			if(_namespace == null) {
				_namespace =  new SingleAssociation<InputSource, InputSourceNamespace>(this);
			}
			ns.addInputSource(this);
			_uniNamespace = null;
		}
	}
	
	public Namespace namespace() {
		if(_namespace != null) {
			return _namespace.getOtherEnd();
		} else {
			return _uniNamespace;
		}
	}
	
	protected void setUniNamespace(Namespace ns) {
		if(_namespace != null) {
			_namespace.connectTo(null);
		}
		if(ns != null) {
			_namespace = null;
		} else if(_namespace == null) {
			_namespace = new SingleAssociation<InputSource, InputSourceNamespace>(this);
		}
		_uniNamespace = ns;
	}
	
	private Namespace _uniNamespace;
	
	public SingleAssociation<InputSource, InputSourceNamespace> namespaceLink() {
		return _namespace;
	}
	
	// This one is lazily initialized!
	protected SingleAssociation<InputSource, InputSourceNamespace> _namespace;

	/**
	 * Return the document that is managed by this input source.
	 * @return
	 */
	public Document document() {
		return _document.getOtherEnd();
	}
	
	protected void setDocument(Document doc) {
		if(doc != null) {
			_document.connectTo(doc.inputSourceLink());
		} else {
			_document.connectTo(null);
		}
	}
	
	public boolean isLoaded() {
		return document() != null;
	}
	
	public final Document load() throws InputException {
		if(! isLoaded()) {
			doLoad();
			Document result = document();
			result.activate();
			return result;
		} else {
			return document();
		}
	}
	
	protected abstract void doLoad() throws InputException;
	
	@Override
	public Project project() {
		return loader().project();
	}
	
	protected SingleAssociation<InputSourceImpl, Document> _document = new SingleAssociation<InputSourceImpl, Document>(this);
	
	public ProjectLoader loader() {
		return _loader.getOtherEnd();
	}
	
	public SingleAssociation<InputSource, ProjectLoader> loaderLink() {
		return _loader;
	}
	
	protected SingleAssociation<InputSource, ProjectLoader> _loader = new SingleAssociation<InputSource, ProjectLoader>(this);
}
