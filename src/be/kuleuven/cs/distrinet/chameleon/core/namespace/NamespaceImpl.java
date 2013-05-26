package be.kuleuven.cs.distrinet.chameleon.core.namespace;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import be.kuleuven.cs.distrinet.chameleon.core.Config;
import be.kuleuven.cs.distrinet.chameleon.core.declaration.Declaration;
import be.kuleuven.cs.distrinet.chameleon.core.declaration.DeclarationContainer;
import be.kuleuven.cs.distrinet.chameleon.core.declaration.Signature;
import be.kuleuven.cs.distrinet.chameleon.core.declaration.SimpleNameSignature;
import be.kuleuven.cs.distrinet.chameleon.core.declaration.TargetDeclaration;
import be.kuleuven.cs.distrinet.chameleon.core.element.ElementImpl;
import be.kuleuven.cs.distrinet.chameleon.core.lookup.DeclarationSelector;
import be.kuleuven.cs.distrinet.chameleon.core.lookup.LocalLookupContext;
import be.kuleuven.cs.distrinet.chameleon.core.lookup.LookupContext;
import be.kuleuven.cs.distrinet.chameleon.core.lookup.LookupException;
import be.kuleuven.cs.distrinet.chameleon.core.namespacedeclaration.NamespaceDeclaration;
import be.kuleuven.cs.distrinet.chameleon.exception.ChameleonProgrammerException;
import be.kuleuven.cs.distrinet.chameleon.util.Util;
import be.kuleuven.cs.distrinet.chameleon.util.association.Single;
import be.kuleuven.cs.distrinet.rejuse.predicate.SafePredicate;
import be.kuleuven.cs.distrinet.rejuse.predicate.TypePredicate;

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

public abstract class NamespaceImpl extends ElementImpl implements TargetDeclaration, DeclarationContainer, Namespace {

	//SPEED : use hashmap to store the subnamespaces and forbid
	//        adding multiple namespaces with the same name. That is
	//        never useful anyway.
	
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
	protected NamespaceImpl(SimpleNameSignature sig) {
      setSignature(sig);
	}
	
	protected NamespaceImpl() {
		
	}


	/*@
   @ public behavior
   @
   @ pre signature != null;
   @
   @ post signature() = signature; 
   @*/
	public void setSignature(Signature signature) {
		if(! (signature instanceof SimpleNameSignature)) {
			throw new ChameleonProgrammerException("A namespace must have a simple name signature. The argument is of type "+
		                                         (signature == null ? "null type" : signature.getClass().getName()));
		}
	  set(_signature,(SimpleNameSignature)signature);
	}
	
	public void setName(String name) {
		setSignature(new SimpleNameSignature(name));
	}

		  
	  public SimpleNameSignature signature() {
	    return _signature.getOtherEnd();
	  }
		  
	  private Single<SimpleNameSignature> _signature = new Single<SimpleNameSignature>(this,true);

	  public String name() {
		  return signature().name();
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
			try {
				currentNamespace = getSubNamespace(current);
			} catch (LookupException e) {
				// currentNamespace == null
			}
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
	public Namespace getSubNamespace(final String name) throws LookupException {
		List<Namespace> packages = getSubNamespaces();

		new SafePredicate<Namespace>() {
			public boolean eval(Namespace o) {
				return o.name().equals(name);
			}
		}.filter(packages);
		if (packages.isEmpty()) {
			return null;
		}
		else if(packages.size() == 1){
			return (Namespace)packages.iterator().next();
		}
		else {
			throw new LookupException("Namespace "+getFullyQualifiedName()+ " contains "+packages.size()+" sub namespaces with name "+name);
		}

	}

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
		return language().lookupFactory().createTargetLookupStrategy(this);
	}
	
	public LookupContext localContext() {
		return language().lookupFactory().createLocalLookupStrategy(this);
	}

	public List<Declaration> declarations() throws LookupException {
		return directDeclarations();
	}
	
	protected List<Declaration> directDeclarations() throws LookupException {
		List<Declaration> result = (List)getSubNamespaces();
		for(NamespaceDeclaration part: getNamespaceParts()) {
			result.addAll(part.declarations());
		}
		return result;
	}


	@Override
	public synchronized void flushLocalCache() {
		_declarationCache = null;
	}
	
	protected void initDirectCache() throws LookupException {
		if(_declarationCache == null) {
			// We don't want to trigger loading of lazy input sources to
			// build the cache of directly connected declarations.
			_declarationCache = new HashMap<String, List<Declaration>>();
		  for(Declaration declaration: directDeclarations()) {
		  	String name = declaration.name();
				List<Declaration> list = directDeclarations(name);
		  	if(list == null) {
		  		list = new ArrayList<Declaration>();
		  		_declarationCache.put(name, list);
		  	}
		  	// list != null
		  	list.add(declaration);
		  }
		}
	}
	
	protected void updateLocalCacheNamespaceAdd(Namespace namespace) {
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
	
	public <D extends Declaration> List<D> declarations(DeclarationSelector<D> selector) throws LookupException {
//		System.out.println("Requesting declarations() of "+getFullyQualifiedName());
		if(selector.usesSelectionName()) {
			List<Declaration> list = null;
			if(Config.cacheDeclarations()) {
				synchronized(this) {
					initDirectCache();
				  list = searchDeclarations(selector.selectionName(this));
				}
			} else {
				list = declarations();
			}
			if(list == null) {
				list = Collections.EMPTY_LIST;
			}
			List<D> result = selector.selection(Collections.unmodifiableList(list));
			// If nothing was found and a namespace or more generic type is searched,
			// the namespace is resolved and given to the selector.
			if(result.isEmpty() && selector.selectedClass().isAssignableFrom(Namespace.class)) {
				result = selector.selection(Collections.singletonList(getOrCreateNamespace(selector.selectionName(this))));
			}
			return result;
			
		} else {
			return selector.selection(declarations());
		}
	}
	
	public <T extends Declaration> List<T> declarations(Class<T> kind) throws LookupException {
    return new TypePredicate<Declaration,T>(kind).filterReturn(declarations());
  }
	

	public NamespaceAlias alias(SimpleNameSignature sig) {
		return new NamespaceAlias(sig,this);
	}

	public Namespace selectionDeclaration() {
		return this;
	}
	
	public Namespace actualDeclaration() {
		return this;
	}
	
}
