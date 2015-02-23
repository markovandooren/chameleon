package be.kuleuven.cs.distrinet.chameleon.workspace;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import be.kuleuven.cs.distrinet.chameleon.core.document.Document;
import be.kuleuven.cs.distrinet.chameleon.core.element.Element;
import be.kuleuven.cs.distrinet.chameleon.core.language.Language;
import be.kuleuven.cs.distrinet.chameleon.core.language.ListMapWrapper;
import be.kuleuven.cs.distrinet.chameleon.core.language.WrongLanguageException;
import be.kuleuven.cs.distrinet.chameleon.core.namespace.RootNamespace;
import be.kuleuven.cs.distrinet.chameleon.exception.ChameleonProgrammerException;
import be.kuleuven.cs.distrinet.chameleon.plugin.PluginContainer;
import be.kuleuven.cs.distrinet.chameleon.plugin.PluginContainerImpl;
import be.kuleuven.cs.distrinet.chameleon.plugin.ProcessorContainer;
import be.kuleuven.cs.distrinet.chameleon.plugin.ViewPlugin;
import be.kuleuven.cs.distrinet.chameleon.plugin.ViewProcessor;
import be.kuleuven.cs.distrinet.chameleon.util.Handler;
import be.kuleuven.cs.distrinet.chameleon.workspace.DocumentScannerImpl.TunnelException;
import be.kuleuven.cs.distrinet.rejuse.action.Action;
import be.kuleuven.cs.distrinet.rejuse.association.Association;
import be.kuleuven.cs.distrinet.rejuse.association.AssociationListener;
import be.kuleuven.cs.distrinet.rejuse.association.OrderedMultiAssociation;
import be.kuleuven.cs.distrinet.rejuse.association.SingleAssociation;
import be.kuleuven.cs.distrinet.rejuse.predicate.TypePredicate;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableList.Builder;

public class View extends PluginContainerImpl<ViewPlugin> 
       implements PluginContainer<ViewPlugin>, 
                  ProcessorContainer<ViewProcessor>, 
                  DocumentLoaderContainer {
	
	public View(RootNamespace namespace, Language language) {
		setNamespace(namespace);
		setLanguage(language);
		_binaryLoaders.addListener(new AssociationListener<DocumentScanner>() {

			@Override
			public void notifyElementAdded(DocumentScanner element) {
				notifyBinaryAdded(element);
			}

			@Override
			public void notifyElementRemoved(DocumentScanner element) {
				notifyBinaryRemoved(element);
			}

			@Override
			public void notifyElementReplaced(DocumentScanner oldElement, DocumentScanner newElement) {
				notifyElementRemoved(oldElement);
				notifyElementAdded(newElement);
			}
		});
		_sourceLoaders.addListener(new AssociationListener<DocumentScanner>() {

			@Override
			public void notifyElementAdded(DocumentScanner element) {
				notifySourceAdded(element);
			}

			@Override
			public void notifyElementRemoved(DocumentScanner element) {
				notifySourceRemoved(element);
			}

			@Override
			public void notifyElementReplaced(DocumentScanner oldElement, DocumentScanner newElement) {
				notifyElementRemoved(oldElement);
				notifyElementAdded(newElement);
			}
		});
	}
	
	private void notifyBinaryAdded(DocumentScanner loader) {
		for(ViewListener listener: _listeners) {
			listener.binaryLoaderAdded(loader);
		}
	}

	private void notifyBinaryRemoved(DocumentScanner loader) {
		for(ViewListener listener: _listeners) {
			listener.binaryLoaderRemoved(loader);
		}
	}

	private void notifySourceAdded(DocumentScanner loader) {
		for(ViewListener listener: _listeners) {
			listener.sourceLoaderAdded(loader);
		}
	}

	private void notifySourceRemoved(DocumentScanner loader) {
		for(ViewListener listener: _listeners) {
			listener.sourceLoaderRemoved(loader);
		}
	}

	private SingleAssociation<View, Project> _projectLink = new SingleAssociation<View, Project>(this);
	
	public SingleAssociation<View, Project> projectLink() {
		return _projectLink;
	}

	public Project project() {
		return _projectLink.getOtherEnd();
	}

  public void setLanguage(Language language) {
  	if(language != null) {
  		_language = language;
  	} else {
  		throw new IllegalArgumentException();
  	}
  }

  public Language language() {
    return _language;
  }
  
	public <T extends Language> T language(Class<T> kind) {
		if(kind == null) {
			throw new ChameleonProgrammerException("The given language class is null.");
		}
		Language language = language();
		if(kind.isInstance(language) || language == null) {
			return (T) language;
		} else {
			throw new WrongLanguageException("The language of this model is of the wrong kind. Expected: "+kind.getName()+" but got: " +language.getClass().getName());
		}
	}
	    
  private Language _language;

	public RootNamespace namespace() {
		return _namespace.getOtherEnd();
	}
	
	public void setNamespace(RootNamespace namespace) {
		if(namespace != null) {
			_namespace.connectTo(namespace.projectLink());
		} else {
			_namespace.connectTo(null);
		}
	}
	
  public SingleAssociation<View,RootNamespace> namespaceLink() {
  	return _namespace;
  }
	
	private SingleAssociation<View,RootNamespace> _namespace = new SingleAssociation<View,RootNamespace>(this);
	
	/**
	 * Add the given source loader to this project.
	 */
	/*@
   @ public behavior
   @
   @ pre input != null;
   @
   @ post ! inputSources().contains(input);
   @*/
	public void addBinary(DocumentScanner loader) throws ProjectException {
		if(loader != null) {
			if(! canAddBinary(loader)) {
				throw new ProjectException("There is already a binary loader present to load these resources.");
			}
			Association<? extends DocumentScanner, ? super View> projectLink = loader.containerLink();
			try {
				_binaryLoaders.add(projectLink);
			} catch(TunnelException exc) {
				// Rollback
				_binaryLoaders.remove(projectLink);
				throw (ProjectException)exc.getCause();
			}
		}
	}
	
//	public OrderedMultiAssociation<View, DocumentLoader> sourceLink() {
//		return _sourceLoaders;
//	}
//
//	public OrderedMultiAssociation<View, DocumentLoader> binaryLink() {
//		return _sourceLoaders;
//	}
//
	/**
	 * Check whether the given document loader can be added
	 * as a binary loader. If there is already an equal
	 * loader present, false is returned.
	 * 
	 * @param loader The loader of which must be determined whether it can be added.
	 * @return
	 */
	public boolean canAddBinary(DocumentScanner loader) {
		for(DocumentScanner l: binaryLoaders()) {
			if(l.loadsSameAs(loader)) {
				return false;
			}
		}
		return true;
//		return ! _binaryLoaders.containsObject(loader);
	}
	
	public void removeBinary(DocumentScanner loader) {
		_binaryLoaders.remove(loader.containerLink());
	}
	
	public List<DocumentScanner> binaryLoaders() {
		return _binaryLoaders.getOtherEnds();
	}

	private OrderedMultiAssociation<View, DocumentScanner> _binaryLoaders = new OrderedMultiAssociation<View, DocumentScanner>(this);
	{
		_binaryLoaders.enableCache();
	}

	private OrderedMultiAssociation<View, DocumentScanner> _sourceLoaders = new OrderedMultiAssociation<View, DocumentScanner>(this);
	{
		_sourceLoaders.enableCache();
	}

	private List<ViewListener> _listeners = new ArrayList<ViewListener>();
	
	public void addListener(ViewListener listener) {
		if(listener == null) {
			throw new IllegalArgumentException();
		}
		_listeners.add(listener);
	}
	
	public void removeListener(ViewListener listener) {
		_listeners.remove(listener);
	}
	
	/**
	 * Add the given source loader to this project.
	 */
	/*@
   @ public behavior
   @
   @ pre input != null;
   @
   @ post ! inputSources().contains(input);
   @*/
	public void addSource(DocumentScanner loader) throws ProjectException {
		if(loader != null) {
			if(! canAddSource(loader)) {
				throw new ProjectException("There is already a source loader present to load these resources.");
			}
			Association<? extends DocumentScanner, ? super View> projectLink = loader.containerLink();
			try {
				_sourceLoaders.add(projectLink);
			} catch(TunnelException exc) {
				// Rollback
				_sourceLoaders.remove(projectLink);
				throw (ProjectException)exc.getCause();
			}
		}
	}
	
	/**
	 * Check whether the given document loader can be added
	 * as a source loader. If there is already an equal
	 * loader present, false is returned.
	 * 
	 * @param loader The loader of which must be determined whether it can be added.
	 * @return
	 */
	public boolean canAddSource(DocumentScanner loader) {
		for(DocumentScanner l: sourceLoaders()) {
			if(l.loadsSameAs(loader)) {
				return false;
			}
		}
		return true;
//		return ! _sourceLoaders.containsObject(loader);
	}
	
	public void removeSource(DocumentScanner loader) {
		_sourceLoaders.remove(loader.containerLink());
	}
	
	public List<DocumentScanner> sourceLoaders() {
		return _sourceLoaders.getOtherEnds();
	}
	
	public <T extends DocumentScanner> List<T> loaders(Class<T> kind) {
		Builder<T> builder = ImmutableList.<T>builder();
		builder.addAll((List<T>) new TypePredicate<>(kind).filteredList(binaryLoaders()));
		builder.addAll((List<T>) new TypePredicate<>(kind).filteredList(sourceLoaders()));
		return builder.build();
	}

	public <T extends DocumentScanner> List<T> binaryLoaders(Class<T> kind) {
		return (List<T>) new TypePredicate<>(kind).filteredList(binaryLoaders());
	}


	public <T extends DocumentScanner> List<T> sourceLoaders(Class<T> kind) {
		return (List<T>) new TypePredicate<>(kind).filteredList(sourceLoaders());
	}

	public void flushSourceCache() {
		for(DocumentScanner loader: sourceLoaders()) {
			loader.flushCache();
		}
	}
	
	public List<Document> sourceDocuments() throws InputException {
		List<Document> result = new ArrayList<Document>();
		for(DocumentScanner loader: sourceLoaders()) {
			result.addAll(loader.documents());
		}
		return result;
	}
	
	public boolean isSource(Element element) {
		Document doc = element.nearestAncestorOrSelf(Document.class);
		// Namespace are not in documents and thus cannot be source elements.
		if(doc != null) {
			DocumentScanner loader = doc.inputSource().loader().rootScanner();
			return loader.containerLink().getOtherRelation() == _sourceLoaders;
		}
		return false;
	}
	
	public boolean isBinary(Element element) {
		Document doc = element.nearestAncestorOrSelf(Document.class);
		// Namespace are not in documents and thus cannot be source elements.
		if(doc != null) {
			DocumentScanner loader = doc.inputSource().loader().rootScanner();
			return loader.containerLink().getOtherRelation() == _binaryLoaders;
		}
		return false;
	}

	
	
  private ListMapWrapper<ViewProcessor> _processors = new ListMapWrapper<ViewProcessor>();

  @Override
public <T extends ViewProcessor> List<T> processors(Class<T> connectorInterface) {
    return _processors.get(connectorInterface);
  }

  @Override
  public <T extends ViewProcessor> void removeProcessor(Class<T> connectorInterface, T processor) {
      List<T> list = _processors.get(connectorInterface);
      if (list!=null && list.contains(processor)) {
          processor.setContainer(null, connectorInterface);
          list.remove(processor);
      }
  }

  @Override
  public <K extends ViewProcessor, V extends K> void addProcessor(Class<K> keyInterface, V processor) {
    _processors.add(keyInterface, processor);
    if(processor.container() != this) {
    	processor.setContainer(this, keyInterface);
    }
  }

  /**
   * Copy the processor mapping from the given language to this language.
   */
 /*@
   @ public behavior
   @
   @ post (\forall Class<? extends Processor> cls; from.processorMap().containsKey(cls);
   @         processors(cls).containsAll(from.processorMap().valueSet());
   @*/
  @Override
	public void cloneProcessorsFrom(ProcessorContainer<ViewProcessor> from) {
		for(Entry<Class<? extends ViewProcessor>, List<? extends ViewProcessor>> entry: from.processorMap().entrySet()) {
			Class<ViewProcessor> key = (Class<ViewProcessor>) entry.getKey();
			List<ViewProcessor> value = (List<ViewProcessor>) entry.getValue();
			for(ViewProcessor processor: value) {
			  _processors.add(key, (ViewProcessor)processor.clone());
			}
		}
	}
	
	/**
	 * Return the mapping of classes/interfaces to the processors of that kind.
	 */
 /*@
   @ public behavior
   @
   @ post \result != null;
   @*/
	@Override
   public Map<Class<? extends ViewProcessor>, List<? extends ViewProcessor>> processorMap() {
		return _processors.map();
	}

	public <T extends Element> List<T> sourceElements(Class<T> kind, Handler handler) throws InputException {
		List<T> result = new ArrayList<T>();
		for(DocumentScanner loader: sourceLoaders()) {
			try {
			for(Document doc: loader.documents()) {
				result.addAll(doc.descendants(kind));
				if(kind.isInstance(doc)) {
					result.add((T)doc);
				}
			}
			} catch(InputException exc) {
				if(handler != null) {
					handler.handle(exc);
				}
			}
		}
		return result;
	}
	
	public <E extends Exception> void applyToSource(Action<? extends Element, E> action) throws E, InputException {
		for(DocumentScanner loader:sourceLoaders()) {
			loader.apply(action);
		}
	}

	@Override
   public View view() {
		return this;
	}
}
