package be.kuleuven.cs.distrinet.chameleon.oo.type.inheritance;

import java.util.List;

import be.kuleuven.cs.distrinet.chameleon.core.element.Element;
import be.kuleuven.cs.distrinet.chameleon.core.lookup.LookupException;
import be.kuleuven.cs.distrinet.chameleon.core.validation.BasicProblem;
import be.kuleuven.cs.distrinet.chameleon.core.validation.Valid;
import be.kuleuven.cs.distrinet.chameleon.core.validation.Verification;
import be.kuleuven.cs.distrinet.chameleon.oo.language.ObjectOrientedLanguage;
import be.kuleuven.cs.distrinet.chameleon.oo.member.Member;
import be.kuleuven.cs.distrinet.chameleon.oo.member.MemberRelationSelector;
import be.kuleuven.cs.distrinet.chameleon.oo.type.Type;
import be.kuleuven.cs.distrinet.chameleon.oo.type.TypeReference;
import be.kuleuven.cs.distrinet.rejuse.logic.ternary.Ternary;

public class SubtypeRelation extends AbstractInheritanceRelation {

	public SubtypeRelation(TypeReference ref) {
		super(ref);
	}

	@Override
   protected SubtypeRelation cloneSelf() {
		return new SubtypeRelation(null);
	}

	@Override
	public Type superType() throws LookupException {
		return superClass();
	}
	@Override
	public Verification verifySelf() {
		try {
			Type superClass = superClass();
			if(superClass.is(language(ObjectOrientedLanguage.class).EXTENSIBLE) == Ternary.TRUE) {
				return Valid.create();
			} else {
				return new SuperClassNotExtensible(this);
			}
		} catch (LookupException e) {
			return new SuperClassNotExtensible(this);
		}
	}
	
	public static class SuperClassNotExtensible extends BasicProblem {

		public SuperClassNotExtensible(Element element) {
			super(element, "The super class is not extensible");
		}
		
	}

	@Override
	public <D extends Member> List<D> membersDirectlyOverriddenBy(MemberRelationSelector<D> selector) throws LookupException {
		return superClass().membersDirectlyOverriddenBy(selector);
	}

	@Override
	public <D extends Member> List<D> membersDirectlyAliasedBy(MemberRelationSelector<D> selector) throws LookupException {
		return superClass().membersDirectlyAliasedBy(selector);
	}


}
