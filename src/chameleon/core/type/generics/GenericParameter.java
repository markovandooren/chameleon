package chameleon.core.type.generics;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.rejuse.association.ReferenceSet;

import chameleon.core.MetamodelException;
import chameleon.core.declaration.SimpleNameSignature;
import chameleon.core.element.ChameleonProgrammerException;
import chameleon.core.element.Element;
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
	 * @throws MetamodelException 
	 */
	public Set<Member> getIntroducedMembers() {
		Set<Member> result = new HashSet<Member>();
		result.add(this);
		return result;
	}
	
	/**
	 * Resolving a generic parameter results in a constructed type whose bound
	 * is the upper bound of this generic parameter as defined by the upperBound method.
	 */
	public Type resolve() throws MetamodelException {
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
	
	private ReferenceSet<GenericParameter,TypeConstraint> _typeConstraints = new ReferenceSet<GenericParameter,TypeConstraint>(this);
	
	public List<TypeConstraint> constraints() {
		return _typeConstraints.getOtherEnds();
	}
	
	public void addConstraint(TypeConstraint constraint) {
		if(constraint != null) {
			_typeConstraints.add(constraint.parentLink());
		}
	}

	public Type upperBound() throws MetamodelException {
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
