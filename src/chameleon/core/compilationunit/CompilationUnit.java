package chameleon.core.compilationunit;

import java.util.List;

import org.rejuse.association.OrderedReferenceSet;

import chameleon.core.element.ChameleonProgrammerException;
import chameleon.core.element.Element;
import chameleon.core.element.ElementImpl;
import chameleon.core.language.Language;
import chameleon.core.lookup.LookupException;
import chameleon.core.lookup.LookupStrategy;
import chameleon.core.namespacepart.NamespacePart;


/**
 * A compilation unit basically represents a file in which elements of the program/model are defined.
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

//	/**
//	 * Disconnect this compilation unit from the model by recursively
//	 * disconnecting all namespace declarations.
//	 */
//	public void disconnect() {
//		for(NamespacePart namespacePart: namespaceParts()) {
//			namespacePart.disconnect();
//		}
//	}
	
	/**
	 * NAMESPACEPARTS
	 */
	public List<NamespacePart> namespaceParts() {
		return _subNamespaceParts.getOtherEnds();
	}

	public void add(NamespacePart pp) {
		_subNamespaceParts.add(pp.parentLink());
	}

	public void remove(NamespacePart pp) {
		_subNamespaceParts.remove(pp.parentLink());
	}

	private OrderedReferenceSet<CompilationUnit, NamespacePart> _subNamespaceParts = new OrderedReferenceSet<CompilationUnit, NamespacePart>(this);


	public LookupStrategy lexicalLookupStrategy(Element child) throws LookupException {
		throw new ChameleonProgrammerException("A compilation unit should not be involved in the lookup");
	}


	public Language language() {
	  NamespacePart firstNamespace = namespaceParts().get(0);
	  if(firstNamespace != null) {
		  return firstNamespace.language();
	  } else {
	    return null;
	  }
	}


  @Override
  public CompilationUnit clone() {
    CompilationUnit result = new CompilationUnit();
    for(NamespacePart namespacePart: namespaceParts()) {
    	result.add(namespacePart.clone());
    }
    return result;
  }

 }
