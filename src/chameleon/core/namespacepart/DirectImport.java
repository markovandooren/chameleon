package chameleon.core.namespacepart;

import java.util.ArrayList;
import java.util.List;

import chameleon.core.declaration.Declaration;
import chameleon.core.lookup.DeclarationSelector;
import chameleon.core.lookup.LookupException;
import chameleon.core.reference.CrossReference;
import chameleon.core.validation.Valid;
import chameleon.core.validation.VerificationResult;
import chameleon.util.association.Single;

/**
 * An element that represent an import of a specific declaration. This can be for
 * example a direct type import in Java.
 * 
 * @author Marko van Dooren
 */
public class DirectImport<D extends Declaration> extends Import {

	/**
	 * Create a new direct import with the given cross reference and
	 * the class that represents the kind of the imported declaration.
	 * @param ref
	 *        A cross reference that points to the imported declaration.
	 * @param kind
	 *        A class object that represents the type of the imported declaration.
	 */
  public DirectImport(CrossReference<D> ref, Class<D> kind) {
    setCrossReference(ref);
  }
  
  /**
   * Return the kind of declaration that is imported by this type import.
   * @return
   */
  public Class<D> kind() {
  	return _kind;
  }
  
  private Class<D> _kind;
  
	private Single<CrossReference<D>> _typeReference = new Single<CrossReference<D>>(this);
  
  public CrossReference<D> crossReference() {
    return _typeReference.getOtherEnd();
  }

  public void setCrossReference(CrossReference<D> reference) {
  	set(_typeReference, reference);
  }
  
  public D importedElement() throws LookupException {
  	D result = crossReference().getElement();
  	if(result != null) {
      return result;
  	} else {
  		throw new LookupException("Lookup of type import reference returns null",crossReference());
  	}
  }

  @Override
  public DirectImport<D> clone() {
    return new DirectImport<D>((CrossReference<D>)crossReference().clone(),kind());
  }

	@Override
	public List<Declaration> demandImports() {
		return new ArrayList<Declaration>();
	}

	@Override
	public List<Declaration> directImports() throws LookupException {
		List<Declaration> result = new ArrayList<Declaration>();
		result.add(importedElement());
		return result;
	}

	@Override
	public <D extends Declaration> List<D> demandImports(DeclarationSelector<D> selector) throws LookupException {
		return new ArrayList<D>();
	}

	@Override
	public <D extends Declaration> List<D> directImports(DeclarationSelector<D> selector) throws LookupException {
		List<Declaration> tmp = new ArrayList<Declaration>();
		tmp.add(importedElement());
		return selector.selection(tmp);
	}

	@Override
	public VerificationResult verifySelf() {
		return Valid.create();
	}
}
