package chameleon.core.type.generics;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.rejuse.association.Reference;

import chameleon.core.declaration.Declaration;
import chameleon.core.declaration.SimpleNameSignature;
import chameleon.core.element.Element;
import chameleon.core.member.Member;
import chameleon.core.type.Type;
import chameleon.core.type.TypeAlias;
import chameleon.core.type.TypeElementImpl;

public class GenericParameter extends TypeElementImpl<GenericParameter, Type> implements Declaration<GenericParameter,Type,SimpleNameSignature>{

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
		result.add(new ConstructedType(signature().clone(),bound()));
		return result;
	}

	public Type getNearestType() {
		return getParent().getNearestType();
	}

	public List<? extends Element> getChildren() {
		List<? extends Element> result = new ArrayList<Element>();
		return result;
	}

	public Type bound() {
		need_union_types();
	}

	public Declaration alias(SimpleNameSignature sig) {
	}

}
