package org.aikodi.chameleon.core.namespace;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.aikodi.chameleon.core.declaration.BasicDeclaration;
import org.aikodi.chameleon.core.declaration.Declaration;
import org.aikodi.chameleon.core.element.Element;
import org.aikodi.chameleon.core.event.association.ParentRemoved;
import org.aikodi.chameleon.core.lookup.DeclarationSelector;
import org.aikodi.chameleon.core.lookup.LocalLookupContext;
import org.aikodi.chameleon.core.lookup.LookupContext;
import org.aikodi.chameleon.core.lookup.LookupException;
import org.aikodi.chameleon.core.lookup.SelectionResult;
import org.aikodi.chameleon.core.namespacedeclaration.NamespaceDeclaration;
import org.aikodi.chameleon.util.Lists;
import org.aikodi.chameleon.util.Util;
import org.aikodi.contract.Contracts;

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
public abstract class NamespaceImpl extends BasicDeclaration implements Namespace {

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

  public Namespace namespace() {
    Namespace parent = (Namespace) parent();
    return parent == null ? this : parent;
  }


  @Override
  public String fullyQualifiedName() {
    Namespace nearestAncestor = lexical().nearestAncestor(Namespace.class);
    return ((nearestAncestor == null || nearestAncestor.name().equals("")) ? "" : nearestAncestor.fullyQualifiedName() + ".") + name();
  }

  @Override
  public String toString() {
    return fullyQualifiedName();
  }

  /**********************
   * LOGICAL NAVIGATION *
   **********************/
  
  private Navigator<LookupException> _logical = new LogicalNavigator() {
		@Override
		public List<Element> children() throws LookupException {
			// The declarations are cached, so we return a mutable copy instead.
			// Not optimal because an immutable list might be fine for the
			// client, but there is no convenient way for the client to
			// detect this.
				return new ArrayList<>(NamespaceImpl.this.declarations());
		}
	};
	
	@Override
	public Navigator<LookupException> logical() {
		return _logical;
	}

  
  /******************
   * NAMESPACE PART *
   ******************/

  @Override
  public Namespace defaultNamespace() {
    if (parent() == null) {
      return this;
    }
    else {
      return lexical().nearestAncestor(Namespace.class).defaultNamespace();
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
  @Override
  public abstract List<Namespace> subNamespaces();

  @Override
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

  @Override
  public Namespace getSubNamespace(String name) {
    return _nameMap.get(name);
  }

  protected void registerNamespace(Namespace namespace) {
    _nameMap.put(namespace.name(),namespace);
    if(_declarationCache !=null) {
      List<Declaration> decls = _declarationCache.get(namespace.name());
      if(decls == null) {
        decls = new ArrayList<>();
        storeCache(namespace.name(), decls);
      }
      addToList(decls,namespace);
    }
  }
  
  private void addToList(List<Declaration> list, Declaration declaration) {
    list.add(declaration);
    registerListener(declaration);
  }

  /**
   * Register a listener with the given declaration to remote it from
   * the caches when it is removed from the model.
   * @param declaration
   */
	protected void registerListener(Declaration declaration) {
		declaration.when().self().sends(ParentRemoved.class).call(e -> removeDeclaration(declaration));
	}

  private void removeFromList(List<Declaration> list, Declaration declaration) {
    list.remove(declaration);
  }

  protected void unregisterNamespace(Namespace namespace) {
    _nameMap.remove(namespace.name());
    if(_declarationCache !=null) {
      List<Declaration> decls = _declarationCache.get(namespace.name());
      if(decls != null) {
        removeFromList(decls,namespace);
      }
    }
  }
  
  @Override
  public void removeDeclaration(Declaration declaration) {
    Contracts.notNull(declaration, "The declaration cannot be null.");
    if(_declarationCache != null) {
      String name = declaration.name();
      List<Declaration> list = _declarationCache.get(name);
      if(list != null) {
        int size=list.size();
        int i=0;
        while(i<size) {
          if(list.get(i) == declaration) {
            list.remove(i);
            break;
          } else {
            i++;
          }
        }
      }
    }
  }
  
  @Override
  public void addDeclaration(Declaration declaration) {
    Contracts.notNull(declaration, "The declaration cannot be null.");
    if(_declarationCache != null) {
      String name = declaration.name();
      List<Declaration> list = _declarationCache.get(name);
      if(list != null) {
        addToList(list, declaration);
      }
    }
  }

  private Map<String,Namespace> _nameMap = new HashMap<>();

  /***********
   * CONTEXT *
   ***********/

  @Override
  public LocalLookupContext targetContext() {
    if(_target == null) {
      _target = language().lookupFactory().createTargetLookupStrategy(this);
    }
    return _target;
  }

  private LocalLookupContext _target;

  @Override
  public LookupContext localContext() {
    if(_local == null) {
      _local = language().lookupFactory().createLocalLookupStrategy(this);
      _local.enableCache();
    }
    return _local;
  }

  private LookupContext _local;

  @Override
  public List<Declaration> declarations() throws LookupException {
    return directDeclarations();
  }

  protected List<Declaration> directDeclarations() throws LookupException {
    Builder<Declaration> builder = ImmutableList.<Declaration>builder();
    builder.addAll(subNamespaces());
    for(NamespaceDeclaration part: loadedNamespaceDeclarations()) {
      builder.addAll(part.declarations());
    }
    return builder.build();
  }

  @Override
  public abstract List<NamespaceDeclaration> loadedNamespaceDeclarations();


  @Override
  public synchronized void flushLocalCache() {
    super.flushLocalCache();
    _declarationCache = null;
    if(_local != null) {
      _local.flushCache();
    }
  }

  protected void initDirectCache() throws LookupException {
    if(_declarationCache == null) {
      // We don't want to trigger loading of lazy document loaders to
      // build the cache of directly connected declarations.
      _declarationCache = new HashMap<String, List<Declaration>>();
      //		  for(Declaration declaration: directDeclarations()) {
      //		  	_declarationCache.put(declaration.name(), Lists.create(declaration,1));
      //		  }
      for(Declaration declaration: subNamespaces()) {
        storeCache(declaration.name(), Lists.create(declaration,1));
      }
      for(NamespaceDeclaration part: loadedNamespaceDeclarations()) {
        addCacheForNamespaceDeclaration(part);
      }
    }
  }

  protected void addCacheForNamespaceDeclaration(NamespaceDeclaration part) {
    if(_declarationCache != null) {
      for(Declaration declaration: part.declarations()) {
        List<Declaration> matches = _declarationCache.get(declaration.name());
        if(matches == null) {
          matches = new ArrayList<>(1);
          storeCache(declaration.name(), matches);
        }
        addToList(matches,declaration);
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
//    Util.debug(declarations.getClass().getName().contains("EmptyList"));
    _declarationCache.put(name, declarations);
  }

  protected void removeCache(String name) {
    if(_declarationCache != null) {
      synchronized(this) {
        if(_declarationCache != null) {
          _declarationCache.remove(name);
        }
      }
    }
  }
  
  private Map<String,List<Declaration>> _declarationCache;

  public void nonsenseMethodToTestIfTheJarIsUpToDate() {}
  
  @Override
  public <D extends Declaration> List<? extends SelectionResult<D>> declarations(DeclarationSelector<D> selector) throws LookupException {
    List<? extends SelectionResult<D>> result;
    if(selector.usesSelectionName()) {
      List<Declaration> list = null;
			String selectionName = selector.selectionName(this);
			synchronized (this) {
				initDirectCache();
				list = searchDeclarations(selectionName);
			}
			if (list == null) {
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

  @Override
  public NamespaceAlias alias(String name) {
    return new NamespaceAlias(name,this);
  }

}
