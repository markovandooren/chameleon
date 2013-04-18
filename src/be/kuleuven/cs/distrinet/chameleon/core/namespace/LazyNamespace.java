package be.kuleuven.cs.distrinet.chameleon.core.namespace;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;

import be.kuleuven.cs.distrinet.chameleon.core.declaration.Declaration;
import be.kuleuven.cs.distrinet.chameleon.core.declaration.SimpleNameSignature;
import be.kuleuven.cs.distrinet.chameleon.core.element.Element;
import be.kuleuven.cs.distrinet.chameleon.core.lookup.LookupException;
import be.kuleuven.cs.distrinet.chameleon.exception.ChameleonProgrammerException;
import be.kuleuven.cs.distrinet.chameleon.workspace.InputException;
import be.kuleuven.cs.distrinet.chameleon.workspace.InputSource;
import be.kuleuven.cs.distrinet.rejuse.association.OrderedMultiAssociation;
import be.kuleuven.cs.distrinet.rejuse.logic.relation.ComparableOrder;

public class LazyNamespace extends RegularNamespace implements InputSourceNamespace {

	protected LazyNamespace(SimpleNameSignature sig) {
		super(sig);
	}
	
	protected LazyNamespace(String name) {
		this(new SimpleNameSignature(name));
	}
	
	@Override
	protected RegularNamespace cloneThis() {
		LazyNamespace result = new LazyNamespace(signature().clone());
		return result;
	}
	
	@Override
	public Namespace createSubNamespace(String name) {
		LazyNamespace result = new LazyNamespace(name);
		addNamespace(result);
		return result;
	}

	private ComparableOrder<InputSource> _comparableOrder = new ComparableOrder<InputSource>();
	
	@SuppressWarnings("unchecked")
	@Override
	// Only called in synchronized
	protected synchronized List<Declaration> searchDeclarations(String name) throws LookupException {
		// First check the declaration cache.
		List<Declaration> candidates = super.searchDeclarations(name);
		// If there was no cache, the input sources might have something
		if(candidates == null) {
			for(Namespace ns: getSubNamespaces()) {
				if(ns.name().equals(name)) {
					candidates = new ArrayList<Declaration>();
					candidates.add(ns);
					break;
				}
			}
			// inputSources is sorted: the element with the highest priority is in front.
			Queue<InputSource> inputSources = _newSourceMap.get(name);
			if(inputSources != null && ! inputSources.isEmpty()) {
//				List<InputSource> sources = new ArrayList(inputSourceList);
//				try {
//					_comparableOrder.removeBiggerElements(sources);
//				} catch (Exception e) {
//					throw new ChameleonProgrammerException(e);
//				}
//				for(InputSource source: sources) {
					if(candidates == null) {
						candidates = new ArrayList<Declaration>();
					}
//					candidates.addAll(source.targetDeclarations(name));
//				}
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
//			List<InputSource> list = _sourceMap.get(name);
			Queue<InputSource> queue = _newSourceMap.get(name);
			if(queue == null) {
//				list = new ArrayList<InputSource>();
				queue = new PriorityQueue<InputSource>();
//				_sourceMap.put(name, list);
				_newSourceMap.put(name, queue);
			}
//			list.add(source);
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
		//SPEED add view to Association class.
		for(InputSource source: inputSources()) {
			try {
				source.load();
			} catch (InputException e) {
				throw new LoadException("File open error",e);
			}
		}
		return super.children();
	}
	
	public List<InputSource> inputSources() {
		return _inputSources.getOtherEnds();
	}
	
//	private Map<String, List<InputSource>> _sourceMap = new HashMap<String, List<InputSource>>();
	private Map<String, Queue<InputSource>> _newSourceMap = new HashMap<String, Queue<InputSource>>();

	private OrderedMultiAssociation<LazyNamespace,InputSource> _inputSources = new OrderedMultiAssociation<LazyNamespace, InputSource>(this) {
		@Override
		protected void fireElementRemoved(InputSource removedElement) {
			List<String> obsoleteKeys = new ArrayList<String>();
			for(Map.Entry<String, Queue<InputSource>> entry: _newSourceMap.entrySet()) {
				Queue<InputSource> value = entry.getValue();
				value.remove(removedElement);
				if(value.isEmpty()) {
					obsoleteKeys.add(entry.getKey());
				}
			}
			for(String obsoleteKey: obsoleteKeys) {
//				_sourceMap.remove(obsoleteKey);
				_newSourceMap.remove(obsoleteKey);
			}
		}
	};
}
