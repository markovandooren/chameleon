package be.kuleuven.cs.distrinet.chameleon.core.namespacedeclaration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.google.common.collect.ImmutableList;

import be.kuleuven.cs.distrinet.chameleon.core.declaration.Declaration;
import be.kuleuven.cs.distrinet.chameleon.core.lookup.DeclarationSelector;
import be.kuleuven.cs.distrinet.chameleon.core.lookup.LookupException;
import be.kuleuven.cs.distrinet.chameleon.core.lookup.SelectionResult;
import be.kuleuven.cs.distrinet.chameleon.core.reference.CrossReference;
import be.kuleuven.cs.distrinet.chameleon.core.validation.Valid;
import be.kuleuven.cs.distrinet.chameleon.core.validation.Verification;
import be.kuleuven.cs.distrinet.chameleon.oo.type.Type;
import be.kuleuven.cs.distrinet.chameleon.test.CrossReferenceTest;
import be.kuleuven.cs.distrinet.chameleon.util.association.Single;

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
  protected DirectImport<D> cloneSelf() {
    return new DirectImport<D>(null,kind());
  }

	@Override
	public List<Declaration> demandImports() {
		return Collections.EMPTY_LIST;
	}

	@Override
	public List<Declaration> directImports() throws LookupException {
		if(_cache == null) {
			_cache = (List)ImmutableList.of(importedElement());
		}
		return _cache;
	}

	@Override
	public <D extends Declaration> List<? extends SelectionResult> demandImports(DeclarationSelector<D> selector) throws LookupException {
		return Collections.EMPTY_LIST;
	}

	@Override
	public <D extends Declaration> List<? extends SelectionResult> directImports(DeclarationSelector<D> selector) throws LookupException {
		if(_cache == null) {
			_cache = (List)ImmutableList.of(importedElement());
		}
		return selector.selection(_cache);
	}

	@Override
	public synchronized void flushLocalCache() {
		_cache = null;
	}
	
	private List<Declaration> _cache;
	
	@Override
	public Verification verifySelf() {
		return Valid.create();
	}
}
