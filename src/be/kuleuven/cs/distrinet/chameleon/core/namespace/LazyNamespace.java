package be.kuleuven.cs.distrinet.chameleon.core.namespace;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;

import be.kuleuven.cs.distrinet.chameleon.core.declaration.Declaration;
import be.kuleuven.cs.distrinet.chameleon.core.element.Element;
import be.kuleuven.cs.distrinet.chameleon.core.lookup.LookupException;
import be.kuleuven.cs.distrinet.chameleon.core.namespacedeclaration.NamespaceDeclaration;
import be.kuleuven.cs.distrinet.chameleon.exception.ChameleonProgrammerException;
import be.kuleuven.cs.distrinet.chameleon.util.Lists;
import be.kuleuven.cs.distrinet.chameleon.workspace.InputException;
import be.kuleuven.cs.distrinet.chameleon.workspace.DocumentLoader;
import be.kuleuven.cs.distrinet.rejuse.association.OrderedMultiAssociation;

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
	public synchronized List<NamespaceDeclaration> getNamespaceParts() {
		for(DocumentLoader loader: documentLoaders()) {
			try {
				loader.load();
			} catch (InputException e) {
				throw new ChameleonProgrammerException(e);
			}
		}
		return super.getNamespaceParts();
	}
	
	@Override
	public Namespace createSubNamespace(String name) {
		LazyNamespace result = new LazyNamespace(name);
		addNamespace(result);
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
			} else {
				// loaders is sorted: the element with the highest priority is in front.
				Queue<DocumentLoader> loaders = _sourceMap.get(name);
				if(loaders != null && ! loaders.isEmpty()) {
					candidates = loaders.peek().targetDeclarations(name);
				} else {
					candidates = Collections.EMPTY_LIST;
				}
			}
			storeCache(name, candidates);
		}
		return candidates;
	}
	
	@Override
   public void addDocumentLoader(DocumentLoader source) throws InputException {
		_documentLoaders.add(source.namespaceLink());
		List<String> targetDeclarationNames = source.targetDeclarationNames(this);
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
			}
		}
	};
}
