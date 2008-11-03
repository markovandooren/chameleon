package chameleon.core.type.generics;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.rejuse.association.Reference;
import org.rejuse.association.ReferenceSet;

import chameleon.core.declaration.Declaration;
import chameleon.core.declaration.SimpleNameSignature;
import chameleon.core.element.Element;
import chameleon.core.member.Member;
import chameleon.core.type.ConstructedType;
import chameleon.core.type.Type;
import chameleon.core.type.TypeElementImpl;

public class GenericParameter extends TypeElementImpl<GenericParameter, Type> {

	public GenericParameter(SimpleNameSignature signature) {
		setSignature(signature);
	}
	
  public void setSignature(SimpleNameSignature signature) {
    if(signature != null) {
      _signature.connectTo(signature.getParentLink());
    } else {
      _signature.connectTo(null);
    }
  }
  
  /**
   * Return the signature of this member.
   */
  public SimpleNameSignature signature() {
    return _signature.getOtherEnd();
  }
  
  private Reference<GenericParameter, SimpleNameSignature> _signature = new Reference<GenericParameter, SimpleNameSignature>(this);

	@Override
	public GenericParameter clone() {
		GenericParameter result = new GenericParameter(signature().clone());
		return result;
	}
	
	/**
	 * A generic parameter introduces a special type alias for the bound.
	 */
	public Set<Member> getIntroducedMembers() {
		Set<Member> result = new HashSet<Member>();
		result.add(new ConstructedType(signature().clone(),lowerBound(),this));
		return result;
	}

	public Type getNearestType() {
		return getParent().getNearestType();
	}

	public List<? extends Element> getChildren() {
		List<? extends Element> result = new ArrayList<Element>();
		return result;
	}
	
	private ReferenceSet<GenericParameter,TypeConstraint> _typeConstraints = new ReferenceSet<GenericParameter,TypeConstraint>(this);
	
	public List<TypeConstraint> constraints() {
		return _typeConstraints.getOtherEnds();
	}
	
	public void addConstraint(TypeConstraint constraint) {
		if(constraint != null) {
			_typeConstraints.add(constraint.getParentLink());
		}
	}

	public Type lowerBound() {
		Type result = language().getDefaultSuperClass();
		for(TypeConstraint constraint: constraints()) {
			result = result.union(constraint.lowerBound());
		}
		return result;
	}

}
