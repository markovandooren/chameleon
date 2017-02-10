package org.aikodi.chameleon.core.namespace;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import java.lang.ref.SoftReference;

import org.aikodi.chameleon.core.declaration.Declaration;
import org.aikodi.chameleon.core.element.Element;
import org.aikodi.chameleon.core.lookup.LookupException;
import org.aikodi.chameleon.core.namespacedeclaration.NamespaceDeclaration;
import org.aikodi.chameleon.exception.ChameleonProgrammerException;
import org.aikodi.chameleon.util.Lists;
import org.aikodi.chameleon.workspace.DocumentLoader;
import org.aikodi.chameleon.workspace.InputException;
import org.aikodi.contract.Contracts;
import org.aikodi.rejuse.association.OrderedMultiAssociation;

import com.google.common.collect.ImmutableList;

public class LazyNamespace extends RegularNamespace implements DocumentLoaderNamespace {

	protected LazyNamespace(String name) {
		super(name);
	}

	@Override
	protected RegularNamespace cloneSelf() {
		return new LazyNamespace(name());
	}

	@Override
	public synchronized List<NamespaceDeclaration> namespaceDeclarations() {
		for(DocumentLoader loader: documentLoaders()) {
			try {
				loader.load();
			} catch (InputException e) {
				throw new ChameleonProgrammerException(e);
			}
		}
		return loadedNamespaceDeclarations();
	}

	@Override
	public Namespace createSubNamespace(String name) {
		LazyNamespace result = new LazyNamespace(name);
		addNamespace(result);
		return result;
	}

	/*******************
	 * NAMESPACE PARTS *
	 *******************/

//	private Multi<NamespaceDeclaration> _namespaceDeclarations = new Multi<NamespaceDeclaration>(this,"namespace parts") {
		private List<SoftReference<NamespaceDeclaration>> _namespaceDeclarations = new ArrayList<>();

	@Override
   public synchronized void addNamespacePart(NamespaceDeclaration namespacePart){
		if(namespacePart == null) {
			throw new IllegalArgumentException();
		}
		_namespaceDeclarations.add(new SoftReference<>(namespacePart));
		namespacePart.connectNamespace(this);
		addCacheForNamespaceDeclaration(namespacePart);
	}
	
	@Override
	public void disconnectNamespaceDeclaration(NamespaceDeclaration namespaceDeclaration) {
		// Horribly inefficient.
		boolean removed = false;
		for(int i=0; i < _namespaceDeclarations.size() && ! removed; i++) {
			if(_namespaceDeclarations.get(i).get().equals(namespaceDeclaration)) {
				_namespaceDeclarations.remove(i);
				removed = true;
			}
		}
		namespaceDeclaration.declarations().forEach(d -> removeDeclaration(d));
	}

	@Override
	public List<NamespaceDeclaration> loadedNamespaceDeclarations() {
		List<NamespaceDeclaration> result = new ArrayList<>();
		for(SoftReference<NamespaceDeclaration> sr: _namespaceDeclarations) {
			result.add(sr.get());
		}
		return result;
	}
	
	
	@SuppressWarnings("unchecked")
	@Override
	// Only called in synchronized
	protected synchronized List<Declaration> searchDeclarations(String name) throws LookupException {
		// First check the declaration cache.
		List<Declaration> candidates = super.searchDeclarations(name);
		// If there was no cache, the document loaders might have something
		if(candidates == null) {
			Namespace ns = getSubNamespace(name);
			if(ns != null) {
				candidates = ImmutableList.<Declaration>of(ns);
				storeCache(name, candidates);
			} else {
				candidates = searchLazyDeclarations(name);
			}
		}
		return candidates;
	}
	
	@Override
	public void addDeclaration(Declaration declaration) {
    Contracts.notNull(declaration, "The declaration cannot be null.");
    if(_lazyDeclarationCache != null) {
      String name = declaration.name();
      List<SoftReference<Declaration>> list = _lazyDeclarationCache.get(name);
      if(list != null) {
        addToList(list, declaration);
      }
    }

	}
	
	@Override
	public void removeDeclaration(Declaration declaration) {
		super.removeDeclaration(declaration);
    if(_lazyDeclarationCache != null) {
      String name = declaration.name();
      List<SoftReference<Declaration>> list = _lazyDeclarationCache.get(name);
      if(list != null) {
      	boolean working = true;
      	for(int i=0; i < list.size() && working; i++) {
      		if(list.get(i).get().equals(declaration)) {
      			list.remove(i);
      			working = true;
      		}
      	}
      }
    }
	}

  private void addToList(List<SoftReference<Declaration>> list, Declaration declaration) {
    list.add(new SoftReference<>(declaration));
    registerListener(declaration);
  }

	protected synchronized void storeLazyCache(String name, List<SoftReference<Declaration>> declarations) {
		_lazyDeclarationCache.put(name, declarations);
	}

	private Map<String,List<SoftReference<Declaration>>> _lazyDeclarationCache = new HashMap<>();

	protected List<Declaration> searchLazyDeclarations(String name) throws LookupException {
		List<SoftReference<Declaration>> list = _lazyDeclarationCache.get(name);
		List<Declaration> candidates = null;
		while(candidates == null) {
			if(list == null) {
				// loaders is sorted: the element with the highest priority is in front.
				//FIXME This is not generic enough. It does not allow
				//      a namespace to contain multiple declarations with the
				//      same name, but of different types.
				Queue<DocumentLoader> loaders = _sourceMap.get(name);
				if(loaders != null && ! loaders.isEmpty()) {
					candidates = loaders.peek().targetDeclarations(name);
					list = candidates.stream().map(d -> new SoftReference<>(d)).collect(Collectors.toList());
					storeLazyCache(name, list);
				} else {
					candidates = new ArrayList<>();
				}
			} else {
				final List<Declaration> result = new ArrayList<>();
				boolean allLoaded = true;
				int index = 0;
				while(allLoaded && index < list.size()) {
					Declaration declaration = list.get(index).get();
					if(declaration != null) {
						result.add(declaration);
					} else {
						allLoaded = false;
					}
					index++;
				}
				if(allLoaded) {
					candidates = result;
				}
			}
		}
		return candidates;
	}

	@Override
	public void addDocumentLoader(DocumentLoader loader) throws InputException {
		if(loader == null) {
			throw new IllegalArgumentException("The given document loader is null.");
		}
		_documentLoaders.add(loader.namespaceLink());
		addDocumentLoaderToCache(loader, loader.targetDeclarationNames(this));
	}

	protected void addDocumentLoaderToCache(DocumentLoader loader, List<String> targetDeclarationNames) {
		for(String name: targetDeclarationNames) {
			if(name == null) {
				throw new ChameleonProgrammerException("A document loader uses null as a declaration name.");
			}
			Queue<DocumentLoader> queue = _sourceMap.get(name);
			if(queue == null) {
				queue = new PriorityQueue<DocumentLoader>();
				_sourceMap.put(name, queue);
			}
			queue.add(loader);
		}
	}

	@Override
	public synchronized void flushLocalCache() {
		super.flushLocalCache();
		for(DocumentLoader loader: documentLoaders()) {
			updateDocumentLoader(loader);
		}
	}

	/**
	 * First, all document loaders are loaded. This attaches all namespace declarations
	 * to their corresponding namespace. After that, a super call is performed to
	 * 
	 */
	@Override
	public List<Declaration> declarations() throws LookupException {
		for(DocumentLoader source: documentLoaders()) {
			try {
				source.load();
			} catch (InputException e) {
				throw new LookupException("An input exception occurred while loading a document loader.",e);
			}
		}
		return super.declarations();
	}

	@Override
	public List<? extends Element> children() {
		for(Queue<DocumentLoader> q: _sourceMap.values()) {
			try {
				q.peek().load();
			} catch (InputException e) {
				throw new LoadException("File open error",e);
			}
		}
		return super.children();
	}

	@Override
	public List<DocumentLoader> documentLoaders() {
		return _documentLoaders.getOtherEnds();
	}

	private Map<String, Queue<DocumentLoader>> _sourceMap = new HashMap<String, Queue<DocumentLoader>>();

	private OrderedMultiAssociation<LazyNamespace,DocumentLoader> _documentLoaders = new OrderedMultiAssociation<LazyNamespace, DocumentLoader>(this) {
		@Override
		protected void fireElementRemoved(DocumentLoader removedElement) {
			removeDocumentLoaderFromCache(removedElement);
		}

	};

	protected void removeDocumentLoaderFromCache(DocumentLoader removedElement) {
		List<String> obsoleteKeys = Lists.create();
		for(Map.Entry<String, Queue<DocumentLoader>> entry: _sourceMap.entrySet()) {
			Queue<DocumentLoader> value = entry.getValue();
			value.remove(removedElement);
			if(value.isEmpty()) {
				obsoleteKeys.add(entry.getKey());
			}
		}
		for(String obsoleteKey: obsoleteKeys) {
			_sourceMap.remove(obsoleteKey);
			LazyNamespace.this.removeCache(obsoleteKey);
		}
	}

	protected void updateDocumentLoader(DocumentLoader updatedLoader) {
		removeDocumentLoaderFromCache(updatedLoader);
		addDocumentLoaderToCache(updatedLoader, updatedLoader.refreshTargetDeclarationNames(this));
	}

}
