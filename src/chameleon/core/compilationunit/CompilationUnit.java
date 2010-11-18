package chameleon.core.compilationunit;

import java.util.Iterator;
import java.util.List;

import org.rejuse.association.OrderedMultiAssociation;

import chameleon.core.element.Element;
import chameleon.core.element.ElementImpl;
import chameleon.core.language.Language;
import chameleon.core.lookup.LookupException;
import chameleon.core.lookup.LookupStrategy;
import chameleon.core.namespace.Namespace;
import chameleon.core.namespacepart.NamespacePart;
import chameleon.core.validation.Valid;
import chameleon.core.validation.VerificationResult;
import chameleon.exception.ChameleonProgrammerException;

/**
 * A compilation unit represents a file in which elements of the program/model are defined.
 * 
 * A compilation unit does not directly contain the source code elements. It contains the namespace parts that contain
 * the source code. 
 * 
 * @author Marko van Dooren
 */
public class CompilationUnit extends ElementImpl<CompilationUnit,Element> {

	public CompilationUnit() {
		
	}
	
  /**
   * Create a new compilation unit with the given namespace part.
   * @param namespacePart
   */
	public CompilationUnit(NamespacePart defaultNamespacePart) {
    add(defaultNamespacePart);
	}
	
	
	/************
	 * Children *
	 ************/

	public List<? extends Element> children() {
		return namespaceParts();
	}

	/**
	 * NAMESPACEPARTS
	 */
	public List<NamespacePart> namespaceParts() {
		return _subNamespaceParts.getOtherEnds();
	}
	
	/**
	 * Indices start at 1.
	 * @param index
	 * @return
	 */
	public NamespacePart namespacePart(int index) {
		return _subNamespaceParts.elementAt(index);
	}

	public void add(NamespacePart pp) {
		_subNamespaceParts.add(pp.parentLink());
	}

	public void remove(NamespacePart pp) {
		_subNamespaceParts.remove(pp.parentLink());
	}

	private OrderedMultiAssociation<CompilationUnit, NamespacePart> _subNamespaceParts = new OrderedMultiAssociation<CompilationUnit, NamespacePart>(this);


	public LookupStrategy lexicalLookupStrategy(Element child) throws LookupException {
		throw new ChameleonProgrammerException("A compilation unit should not be involved in the lookup");
	}


	public Language language() {
		List<NamespacePart> parts = namespaceParts();
		Language result = null;
		if(parts.size() > 0) {
	    NamespacePart firstNamespace = parts.get(0);
	    if(firstNamespace != null) {
		    result = firstNamespace.language();
	    }
		}
		return result;
	}


  @Override
  public CompilationUnit clone() {
    CompilationUnit result = new CompilationUnit();
    for(NamespacePart namespacePart: namespaceParts()) {
    	result.add(namespacePart.clone());
    }
    return result;
  }

	@Override
	public VerificationResult verifySelf() {
		return Valid.create();
	}

	public CompilationUnit cloneTo(Language targetLanguage) throws LookupException {
		CompilationUnit clone = clone();
		List<NamespacePart> originalNamespaceParts = namespaceParts();
		List<NamespacePart> newNamespaceParts = clone.namespaceParts();
		Iterator<NamespacePart> originalIterator = originalNamespaceParts.iterator();
		Iterator<NamespacePart> newIterator = newNamespaceParts.iterator();
		while(originalIterator.hasNext()) {
			NamespacePart originalNamespacePart = originalIterator.next();
			NamespacePart newNamespacePart = newIterator.next();
			Namespace originalNamespace = originalNamespacePart.namespace();
			String fqn = originalNamespace.getFullyQualifiedName();
			Namespace newNamespace = targetLanguage.defaultNamespace().getOrCreateNamespace(fqn);
			newNamespace.addNamespacePart(newNamespacePart);
		}
		return clone;
	}

 }
