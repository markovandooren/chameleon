package chameleon.core.namespace;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import chameleon.core.declaration.Declaration;
import chameleon.core.declaration.SimpleNameSignature;
import chameleon.core.element.Element;
import chameleon.core.element.LoadException;
import chameleon.core.lookup.LookupException;
import chameleon.core.namespacedeclaration.NamespaceDeclaration;
import chameleon.input.ParseException;
import chameleon.workspace.InputSource;
import chameleon.workspace.Project;

public class LazyRootNamespace extends RootNamespace implements InputSourceNamespace {

	public LazyRootNamespace() {
		super(new LazyNamespaceFactory());
	}

	public LazyRootNamespace(SimpleNameSignature sig, Project project) {
		super(sig,project,new LazyNamespaceFactory());
	}

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
			Set<Declaration> tmp;
			for(Namespace ns: getSubNamespaces()) {
				if(ns.name().equals(name)) {
					candidates = new ArrayList<Declaration>();
					candidates.add(ns);
					break;
				}
			}
			for(NamespaceDeclaration part: getNamespaceParts()) {
				for(Declaration decl : part.declarations()) {
					if(decl.name().equals(name)) {
						if(candidates == null) {
							candidates = new ArrayList<Declaration>();
						}
						candidates.add(decl);
					}
				}
			}
			if(candidates != null) {
				tmp = new HashSet<Declaration>(candidates);
			} else {
				tmp = new HashSet<Declaration>();
			}
			List<InputSource> sources = _sourceMap.get(name);
			if(sources != null) {
				for(InputSource source: sources) {
					tmp.addAll(source.targetDeclarations(name));
				}
			} 
			candidates = new ArrayList<Declaration>(tmp);
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
		for(InputSource source: inputSourcesView()) {
			try {
				source.load();
			} catch (IOException e) {
				throw new LookupException("File open error",e);
			} catch (ParseException e) {
				throw new LookupException("File parse error",e);
			}
		}
		return super.declarations();
	}
	
	@Override
	public List<? extends Element> children() {
		for(InputSource source: inputSourcesView()) {
			try {
				source.load();
			} catch (IOException e) {
				throw new LoadException("File open error",e);
			} catch (ParseException e) {
				throw new LoadException("File parse error",e);
			}
		}
		return super.children();
	}
	
	public List<InputSource> inputSources() {
		return new ArrayList<InputSource>(_inputSources);
	}
	
	public List<InputSource> inputSourcesView() {
		return Collections.unmodifiableList(_inputSources);
	}
	
	private Map<String, List<InputSource>> _sourceMap = new HashMap<String, List<InputSource>>();

	private List<InputSource> _inputSources = new ArrayList<InputSource>();

}
