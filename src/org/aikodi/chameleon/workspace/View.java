package org.aikodi.chameleon.workspace;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Stream;

import org.aikodi.chameleon.core.document.Document;
import org.aikodi.chameleon.core.element.Element;
import org.aikodi.chameleon.core.language.Language;
import org.aikodi.chameleon.core.language.ListMapWrapper;
import org.aikodi.chameleon.core.language.WrongLanguageException;
import org.aikodi.chameleon.core.namespace.Namespace;
import org.aikodi.chameleon.core.namespace.RootNamespace;
import org.aikodi.chameleon.exception.ChameleonProgrammerException;
import org.aikodi.chameleon.plugin.PluginContainer;
import org.aikodi.chameleon.plugin.PluginContainerImpl;
import org.aikodi.chameleon.plugin.ProcessorContainer;
import org.aikodi.chameleon.plugin.ViewPlugin;
import org.aikodi.chameleon.plugin.ViewProcessor;
import org.aikodi.chameleon.util.Handler;
import org.aikodi.chameleon.workspace.DocumentScannerImpl.TunnelException;
import org.aikodi.rejuse.action.UniversalConsumer;
import org.aikodi.rejuse.association.Association;
import org.aikodi.rejuse.association.AssociationListener;
import org.aikodi.rejuse.association.OrderedMultiAssociation;
import org.aikodi.rejuse.association.SingleAssociation;
import org.aikodi.rejuse.predicate.Predicate;
import org.aikodi.rejuse.predicate.TypePredicate;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableList.Builder;

/**
 * A view is a part of a {@link Project} whose documents are written in a 
 * single language. A project can have multiple views.
 * 
 * A view organizes its declaration in a {@link Namespace} and keeps
 * a reference to the default (root) namespace.
 * 
 * To populate the model, {@link DocumentScanner}s are attached to a view.
 * A document scanner ensures that all document for a particular resource,
 * such as an archive file, directory, or database, are loaded into the view.
 * 
 * Document scanners are divided into two collections:
 * <ul>
 *   <li>{@link #sourceScanners()} load documents that are part of the
 *   project and are supposed to be modified.</li>
 *   <li>{@link #binaryScanners()} load documents that are used by
 *   the project, but not modified.</li>
 * </ul>
 * 
 * To connect a view to a project, invoke {@link Project#addView(View)} on
 * the project. It will main the bidirectional association between both.
 * 
 * <h3>Caching</h3>
 * After modification of a source document, you must invoke
 * {@link #flushSourceCache()} to ensure that there are no stale caches
 * in the model. Caching is used to dramatically speed up the lookup
 * procedure, but changing the model will of course invalidate the caches.
 * Currently no mechanism is in place to automatically flush the cache
 * or to compute exactly which caches need to be flushed.
 * 
 * @author Marko van Dooren
 */
public class View extends PluginContainerImpl<ViewPlugin> 
       implements PluginContainer<ViewPlugin>, 
                  ProcessorContainer<ViewProcessor>, 
                  DocumentScannerContainer {
   
   private Language _language;

   /**
    * Create a name view with the given root namespace and language.
    * 
    * @param defaultNamespace The default namespace for the new view.
    * @param language The language in which the documents of the new view
    *                 will be described.
    */
  /*@
    @ public behavior
    @
    @ pre defaultNamespace != null;
    @ pre language != null;
    @
    @ post namespace() == defaultNamespace;
    @ post language() == language;
    @*/
	public View(RootNamespace defaultNamespace, Language language) {
		setNamespace(defaultNamespace);
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
	
	/**
	 * @return the association end that connects the view to its project.
	 */
  /*@
    @ public behavior
    @
    @ post \result != null;
    @*/
	public SingleAssociation<View, Project> projectLink() {
		return _projectLink;
	}

	/**
	 * @return the project to which the view is connected.
	 */
	public Project project() {
		return _projectLink.getOtherEnd();
	}

	/**
	 * Set the language of this view.
	 * 
	 * This method is private because I see no use for changing the
	 * language of a view.
	 * 
	 * @param language The new language of the view.
	 */
   private void setLanguage(Language language) {
      if (language != null) {
         _language = language;
      } else {
         throw new IllegalArgumentException();
      }
   }

   /**
    * @return The language in which the documents of this view are described.
    */
  /*@
    @ public behavior
    @
    @ post \result != null;
    @*/
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
	    
	/**
	 * @return The default namespace of this view.
	 */
	public RootNamespace namespace() {
		return _namespace.getOtherEnd();
	}
	
	/**
	 * Set the default namespace of this view. The bidirectional association
	 * between the namespace and the view is kept consistent.
	 * 
	 * This method is protected because there does not seem to be a good
	 * reason to change the default namespace.
	 * 
	 * @param namespace The new default namespace for this view.
	 */
	protected void setNamespace(RootNamespace namespace) {
		if(namespace != null) {
			_namespace.connectTo(namespace.projectLink());
		} else {
			_namespace.connectTo(null);
		}
	}
	
   /**
    * @return the association end that connects this view to its default
    *         namespace.
    */
   public SingleAssociation<View, RootNamespace> namespaceLink() {
      return _namespace;
   }

	private SingleAssociation<View,RootNamespace> _namespace = new SingleAssociation<View,RootNamespace>(this);
	
	/**
	 * Add the given document to this view as a binary scanner.
	 * 
	 * @param scanner The document scanner to be added as a binary scanner.
	 */
  /*@
    @ public behavior
    @
    @ pre input != null;
    @
    @ post binaryScanners().get(binaryScanners().size()-1) == scanner;
    @ post binaryScanners().subList(0,binaryScanners().size()-1).equals(\old(binaryScanners()));
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
	 * as a binary scanner.
	 * 
	 * @param scanner The scanner of which must be determined whether it can be added.
	 * @return True if there is no binary scanner that already loads the same 
	 *         documents, false otherwise.
	 */
  /*@
    @ public behavior
    @
    @ post binaryScanners().stream().allMatch(s -> !s.scansSameAs(scanner));
    @*/
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
  /*@
    @ public behavior
    @
    @ pre input != null;
    @
    @ post \old(binaryScanners().subList(0,binaryScanners().size()-1)).equals(binaryScanners());
    @*/
	public void removeBinary(DocumentScanner scanner) {
		_binaryScanners.remove(scanner.containerLink());
	}
	
	/**
	 * @return the list of binary scanners.
	 */
  /*@
    @ public behavior
    @
    @ post result != null;
    @*/
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
	
	/**
	 * Remove the given view listener. The listener will no longer be notified
	 * by this view.
	 * 
	 * @param listener The view listener to be removed.
	 */
	public void removeListener(ViewListener listener) {
		_listeners.remove(listener);
	}
	
   /**
    * Add the given document to this view as a source scanner.
    * 
    * @param scanner The document scanner to be added as a source scanner.
    */
  /*@
    @ public behavior
    @
    @ pre input != null;
    @
    @ post sourceScanners().get(sourceScanners().size()-1) == scanner;
    @ post sourceScanners().subList(0,sourceScanners().size()-1).equals(\old(sourceScanners()));
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
    * as a source scanner.
    * 
    * @param scanner The scanner of which must be determined whether it can be added.
    * @return True if there is no source scanner that already loads the same 
    *         documents, false otherwise.
    */
  /*@
    @ public behavior
    @
    @ post binaryScanners().stream().allMatch(s -> !s.scansSameAs(scanner));
    @*/
	public boolean canAddSource(DocumentScanner scanner) {
		for(DocumentScanner l: sourceScanners()) {
			if(l.scansSameAs(scanner)) {
				return false;
			}
		}
		return true;
	}
	
   /**
    * Remove the given source scanner.
    * 
    * @param scanner The source scanner to be removed.
    */
  /*@
    @ public behavior
    @
    @ pre input != null;
    @
    @ post \old(sourceScanners().subList(0,sourceScanners().size()-1)).equals(sourceScanners());
    @*/
	public void removeSource(DocumentScanner scanner) {
		_sourceScanners.remove(scanner.containerLink());
	}
	
   /**
    * @return the list of source scanners.
    */
  /*@
    @ public behavior
    @
    @ post result != null;
    @*/
	public List<DocumentScanner> sourceScanners() {
		return _sourceScanners.getOtherEnds();
	}
	
	/**
	 * Return all document scanners (both source and binary) of a given type.
	 * 
	 * @param type The type of the requested document scanners.
	 * @return All source and binary scanners of the given type.
	 */
  /*@
    @ public behavior
    @
    @ pre type != null;
    @
    @ post \result != null;
    @ post \result.stream().allMatch(s -> type.isInstance(s));
    @*/
	public <T extends DocumentScanner> List<T> scanners(Class<T> type) {
		Builder<T> builder = ImmutableList.<T>builder();
		builder.addAll((List<T>) new TypePredicate<>(type).filteredList(binaryScanners()));
		builder.addAll((List<T>) new TypePredicate<>(type).filteredList(sourceScanners()));
		return builder.build();
	}

   /**
    * Return all binary scanners of a given type.
    * 
    * @param type The type of the requested document scanners.
    * @return All binary scanners of the given type.
    */
  /*@
    @ public behavior
    @
    @ pre type != null;
    @
    @ post \result != null;
    @ post \result.stream().allMatch(s1 -> 
    @   \result.contains(s1) == 
    @   binaryScanners().stream().filter(s -> type.isInstance(s)).contains(s1)
    @ );
    @*/
	public <T extends DocumentScanner> List<T> binaryScanners(Class<T> kind) {
		return (List<T>) new TypePredicate<>(kind).filteredList(binaryScanners());
	}

   /**
    * Return all source scanners of a given type.
    * 
    * @param type The type of the requested document scanners.
    * @return All source scanners of the given type.
    */
  /*@
    @ public behavior
    @
    @ pre type != null;
    @
    @ post \result != null;
    @ post \result.stream().allMatch(s1 -> 
    @   \result.contains(s1) == 
    @   sourceScanners().stream().filter(s -> type.isInstance(s)).contains(s1)
    @ );
    @*/
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
		Document doc = element.lexical().nearestAncestorOrSelf(Document.class);
		// Namespace are not in documents and thus cannot be source elements.
		if(doc != null) {
			DocumentScanner scanner = doc.loader().scanner().rootScanner();
			return scanner.containerLink().getOtherRelation() == _sourceScanners;
		}
		return false;
	}
	
   /**
    * Check whether the given element is a binary element.
    * 
    * @param element The element for which must be checked whether it is
    *                a source element.
    * @return True if and only if the document of the element is a source
    *         document.
    */
	public boolean isBinary(Element element) {
		Document doc = element.lexical().nearestAncestorOrSelf(Document.class);
		// Namespace are not in documents and thus cannot be source elements.
		if(doc != null) {
			DocumentScanner scanner = doc.loader().scanner().rootScanner();
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
      if (list != null && list.contains(processor)) {
         processor.setContainer(null, connectorInterface);
         list.remove(processor);
      }
   }

   @Override
   public <K extends ViewProcessor, V extends K> void addProcessor(Class<K> keyInterface, V processor) {
      _processors.add(keyInterface, processor);
      if (processor.container() != this) {
         processor.setContainer(this, keyInterface);
      }
   }

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

	/**
	 * Return all source elements of this view. Any input exceptions are handled by the given
	 * handler. The handler makes it possible to choose between failing and resuming
	 * (or any other strategy) without creating multiple methods.
	 * 
	 * @param type A class object representing the type of the requested elements.
	 * @param handler A handler to deal with potential input exceptions.
	 * @return
	 * @throws InputException An input exception was thrown while loading a document
	 *                        and the exception was not handled by the given
	 *                        handler.
	 */
	public <T extends Element> List<T> sourceElements(Class<T> type, Handler handler) throws InputException {
		List<T> result = new ArrayList<T>();
		for(DocumentScanner scanner: sourceScanners()) {
			try {
			for(Document doc: scanner.documents()) {
				result.addAll(doc.lexical().descendants(type));
				if(type.isInstance(doc)) {
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
	
	/**
	 * Apply the given action to all source elements.
	 * 
	 * @param action The action to apply
	 * @throws E The action has thrown an exception of type E.
	 * @throws InputException An exception was thrown while attempting to
	 *                        load a document.
	 */
	public <E extends Exception> void applyToSource(UniversalConsumer<? extends Element, E> action) throws E, InputException {
		for(DocumentScanner scanner:sourceScanners()) {
			scanner.apply(action);
		}
	}

    /**
     * Apply the given action to all source loaders.
     * 
     * @param action The action to apply
     * @throws E The action has thrown an exception of type E.
     * @throws InputException An exception was thrown while attempting to
     *                        load a document.
     */
    public <E extends Exception> void applyToSourceLoaders(UniversalConsumer<DocumentLoader, E> action) throws E {
        for(DocumentScanner scanner:sourceScanners()) {
            scanner.applyToLoaders(action);
        }
    }

	public <T extends Element,E extends Exception> List<T> findInSource(Class<T> kind, Predicate<T, E> predicate) throws E, InputException {
	  List<T> result = new ArrayList<>();
	  for(DocumentScanner scanner:sourceScanners()) {
	    List<Document> stream = scanner.documents();
        for(Document document: stream) {
          result.addAll(document.lexical().descendants(kind, predicate));
        }
	  }
	  return result;
	}
	
	/**
	 * {@inheritDoc}
	 * 
	 * The view of a view is the view itself.
	 * 
	 * @return this
	 */
  /*@
    @ post \result == this.
    @*/
	@Override
   public View view() {
		return this;
	}
}
