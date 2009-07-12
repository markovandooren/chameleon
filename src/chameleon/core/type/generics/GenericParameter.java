package chameleon.core.type.generics;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.rejuse.association.OrderedReferenceSet;

import chameleon.core.declaration.SimpleNameSignature;
import chameleon.core.element.ChameleonProgrammerException;
import chameleon.core.element.Element;
import chameleon.core.lookup.LookupException;
import chameleon.core.member.FixedSignatureMember;
import chameleon.core.member.Member;
import chameleon.core.type.ConstructedType;
import chameleon.core.type.Type;

/**
 * This class represents generic parameters as used in Java and C#.
 * 
 * @author Marko van Dooren
 */
public class GenericParameter extends FixedSignatureMember<GenericParameter, Type, SimpleNameSignature,GenericParameter> {

	public GenericParameter(SimpleNameSignature signature) {
		setSignature(signature);
	}
	
  
	@Override
	public GenericParameter clone() {
		GenericParameter result = new GenericParameter(signature().clone());
		return result;
	}
	
	/**
	 * A generic parameter introduces itself. During lookup, the resolve() method will
	 * introduce an alias.
	 */
	public List<Member> getIntroducedMembers() {
		List<Member> result = new ArrayList<Member>();
		result.add(this);
		return result;
	}
	
	/**
	 * Resolving a generic parameter results in a constructed type whose bound
	 * is the upper bound of this generic parameter as defined by the upperBound method.
	 */
	public Type resolve() throws LookupException {
		return new ConstructedType(signature().clone(),upperBound(),this);
	}

	public Type getNearestType() {
		return parent().getNearestType();
	}

	public List<Element> children() {
		List<Element> result = new ArrayList<Element>();
		result.add(signature());
		result.addAll(constraints());
		return result;
	}
	
	private OrderedReferenceSet<GenericParameter,TypeConstraint> _typeConstraints = new OrderedReferenceSet<GenericParameter,TypeConstraint>(this);
	
	public List<TypeConstraint> constraints() {
		return _typeConstraints.getOtherEnds();
	}
	
	public void addConstraint(TypeConstraint constraint) {
		if(constraint != null) {
			_typeConstraints.add(constraint.parentLink());
		}
	}

	public Type upperBound() throws LookupException {
		Type result = language().getDefaultSuperClass();
		for(TypeConstraint constraint: constraints()) {
			result = result.intersection(constraint.upperBound());
		}
		return result;
	}

	public GenericParameter alias(SimpleNameSignature signature) {
		throw new ChameleonProgrammerException();
	}

}
