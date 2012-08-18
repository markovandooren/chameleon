package chameleon.core.namespace;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import chameleon.core.Config;
import chameleon.core.declaration.Declaration;
import chameleon.core.declaration.SimpleNameSignature;
import chameleon.core.lookup.DeclarationSelector;
import chameleon.core.lookup.LookupException;
import chameleon.input.ParseException;
import chameleon.workspace.InputSource;

public class LazyNamespace extends RegularNamespace {

	protected LazyNamespace(SimpleNameSignature sig) {
		super(sig);
		_inputSources = new ArrayList<InputSource>();
		_sourceMap = new HashMap<String, List<InputSource>>();
	}
	
	protected LazyNamespace(String name) {
		this(new SimpleNameSignature(name));
	}
	
	@Override
	protected Namespace createSubNamespace(String name) {
		LazyNamespace result = new LazyNamespace(name);
		addNamespace(result);
		return result;
	}

//	public <D extends Declaration> List<D> declarations(DeclarationSelector<D> selector) throws LookupException {
//		if(selector.usesSelectionName() && Config.cacheDeclarations()) {
//			List<? extends Declaration> list = null;
//			synchronized(this) {
//				list = fetchCandidatesFromCache(selector);
//			}
//			if(list == null) {
//				list = Collections.EMPTY_LIST;
//			}
//			return selector.selection(Collections.unmodifiableList(list));
//		} else {
//			return selector.selection(declarations());
//		}
//	}
	
	@Override
	protected synchronized void ensureLocalCache() throws LookupException {
		if(_declarationCache == null) {
			_declarationCache = new HashMap<String, List<Declaration>>();
		}
	}
	
	@Override
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
	
	public void addInputSource(InputSource source) {
		_inputSources.add(source);
		for(String name: source.targetDeclarationNames(this)) {
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
		if(! _allLoaded) {
			for(InputSource source: inputSourcesView()) {
				try {
					source.load();
				} catch (IOException e) {
					throw new LookupException("File open error",e);
				} catch (ParseException e) {
					throw new LookupException("File parse error",e);
				}
			}
		}
		return super.declarations();
	}
	
	public List<InputSource> inputSources() {
		return new ArrayList<InputSource>(_inputSources);
	}
	
	public List<InputSource> inputSourcesView() {
		return Collections.unmodifiableList(_inputSources);
	}
	
	private boolean _allLoaded = false;
		
	private Map<String, List<InputSource>> _sourceMap;

	private List<InputSource> _inputSources;
}
