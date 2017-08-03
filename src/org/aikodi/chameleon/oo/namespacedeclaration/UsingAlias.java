package org.aikodi.chameleon.oo.namespacedeclaration;

import java.util.ArrayList;
import java.util.List;

import org.aikodi.chameleon.core.declaration.Declaration;
import org.aikodi.chameleon.core.element.Element;
import org.aikodi.chameleon.core.lookup.DeclarationSelector;
import org.aikodi.chameleon.core.lookup.LookupException;
import org.aikodi.chameleon.core.lookup.SelectionResult;
import org.aikodi.chameleon.core.namespace.Namespace;
import org.aikodi.chameleon.core.namespacedeclaration.Import;
import org.aikodi.chameleon.core.reference.CrossReference;
import org.aikodi.chameleon.core.validation.BasicProblem;
import org.aikodi.chameleon.core.validation.Valid;
import org.aikodi.chameleon.core.validation.Verification;
import org.aikodi.chameleon.oo.type.Type;
import org.aikodi.chameleon.util.association.Single;

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
	  if(identifier == null) {
	    throw new IllegalArgumentException("The given identifier cannot be null.");
	  }
		setNamespaceOrTypeReference(ref);
    this._identifier = identifier;
	}

	private final String _identifier;
	 
	/*@
	  @ public behavior
	  @
	  @ post \result != null;
	  @*/
	public String getIdentifier() {
		return _identifier;
	}
	
	private Single<CrossReference> _packageOrType = new Single<CrossReference>(this, "package or type");

  
  public CrossReference getCrossReference() {
    return _packageOrType.getOtherEnd();
  }
  
  public void setNamespaceOrTypeReference(CrossReference ref) {
  	set(_packageOrType,ref);
  }

	public Declaration getElement() throws LookupException {
		return getCrossReference().getElement();
	}

  @Override
  protected UsingAlias cloneSelf() {
    return new UsingAlias(getIdentifier(),null);
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
      result.add(((Type) nst).alias(getIdentifier()));
    } else if (nst instanceof Namespace) {
    	result.add(((Namespace) nst).alias(getIdentifier()));    	
    }
    return result;
	}

	@Override
	public <D extends Declaration> List<? extends SelectionResult> demandImports(DeclarationSelector<D> selector) throws LookupException {
		return new ArrayList<SelectionResult>();
	}

	@Override
	public <D extends Declaration> List<? extends SelectionResult> directImports(DeclarationSelector<D> selector) throws LookupException {
		return selector.selection(directImports());
	}

	@Override
	public Verification verifySelf() {
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
