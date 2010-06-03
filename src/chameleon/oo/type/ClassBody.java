package chameleon.oo.type;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import org.rejuse.association.OrderedMultiAssociation;

import chameleon.core.Config;
import chameleon.core.declaration.Declaration;
import chameleon.core.declaration.DeclarationContainer;
import chameleon.core.element.Element;
import chameleon.core.lookup.DeclarationSelector;
import chameleon.core.lookup.LookupException;
import chameleon.core.lookup.LookupStrategy;
import chameleon.core.member.Member;
import chameleon.core.namespace.NamespaceElement;
import chameleon.core.namespace.NamespaceElementImpl;
import chameleon.core.validation.Valid;
import chameleon.core.validation.VerificationResult;

public class ClassBody extends NamespaceElementImpl<ClassBody,NamespaceElement> implements NamespaceElement<ClassBody, NamespaceElement>, DeclarationContainer<ClassBody, NamespaceElement> {

	@Override
	public ClassBody clone() {
		ClassBody result = new ClassBody();
		for(TypeElement element: elements()) {
			result.add(element.clone());
		}
		return result;
	}

	public void addAll(Collection<? extends TypeElement> elements) {
		for(TypeElement element:elements) {
			add(element);
		}
	}
	
	private OrderedMultiAssociation<ClassBody, TypeElement> _elements = new OrderedMultiAssociation<ClassBody, TypeElement>(this);

	public void add(TypeElement element) {
	  if(element != null) {
	    _elements.add(element.parentLink());
	  }
	}
	
	public void remove(TypeElement element) {
	  if(element != null) {
	    _elements.remove(element.parentLink());
	  }
	}
	
	public List<TypeElement> elements() {
		return _elements.getOtherEnds();
	}
	
	public  <D extends Member> List<D> members(DeclarationSelector<D> selector) throws LookupException {
		if(selector.usesSelectionName()) {
			List<? extends Declaration> list = null;
			if(Config.cacheDeclarations()) {
				ensureLocalCache();
				list = _declarationCache.get(selector.selectionName(this));
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
	
	private void ensureLocalCache() throws LookupException {
		if(_declarationCache == null) {
		  List<Member> members = members();
		  _declarationCache = new HashMap<String, List<Declaration>>();
		  for(Member member: members) {
		  	String name = member.signature().name();
				List<Declaration> list = _declarationCache.get(name);
		  	boolean newList = false;
		  	if(list == null) {
		  		list = new ArrayList<Declaration>();
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
	
	@Override
	public void flushLocalCache() {
		_declarationCache = null;
	}
	
	private HashMap<String, List<Declaration>> _declarationCache;

	public List<Member> members() throws LookupException {
		List<Member> result = new ArrayList<Member>();
    for(TypeElement m: _elements.getOtherEnds()) {
      result.addAll(m.getIntroducedMembers());
    }
    return result;
	}

	public List<? extends Element> children() {
		return new ArrayList<Element>(elements());
	}

//	public LookupStrategy lexicalLookupStrategy(Element element) {
//		return language().lookupFactory().createLexicalLookupStrategy(localContext(), this);
//	}
	
//	public LookupStrategy localContext() {
//		return language().lookupFactory().createTargetLookupStrategy(this);
//	}

	public List<Member> declarations() throws LookupException {
		return members();
	}

	public <D extends Declaration> List<D> declarations(DeclarationSelector<D> selector) throws LookupException {
		return selector.selection(declarations());
	}

	public void replace(TypeElement oldElement, TypeElement newElement) {
		_elements.replace(oldElement.parentLink(), newElement.parentLink());
	}

	@Override
	public VerificationResult verifySelf() {
		return Valid.create();
	}
	
  public LookupStrategy lexicalLookupStrategy(Element element) throws LookupException {
  	// WE DO NOT USE 'this' such that a subclass of regular type can override the lookup for specific members.
  	return parent().lexicalLookupStrategy(element);
  }

}
