package be.kuleuven.cs.distrinet.chameleon.workspace;

import java.util.ArrayList;
import java.util.List;

import be.kuleuven.cs.distrinet.rejuse.association.AssociationListener;
import be.kuleuven.cs.distrinet.rejuse.association.OrderedMultiAssociation;
import be.kuleuven.cs.distrinet.rejuse.association.SingleAssociation;
import be.kuleuven.cs.distrinet.chameleon.core.document.Document;

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
		_viewLink.addListener(new AssociationListener<View>() {

			// WARNING
			
			// WE TUNNEL THE EXCEPTION THROUGH THE ASSOCIATION CLASSES
			// AND PERFORM THE ROLLBACK IN {@link Project#addSource(ProjectLoader)}
			@Override
			public void notifyElementAdded(View element) {
				try {
					notifyViewAdded(element);
				} catch (ProjectException e) {
					throw new TunnelException(e);
				}
			}

			@Override
			public void notifyElementRemoved(View element) {
				try {
					notifyProjectRemoved(element);
				} catch (ProjectException e) {
					throw new TunnelException(e);
				}
			}

			@Override
			public void notifyElementReplaced(View oldElement, View newElement) {
				try {
					notifyProjectReplaced(oldElement, newElement);
				} catch (ProjectException e) {
					throw new TunnelException(e);
				}
			}
		});
		_inputSources.addListener(new AssociationListener<InputSource>() {

			@Override
			public void notifyElementAdded(InputSource element) {
				notifyAdded(element);
			}

			@Override
			public void notifyElementRemoved(InputSource element) {
				notifyRemoved(element);
			}

			@Override
			public void notifyElementReplaced(InputSource oldElement, InputSource newElement) {
				notifyAdded(oldElement);
				notifyRemoved(newElement);
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
	protected void notifyViewAdded(View project) throws ProjectException {
	}
	
	protected void notifyProjectRemoved(View project) throws ProjectException {
	}

	protected void notifyProjectReplaced(View old, View newProject) throws ProjectException {
	}

	private SingleAssociation<DocumentLoaderImpl, View> _viewLink = new SingleAssociation<DocumentLoaderImpl, View>(this);
	
	public SingleAssociation<DocumentLoaderImpl, View> viewLink() {
		return _viewLink;
	}
	
	public View view() {
		return _viewLink.getOtherEnd();
	}
	
	public Project project() {
		return view().project();
	}

	private OrderedMultiAssociation<DocumentLoaderImpl, InputSource> _inputSources = new OrderedMultiAssociation<DocumentLoaderImpl, InputSource>(this);
	
	public void addInputSource(InputSource source) {
		// The Association object will send the event and the attached listener
		// will invoke notifyAdded(InputSource).
//		System.out.println("Adding "+source);
		if(source != null) {
			_inputSources.add(source.loaderLink());
		}
	}

	@Override
	public List<InputSource> inputSources() {
		return _inputSources.getOtherEnds();
	}
	
	public List<Document> documents() throws InputException {
		List<Document> result = new ArrayList<Document>();
		for(InputSource source: inputSources()) {
			result.add(source.load());
		}
		return result;
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
		for(InputSourceListener listener: inputSourceListeners()) {
			listener.notifyInputSourceAdded(source);
		}
	}
	
	private void notifyRemoved(InputSource source) {
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
	
	@Override
	public void flushCache() {
		for(InputSource source:inputSources()) {
			source.flushCache();
		}
	}
	

}
