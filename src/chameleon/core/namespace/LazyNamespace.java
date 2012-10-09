package chameleon.core.namespace;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.rejuse.association.OrderedMultiAssociation;

import chameleon.core.declaration.Declaration;
import chameleon.core.declaration.SimpleNameSignature;
import chameleon.core.element.Element;
import chameleon.core.element.LoadException;
import chameleon.core.lookup.LookupException;
import chameleon.exception.ChameleonProgrammerException;
import chameleon.workspace.InputException;
import chameleon.workspace.InputSource;

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

	@Override
	protected void initLocalCache() throws LookupException {
		if(_declarationCache == null) {
			_declarationCache = new HashMap<String, List<Declaration>>();
		}
	}
	
	@Override
	// Only called in synchronized
	protected synchronized List<Declaration> auxDeclarations(String name) throws LookupException {
		// First check the declaration cache.
		List<Declaration> candidates = super.auxDeclarations(name);
		// If there was no cache, the input sources might have something
		if(candidates == null) {
			for(Namespace ns: getSubNamespaces()) {
				if(ns.name().equals(name)) {
					candidates = new ArrayList<Declaration>();
					candidates.add(ns);
					break;
				}
			}
			List<InputSource> sources = _sourceMap.get(name);
			if(sources != null) {
				for(InputSource source: sources) {
					if(candidates == null) {
						candidates = new ArrayList<Declaration>();
					}
					candidates.addAll(source.targetDeclarations(name));
				}
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
			List<InputSource> list = _sourceMap.get(name);
			if(list == null) {
				list = new ArrayList<InputSource>();
				_sourceMap.put(name, list);
			}
			list.add(source);
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
	
	private Map<String, List<InputSource>> _sourceMap = new HashMap<String, List<InputSource>>();

	private OrderedMultiAssociation<LazyNamespace,InputSource> _inputSources = new OrderedMultiAssociation<LazyNamespace, InputSource>(this);
}
