package chameleon.core.type;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.rejuse.logic.ternary.Ternary;

import chameleon.core.declaration.SimpleNameSignature;
import chameleon.core.element.ChameleonProgrammerException;
import chameleon.core.member.Member;

public class UnionType extends Type {

	UnionType(Type first, Type second) {
		super(createSignature(Arrays.asList(new Type[]{first,second})));
		add(first);
		add(second);
	}
	
	private UnionType(Set<Type> types) {
		super(createSignature(types));
		_types = types;
	}

	protected Type unionDoubleDispatch(Type type) {
		return type.unionDoubleDispatch(this);
	}

	protected Type unionDoubleDispatch(UnionType type) {
		UnionType result = clone();
		result.addAll(type);
		return type;
	}

	public void addAll(UnionType type) {
		_types.addAll(type.types());
	}
	
	private Set<Type> _types = new HashSet<Type>();
	
	public void addType(Type type) {
		_types.add(type);
	}
	
	public Set<Type> types() {
		return new HashSet<Type>(_types);
	}

	
	@Override
	public void add(TypeElement element) throws ChameleonProgrammerException {
		throw new ChameleonProgrammerException("Trying to add an element to a union type.");
	}

	@Override
	public void addSuperType(TypeReference type) throws ChameleonProgrammerException {
		throw new ChameleonProgrammerException("Trying to add a super type to a union type.");
	}

	
	public static SimpleNameSignature createSignature(Collection<Type> types) {
		StringBuffer name = new StringBuffer("union of ");
		for(Type type:types) {
			name.append(type.getFullyQualifiedName()+", ");
		}
		name.delete(name.length()-2, name.length()-1);
		return new SimpleNameSignature(name.toString());
	}
	
	@Override
	public UnionType clone() {
		return new UnionType(types());
	}

	@Override
	public Set<Member> directlyDeclaredElements() {
		//FIXME: renaming and so on. Extend both types and perform automatic renaming?
		//       what about conflicting member definitions?
		Set<Member> result = new HashSet<Member>();
		for(Type type: types()) {
		  result.addAll(type.directlyDeclaredElements(Member.class));
		}
		
		Iterator<Member> iter = result.iterator();
		// Remove constructors. We really do need metaclasses so it seems.
		while(iter.hasNext()) {
			Member member = iter.next();
			if(member.is(language().CONSTRUCTOR) == Ternary.TRUE) {
				iter.remove();
			}
		}
		return result;
	}

	@Override
	public List<TypeReference> getSuperTypeReferences() {
		List<TypeReference> result = new ArrayList<TypeReference>();
		for(Type type: types()) {
		  result.addAll(type.getSuperTypeReferences());
		}
		return result;
	}

	@Override
	public void removeSuperType(TypeReference type) {
		throw new ChameleonProgrammerException("Trying to remove a super type from a union type.");
	}
	
	

}
