package be.kuleuven.cs.distrinet.chameleon.core.namespace;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import be.kuleuven.cs.distrinet.chameleon.core.declaration.Declaration;
import be.kuleuven.cs.distrinet.chameleon.core.declaration.SimpleNameSignature;
import be.kuleuven.cs.distrinet.chameleon.core.element.Element;
import be.kuleuven.cs.distrinet.chameleon.core.element.LoadException;
import be.kuleuven.cs.distrinet.chameleon.core.lookup.LookupException;
import be.kuleuven.cs.distrinet.chameleon.core.namespacedeclaration.NamespaceDeclaration;
import be.kuleuven.cs.distrinet.chameleon.workspace.InputException;
import be.kuleuven.cs.distrinet.chameleon.workspace.InputSource;
import be.kuleuven.cs.distrinet.chameleon.workspace.View;
import be.kuleuven.cs.distrinet.rejuse.association.OrderedMultiAssociation;

public class LazyRootNamespace extends RootNamespace implements InputSourceNamespace {

	public LazyRootNamespace() {
		super(new LazyNamespaceFactory());
	}

	
	@Override
	protected RootNamespace cloneThis() {
		LazyRootNamespace result = new LazyRootNamespace();
		return result;
	}
	
	public LazyRootNamespace(SimpleNameSignature sig, View view) {
		super(sig,view,new LazyNamespaceFactory());
	}

	@Override
	protected synchronized void initDirectCache() throws LookupException {
		if(_declarationCache == null) {
			_declarationCache = new HashMap<String, List<Declaration>>();
		}
	}
	
	@Override
	protected synchronized List<Declaration> searchDeclarations(String name) throws LookupException {
		// First check the declaration cache.
		List<Declaration> candidates = super.searchDeclarations(name);
		// If there was no cache, the input sources might have something
		if(candidates == null) {
			Set<Declaration> tmp;
			// 1. Search for a subnamespace with the given name.
			for(Namespace ns: getSubNamespaces()) {
				if(ns.name().equals(name)) {
					candidates = new ArrayList<Declaration>();
					candidates.add(ns);
					break;
				}
			}

			// 2. Search in the namespace declarations for a declaration with the given name.
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
	
	public void addInputSource(InputSource source) throws InputException {
		_inputSources.add(source.namespaceLink());
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

	private OrderedMultiAssociation<LazyRootNamespace,InputSource> _inputSources = new OrderedMultiAssociation<LazyRootNamespace, InputSource>(this);

}
