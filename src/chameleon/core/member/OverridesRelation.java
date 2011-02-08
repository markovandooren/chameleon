package chameleon.core.member;

import org.rejuse.logic.ternary.Ternary;

import chameleon.core.declaration.Declaration;
import chameleon.core.lookup.LookupException;
import chameleon.oo.language.ObjectOrientedLanguage;

public abstract class OverridesRelation<D extends Declaration<?,?,?>> extends DeclarationComparator<D> {

	public OverridesRelation(Class<D> kind) {
		super(kind);
	}
	
	@Override
	public boolean contains(Member first, Member second) throws LookupException {
		return isOverridable(second) && super.contains(first, second);
	}

	public boolean isOverridable(Member<?,?,?> d) throws LookupException {
		boolean result;
		Ternary temp = d.is(d.language(ObjectOrientedLanguage.class).OVERRIDABLE);
		if(temp == Ternary.TRUE) {
			result = true;
		} else if (temp == Ternary.FALSE) {
			result = false;
		} else {
			throw new LookupException("The overridability of the other method could not be determined.");
		}
		return result;
	}
}
