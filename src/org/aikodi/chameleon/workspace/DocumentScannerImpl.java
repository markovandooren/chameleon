package org.aikodi.chameleon.workspace;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.aikodi.chameleon.core.document.Document;
import org.aikodi.chameleon.core.element.Element;
import org.aikodi.chameleon.core.namespace.Namespace;
import org.aikodi.rejuse.action.UniversalConsumer;
import org.aikodi.rejuse.association.AssociationListener;
import org.aikodi.rejuse.association.OrderedMultiAssociation;
import org.aikodi.rejuse.association.SingleAssociation;
import org.aikodi.rejuse.contract.Contracts;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableList.Builder;
import com.google.common.collect.ImmutableSet;

/**
 * A default implementation for document scanners.
 * 
 * Subclasses should override {@link #notifyContainerConnected(DocumentScannerContainer)}
 * to create {@link DocumentLoader}s and attach them to the appropriate namespaces.
 *
 * @author Marko van Dooren
 */
public abstract class DocumentScannerImpl implements DocumentScanner {

   /**
    * Create a new document scanner that is not responsible for scanning
    * a base library.
    */
	public DocumentScannerImpl() {
		this(false);
	}
	
	/**
	 * Create a new document scanner with the given base scanner setting.
	 * 
	 * @param isBaseScanner Set to 'true' if the new scanner is responsible for scanning a base library.
	 */
	public DocumentScannerImpl(boolean isBaseScanner) {
		_isBaseScanner = isBaseScanner;
		_viewLink.addListener(new AssociationListener<DocumentScannerContainer>() {

			/** 
			 * WARNING: WE TUNNEL THE EXCEPTION THROUGH THE ASSOCIATION CLASSES
			 * AND PERFORM THE ROLLBACK IN {@link View#addSource(DocumentScanner)}
			 *
			 * @param element
			 */
			@Override
			public void notifyElementAdded(DocumentScannerContainer element) {
				try {
					notifyContainerConnected(element);
				} catch (ProjectException e) {
					throw new TunnelException(e);
				}
			}

			@Override
			public void notifyElementRemoved(DocumentScannerContainer element) {
				try {
					notifyContainerRemoved(element);
				} catch (ProjectException e) {
					throw new TunnelException(e);
				}
			}

			@Override
			public void notifyElementReplaced(DocumentScannerContainer oldElement, DocumentScannerContainer newElement) {
				try {
					notifyProjectReplaced(oldElement, newElement);
				} catch (ProjectException e) {
					throw new TunnelException(e);
				}
			}
		});
	}

	/**
	 * Store whether this document scanner is responsible for loading a base library.
	 */
	private boolean _isBaseScanner;

	@Override
   public boolean isBaseScanner() {
		return _isBaseScanner;
	}

	/**
	 * A class for tunneling exceptions.
	 * 
	 * @author Marko van Dooren
	 */
	static class TunnelException extends RuntimeException {

      private static final long serialVersionUID = 1L;

      public TunnelException(Throwable cause) {
			super(cause);
		}

	}

	/**
	 * This method is invoked when the scanner is connected to a container. It should
	 * then put the required objects in place to populate the project. The scanner
	 * is free to load the source files eagerly or lazily.
	 * 
	 * @param container The container to which this scanner is added.
	 * @throws ProjectException
	 */
	@Override
   public void notifyContainerConnected(DocumentScannerContainer container) throws ProjectException {
	}

   /**
    * This method is invoked when the scanner is removed from a container. It should
    * destroy all loaded documents.
    * 
    * @param container The container from which this scanner is removed.
    * @throws ProjectException
    */
	@Override
   public void notifyContainerRemoved(DocumentScannerContainer container) throws ProjectException {
	}

   /**
    * This method is invoked when the scanner is removed from a container. It should
    * destroy all loaded documents.
    * 
    * @param oldContainer The container to which this scanner is added.
    * @param newContainer The container from which this scanner is removed.
    * @throws ProjectException
    */
	@Override
   public void notifyProjectReplaced(DocumentScannerContainer oldContainer, DocumentScannerContainer newContainer) throws ProjectException {
	}

	private SingleAssociation<DocumentScannerImpl, DocumentScannerContainer> _viewLink = new SingleAssociation<>(this);

	@Override
   public SingleAssociation<DocumentScannerImpl, DocumentScannerContainer> containerLink() {
		return _viewLink;
	}

	@Override
	public DocumentScannerContainer container() {
		return _viewLink.getOtherEnd();
	}
	
	@Override
   public View view() {
		DocumentScannerContainer container = container();
		return container == null ? null : container.view();
	}

	@Override
   public Project project() {
		return view().project();
	}

	private OrderedMultiAssociation<DocumentScannerImpl, DocumentLoader> _documentLoaders = new OrderedMultiAssociation<DocumentScannerImpl, DocumentLoader>(this) {
		@Override
      protected void fireElementAdded(DocumentLoader addedElement) {
			flushLocalCache();
			notifyAdded(addedElement);
			super.fireElementAdded(addedElement);

		};
		
		@Override
      protected void fireElementRemoved(DocumentLoader addedElement) {
			flushLocalCache();
			notifyRemoved(addedElement);
			super.fireElementRemoved(addedElement);
		};
		
		@Override
      protected void fireElementReplaced(DocumentLoader oldElement, DocumentLoader newElement) {
			flushLocalCache();
			notifyAdded(oldElement);
			notifyRemoved(newElement);
			super.fireElementReplaced(oldElement, newElement);
		};
	};
	

	/**
	 * Add the given document loader.
	 * @param source
	 */
	/*@
   @ public behavior
   @
   @ pre source != null;
   @
   @ post documentLoaders().contains(source);
   @ post documentLoaders().containsAll(\old(documentLoaders()));
   @*/
	@Override
   public void add(DocumentLoader source) {
		// The Association object will send the event and the attached listener
		// will invoke notifyAdded(DocumentLoader).
		Contracts.check(canAddDocumentLoader(source), "The given document loader cannot be handled by this scanner.");
		if(source != null) {
			_documentLoaders.add(source.scannerLink());
		}
	}

	public boolean canAddDocumentLoader(DocumentLoader source) {
		return true;
	}

	@Override
	public List<DocumentLoader> documentLoaders() {
		return _documentLoaders.getOtherEnds();
	}

	@Override
   public List<Document> documents() throws InputException {
		if(_documentsCache == null) {
			Builder<Document> builder = ImmutableList.<Document>builder();
			for(DocumentLoader source: documentLoaders()) {
				builder.add(source.load());
			}
			_documentsCache = builder.build();
		}
		return _documentsCache;
	}

	private List<Document> _documentsCache;

	@Override
	public <E extends Exception> void apply(UniversalConsumer<? extends Element, E> action) throws E, InputException {
		for(Document document: documents()) {
			document.lexical().apply(action);
		}
	}

    @Override
    public <E extends Exception> void applyToLoaders(UniversalConsumer<DocumentLoader, E> action) throws E {
        for(DocumentLoader loader: documentLoaders()) {
            action.perform(loader);
        }
    }

	/**
	 * Remove the given document loader.
	 * 
	 * @param source The document loader to be removed.ƒ
	 */
	public void remove(DocumentLoader source) {
		// The Association object will send the event and the attached listener
		// will invoke notifyRemoved(DocumentLoader).
		if(source != null) {
	    source.destroy();
			_documentLoaders.remove(source.scannerLink());
		}
	}

	@Override
   public void addListener(DocumentLoaderListener listener) {
		_listeners.add(listener);
	}

	@Override
   public void removeListener(DocumentLoaderListener listener) {
		_listeners.remove(listener);
	}

	@Override
   public List<DocumentLoaderListener> documentLoaderListeners() {
		return new ArrayList<DocumentLoaderListener>(_listeners);
	}

	private List<DocumentLoaderListener> _listeners = new ArrayList<DocumentLoaderListener>();

	private void notifyAdded(DocumentLoader source) {
		flushLocalCache();
		for(DocumentLoaderListener listener: documentLoaderListeners()) {
			listener.notifyDocumentLoaderAdded(source);
		}
	}

	private void notifyRemoved(DocumentLoader source) {
		flushLocalCache();
		for(DocumentLoaderListener listener: documentLoaderListeners()) {
			listener.notifyDocumentLoaderRemoved(source);
		}
	}

	@Override
	public void addAndSynchronizeListener(DocumentLoaderListener listener) {
		addListener(listener);
		for(DocumentLoader source: documentLoaders()) {
			notifyAdded(source);
		}
	}

	protected void flushLocalCache() {
		_documentsCache = null;
		_namespaceCache = null;
	}

	@Override
	public void flushCache() {
		flushLocalCache();
		for(DocumentLoader source:documentLoaders()) {
			source.flushCache();
		}
	}

	/**
	 * Returns 1 when o is null. Otherwise, the return value 
	 * may differ. The default (non-binding) implementation is
	 * to return 0.
	 * 
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 */
	/*@
   @ public behavior
   @
   @ post o == null ==> \result == -1;
   @*/
	@Override
	public int compareTo(DocumentScanner o) {
		return o == null ? -1 : 0;
	}

	@Override
   public List<Namespace> topLevelNamespaces() {
		ImmutableSet.Builder<Namespace> builder = ImmutableSet.<Namespace>builder();
		for(DocumentLoader source: documentLoaders()) {
			Namespace namespace = source.namespace();
			boolean added = false;
			while(! added) { 
				if(namespace.lexical().parent() != null && (namespace.lexical().parent().lexical().parent() != null)) { 
					namespace = (Namespace) namespace.lexical().parent();
				} else {
					added = true;
					builder.add(namespace);
				}
			}
		}
		ImmutableSet<Namespace> allNamespaces = builder.build();
		return ImmutableList.copyOf(allNamespaces);
	}

	@Override
   public Set<Namespace> namespaces() {
		if(_namespaceCache == null) {
			ImmutableSet.Builder<Namespace> builder = ImmutableSet.<Namespace>builder();
			for(DocumentLoader source: documentLoaders()) {
				builder.add(source.namespace());
			}
			_namespaceCache = builder.build(); 
		}
		return _namespaceCache;
	}
	
	private ImmutableSet<Namespace> _namespaceCache;

	@Override
	public int nbDocumentLoaders() {
		return _documentLoaders.size();
	}

	/**
	 * DEFAULT IMPLEMENTATION returns true if and only if
	 * the given scanner is this object.
	 */
	@Override
	public boolean scansSameAs(DocumentScanner scanner) {
		return scanner == this;
	}
	
	@Override
	public DocumentScanner rootScanner() {
		if(container()  instanceof DocumentScanner) {
			return ((DocumentScanner)container()).rootScanner();
		} else {
			return this;
		}
	}

}
