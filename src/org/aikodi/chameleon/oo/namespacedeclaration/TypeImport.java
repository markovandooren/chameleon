package org.aikodi.chameleon.oo.namespacedeclaration;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.aikodi.chameleon.core.declaration.Declaration;
import org.aikodi.chameleon.core.lookup.DeclarationSelector;
import org.aikodi.chameleon.core.lookup.LookupException;
import org.aikodi.chameleon.core.lookup.SelectionResult;
import org.aikodi.chameleon.core.namespacedeclaration.Import;
import org.aikodi.chameleon.oo.type.Type;
import org.aikodi.chameleon.oo.type.TypeReference;
import org.aikodi.chameleon.util.association.Single;

import com.google.common.collect.ImmutableList;

/**
 * @author Marko van Dooren
 */
public class TypeImport extends Import {

  public TypeImport(TypeReference ref) {
    setTypeReference(ref);
  }
  
	private Single<TypeReference> _typeReference = new Single<TypeReference>(this);

  public TypeReference getTypeReference() {
    return _typeReference.getOtherEnd();
  }

  public void setTypeReference(TypeReference reference) {
  	set(_typeReference,reference);
  }
  
  public Type type() throws LookupException {
  	Type result = getTypeReference().getType().baseType();
  	if(result != null) {
      return result;
  	} else {
  		throw new LookupException("Lookup of type import reference returns null",getTypeReference());
  	}
  }

  @Override
  protected TypeImport cloneSelf() {
    return new TypeImport(null);
  }

	@Override
	public List<Declaration> demandImports() {
		return Collections.EMPTY_LIST;
	}

	@Override
	public List<Declaration> directImports() throws LookupException {
		//lookupLogger().debug("NamespacePart of " + nearestAncestor(NamespacePart.class).getFullyQualifiedName()+"Looking up direct import: "+getTypeReference().signature());
		List<Declaration> result = new ArrayList<Declaration>(1);
		result.add(type());
		return result;
	}

	@Override
	public <D extends Declaration> List<? extends SelectionResult> demandImports(DeclarationSelector<D> selector) throws LookupException {
		return Collections.EMPTY_LIST;
	}

	@Override
	public <D extends Declaration> List<? extends SelectionResult> directImports(DeclarationSelector<D> selector) throws LookupException {
		if(_cache == null) {
			_cache = ImmutableList.of(type());
		}
		return selector.selection(_cache);
	}

	@Override
	public synchronized void flushLocalCache() {
		_cache = null;
	}
	
	private List<Type> _cache;
	
	@Override
	public String toString() {
		return "type import of " + getTypeReference() == null ? null : getTypeReference().toString();
	}

}
