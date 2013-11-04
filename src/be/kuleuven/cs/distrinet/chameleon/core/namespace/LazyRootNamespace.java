package be.kuleuven.cs.distrinet.chameleon.core.namespace;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;

import be.kuleuven.cs.distrinet.chameleon.core.declaration.Declaration;
import be.kuleuven.cs.distrinet.chameleon.core.element.Element;
import be.kuleuven.cs.distrinet.chameleon.core.lookup.LookupException;
import be.kuleuven.cs.distrinet.chameleon.exception.ChameleonProgrammerException;
import be.kuleuven.cs.distrinet.chameleon.util.Lists;
import be.kuleuven.cs.distrinet.chameleon.workspace.InputException;
import be.kuleuven.cs.distrinet.chameleon.workspace.InputSource;
import be.kuleuven.cs.distrinet.chameleon.workspace.View;
import be.kuleuven.cs.distrinet.rejuse.association.OrderedMultiAssociation;

import com.google.common.collect.ImmutableList;

public class LazyRootNamespace extends RootNamespace implements InputSourceNamespace {

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

//	@Override
//	protected synchronized void initDirectCache() throws LookupException {
//		if(_declarationCache == null) {
//			_declarationCache = new HashMap<String, List<Declaration>>();
//		}
//	}
	
	@Override
	protected synchronized List<Declaration> searchDeclarations(String name) throws LookupException {
		// First check the declaration cache.
		List<Declaration> candidates = super.searchDeclarations(name);
		// If there was no cache, the input sources might have something
		if(candidates == null) {
			for(Namespace ns: getSubNamespaces()) {
				if(ns.name().equals(name)) {
					candidates = ImmutableList.<Declaration>of(ns);
					break;
				}
			}
			// inputSources is sorted: the element with the highest priority is in front.
			Queue<InputSource> inputSources = _sourceMap.get(name);
			if(inputSources != null && ! inputSources.isEmpty()) {
					if(candidates == null) {
						candidates = Lists.create();
					}
				candidates.addAll(inputSources.peek().targetDeclarations(name));
			} 
			if(candidates == null){
				candidates = ImmutableList.of();
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
	
	@Override
	public List<Declaration> declarations() throws LookupException {
		for(InputSource source: inputSources()) {
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
		for(Queue<InputSource> q: _sourceMap.values()) {
			try {
				q.peek().load();
			} catch (InputException e) {
				throw new LoadException("File open error",e);
			}
		}
		return super.children();
//		for(InputSource source: inputSources()) {
//			try {
//				source.load();
//			} catch (InputException e) {
//				throw new LoadException("File open error",e);
//			}
//		}
//		return super.children();
	}
	
	public List<InputSource> inputSources() {
		return _inputSources.getOtherEnds();
	}
		
	private Map<String, Queue<InputSource>> _sourceMap = new HashMap<String, Queue<InputSource>>();

//	private Map<String, List<InputSource>> _sourceMap = new HashMap<String, List<InputSource>>();

//	private OrderedMultiAssociation<LazyRootNamespace,InputSource> _inputSources = new OrderedMultiAssociation<LazyRootNamespace, InputSource>(this);

	private OrderedMultiAssociation<LazyRootNamespace,InputSource> _inputSources = new OrderedMultiAssociation<LazyRootNamespace, InputSource>(this) {
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
