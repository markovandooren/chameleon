package chameleon.workspace;

import org.rejuse.association.SingleAssociation;

import chameleon.core.document.Document;
import chameleon.core.namespace.InputSourceNamespace;
import chameleon.core.namespace.Namespace;

public abstract class InputSourceImpl implements InputSource {
	
	public InputSourceImpl() {
	}
	
	public InputSourceImpl(InputSourceNamespace ns) throws InputException {
		setNamespace(ns);
	}
	
	protected void setNamespace(InputSourceNamespace ns) throws InputException {
		if(ns != null) {
			ns.addInputSource(this);
		}
	}
	
	public Namespace namespace() {
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
	
	public View view() {
		return loader().view();
	}
	
	protected SingleAssociation<InputSourceImpl, Document> _document = new SingleAssociation<InputSourceImpl, Document>(this);
	
	public DocumentLoader loader() {
		return _loader.getOtherEnd();
	}
	
	public SingleAssociation<InputSource, DocumentLoader> loaderLink() {
		return _loader;
	}
	
	protected SingleAssociation<InputSource, DocumentLoader> _loader = new SingleAssociation<InputSource, DocumentLoader>(this);
}
