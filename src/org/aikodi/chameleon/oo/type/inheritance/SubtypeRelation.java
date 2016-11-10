package org.aikodi.chameleon.oo.type.inheritance;

import java.util.List;

import org.aikodi.chameleon.core.declaration.Declaration;
import org.aikodi.chameleon.core.element.Element;
import org.aikodi.chameleon.core.lookup.LookupException;
import org.aikodi.chameleon.core.validation.BasicProblem;
import org.aikodi.chameleon.core.validation.Valid;
import org.aikodi.chameleon.core.validation.Verification;
import org.aikodi.chameleon.oo.language.ObjectOrientedLanguage;
import org.aikodi.chameleon.oo.member.MemberRelationSelector;
import org.aikodi.chameleon.oo.type.Type;
import org.aikodi.chameleon.oo.type.TypeReference;

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

//	@Override
//	public <D extends Declaration> List<D> membersDirectlyOverriddenBy(MemberRelationSelector<D> selector) throws LookupException {
//		return superClass().membersDirectlyOverriddenBy(selector);
//	}
//
//	@Override
//	public <D extends Declaration> List<D> membersDirectlyAliasedBy(MemberRelationSelector<D> selector) throws LookupException {
//		return superClass().membersDirectlyAliasedBy(selector);
//	}


}
