package be.kuleuven.cs.distrinet.chameleon.oo.namespacedeclaration;

import java.util.ArrayList;
import java.util.List;

import be.kuleuven.cs.distrinet.chameleon.core.declaration.Declaration;
import be.kuleuven.cs.distrinet.chameleon.core.declaration.SimpleNameSignature;
import be.kuleuven.cs.distrinet.chameleon.core.element.Element;
import be.kuleuven.cs.distrinet.chameleon.core.lookup.DeclarationSelector;
import be.kuleuven.cs.distrinet.chameleon.core.lookup.LookupException;
import be.kuleuven.cs.distrinet.chameleon.core.namespace.Namespace;
import be.kuleuven.cs.distrinet.chameleon.core.namespacedeclaration.Import;
import be.kuleuven.cs.distrinet.chameleon.core.reference.CrossReference;
import be.kuleuven.cs.distrinet.chameleon.core.validation.BasicProblem;
import be.kuleuven.cs.distrinet.chameleon.core.validation.Valid;
import be.kuleuven.cs.distrinet.chameleon.core.validation.VerificationResult;
import be.kuleuven.cs.distrinet.chameleon.oo.type.Type;
import be.kuleuven.cs.distrinet.chameleon.util.association.Single;

/**
 * @author Tim Laeremans
 * @author Marko van Dooren
 *
 * A UsingAlias in C# contains an identifier and a package-or-type-name
 * that second part is equal to a DemandImport which can have as initializing parameter
 * a Type as well as a Namespace
 */
public class UsingAlias extends Import {

	//TODO use ElementReference<?, ?, ? extends Namespace>
	
	public UsingAlias(String identifier, CrossReference ref) {
		setNamespaceOrTypeReference(ref);
		setIdentifier(identifier);
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

	private Single<CrossReference> _packageOrType = new Single<CrossReference>(this);

  
  public CrossReference getCrossReference() {
    return _packageOrType.getOtherEnd();
  }
  
  public void setNamespaceOrTypeReference(CrossReference ref) {
  	set(_packageOrType,ref);
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

	public Declaration getElement() throws LookupException {
		return getCrossReference().getElement();
	}

  @Override
  public UsingAlias clone() {
    return new UsingAlias(getIdentifier(),(CrossReference) getCrossReference().clone());
  }

	@Override
	public List<Declaration> demandImports() throws LookupException {
		return new ArrayList<Declaration>();
	}

	@Override
	public List<Declaration> directImports() throws LookupException {
		//@FIXME bad design: instanceof 
		//@FIXME why are these aliased?
		Declaration nst = getCrossReference().getElement(); 
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
