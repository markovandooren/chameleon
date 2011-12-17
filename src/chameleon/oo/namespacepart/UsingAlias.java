package chameleon.oo.namespacepart;

import java.util.ArrayList;
import java.util.List;

import org.rejuse.association.SingleAssociation;

import chameleon.core.declaration.Declaration;
import chameleon.core.declaration.SimpleNameSignature;
import chameleon.core.element.Element;
import chameleon.core.lookup.DeclarationSelector;
import chameleon.core.lookup.LookupException;
import chameleon.core.namespace.Namespace;
import chameleon.core.namespace.NamespaceOrType;
import chameleon.core.namespace.NamespaceOrTypeReference;
import chameleon.core.namespacepart.Import;
import chameleon.core.validation.BasicProblem;
import chameleon.core.validation.Valid;
import chameleon.core.validation.VerificationResult;
import chameleon.oo.type.Type;
import chameleon.util.Util;

/**
 * @author Tim Laeremans
 * @author Marko van Dooren
 *
 * A UsingAlias in C# contains an identifier and a package-or-type-name
 * that second part is equal to a DemandImport which can have as initializing parameter
 * a Type as well as a Namespace
 */
public class UsingAlias extends Import<UsingAlias> {

	//TODO use ElementReference<?, ?, ? extends Namespace>
	
	public UsingAlias(String identifier, NamespaceOrTypeReference ref) {
		setNamespaceOrTypeReference(ref);
		setIdentifier(identifier);
	}

	public List<Element> children() {
		return Util.createNonNullList(getNamespaceOrTypeReference());
	}

	private String _identifier;
	 
	/*@
	  @ public behavior
	  @
	  @ post \result != null;
	  @*/
	public String getIdentifier() {
		return _identifier;
	}
	
	/**
	 * @param identifier The Identifier to set.
	 */
	public void setIdentifier(String identifier) {
		this._identifier = identifier;
	}

	private SingleAssociation<UsingAlias,NamespaceOrTypeReference> _packageOrType = new SingleAssociation<UsingAlias,NamespaceOrTypeReference>(this);

  
  public NamespaceOrTypeReference getNamespaceOrTypeReference() {
    return _packageOrType.getOtherEnd();
  }
  
  public void setNamespaceOrTypeReference(NamespaceOrTypeReference ref) {
  	if(ref != null) {
  		_packageOrType.connectTo(ref.parentLink());
  	}
  	else {
  		_packageOrType.connectTo(null);
  	}
  }

//	public Type getType(String name) throws MetamodelException {
//		NamespaceOrType self = getElement();
//
//		if (self instanceof Type) {
//			if (((Type) self).getName().equals(name))
//				//the using alias points to a type
//				return (Type) self;
//		}
//		//otherwise self the using-alias points to a namspace
//		return self.getType(name);
//
//	}

	public NamespaceOrType getElement() throws LookupException {
		return getNamespaceOrTypeReference().getNamespaceOrType();
	}

  @Override
  public UsingAlias clone() {
    return new UsingAlias(getIdentifier(),getNamespaceOrTypeReference().clone());
  }

	@Override
	public List<Declaration> demandImports() throws LookupException {
		return new ArrayList<Declaration>();
	}

	@Override
	public List<Declaration> directImports() throws LookupException {
		//@FIXME bad design: instanceof 
		//@FIXME why are these aliased?
		NamespaceOrType nst = getNamespaceOrTypeReference().getNamespaceOrType(); 
		List<Declaration> result = new ArrayList<Declaration>();
    if(nst instanceof Type) {
      result.add(((Type) nst).alias(new SimpleNameSignature(getIdentifier())));
    } else if (nst instanceof Namespace) {
    	result.add(((Namespace) nst).alias(new SimpleNameSignature(getIdentifier())));    	
    }
    return result;
	}

	@Override
	public <D extends Declaration> List<D> demandImports(DeclarationSelector<D> selector) throws LookupException {
		return new ArrayList<D>();
	}

	@Override
	public <D extends Declaration> List<D> directImports(DeclarationSelector<D> selector) throws LookupException {
		return selector.selection(directImports());
	}

	@Override
	public VerificationResult verifySelf() {
		if(_identifier != null) {
			return new MissingAliasName(this);
		} else {
			return Valid.create();
		}
	}

	public static class MissingAliasName extends BasicProblem {

		public MissingAliasName(Element element) {
			super(element, "This using alias has no name.");
		}
		
	}
}
