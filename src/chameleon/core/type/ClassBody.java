package chameleon.core.type;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.rejuse.association.OrderedReferenceSet;

import chameleon.core.compilationunit.CompilationUnit;
import chameleon.core.declaration.Declaration;
import chameleon.core.declaration.DeclarationContainer;
import chameleon.core.element.Element;
import chameleon.core.lookup.DeclarationSelector;
import chameleon.core.lookup.LookupException;
import chameleon.core.lookup.LookupStrategy;
import chameleon.core.member.Member;
import chameleon.core.namespace.Namespace;
import chameleon.core.namespace.NamespaceElement;
import chameleon.core.namespacepart.NamespaceElementImpl;
import chameleon.core.namespacepart.NamespacePart;

public class ClassBody extends NamespaceElementImpl<ClassBody,NamespaceElement> implements NamespaceElement<ClassBody, NamespaceElement>, DeclarationContainer<ClassBody,NamespaceElement> {

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
	
	private OrderedReferenceSet<ClassBody, TypeElement> _elements = new OrderedReferenceSet<ClassBody, TypeElement>(this);

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

	public List<Member> members() {
		List<Member> result = new ArrayList<Member>();
    for(TypeElement m: _elements.getOtherEnds()) {
      result.addAll(m.getIntroducedMembers());
    }
    return result;
	}

	public List<? extends Element> children() {
		return new ArrayList<Element>(members());
	}

	public LookupStrategy lexicalLookupStrategy(Element element) {
		return language().lookupFactory().createLexicalLookupStrategy(localContext(), this);
	}
	
	public LookupStrategy localContext() {
		return language().lookupFactory().createTargetLookupStrategy(this);
	}

	public List<Member> declarations() throws LookupException {
		return members();
	}

	public <D extends Declaration> List<D> declarations(DeclarationSelector<D> selector) throws LookupException {
		return selector.selection(declarations());
	}

	public void replace(TypeElement oldElement, TypeElement newElement) {
		_elements.replace(oldElement.parentLink(), newElement.parentLink());
	}
}
