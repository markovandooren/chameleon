package chameleon.core.namespace;


import be.kuleuven.cs.distrinet.rejuse.association.SingleAssociation;
import chameleon.core.declaration.SimpleNameSignature;
import chameleon.core.element.Element;
import chameleon.core.language.Language;
import chameleon.core.lookup.LookupException;
import chameleon.core.lookup.LookupStrategy;
import chameleon.core.validation.BasicProblem;
import chameleon.core.validation.Valid;
import chameleon.core.validation.VerificationResult;
import chameleon.exception.ChameleonProgrammerException;
import chameleon.oo.language.ObjectOrientedLanguage;
import chameleon.oo.type.Type;
import chameleon.workspace.Project;
import chameleon.workspace.View;

public class RootNamespace extends RegularNamespace {
  static {
    excludeFieldName(RootNamespace.class,"_language");
  }
  
	private NamespaceFactory _namespaceFactory;
	
	protected NamespaceFactory namespaceFactory() {
		return _namespaceFactory;
	}
	
	private void setNamespaceFactory(NamespaceFactory factory) {
		if(factory == null) {
			throw new ChameleonProgrammerException("Namespace factory cannot be null.");
		}
		_namespaceFactory = factory;
	}
	
	@Override
	public Namespace createSubNamespace(String name) {
		Namespace result = namespaceFactory().create(name);
		addNamespace(result);
		return result;
	}

// @FIXME
// Create Model
	
  /**
   * @param name
   */
  protected RootNamespace(SimpleNameSignature sig, View view,NamespaceFactory factory) {
    super(sig);
    setNamespaceFactory(factory);
    setView(view); 
//    NamespacePart primitiveNP = new NamespacePart(this);
//    _primitiveNamespacePart.connectTo(primitiveNP.getNamespaceLink());
  }
  
  /**
   * @param name
   */
  public RootNamespace(NamespaceFactory factory) {
  	this(new SimpleNameSignature(""),null, factory);
  }


	protected RootNamespace cloneThis() {
		return new RootNamespace(signature().clone(),null, namespaceFactory());
	}

  public void setView(View view) {
  	if(view != null) {
  		_view.connectTo(view.namespaceLink());
  	}
  }

  public Language language() {
    return view().language();
  }
  
//  public Project project() {
//    return view().project();
//  }
  
  public View view() {
  	return _view.getOtherEnd();
  }
  
  public SingleAssociation<RootNamespace,View> projectLink() {
  	return _view;
  }
	    
  private SingleAssociation<RootNamespace,View> _view = new SingleAssociation<RootNamespace,View>(this);

  public Type getNullType() {
	  return this.language(ObjectOrientedLanguage.class).getNullType(view().namespace());
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
