package org.aikodi.chameleon.core.namespace;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;

import org.aikodi.chameleon.core.declaration.Declaration;
import org.aikodi.chameleon.core.element.Element;
import org.aikodi.chameleon.core.lookup.LookupException;
import org.aikodi.chameleon.exception.ChameleonProgrammerException;
import org.aikodi.chameleon.util.Lists;
import org.aikodi.chameleon.workspace.DocumentLoader;
import org.aikodi.chameleon.workspace.InputException;
import org.aikodi.chameleon.workspace.View;
import org.aikodi.rejuse.association.OrderedMultiAssociation;

import com.google.common.collect.ImmutableList;

/**
 * FIXME: use default methods in {@link DocumentLoaderNamespace} to get rid
 * of the code duplication.
 * 
 * @author Marko van Dooren
 */
public class LazyRootNamespace extends RootNamespace implements DocumentLoaderNamespace {

	public LazyRootNamespace() {
		this(null);
	}
	
	@Override
	protected RootNamespace cloneSelf() {
		return new LazyRootNamespace();
	}
	
	public LazyRootNamespace(View view) {
		super(view,new LazyNamespaceFactory());
	}

	@Override
	protected synchronized List<Declaration> searchDeclarations(String name) throws LookupException {
		// First check the declaration cache.
		List<Declaration> candidates = super.searchDeclarations(name);
		// If there was no cache, the document loaders might have something
		if(candidates == null) {
			for(Namespace ns: subNamespaces()) {
				if(ns.name().equals(name)) {
					candidates = ImmutableList.<Declaration>of(ns);
					break;
				}
			}
			// documentLoaders is sorted: the element with the highest priority is in front.
			Queue<DocumentLoader> documentLoaders = _sourceMap.get(name);
			if(documentLoaders != null && ! documentLoaders.isEmpty()) {
					if(candidates == null) {
						candidates = Lists.create();
					}
				candidates.addAll(documentLoaders.peek().targetDeclarations(name));
			} 
			if(candidates == null){
				candidates = Lists.create();
			}
			storeCache(name, candidates);
		}
		return candidates;
	}
	
	@Override
   public void addDocumentLoader(DocumentLoader source) throws InputException {
		_documentLoaders.add(source.namespaceLink());
		List<String> targetDeclarationNames = source.targetDeclarationNames(this);
		addDocumentLoaderToCache(source, targetDeclarationNames);
	}

  protected void addDocumentLoaderToCache(DocumentLoader source, List<String> targetDeclarationNames) {
    for(String name: targetDeclarationNames) {
			if(name == null) {
				throw new ChameleonProgrammerException("A document loader uses null as a declaration name.");
			}
			Queue<DocumentLoader> queue = _sourceMap.get(name);
			if(queue == null) {
				queue = new PriorityQueue<DocumentLoader>();
				_sourceMap.put(name, queue);
			}
			queue.add(source);
		}
  }
	
	@Override
	public List<Declaration> declarations() throws LookupException {
		for(DocumentLoader source: documentLoaders()) {
			try {
				source.load();
			} catch (InputException e) {
				throw new LookupException("File open error",e);
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

	private OrderedMultiAssociation<LazyRootNamespace,DocumentLoader> _documentLoaders = new OrderedMultiAssociation<LazyRootNamespace, DocumentLoader>(this) {
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
      LazyRootNamespace.this.removeCache(obsoleteKey);
    }
  }

  @Override
  public synchronized void flushLocalCache() {
    super.flushLocalCache();
    for(DocumentLoader loader: documentLoaders()) {
      updateDocumentLoader(loader);
    }
  }
  
  protected void updateDocumentLoader(DocumentLoader updatedLoader) {
    removeDocumentLoaderFromCache(updatedLoader);
    addDocumentLoaderToCache(updatedLoader, updatedLoader.refreshTargetDeclarationNames(this));
  }

}
