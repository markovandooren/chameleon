package chameleon.core.namespace;


import org.rejuse.association.SingleAssociation;

import chameleon.core.declaration.SimpleNameSignature;
import chameleon.core.document.Document;
import chameleon.core.element.Element;
import chameleon.core.language.Language;
import chameleon.core.lookup.LookupException;
import chameleon.core.lookup.LookupStrategy;
import chameleon.core.namespacedeclaration.NamespaceDeclaration;
import chameleon.core.validation.BasicProblem;
import chameleon.core.validation.Valid;
import chameleon.core.validation.VerificationResult;
import chameleon.oo.language.ObjectOrientedLanguage;
import chameleon.oo.type.Type;

public class RootNamespace extends RegularNamespace {
  static {
    excludeFieldName(RootNamespace.class,"_language");
  }
  
// @FIXME
// Create Model
	
  /**
   * @param name
   */
  public RootNamespace(SimpleNameSignature sig, Language language) {
    super(sig);
    setLanguage(language); 
//    NamespacePart primitiveNP = new NamespacePart(this);
//    _primitiveNamespacePart.connectTo(primitiveNP.getNamespaceLink());
  }
  
  /**
   * @param name
   */
  public RootNamespace(SimpleNameSignature sig) {
  	this(sig,null);
  }

  	public void setLanguage(Language language) {
  	if(language != null) {
      _language.connectTo(language.defaultNamespaceLink());
  	}
  }
  
	protected RootNamespace cloneThis() {
		return new RootNamespace(signature().clone());
	}

  public void setNullType(){
	  NamespaceDeclaration pp = new NamespaceDeclaration(this);
	  pp.add(language(ObjectOrientedLanguage.class).getNullType());
	  new Document(pp);
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

	public LookupStrategy lexicalLookupStrategy() throws LookupException {
		return targetContext();
	}

}
