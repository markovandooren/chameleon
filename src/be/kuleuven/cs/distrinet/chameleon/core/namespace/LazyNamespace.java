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
import be.kuleuven.cs.distrinet.chameleon.workspace.InputSource;
import be.kuleuven.cs.distrinet.rejuse.association.OrderedMultiAssociation;

import com.google.common.collect.ImmutableList;

public class LazyNamespace extends RegularNamespace implements InputSourceNamespace {

	protected LazyNamespace(String name) {
		super(name);
	}
	
	@Override
	protected RegularNamespace cloneSelf() {
		return new LazyNamespace(name());
	}
	
	@Override
	public synchronized List<NamespaceDeclaration> getNamespaceParts() {
		for(InputSource inputSource: inputSources()) {
			try {
				inputSource.load();
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
		// If there was no cache, the input sources might have something
		if(candidates == null) {
			Namespace ns = getSubNamespace(name);
			if(ns != null) {
				candidates = ImmutableList.<Declaration>of(ns);
			}
			// inputSources is sorted: the element with the highest priority is in front.
			Queue<InputSource> inputSources = _sourceMap.get(name);
			if(inputSources != null && ! inputSources.isEmpty()) {
					if(candidates == null) {
						candidates = Lists.create();
					}
				candidates.addAll(inputSources.peek().targetDeclarations(name));
			}
			if (candidates == null){
				candidates = Collections.EMPTY_LIST;
			}
			storeCache(name, candidates);
		}
		return candidates;
	}
	
	public void addInputSource(InputSource source) throws InputException {
		_inputSources.add(source.namespaceLink());
		List<String> targetDeclarationNames = source.targetDeclarationNames(this);
		for(String name: targetDeclarationNames) {
			if(name == null) {
				throw new ChameleonProgrammerException("An input source uses null as a declaration name.");
			}
			Queue<InputSource> queue = _sourceMap.get(name);
			if(queue == null) {
				queue = new PriorityQueue<InputSource>();
				_sourceMap.put(name, queue);
			}
			queue.add(source);
		}
	}
	
	/**
	 * First, all input sources are loaded. This attaches all namespace declarations
	 * to their corresponding namespace. After that, a super call is performed to
	 * 
	 */
	@Override
	public List<Declaration> declarations() throws LookupException {
		for(InputSource source: inputSources()) {
			try {
				source.load();
			} catch (InputException e) {
				throw new LookupException("An input exception occurred while loading an input source.",e);
			}
		}
		return super.declarations();
	}
	
	@Override
	public List<? extends Element> children() {
		for(Queue<InputSource> q: _sourceMap.values()) {
			try {
				q.peek().load();
			} catch (InputException e) {
				throw new LoadException("File open error",e);
			}
		}
		return super.children();
	}
	
	public List<InputSource> inputSources() {
		return _inputSources.getOtherEnds();
	}
	
	private Map<String, Queue<InputSource>> _sourceMap = new HashMap<String, Queue<InputSource>>();

	private OrderedMultiAssociation<LazyNamespace,InputSource> _inputSources = new OrderedMultiAssociation<LazyNamespace, InputSource>(this) {
		@Override
		protected void fireElementRemoved(InputSource removedElement) {
			List<String> obsoleteKeys = Lists.create();
			for(Map.Entry<String, Queue<InputSource>> entry: _sourceMap.entrySet()) {
				Queue<InputSource> value = entry.getValue();
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
