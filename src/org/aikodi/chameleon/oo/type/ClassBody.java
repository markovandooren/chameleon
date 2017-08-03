package org.aikodi.chameleon.oo.type;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import org.aikodi.chameleon.core.Config;
import org.aikodi.chameleon.core.declaration.Declaration;
import org.aikodi.chameleon.core.declaration.DeclarationContainer;
import org.aikodi.chameleon.core.declaration.Declarator;
import org.aikodi.chameleon.core.element.Element;
import org.aikodi.chameleon.core.element.ElementImpl;
import org.aikodi.chameleon.core.lookup.DeclarationSelector;
import org.aikodi.chameleon.core.lookup.LookupContext;
import org.aikodi.chameleon.core.lookup.LookupException;
import org.aikodi.chameleon.core.lookup.SelectionResult;
import org.aikodi.chameleon.core.validation.Valid;
import org.aikodi.chameleon.core.validation.Verification;
import org.aikodi.chameleon.util.Lists;
import org.aikodi.chameleon.util.association.Multi;
import org.aikodi.rejuse.association.Association;
import org.aikodi.rejuse.association.AssociationListener;
import org.aikodi.rejuse.collection.CollectionOperations;

import com.google.common.collect.ImmutableList;

public class ClassBody extends ElementImpl implements DeclarationContainer {

	public ClassBody() {
		parentLink().addListener(new AssociationListener<Element>() {

			@Override
			public void notifyElementAdded(Element element) {
				_elements.addListener(_listener);
			}

			@Override
			public void notifyElementRemoved(Element element) {
				_elements.removeListener(_listener);
			}

			@Override
			public void notifyElementReplaced(Element oldElement, Element newElement) {
			}
		});
	}
	
	@Override
	public ClassBody cloneSelf() {
		return new ClassBody();
	}

	public void addAll(Collection<? extends Declarator> elements) {
		elements.forEach(e -> add(e));
	}
	
	private Multi<Declarator> _elements = new Multi<Declarator>(this);
	
		
	private AssociationListener<Declarator> _listener = new AssociationListener<Declarator>() {

		@Override
		public void notifyElementAdded(Declarator element) {
			Type parent = lexical().nearestAncestor(Type.class);
			if(parent != null) {
			  parent.reactOnDescendantAdded(element);
			}
		}

		@Override
		public void notifyElementRemoved(Declarator element) {
      Type parent = lexical().nearestAncestor(Type.class);
			if(parent != null) {
			  parent.reactOnDescendantRemoved(element);
			}
		}

		@Override
		public void notifyElementReplaced(Declarator oldElement, Declarator newElement) {
      Type parent = lexical().nearestAncestor(Type.class);
			if(parent != null) {
			  parent.reactOnDescendantReplaced(oldElement, newElement);
			}
		}
		
	};

	public void add(Declarator element) {
	  add(_elements,element);
	}
	
	public void clear() {
		_elements.clear();
	}
	
	public void remove(Declarator element) {
	  remove(_elements,element);
	}
	
	public List<Declarator> elements() {
		return _elements.getOtherEnds();
	}
	
	public <D extends Declaration> List<? extends SelectionResult> members(DeclarationSelector<D> selector) throws LookupException {
		if(selector.usesSelectionName()) {
			List<? extends Declaration> list = null;
			if(Config.cacheDeclarations()) {
				ensureLocalCache();
				synchronized(this) {
				  list = _declarationCache.get(selector.selectionName(this));
				}
			} else {
				list = members();
			}
			if(list == null) {
				list = Collections.EMPTY_LIST;
			}
			return selector.selection(Collections.unmodifiableList(list));
		} else {
			return selector.selection(declarations());
		}
	}
	
	public <D extends Declaration> List<D> members(Class<D> kind) throws LookupException {
    List<D> result = (List)members();
    CollectionOperations.filter(result, d -> kind.isInstance(d));
    return result;
	}
	
	protected List<Declaration> declarations(String selectionName) throws LookupException {
		synchronized (this) {
			ensureLocalCache();
		}
		List<Declaration> result = cachedDeclarations(selectionName);
		if(result == null) {
			result = ImmutableList.of();
		}
		return result;
	}
	private synchronized void ensureLocalCache() throws LookupException {
		if(! _completelyCached) {
			_completelyCached = true;
			List<Declaration> members = members();
		  _declarationCache = new HashMap<String, List<Declaration>>();
		  for(Declaration member: members) {
		  	String name = member.name();
				List<Declaration> list = cachedDeclarations(name);
		  	boolean newList = false;
		  	if(list == null) {
		  		list = Lists.create();
		  		newList = true;
		  	}
		  	// list != null
		  	list.add(member);
		  	if(newList) {
		  		_declarationCache.put(name, list);
		  	}
		  }
		}
	}
	
	private boolean _completelyCached = false;

	protected synchronized List<Declaration> cachedDeclarations(String name) {
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
	
	@Override
	public synchronized void flushLocalCache() {
		_declarationCache = null;
		_completelyCached = false;
	}
	
	private HashMap<String, List<Declaration>> _declarationCache;
	
	public List<Declaration> members() throws LookupException {
		List<Declaration> result = Lists.create();
    for(Declarator m: elements()) {
      result.addAll(m.declaredDeclarations());
    }
    return result;
	}

//	public LookupStrategy lookupContext(Element element) {
//		return language().lookupFactory().createLexicalLookupStrategy(localContext(), this);
//	}
	
//	public LookupStrategy localContext() {
//		return language().lookupFactory().createTargetLookupStrategy(this);
//	}

	@Override
   public List<Declaration> declarations() throws LookupException {
		return members();
	}

	@Override
   public <D extends Declaration> List<? extends SelectionResult<D>> declarations(DeclarationSelector<D> selector) throws LookupException {
		return selector.selection(declarations());
	}

	public void replace(Declarator oldElement, Declarator newElement) {
		_elements.replace((Association)oldElement.parentLink(), (Association)newElement.parentLink());
	}

	@Override
	public Verification verifySelf() {
		return Valid.create();
	}
	
	/**
	 * This method passes 'element' to the lexicalLookupStrategy method of the parent. It does this
	 * such that the parent can override the lookup for specific members.
	 */
  @Override
public LookupContext lookupContext(Element element) throws LookupException {
  	// WE DO NOT USE 'this' such that a subclass of regular type can override the lookup for specific members.
  	return parent().lookupContext(element);
  }

	@Override
   public List<? extends Declaration> locallyDeclaredDeclarations() throws LookupException {
		return declarations();
	}

	@Override
	public LookupContext localContext() throws LookupException {
		return lexical().nearestAncestor(Type.class).localContext();
	}

}
