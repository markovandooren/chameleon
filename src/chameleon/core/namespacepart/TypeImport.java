package chameleon.core.namespacepart;

import java.util.ArrayList;
import java.util.List;

import org.rejuse.association.SingleAssociation;

import chameleon.core.declaration.Declaration;
import chameleon.core.element.Element;
import chameleon.core.lookup.DeclarationSelector;
import chameleon.core.lookup.LookupException;
import chameleon.core.validation.Valid;
import chameleon.core.validation.VerificationResult;
import chameleon.oo.type.Type;
import chameleon.oo.type.TypeReference;
import chameleon.util.Util;

/**
 * @author Marko van Dooren
 */
public class TypeImport extends Import<TypeImport> {

  public TypeImport(TypeReference ref) {
    setTypeReference(ref);
  }
  
  public List<Element> children() {
    return Util.createNonNullList(getTypeReference());
  }

	private SingleAssociation<TypeImport, TypeReference> _typeReference = new SingleAssociation<TypeImport, TypeReference>(this);

  
  public TypeReference getTypeReference() {
    return (TypeReference)_typeReference.getOtherEnd();
  }

  public void setTypeReference(TypeReference reference) {
  	if(reference != null) {
  		_typeReference.connectTo(reference.parentLink());
  	}
  	else {
  		_typeReference.connectTo(null);
  	}
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
  public TypeImport clone() {
    return new TypeImport(getTypeReference().clone());
  }

	@Override
	public List<Declaration> demandImports() {
		return new ArrayList<Declaration>();
	}

	@Override
	public List<Declaration> directImports() throws LookupException {
		//lookupLogger().debug("NamespacePart of " + nearestAncestor(NamespacePart.class).getFullyQualifiedName()+"Looking up direct import: "+getTypeReference().signature());
		List<Declaration> result = new ArrayList<Declaration>();
		result.add(type());
		return result;
	}

	@Override
	public <D extends Declaration> List<D> demandImports(DeclarationSelector<D> selector) throws LookupException {
		return new ArrayList<D>();
	}

	@Override
	public <D extends Declaration> List<D> directImports(DeclarationSelector<D> selector) throws LookupException {
//		lookupLogger().debug("NamespacePart of " + nearestAncestor(NamespacePart.class).getFullyQualifiedName()+"Looking up direct import: "+getTypeReference().signature());
		List<Declaration> tmp = new ArrayList<Declaration>();
		tmp.add(type());
		return selector.selection(tmp);
//		D element = selector.selection(type());
//		if(element != null) {
//		  result.add(element);
//		}
//		return result;
	}

	@Override
	public VerificationResult verifySelf() {
		return Valid.create();
	}
  
//	@Override
//	public boolean importsSameAs(Import other) throws LookupException {
//		return other instanceof TypeImport && ((TypeImport)other).type().sameAs(type());
//	}
}
