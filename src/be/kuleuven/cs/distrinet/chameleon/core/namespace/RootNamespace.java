package be.kuleuven.cs.distrinet.chameleon.core.namespace;


import be.kuleuven.cs.distrinet.chameleon.core.element.Element;
import be.kuleuven.cs.distrinet.chameleon.core.language.Language;
import be.kuleuven.cs.distrinet.chameleon.core.lookup.LookupContext;
import be.kuleuven.cs.distrinet.chameleon.core.lookup.LookupException;
import be.kuleuven.cs.distrinet.chameleon.core.validation.BasicProblem;
import be.kuleuven.cs.distrinet.chameleon.core.validation.Valid;
import be.kuleuven.cs.distrinet.chameleon.core.validation.Verification;
import be.kuleuven.cs.distrinet.chameleon.exception.ChameleonProgrammerException;
import be.kuleuven.cs.distrinet.chameleon.oo.language.ObjectOrientedLanguage;
import be.kuleuven.cs.distrinet.chameleon.oo.type.Type;
import be.kuleuven.cs.distrinet.chameleon.workspace.View;
import be.kuleuven.cs.distrinet.rejuse.association.SingleAssociation;

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
	

  public RootNamespace(NamespaceFactory factory) {
  	this(null,factory);
  }
/**
   * @param name
   */
  protected RootNamespace(View view,NamespaceFactory factory) {
    super("");
    setNamespaceFactory(factory);
    setView(view); 
//    NamespacePart primitiveNP = new NamespacePart(this);
//    _primitiveNamespacePart.connectTo(primitiveNP.getNamespaceLink());
  }
  

	@Override
   protected RootNamespace cloneSelf() {
		return new RootNamespace(null, namespaceFactory());
	}

  public void setView(View view) {
  	if(view != null) {
  		_view.connectTo(view.namespaceLink());
  	}
  }

  @Override
public Language language() {
    return view().language();
  }
  
//  public Project project() {
//    return view().project();
//  }
  
  @Override
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
	public Verification verifySelf() {
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

	@Override
   public LookupContext lexicalContext() throws LookupException {
		return targetContext();
	}

}
