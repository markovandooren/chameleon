package be.kuleuven.cs.distrinet.chameleon.core.namespace;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import be.kuleuven.cs.distrinet.chameleon.core.Config;
import be.kuleuven.cs.distrinet.chameleon.core.declaration.Declaration;
import be.kuleuven.cs.distrinet.chameleon.core.declaration.DeclarationContainer;
import be.kuleuven.cs.distrinet.chameleon.core.declaration.BasicDeclaration;
import be.kuleuven.cs.distrinet.chameleon.core.declaration.TargetDeclaration;
import be.kuleuven.cs.distrinet.chameleon.core.lookup.DeclarationSelector;
import be.kuleuven.cs.distrinet.chameleon.core.lookup.LocalLookupContext;
import be.kuleuven.cs.distrinet.chameleon.core.lookup.LookupContext;
import be.kuleuven.cs.distrinet.chameleon.core.lookup.LookupException;
import be.kuleuven.cs.distrinet.chameleon.core.lookup.SelectionResult;
import be.kuleuven.cs.distrinet.chameleon.core.namespacedeclaration.NamespaceDeclaration;
import be.kuleuven.cs.distrinet.chameleon.util.Lists;
import be.kuleuven.cs.distrinet.chameleon.util.Util;
import be.kuleuven.cs.distrinet.rejuse.predicate.TypePredicate;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableList.Builder;

/**
 * <p>Namespaces are a completely logical structure. You do not explicitly create a namespace, but query it using
 * the getOrCreateNamespace method.</p> 
 * 
 * <p>The declarations in a namespace are not stored directly in that namespace. Instead, they are stored in namespace parts.
 * A namespace contains a number of namespace parts. The reason for this decision is that some language, such as C#, allow a programmer
 * to add elements to different namespaces in a single compilation unit (file). Namespace parts can also contain other namespace parts.</p>
 * 
 * <p>For example, if you write a namespace declaration in C#, that will correspond to a namespace part in the model. Thus,
 * "namespace a {class{X} namespace b.c{class Y{} }} namespace d {class Z{}} adds class X to namespace a, lass Y to namespace a.b.c,
 * and class Z to namespace d.</p>
 * 
 * <img src="namespaces.jpg"/>
 * 
 * <p>We chose to unify this structure for all languages. For example, a Java compilation unit with a package declaration
 * will contain a namespace part for the root namespace. A Java compilation with a package declaration contains a namespace 
 * part for that namespace (package).</p>
 *  
 * @author Marko van Dooren
 */
//FIXME Make this extends DeclarationImpl
public abstract class NamespaceImpl extends BasicDeclaration implements TargetDeclaration, DeclarationContainer, Namespace {

	/**
	 * Initialize a new Namespace with the given name.
	 *
	 * @param name
	 *        The name of the new package.
	 */
 /*@
	 @ public behavior
	 @
	 @ pre sig != null;
	 @
	 @ post signature()==sig;
	 @*/
	protected NamespaceImpl(String name) {
      super(name);
	}
	
	protected NamespaceImpl() {
		super("");
	}
	
	@Override
	public Namespace namespace() {
		return (Namespace) parent();
	}


	public String getFullyQualifiedName() {
		Namespace nearestAncestor = nearestAncestor(Namespace.class);
		return ((parent() == null || nearestAncestor.name().equals("")) ? "" : nearestAncestor.getFullyQualifiedName() + ".") + name();
	}
	
	public String toString() {
		return getFullyQualifiedName();
	}

	/**************
	 * PACKAGEPART
	 **************/

	public Namespace defaultNamespace() {
		if (parent() == null) {
			return this;
		}
		else {
			return nearestAncestor(Namespace.class).defaultNamespace();
		}
	}

	/**
	 * Return the subnamespaces of this namespace.
	 */
 /*@
   @ public behavior
   @
   @ post \result != null;
   @*/
	public abstract List<Namespace> getSubNamespaces();

	public Namespace getOrCreateNamespace(final String qualifiedName) {
		Namespace currentNamespace = null;
		String next;
		synchronized(this) {
			if ((qualifiedName == null) || qualifiedName.equals("")) {
				return this;
			}
			String current = Util.getFirstPart(qualifiedName);
			next = Util.getAllButFirstPart(qualifiedName); //rest
			currentNamespace = getSubNamespace(current);
			if(currentNamespace == null) {
				currentNamespace = createSubNamespace(current);
			}
		}
		return currentNamespace.getOrCreateNamespace(next);
	}

	/**
	 * Return the direct subpackage with the given short name.
	 *
	 * @param name
	 *        The short name of the package to be returned.
	 */
 /*@
	 @ public behavior
	 @
	 @ post getSubNamespaces().contains(\result);
	 @ post \result.getName().equals(name);
	 @
	 @ signals (LookupException) (* There are multiple namespaces with the given name. *);
	 @*/
//	public Namespace getSubNamespace(final String name) throws LookupException {
//		// SLOW keep a map name -> subnamespace
//		List<Namespace> packages = getSubNamespaces();
//		for(Namespace n: packages) {
//			if(n.name().equals(name)) {
//				return n;
//			}
//		}
//		return null;
//	}

	public Namespace getSubNamespace(String name) {
		return _nameMap.get(name);
	}

	protected void registerNamespace(Namespace namespace) {
		_nameMap.put(namespace.name(),namespace);
	}

	protected void unregisterNamespace(Namespace namespace) {
		_nameMap.remove(namespace.name());
	}

	private Map<String,Namespace> _nameMap = new HashMap<>();

	public <T extends Declaration> List<T> allDescendantDeclarations(Class<T> kind) throws LookupException {
  	final List<T> result = declarations(kind);
  	for(Namespace ns:getSubNamespaces()) {
		  result.addAll(ns.allDescendantDeclarations(kind));
  	}
 	  return result;
	}

		/***********
		 * CONTEXT *
		 ***********/
		
	public LocalLookupContext targetContext() {
		if(_target == null) {
			_target = language().lookupFactory().createTargetLookupStrategy(this);
		}
		return _target;
	}
	
	private LocalLookupContext _target;
	
	public LookupContext localContext() {
		if(_local == null) {
			_local = language().lookupFactory().createLocalLookupStrategy(this);
			_local.enableCache();
		}
		return _local;
	}

	private LookupContext _local;

	public List<Declaration> declarations() throws LookupException {
		return directDeclarations();
	}
	
	protected List<Declaration> directDeclarations() throws LookupException {
		Builder<Declaration> builder = ImmutableList.<Declaration>builder();
		builder.addAll(getSubNamespaces());
		for(NamespaceDeclaration part: loadedNamespaceParts()) {
			builder.addAll(part.declarations());
		}
		return builder.build();
	}

	public abstract List<NamespaceDeclaration> loadedNamespaceParts();


	@Override
	public synchronized void flushLocalCache() {
		_declarationCache = null;
		if(_local != null) {
			_local.flushCache();
		}
	}
	
	protected void initDirectCache() throws LookupException {
		if(_declarationCache == null) {
			// We don't want to trigger loading of lazy input sources to
			// build the cache of directly connected declarations.
			_declarationCache = new HashMap<String, List<Declaration>>();
//		  for(Declaration declaration: directDeclarations()) {
//		  	_declarationCache.put(declaration.name(), Lists.create(declaration,1));
//		  }
			for(Declaration declaration: getSubNamespaces()) {
				_declarationCache.put(declaration.name(), Lists.create(declaration,1));
			}
			for(NamespaceDeclaration part: loadedNamespaceParts()) {
				for(Declaration declaration: part.declarations()) {
					_declarationCache.put(declaration.name(), Lists.create(declaration,1));
				}
			}
			
		}
	}
	
//	public static Map<String,List<String>> LOG = new HashMap<>();
//	
//	private List<String> getLog() {
//		String fullyQualifiedName = this.getFullyQualifiedName();
//		List<String> result = LOG.get(fullyQualifiedName);
//		if(result == null) {
//			result = new ArrayList<String>();
//			LOG.put(fullyQualifiedName,result);
//		}
//		return result;
//	}
	
//	protected void log(String string) {
//		synchronized (LOG) {
//			Util.debug(string.equals("found no declaration with name ResolvedPackage") && getFullyQualifiedName().equals("org.jnome.mm.java.packages"));
//			List<String> log = getLog();
//			log.add(string);
//		}
//	}
	
	protected synchronized void updateLocalCacheNamespaceAdd(Namespace namespace) {
		if(_declarationCache !=null) {
				List<Declaration> decls = _declarationCache.get(namespace.name());
				if(decls != null) {
					decls.add(namespace);
				}
		}
	}
	protected synchronized List<Declaration> searchDeclarations(String name) throws LookupException {
		return directDeclarations(name);
	}
	
	protected synchronized List<Declaration> directDeclarations(String name) throws LookupException {
		if(_declarationCache != null) {
		  return _declarationCache.get(name);
		} else {
			return null;
		}
	}
	
	protected synchronized void storeCache(String name, List<Declaration> declarations) {
		if(_declarationCache == null) {
			_declarationCache = new HashMap<String, List<Declaration>>();
		}
		_declarationCache.put(name, declarations);
	}

	protected Map<String,List<Declaration>> _declarationCache;
	
	public <D extends Declaration> List<? extends SelectionResult> declarations(DeclarationSelector<D> selector) throws LookupException {
//		System.out.println("Requesting declarations() of "+getFullyQualifiedName());
		List<? extends SelectionResult> result;
		if(selector.usesSelectionName()) {
			List<Declaration> list = null;
			String selectionName = selector.selectionName(this);
			if(Config.cacheDeclarations()) {
				synchronized(this) {
					initDirectCache();
				  list = searchDeclarations(selectionName);
				}
			} else {
				list = declarations();
			}
			if(list == null) {
				list = Collections.EMPTY_LIST;
			}
			result = selector.selection(Collections.unmodifiableList(list));
			// If nothing was found and a namespace or more generic type is searched,
			// the namespace is resolved and given to the selector.
			if(result.isEmpty() && selector.canSelect(Namespace.class)) {
				Namespace subNamespace = getSubNamespace(selectionName);
				if(subNamespace != null) {
					result = selector.selection(Collections.singletonList(subNamespace));
				}
			}
		} else {
			result = selector.selection(declarations());
		}
		return result;
	}
	
	public <T extends Declaration> List<T> declarations(Class<T> kind) throws LookupException {
    return new TypePredicate<T>(kind).downCastedList(declarations());
  }
	

	public NamespaceAlias alias(String name) {
		return new NamespaceAlias(name,this);
	}

	public Namespace selectionDeclaration() {
		return this;
	}
	
	public Namespace actualDeclaration() {
		return this;
	}
	
	@Override
	public Declaration finalDeclaration() {
		return this;
	}
	
	@Override
	public Declaration template() {
		return finalDeclaration();
	}

	@Override
	public SelectionResult updatedTo(Declaration declaration) {
		return declaration;
	}

}
