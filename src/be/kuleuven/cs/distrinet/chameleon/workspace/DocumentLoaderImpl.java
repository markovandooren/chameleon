package be.kuleuven.cs.distrinet.chameleon.workspace;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import be.kuleuven.cs.distrinet.chameleon.core.document.Document;
import be.kuleuven.cs.distrinet.chameleon.core.element.Element;
import be.kuleuven.cs.distrinet.chameleon.core.namespace.Namespace;
import be.kuleuven.cs.distrinet.rejuse.action.Action;
import be.kuleuven.cs.distrinet.rejuse.association.AssociationListener;
import be.kuleuven.cs.distrinet.rejuse.association.OrderedMultiAssociation;
import be.kuleuven.cs.distrinet.rejuse.association.SingleAssociation;
import be.kuleuven.cs.distrinet.rejuse.contract.Contracts;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableList.Builder;
import com.google.common.collect.ImmutableSet;

public abstract class DocumentLoaderImpl implements DocumentLoader {

	public DocumentLoaderImpl() {
		this(false);
	}
	
	/**
	 * Create a new document loader with the given base loader setting.
	 * 
	 * @param isBaseLoader Set to 'true' if the new loader is responsible for loading a base library.
	 */
	public DocumentLoaderImpl(boolean isBaseLoader) {
		_isBaseLoader = isBaseLoader;
		_viewLink.addListener(new AssociationListener<DocumentLoaderContainer>() {

			// WARNING

			// WE TUNNEL THE EXCEPTION THROUGH THE ASSOCIATION CLASSES
			// AND PERFORM THE ROLLBACK IN {@link Project#addSource(ProjectLoader)}
			@Override
			public void notifyElementAdded(DocumentLoaderContainer element) {
				try {
					notifyContainerConnected(element);
				} catch (ProjectException e) {
					throw new TunnelException(e);
				}
			}

			@Override
			public void notifyElementRemoved(DocumentLoaderContainer element) {
				try {
					notifyContainerRemoved(element);
				} catch (ProjectException e) {
					throw new TunnelException(e);
				}
			}

			@Override
			public void notifyElementReplaced(DocumentLoaderContainer oldElement, DocumentLoaderContainer newElement) {
				try {
					notifyProjectReplaced(oldElement, newElement);
				} catch (ProjectException e) {
					throw new TunnelException(e);
				}
			}
		});
	}

	/**
	 * Store whether this document loader is responsible for loading a base library.
	 */
	private boolean _isBaseLoader;

	public boolean isBaseLoader() {
		return _isBaseLoader;
	}

	static class TunnelException extends RuntimeException {

		public TunnelException(Throwable cause) {
			super(cause);
		}

	}

	/**
	 * This method is invoked when the loader is connected to a project. It should
	 * then put the required objects in place to populate the project. The loader
	 * is free to load the source files eagerly or lazily.
	 * @param project
	 * @throws ProjectException
	 */
	public void notifyContainerConnected(DocumentLoaderContainer project) throws ProjectException {
	}

	public void notifyContainerRemoved(DocumentLoaderContainer project) throws ProjectException {
	}

	public void notifyProjectReplaced(DocumentLoaderContainer old, DocumentLoaderContainer newProject) throws ProjectException {
	}

	private SingleAssociation<DocumentLoaderImpl, DocumentLoaderContainer> _viewLink = new SingleAssociation<>(this);

	public SingleAssociation<DocumentLoaderImpl, DocumentLoaderContainer> containerLink() {
		return _viewLink;
	}

	@Override
	public DocumentLoaderContainer container() {
		return _viewLink.getOtherEnd();
	}
	
	public View view() {
		DocumentLoaderContainer container = container();
		return container == null ? null : container.view();
	}

	public Project project() {
		return view().project();
	}

	private OrderedMultiAssociation<DocumentLoaderImpl, InputSource> _inputSources = new OrderedMultiAssociation<DocumentLoaderImpl, InputSource>(this) {
		protected void fireElementAdded(InputSource addedElement) {
			flushLocalCache();
			notifyAdded(addedElement);
			super.fireElementAdded(addedElement);

		};
		
		protected void fireElementRemoved(InputSource addedElement) {
			flushLocalCache();
			notifyRemoved(addedElement);
			super.fireElementRemoved(addedElement);
		};
		
		protected void fireElementReplaced(InputSource oldElement, InputSource newElement) {
			flushLocalCache();
			notifyAdded(oldElement);
			notifyRemoved(newElement);
			super.fireElementReplaced(oldElement, newElement);
		};
	};
	

	/**
	 * Add the given input source.
	 * @param source
	 */
	/*@
   @ public behavior
   @
   @ pre source != null;
   @
   @ post inputSources().contains(source);
   @ post inputSources().containsAll(\old(inputSources()));
   @*/
	public void addInputSource(InputSource source) {
		// The Association object will send the event and the attached listener
		// will invoke notifyAdded(InputSource).
		//		System.out.println("Adding "+source);
		Contracts.check(canAddInputSource(source), "The given input source cannot be handled by this loader.");
		if(source != null) {
			_inputSources.add(source.loaderLink());
		}
	}

	public boolean canAddInputSource(InputSource source) {
		return true;
	}

	@Override
	public List<InputSource> inputSources() {
		return _inputSources.getOtherEnds();
	}

	public List<Document> documents() throws InputException {
		if(_documentsCache == null) {
			Builder<Document> builder = ImmutableList.<Document>builder();
			for(InputSource source: inputSources()) {
				builder.add(source.load());
			}
			_documentsCache = builder.build();
		}
		return _documentsCache;
	}

	private List<Document> _documentsCache;

	@Override
	public <E extends Exception> void apply(Action<? extends Element, E> action) throws E, InputException {
		for(Document document: documents()) {
			document.apply(action);
		}
	}

	public void removeInputSource(InputSource source) {
		// The Association object will send the event and the attached listener
		// will invoke notifyRemoved(InputSource).
		if(source != null) {
			_inputSources.remove(source.loaderLink());
		}
		source.destroy();
	}

	public void addListener(InputSourceListener listener) {
		_listeners.add(listener);
	}

	public void removeListener(InputSourceListener listener) {
		_listeners.remove(listener);
	}

	public List<InputSourceListener> inputSourceListeners() {
		return new ArrayList<InputSourceListener>(_listeners);
	}

	private List<InputSourceListener> _listeners = new ArrayList<InputSourceListener>();

	private void notifyAdded(InputSource source) {
		flushLocalCache();
		for(InputSourceListener listener: inputSourceListeners()) {
			listener.notifyInputSourceAdded(source);
		}
	}

	private void notifyRemoved(InputSource source) {
		flushLocalCache();
		for(InputSourceListener listener: inputSourceListeners()) {
			listener.notifyInputSourceRemoved(source);
		}
	}

	@Override
	public void addAndSynchronizeListener(InputSourceListener listener) {
		addListener(listener);
		for(InputSource source: inputSources()) {
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
		for(InputSource source:inputSources()) {
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
	public int compareTo(DocumentLoader o) {
		return o == null ? -1 : 0;
	}

	public List<Namespace> topLevelNamespaces() {
		ImmutableSet.Builder<Namespace> builder = ImmutableSet.<Namespace>builder();
		for(InputSource source: inputSources()) {
			Namespace namespace = source.namespace();
			boolean added = false;
			while(! added) { 
				if(namespace.parent() != null && (namespace.parent().parent() != null)) { 
					namespace = (Namespace) namespace.parent();
				} else {
					added = true;
					builder.add(namespace);
				}
			}
		}
		ImmutableSet<Namespace> allNamespaces = builder.build();
		return ImmutableList.copyOf(allNamespaces);
	}

	public Set<Namespace> namespaces() {
		if(_namespaceCache == null) {
			ImmutableSet.Builder<Namespace> builder = ImmutableSet.<Namespace>builder();
			for(InputSource source: inputSources()) {
				builder.add(source.namespace());
			}
			_namespaceCache = builder.build(); 
		}
		return _namespaceCache;
	}
	
	private Set<Namespace> _namespaceCache;

	@Override
	public int nbInputSources() {
		return _inputSources.size();
	}

	/**
	 * DEFAULT IMPLEMENTATION returns true if and only if
	 * the given loader is this object.
	 */
	@Override
	public boolean loadsSameAs(DocumentLoader loader) {
		return loader == this;
	}
	
	@Override
	public DocumentLoader rootLoader() {
		if(container()  instanceof DocumentLoader) {
			return ((DocumentLoader)container()).rootLoader();
		} else {
			return this;
		}
	}

}
