package chameleon.core.type.generics;

import java.util.ArrayList;
import java.util.List;

import chameleon.core.declaration.SimpleNameSignature;
import chameleon.core.element.Element;
import chameleon.core.lookup.LookupException;
import chameleon.core.member.FixedSignatureMember;
import chameleon.core.member.Member;
import chameleon.core.type.Type;
import chameleon.core.type.TypeIndirection;

public class InstantiatedGenericParameter extends GenericParameter<InstantiatedGenericParameter> {

	private Type _type;
	
	public InstantiatedGenericParameter(SimpleNameSignature signature, Type type) {
		super(signature);
		setType(type);
	}

	@Override
	public InstantiatedGenericParameter clone() {
		throw new Error();
	}
	
//	/**
//	 * A generic parameter introduces itself. During lookup, the resolve() method will
//	 * introduce an alias.
//	 */
//	public List<Member> getIntroducedMembers() {
//		List<Member> result = new ArrayList<Member>();
//		result.add(this);
//		return result;
//	}


	public List<Element> children() {
		return new ArrayList<Element>();
	}
	
	private void setType(Type type) {
		_type = type;
	}
	
	public Type type() {
		return _type;
	}
	
	public Type selectionDeclaration() {
		return new ActualTypeArgument(signature().clone(), type());
	}

	public static class ActualTypeArgument extends TypeIndirection {

		public ActualTypeArgument(SimpleNameSignature sig, Type aliasedType) {
			super(sig,aliasedType);
		}

		@Override
		public Type clone() {
			return new ActualTypeArgument(signature().clone(), aliasedType());
		}
		
		@Override
		public Type actualDeclaration() {
			return aliasedType();
		}
		
	}

	public boolean compatibleWith(GenericParameter other) throws LookupException {
		return  (other instanceof InstantiatedGenericParameter) && ((InstantiatedGenericParameter)other).type().equals(type());
	}

	@Override
	public InstantiatedGenericParameter resolveForRoundTrip() throws LookupException {
		return this;
	}
}
