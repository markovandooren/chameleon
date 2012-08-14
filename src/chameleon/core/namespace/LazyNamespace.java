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

	public LazyNamespace(SimpleNameSignature sig) {
		super(sig);
		_inputSources = new ArrayList<InputSource>();
		_sourceMap = new HashMap<String, List<InputSource>>();
	}
	
	public LazyNamespace(String name) {
		this(new SimpleNameSignature(name));
	}

	public <D extends Declaration> List<D> declarations(DeclarationSelector<D> selector) throws LookupException {
		if(selector.usesSelectionName() && Config.cacheDeclarations()) {
			List<? extends Declaration> list = null;
			synchronized(this) {
				list = fetchCandidatesFromCache(selector);
			}
			if(list == null) {
				list = Collections.EMPTY_LIST;
			}
			return selector.selection(Collections.unmodifiableList(list));
		} else {
			return selector.selection(declarations());
		}
	}
	
	private List<? extends Declaration> fetchCandidatesFromCache(DeclarationSelector<?> selector) throws LookupException {
		String name = selector.selectionName(this);
		List<InputSource> sources = _sourceMap.get(name);
		List<Declaration> candidates = new ArrayList<Declaration>();
		for(InputSource source: sources) {
			candidates.addAll(source.declarations(name));
		}
		return candidates;
	}
	
	public void addInputSource(InputSource source) {
		_inputSources.add(source);
		for(String name: source.declarationNames()) {
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
