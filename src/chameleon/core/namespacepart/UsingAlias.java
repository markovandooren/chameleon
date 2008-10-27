package chameleon.core.namespacepart;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.rejuse.association.Reference;

import chameleon.core.MetamodelException;
import chameleon.core.declaration.Declaration;
import chameleon.core.element.Element;
import chameleon.core.namespace.NamespaceOrType;
import chameleon.core.namespace.NamespaceOrTypeReference;
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

	public UsingAlias(String identifier, NamespaceOrTypeReference ref) {
		setNamespaceOrTypeReference(ref);
		setIdentifier(identifier);
	}

	public List<Element> getChildren() {
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

	private Reference<UsingAlias,NamespaceOrTypeReference> _packageOrType = new Reference<UsingAlias,NamespaceOrTypeReference>(this);

  
  public NamespaceOrTypeReference getNamespaceOrTypeReference() {
    return _packageOrType.getOtherEnd();
  }
  
  public void setNamespaceOrTypeReference(NamespaceOrTypeReference ref) {
  	if(ref != null) {
  		_packageOrType.connectTo(ref.getParentLink());
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

	public NamespaceOrType getElement() throws MetamodelException {
		return getNamespaceOrTypeReference().getNamespaceOrType();
	}

  @Override
  public UsingAlias clone() {
    return new UsingAlias(getIdentifier(),getNamespaceOrTypeReference().clone());
  }

	@Override
	public Set<Declaration> demandImports() throws MetamodelException {
		return new HashSet<Declaration>();
	}

	@Override
	public Set<Declaration> directImports() throws MetamodelException {
		need_type_alias();
	}

}
