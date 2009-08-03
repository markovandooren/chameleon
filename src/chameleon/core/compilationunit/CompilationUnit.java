package chameleon.core.compilationunit;

import java.util.List;

import org.rejuse.association.Reference;

import chameleon.core.element.ChameleonProgrammerException;
import chameleon.core.element.Element;
import chameleon.core.element.ElementImpl;
import chameleon.core.language.Language;
import chameleon.core.lookup.LookupException;
import chameleon.core.lookup.LookupStrategy;
import chameleon.core.namespacepart.NamespacePart;
import chameleon.util.Util;


/**
 * A compilation unit basically represents a file in which elements of the program/model are defined.
 * 
 * @author Marko van Dooren
 */
public class CompilationUnit extends ElementImpl<CompilationUnit,Element> {

  /**
   * Create a new compilation unit with the given namespace part.
   * @param namespacePart
   */
	public CompilationUnit(NamespacePart defaultNamespacePart) {
    setDefaultNamespacePart(defaultNamespacePart);
	}
	
	
	/************
	 * Children *
	 ************/

	public List<? extends Element> children() {
		return getNamespaceParts();
	}


//	public Type getVisibleType() {
//		List types = getTypes();
//		new PrimitiveTotalPredicate() {
//			public boolean eval(Object o) {
//				return !((Type)o).is(new Private()) &&
//				!((Type)o).is(new Protected());
//			}
//		}.filter(types);
//		if(types.size() > 0) {
//			return (Type)types.iterator().next();
//		}
//		else {
//			return null;
//		}
//	}

//	/**
//	 * @param name
//	 * @return
//	 */
//	public Type getImportedType(final String name) throws MetamodelException {
//		List allTypes = getImportedTypes();
//		new PrimitiveTotalPredicate() {
//			public boolean eval(Object o) {
//				return ((Type)o).getName().equals(name);
//			}
//		}.filter(allTypes);
//		if(allTypes.isEmpty()) {
//			return null;
//		}
//		else {
//			return (Type)allTypes.iterator().next();
//		}
//	}

//	public AccessibilityDomain getTypeAccessibilityDomain() {
//		return new All();
//	}

	private Reference<CompilationUnit,NamespacePart> _defaultNamespacePart = new Reference(this);

	public NamespacePart namespacePart(){
		return _defaultNamespacePart.getOtherEnd();
	}
	
	/**
	 * Disconnect this compilation unit from the model by recursively
	 * disconnecting all namespace declarations.
	 */
	public void disconnect() {
		namespacePart().disconnect();
	}
	
	public void setDefaultNamespacePart(NamespacePart nsp) {
		if(nsp == null) {
			_defaultNamespacePart.connectTo(null);
		}
		else {
			_defaultNamespacePart.connectTo(nsp.parentLink());
		}
	}

  /**
   * NAMESPACEPARTS
   */
	public List<NamespacePart> getNamespaceParts() {
		return Util.createNonNullList(namespacePart());
	}

	public LookupStrategy lexicalLookupStrategy(Element child) throws LookupException {
		throw new ChameleonProgrammerException("A compilation unit should not be involved in the lookup");
	}


	public Language language() {
	  NamespacePart defaultNamespace = namespacePart();
	  if(defaultNamespace != null) {
		  return defaultNamespace.language();
	  } else {
	    return null;
	  }
	}


  @Override
  public CompilationUnit clone() {
    CompilationUnit result = new CompilationUnit(namespacePart().clone());
    return result;
  }

 }
