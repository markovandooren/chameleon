package chameleon.core.namespace;


import org.rejuse.association.SingleAssociation;

import chameleon.core.compilationunit.CompilationUnit;
import chameleon.core.declaration.SimpleNameSignature;
import chameleon.core.element.Element;
import chameleon.core.language.Language;
import chameleon.core.language.ObjectOrientedLanguage;
import chameleon.core.namespacepart.NamespacePart;
import chameleon.core.type.Type;
import chameleon.core.validation.BasicProblem;
import chameleon.core.validation.Valid;
import chameleon.core.validation.VerificationResult;

public class RootNamespace extends RegularNamespace {
// @FIXME
// Create Model
	
  /**
   * @param name
   */
  public RootNamespace(SimpleNameSignature sig, Language language) {
    super(sig);
    _language.connectTo(language.defaultNamespaceLink());
    
//    NamespacePart primitiveNP = new NamespacePart(this);
//    _primitiveNamespacePart.connectTo(primitiveNP.getNamespaceLink());
  }

  public void setNullType(){
	  NamespacePart pp = new NamespacePart(this);
	  pp.add(language(ObjectOrientedLanguage.class).getNullType());
	  new CompilationUnit(pp);
  }
  
  private SingleAssociation<RootNamespace,Language> _language = new SingleAssociation<RootNamespace,Language>(this);

  public Type getNullType() {
	  return this.language(ObjectOrientedLanguage.class).getNullType();
  }
  
  public Language language() {
    return _language.getOtherEnd();
  }
  
  public SingleAssociation<RootNamespace,Language> languageLink() {
  	return _language;
  }
	  
  
	@Override
	public VerificationResult verifySelf() {
		if(parent() == null) {
		  return Valid.create();
		} else {
			return new RootNamespaceHasParent(this);
		}
	}
	
	public static class RootNamespaceHasParent extends BasicProblem {

		public RootNamespaceHasParent(Element element) {
			super(element, "The root namespace should not have a parent.");
		}
		
	}

//	  /**
//	   * PRIMITIVE NAMESPACE PART
//	   * 
//	   * In this namespace part a UsingAlias will be present which
//	   * connects the shortcuts for all primitive types (except void) and
//	   * their class in the System namespace
//	   * for example:
//	   * using int = System.int32
//	   * 
//	   * Normally usings are only ment for the type declared in the NamespacePart
//	   * but these usings should be there for every type.
//	   * Therefor the RootNamespace overrides the getType() method.
//	   * 
//	   * !!! This is a logical expansion to fit everything in the metamodel.
//	   * !!! The _primitiveNamespacePart should never be written down with for
//	   * !!! example the CSharpCodeWriter or an editor.
//	   */
//	  private Reference _primitiveNamespacePart = new Reference(this);
//	  
//	  public NamespacePart getPrimitiveNamespacePart(){
//		  return (NamespacePart)_primitiveNamespacePart.getOtherEnd();
//	  }
	  
}
