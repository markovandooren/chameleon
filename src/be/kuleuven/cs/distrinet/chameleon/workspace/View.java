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
                  DocumentScannerContainer {
	
	public View(RootNamespace namespace, Language language) {
		setNamespace(namespace);
		setLanguage(language);
		_binaryScanners.addListener(new AssociationListener<DocumentScanner>() {

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
		_sourceScanners.addListener(new AssociationListener<DocumentScanner>() {

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
	
	private void notifyBinaryAdded(DocumentScanner scanner) {
		for(ViewListener listener: _listeners) {
			listener.binaryScannerAdded(scanner);
		}
	}

	private void notifyBinaryRemoved(DocumentScanner scanner) {
		for(ViewListener listener: _listeners) {
			listener.binaryScannerRemoved(scanner);
		}
	}

	private void notifySourceAdded(DocumentScanner scanner) {
		for(ViewListener listener: _listeners) {
			listener.sourceScannerAdded(scanner);
		}
	}

	private void notifySourceRemoved(DocumentScanner scanner) {
		for(ViewListener listener: _listeners) {
			listener.sourceScannerRemoved(scanner);
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
	 * Add the given source scanner to this project.
	 */
	/*@
   @ public behavior
   @
   @ pre input != null;
   @
   @ post ! inputSources().contains(input);
   @*/
	public void addBinary(DocumentScanner scanner) throws ProjectException {
		if(scanner != null) {
			if(! canAddBinary(scanner)) {
				throw new ProjectException("There is already a binary scanner present to load these resources.");
			}
			Association<? extends DocumentScanner, ? super View> projectLink = scanner.containerLink();
			try {
				_binaryScanners.add(projectLink);
			} catch(TunnelException exc) {
				// Rollback
				_binaryScanners.remove(projectLink);
				throw (ProjectException)exc.getCause();
			}
		}
	}
	
	/**
	 * Check whether the given document scanner can be added
	 * as a binary scanner. If there is already an equal
	 * scanner present, false is returned.
	 * 
	 * @param scanner The scanner of which must be determined whether it can be added.
	 * @return
	 */
	public boolean canAddBinary(DocumentScanner scanner) {
		for(DocumentScanner l: binaryScanners()) {
			if(l.scansSameAs(scanner)) {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * Remove the given binary scanner.
	 * 
	 * @param scanner The binary scanner to be removed.
	 */
	public void removeBinary(DocumentScanner scanner) {
		_binaryScanners.remove(scanner.containerLink());
	}
	
	/**
	 * @return the list of binary scanners.
	 */
	public List<DocumentScanner> binaryScanners() {
		return _binaryScanners.getOtherEnds();
	}

	private OrderedMultiAssociation<View, DocumentScanner> _binaryScanners = new OrderedMultiAssociation<View, DocumentScanner>(this);
	{
		_binaryScanners.enableCache();
	}

	private OrderedMultiAssociation<View, DocumentScanner> _sourceScanners = new OrderedMultiAssociation<View, DocumentScanner>(this);
	{
		_sourceScanners.enableCache();
	}

	private List<ViewListener> _listeners = new ArrayList<ViewListener>();
	
	/**
	 * Add the given listener as a view listener. The listener
	 * is notified when document scanners are added and removed.
	 * 
	 * @param listener The listener to be added.
	 */
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
	 * Add the given document scanner to this project.
	 */
	/*@
   @ public behavior
   @
   @ pre input != null;
   @
   @ post ! inputSources().contains(input);
   @*/
	public void addSource(DocumentScanner scanner) throws ProjectException {
		if(scanner != null) {
			if(! canAddSource(scanner)) {
				throw new ProjectException("There is already a source scanner present to load these resources.");
			}
			Association<? extends DocumentScanner, ? super View> projectLink = scanner.containerLink();
			try {
				_sourceScanners.add(projectLink);
			} catch(TunnelException exc) {
				// Rollback
				_sourceScanners.remove(projectLink);
				throw (ProjectException)exc.getCause();
			}
		}
	}
	
	/**
	 * Check whether the given document scanner can be added
	 * as a source scanner. If there is already an equal
	 * scanner present, false is returned.
	 * 
	 * @param scanner The scanner of which must be determined whether it can be added.
	 * @return
	 */
	public boolean canAddSource(DocumentScanner scanner) {
		for(DocumentScanner l: sourceScanners()) {
			if(l.scansSameAs(scanner)) {
				return false;
			}
		}
		return true;
	}
	
	public void removeSource(DocumentScanner scanner) {
		_sourceScanners.remove(scanner.containerLink());
	}
	
	public List<DocumentScanner> sourceScanners() {
		return _sourceScanners.getOtherEnds();
	}
	
	public <T extends DocumentScanner> List<T> scanners(Class<T> kind) {
		Builder<T> builder = ImmutableList.<T>builder();
		builder.addAll((List<T>) new TypePredicate<>(kind).filteredList(binaryScanners()));
		builder.addAll((List<T>) new TypePredicate<>(kind).filteredList(sourceScanners()));
		return builder.build();
	}

	public <T extends DocumentScanner> List<T> binaryScanners(Class<T> kind) {
		return (List<T>) new TypePredicate<>(kind).filteredList(binaryScanners());
	}


	public <T extends DocumentScanner> List<T> sourceScanners(Class<T> kind) {
		return (List<T>) new TypePredicate<>(kind).filteredList(sourceScanners());
	}

	/**
	 * Flush the cache of all source scanners.
	 */
	public void flushSourceCache() {
		for(DocumentScanner scanner: sourceScanners()) {
			scanner.flushCache();
		}
	}
	
	/**
	 * Return the source document in this view.
	 * 
	 * @return The source documents in this view.
	 * @throws InputException An exception was thrown while loading a document.
	 */
	public List<Document> sourceDocuments() throws InputException {
		List<Document> result = new ArrayList<Document>();
		for(DocumentScanner scanner: sourceScanners()) {
			result.addAll(scanner.documents());
		}
		return result;
	}
	
	/**
	 * Check whether the given element is a source element.
	 * 
	 * @param element The element for which must be checked whether it is
	 *                a source element.
	 * @return True if and only if the document of the element is a source
	 *         document.
	 */
	public boolean isSource(Element element) {
		Document doc = element.nearestAncestorOrSelf(Document.class);
		// Namespace are not in documents and thus cannot be source elements.
		if(doc != null) {
			DocumentScanner scanner = doc.inputSource().scanner().rootScanner();
			return scanner.containerLink().getOtherRelation() == _sourceScanners;
		}
		return false;
	}
	
	public boolean isBinary(Element element) {
		Document doc = element.nearestAncestorOrSelf(Document.class);
		// Namespace are not in documents and thus cannot be source elements.
		if(doc != null) {
			DocumentScanner scanner = doc.inputSource().scanner().rootScanner();
			return scanner.containerLink().getOtherRelation() == _binaryScanners;
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
		for(DocumentScanner scanner: sourceScanners()) {
			try {
			for(Document doc: scanner.documents()) {
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
		for(DocumentScanner scanner:sourceScanners()) {
			scanner.apply(action);
		}
	}

	@Override
   public View view() {
		return this;
	}
}
